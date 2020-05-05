package kr.co.t_woori.market.goods;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.GoodsGridFragmentBinding;
import kr.co.t_woori.market.main.MainActivity;
import kr.co.t_woori.market.order.OrderFragment;
import kr.co.t_woori.market.utilities.RefreshFragment;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-23.
 */

public class GoodsGridFragment extends Fragment {

    private GoodsGridFragmentBinding binding;
    private String type; // "C" : Cart, "L" : LastOrder

    private ArrayList<Goods> goodsList;

    public static GoodsGridFragment createCartFragment(){
        GoodsGridFragment fragment = new GoodsGridFragment();
        Bundle args = new Bundle();
        args.putString("type", "C");
        fragment.setArguments(args);
        return fragment;
    }

    public static GoodsGridFragment createLastOrderFragment(){
        GoodsGridFragment fragment = new GoodsGridFragment();
        Bundle args = new Bundle();
        args.putString("type", "L");
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
        binding = DataBindingUtil.inflate(inflater, R.layout.goods_grid_fragment, container, false);

        switch(type){
            case "C" :
                binding.bottomBtn.setText("배송 신청");
                binding.bottomBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(goodsList.size() != 0){
                            if(getTotalPrice() >= 30000){
                                getFragmentManager().beginTransaction().add(R.id.main_container, new OrderFragment()).addToBackStack(null).commit();
                            }else{
                                Utilities.showToast(getContext(), "최소 주문금액은 30,000원 입니다.");
                            }
                        }else{
                            Utilities.showToast(getContext(), "장바구니에 상품이 없습니다.");
                        }
                    }
                });
                goodsList = Utilities.getCartList(getContext());
                break;

            case "L" :
                binding.bottomBtn.setText("전체상품 담기");
                binding.bottomBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utilities.saveCartList(getContext(), goodsList);
                        Utilities.showToast(getContext(), "전체상품이 장바구니에 추가되었습니다.");
                    }
                });
                goodsList = Utilities.getLastOrder(getContext());
                break;

            default:
                goodsList = new ArrayList<>();
        }

        initGridView();

        setTotalPrice();

        return binding.getRoot();
    }

    private void initGridView(){
        binding.gridView.setEmptyView(binding.emptyView.emptyTextView);
        GoodsGridAdapter goodsGridAdapter = new GoodsGridAdapter(getContext(), getFragmentManager(), goodsList, new GoodsGridAdapter.OnGoodsGridAdapterListener() {

            @Override
            public void onDelBtnClick(final BaseAdapter adapter, final Goods goods) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goodsList.remove(goods);
                                adapter.notifyDataSetChanged();
                                switch (type){
                                    case "C" :
                                        Utilities.saveCartList(getContext(), goodsList);
                                        break;
                                    case "L" :
                                        Utilities.saveLastOrder(getContext(), goodsList);
                                        break;
                                }
                                setTotalPrice();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }

            @Override
            public void onAmountBtnClick(final BaseAdapter adapter, final int position) {
                SelectAmountDialog.create(goodsList.get(position), goodsList.get(position).getAmount(), "선택")
                        .setOnSelectedAmountListener(new SelectAmountDialog.OnSelectedAmountListener() {
                            @Override
                            public void onSelected(int amount) {
                                goodsList.get(position).setAmount(amount);
                                adapter.notifyDataSetChanged();
                                Utilities.saveCartList(getContext(), goodsList);
                                setTotalPrice();
                            }
                        })
                        .show(getFragmentManager(), "select amount dialog");
            }
        });
        binding.gridView.setAdapter(goodsGridAdapter);
    }

    private int getTotalPrice(){
        int totalPrice = 0;
        for(Goods goods : goodsList){
            totalPrice += goods.getTotalPrice();
        }
        return totalPrice;
    }

    private void setTotalPrice(){
        binding.totalPriceTextView.setText(getTotalPrice() + "원");
    }
}
