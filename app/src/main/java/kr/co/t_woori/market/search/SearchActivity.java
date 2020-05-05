package kr.co.t_woori.market.search;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.AdPanelBinding;
import kr.co.t_woori.market.databinding.SearchActivityBinding;
import kr.co.t_woori.market.goods.Goods;
import kr.co.t_woori.market.goods.GoodsAPIService;
import kr.co.t_woori.market.goods.GoodsDetailFragment;
import kr.co.t_woori.market.goods.GoodsListAdapter;
import kr.co.t_woori.market.goods.ManageGoodsDialog;
import kr.co.t_woori.market.utilities.UserInfo;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-09-19.
 */

public class SearchActivity extends AppCompatActivity {

    private SearchActivityBinding binding;
    private String word;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.search_activity);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidWord()){
                    search(word);
                }
            }
        });
    }

    private boolean isValidWord(){
        if(binding.editText.getText() == null || "".equals(binding.editText.getText().toString().trim())){
            Utilities.showToast(this, "검색어를 입력해주세요.");
        }else{
            word = binding.editText.getText().toString().trim();
            return true;
        }
        return false;
    }

    private void initListView(List<Goods> goodsList){
        if(getSupportFragmentManager().findFragmentByTag("search goods detail") != null){
            getSupportFragmentManager().popBackStack();
        }
        GoodsListAdapter listAdapter = new GoodsListAdapter(this, getSupportFragmentManager(), goodsList);
        binding.listView.setAdapter(listAdapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getAdapter().getItem(position) instanceof Goods){
                    getSupportFragmentManager().beginTransaction().add(R.id.search_container, GoodsDetailFragment.create((Goods) parent.getAdapter().getItem(position)), "search goods detail").addToBackStack(null).commit();
                }
            }
        });
        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(UserInfo.isMaster()){
                    if(parent.getAdapter().getItem(position) instanceof Goods){
                        ManageGoodsDialog.create("C", (Goods)parent.getAdapter().getItem(position))
                                .setDismissListener(new ManageGoodsDialog.OnManageGoodsDialogDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        search(word);
                                    }
                                })
                                .show(getSupportFragmentManager(), "manage goods dialog");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void search(String word){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(GoodsAPIService.class).searchGoods(word)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(Integer.parseInt((String)results.get("count")) > 0){
                    ArrayList<Goods> goodsList = new ArrayList<>();
                    if(results.get("goods") instanceof List){
                        for(Object goods : (List)results.get("goods")){
                            if(goods instanceof Map){
                                Map map = (Map)goods;
                                String barcode = (String)map.get("barcode");
                                String name = (String)map.get("name");
                                String price = (String)map.get("price");
                                String capacity = (String)map.get("capacity");
                                boolean hasImg = "Y".equals(map.get("img"));
                                if(map.get("discount") == null){
                                    goodsList.add(new Goods(barcode, name, price, capacity, hasImg));
                                }else{
                                    String discount = (String)map.get("discount");
                                    goodsList.add(new Goods(barcode, name, price, capacity, hasImg, discount));
                                }
                            }
                        }
                        initListView(goodsList);
                    }
                }else{
                    binding.listView.setEmptyView(binding.emptyView.emptyTextView);
                }
            }
        };
        serverCommunicator.execute();
    }
}
