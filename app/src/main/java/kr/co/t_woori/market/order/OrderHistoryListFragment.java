package kr.co.t_woori.market.order;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.OrderHistoryListFragmentBinding;
import kr.co.t_woori.market.goods.Goods;
import kr.co.t_woori.market.utilities.UserInfo;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-25.
 */

public class OrderHistoryListFragment extends Fragment {

    private OrderHistoryListFragmentBinding binding;
    private ArrayList<OrderHistory> historyList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.order_history_list_fragment, container, false);

        historyList = new ArrayList<>();

        if(UserInfo.isMaster()){
            getAllOrderHistory();
        }else{
            getOrderHistory(UserInfo.getPhoneNum());
        }

        return binding.getRoot();
    }

    private void initListView(){
        OrderHistoryListAdapter adapter = new OrderHistoryListAdapter(getContext(), historyList);
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getAdapter().getItem(position) instanceof OrderHistory){
                    OrderHistory history = (OrderHistory) parent.getAdapter().getItem(position);
                    getFragmentManager().beginTransaction().add(R.id.main_container, OrderHistoryDetailFragment.create(history)).addToBackStack(null).commit();
                }
            }
        });
        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                if(UserInfo.isMaster()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("주문 내역을 완료처리 하겠습니까?")
                            .setPositiveButton("완료", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OrderHistory history = historyList.get(position);
                                    int addingPoint = Integer.parseInt(history.getTotalPrice()) * 2 / 100;
                                    completeOrder(history.getOrderNum(), history.getPhone(), Integer.toString(addingPoint));
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void getOrderHistory(String phone){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(OrderAPIService.class).getOrderList(phone)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(!historyList.isEmpty()){
                    historyList.clear();
                }
                if("0".equals(results.get("count"))){
                    binding.listView.setEmptyView(binding.emptyView.emptyTextView);
                }else{
                    if(results.get("history") instanceof List){
                        for(Object object : (List)results.get("history")){
                            if(object instanceof Map){
                                Map map = (Map)object;
                                String orderNum = (String)map.get("orderNum");
                                String totalPrice = (String)map.get("totalPrice");
                                String address = (String)map.get("address");
                                String deliveryTime = (String)map.get("deliveryTime");
                                String request = (String)map.get("request");
                                String payment = (String)map.get("payment");
                                String orderTime = (String)map.get("orderTime");
                                String state = (String)map.get("state");
                                String phone = (String)map.get("phone");
                                String recipient = (String)map.get("recipient");
                                String phone2 = (String)map.get("phone2");

                                historyList.add(new OrderHistory(orderNum, totalPrice, address, deliveryTime, request, payment, orderTime, state, phone, recipient, phone2));
                            }
                        }
                        initListView();
                    }
                }
            }
        };

        serverCommunicator.execute();
    }

    private void getAllOrderHistory(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(OrderAPIService.class).getAllOrderList()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(!historyList.isEmpty()){
                    historyList.clear();
                }
                if("0".equals(results.get("count"))){
                    binding.listView.setEmptyView(binding.emptyView.emptyTextView);
                }else{
                    if(results.get("history") instanceof List){
                        for(Object object : (List)results.get("history")){
                            if(object instanceof Map){
                                Map map = (Map)object;
                                String orderNum = (String)map.get("orderNum");
                                String totalPrice = (String)map.get("totalPrice");
                                String address = (String)map.get("address");
                                String deliveryTime = (String)map.get("deliveryTime");
                                String request = (String)map.get("request");
                                String payment = (String)map.get("payment");
                                String orderTime = (String)map.get("orderTime");
                                String state = (String)map.get("state");
                                String phone = (String)map.get("phone");
                                String recipient = (String)map.get("recipient");
                                String phone2 = (String)map.get("phone2");

                                historyList.add(new OrderHistory(orderNum, totalPrice, address, deliveryTime, request, payment, orderTime, state, phone, recipient, phone2));
                            }
                        }
                        initListView();
                    }
                }
            }
        };
        serverCommunicator.execute();
    }

    private void completeOrder(String orderNum, final String phone, final String point){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(OrderAPIService.class).completeOrder(orderNum)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                addUserPoint(phone, point);
                Utilities.showToast(getContext(), "완료 처리 되었습니다.");
                getAllOrderHistory();
            }
        };
        serverCommunicator.execute();
    }

    private void addUserPoint(String phone, String point){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(OrderAPIService.class).addUserPoint(phone, point)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {

            }
        };
        serverCommunicator.execute();
    }
}
