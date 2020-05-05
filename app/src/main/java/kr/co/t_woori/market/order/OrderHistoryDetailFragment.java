package kr.co.t_woori.market.order;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.OrderHistoryDetailFragmentBinding;
import kr.co.t_woori.market.goods.Goods;
import kr.co.t_woori.market.goods.GoodsAPIService;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-25.
 */

public class OrderHistoryDetailFragment extends Fragment {

    private OrderHistoryDetailFragmentBinding binding;
    private OrderHistory history;

    public static OrderHistoryDetailFragment create(OrderHistory history){
        OrderHistoryDetailFragment fragment = new OrderHistoryDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("history", history);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        history = (OrderHistory) getArguments().getSerializable("history");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.order_history_detail_fragment, container, false);

        getOrderGoodsList(history.getOrderNum());

        binding.listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        binding.orderNumTextView.setText(history.getOrderNum());

        binding.orderTimeTextView.setText(Utilities.convertStringToDateFormat(history.getOrderTime(), "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss"));

        binding.phoneTextView.setText(history.getPhone());

        binding.phone2TextView.setText(history.getPhone2());

        binding.recipientTextView.setText(history.getRecipient());

        binding.addressTextView.setText(history.getAddress());

        binding.requestTextView.setText(history.getRequest());

        binding.orderPriceTextView.setText(history.getTotalPrice() + "원");

        binding.totalPriceTextView.setText(history.getTotalPrice() + "원");

        switch (history.getState()){
            case "R" :
                binding.stateTextView.setText("접수 완료");
                break;
            case "D" :
                binding.stateTextView.setText("배송 완료");
                break;
            case "C" :
                binding.stateTextView.setText("주문 취소");
                break;
        }

        return binding.getRoot();
    }

    private void initListView(List<Goods> goodsList) {
        OrderGoodsListAdapter listAdapter = new OrderGoodsListAdapter(getContext(), goodsList);
        binding.listView.setAdapter(listAdapter);
    }

    private void getOrderGoodsList(String orderNum){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(OrderAPIService.class).getOrderGoodsList(orderNum)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
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
            }
        };
        serverCommunicator.execute();
    }
}
