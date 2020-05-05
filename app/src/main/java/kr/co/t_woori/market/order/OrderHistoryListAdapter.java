package kr.co.t_woori.market.order;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.OrderHistoryListItemBinding;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-25.
 */

public class OrderHistoryListAdapter extends BaseAdapter {

    private Context context;
    private List<OrderHistory> historyList;

    public OrderHistoryListAdapter(Context context, List<OrderHistory> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        OrderHistoryListItemBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_history_list_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (OrderHistoryListItemBinding) convertView.getTag();
        }

        OrderHistory orderHistory = historyList.get(position);

        binding.orderTimeTextView.setText(Utilities.convertStringToDateFormat(orderHistory.getOrderTime(), "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss"));
        binding.totalPriceTextView.setText("주문금액 : " + orderHistory.getTotalPrice());

        switch (orderHistory.getState()){
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

        return convertView;
    }
}
