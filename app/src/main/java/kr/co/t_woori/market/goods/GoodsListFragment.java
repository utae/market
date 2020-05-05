package kr.co.t_woori.market.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.AdPanelBinding;
import kr.co.t_woori.market.databinding.GoodsListFragmentBinding;
import kr.co.t_woori.market.main.MainActivity;
import kr.co.t_woori.market.utilities.RefreshFragment;
import kr.co.t_woori.market.utilities.UserInfo;

/**
 * Created by rladn on 2017-10-23.
 */

public class GoodsListFragment extends RefreshFragment {

    private GoodsListFragmentBinding binding;
    private String type; //"C" : "Category", "P" : "Popular", "B" : "Bargain", "S" : "Sale", "E" : "Event"

    public static GoodsListFragment createFromCategory(ArrayList<Integer> categoryList){
        GoodsListFragment fragment = new GoodsListFragment();
        Bundle args = new Bundle();
        args.putString("type", "C");
        args.putIntegerArrayList("categoryList", categoryList);
        fragment.setArguments(args);
        return fragment;
    }

    public static GoodsListFragment createPopularList(){
        GoodsListFragment fragment = new GoodsListFragment();
        Bundle args = new Bundle();
        args.putString("type", "P");
        fragment.setArguments(args);
        return fragment;
    }

    public static GoodsListFragment createBargainList(){
        GoodsListFragment fragment = new GoodsListFragment();
        Bundle args = new Bundle();
        args.putString("type", "B");
        fragment.setArguments(args);
        return fragment;
    }

    public static GoodsListFragment createSaleList(){
        GoodsListFragment fragment = new GoodsListFragment();
        Bundle args = new Bundle();
        args.putString("type", "S");
        fragment.setArguments(args);
        return fragment;
    }

    public static GoodsListFragment createEventList(){
        GoodsListFragment fragment = new GoodsListFragment();
        Bundle args = new Bundle();
        args.putString("type", "E");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.goods_list_fragment, container, false);

//        AdPanelBinding adPanelBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.ad_panel, null, false);
//        binding.listView.addHeaderView(adPanelBinding.getRoot());

        initData();

        return binding.getRoot();
    }

    private void initData(){
        switch (type){
            case "C" :
                getGoodsListFromCategory(getArguments().getIntegerArrayList("categoryList"));
                break;
            case "P" :
                getPopularGoodsList();
                break;
            case "B" :
                getBargainGoodsList();
                break;
            case "S" :
                getSaleGoodsList();
                break;
            case "E" :
                getEventGoodsList();
                break;
        }
    }

    private void initListView(List<Goods> goodsList){
        GoodsListAdapter listAdapter = new GoodsListAdapter(getContext(), getFragmentManager(), goodsList);
        binding.listView.setAdapter(listAdapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getAdapter().getItem(position) instanceof Goods){
                    getFragmentManager().beginTransaction().add(R.id.main_container, GoodsDetailFragment.create((Goods) parent.getAdapter().getItem(position))).addToBackStack(null).commit();
                }
            }
        });
        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(UserInfo.isMaster()){
                    if(parent.getAdapter().getItem(position) instanceof Goods){
                        ManageGoodsDialog.create(type, (Goods)parent.getAdapter().getItem(position))
                                .setDismissListener(new ManageGoodsDialog.OnManageGoodsDialogDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        refreshFragment();
                                    }
                                })
                                .show(getFragmentManager(), "manage goods dialog");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void getGoodsListFromCategory(ArrayList<Integer> categoryList){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).getGoodsFromCategory(categoryList.toString())
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(Integer.parseInt((String)results.get("count")) > 0){
                    ArrayList<Goods> goodsList = new ArrayList<>();
                    if(results.get("goods") instanceof List){
                        for(Object goods : (List)results.get("goods")){
                            if(goods instanceof Map){
                                Map map = (Map)goods;
                                boolean hasImg = "Y".equals(map.get("img"));
                                if(!UserInfo.isMaster() && !hasImg){
                                    continue;
                                }
                                String barcode = (String)map.get("barcode");
                                String name = (String)map.get("name");
                                String price = (String)map.get("price");
                                String capacity = (String)map.get("capacity");
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

    private void getPopularGoodsList(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).getPopularGoods()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(Integer.parseInt((String)results.get("count")) > 0){
                    ArrayList<Goods> goodsList = new ArrayList<>();
                    if(results.get("goods") instanceof List){
                        for(Object goods : (List)results.get("goods")){
                            if(goods instanceof Map){
                                Map map = (Map)goods;
                                boolean hasImg = "Y".equals(map.get("img"));
                                if(!UserInfo.isMaster() && !hasImg){
                                    continue;
                                }
                                String barcode = (String)map.get("barcode");
                                String name = (String)map.get("name");
                                String price = (String)map.get("price");
                                String capacity = (String)map.get("capacity");
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

    private void getBargainGoodsList(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).getBargainGoods()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(Integer.parseInt((String)results.get("count")) > 0){
                    ArrayList<Goods> goodsList = new ArrayList<>();
                    if(results.get("goods") instanceof List){
                        for(Object goods : (List)results.get("goods")){
                            if(goods instanceof Map){
                                Map map = (Map)goods;
                                boolean hasImg = "Y".equals(map.get("img"));
                                if(!UserInfo.isMaster() && !hasImg){
                                    continue;
                                }
                                String barcode = (String)map.get("barcode");
                                String name = (String)map.get("name");
                                String price = (String)map.get("price");
                                String capacity = (String)map.get("capacity");
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

    private void getSaleGoodsList(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).getSaleGoods()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(Integer.parseInt((String)results.get("count")) > 0){
                    ArrayList<Goods> goodsList = new ArrayList<>();
                    if(results.get("goods") instanceof List){
                        for(Object goods : (List)results.get("goods")){
                            if(goods instanceof Map){
                                Map map = (Map)goods;
                                boolean hasImg = "Y".equals(map.get("img"));
                                if(!UserInfo.isMaster() && !hasImg){
                                    continue;
                                }
                                String barcode = (String)map.get("barcode");
                                String name = (String)map.get("name");
                                String price = (String)map.get("price");
                                String capacity = (String)map.get("capacity");
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

    private void getEventGoodsList(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).getEventGoods()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(Integer.parseInt((String)results.get("count")) > 0){
                    ArrayList<Goods> goodsList = new ArrayList<>();
                    if(results.get("goods") instanceof List){
                        for(Object goods : (List)results.get("goods")){
                            if(goods instanceof Map){
                                Map map = (Map)goods;
                                boolean hasImg = "Y".equals(map.get("img"));
                                if(!UserInfo.isMaster() && !hasImg){
                                    continue;
                                }
                                String barcode = (String)map.get("barcode");
                                String name = (String)map.get("name");
                                String price = (String)map.get("price");
                                String capacity = (String)map.get("capacity");
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

    @Override
    public void refreshFragment() {
        initData();
    }
}
