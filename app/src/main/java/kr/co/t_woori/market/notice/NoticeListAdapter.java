package kr.co.t_woori.market.notice;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.NoticeListItemBinding;
import kr.co.t_woori.market.databinding.OrderHistoryListItemBinding;
import kr.co.t_woori.market.order.OrderHistory;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-25.
 */

public class NoticeListAdapter extends BaseAdapter {

    private Context context;
    private List<Notice> noticeList;

    public NoticeListAdapter(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public int getCount() {
        return noticeList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NoticeListItemBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.notice_list_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (NoticeListItemBinding) convertView.getTag();
        }

        Notice notice = noticeList.get(position);

        binding.timeTextView.setText(Utilities.convertStringToDateFormat(notice.getTime(), "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss"));
        binding.titleTextView.setText(notice.getTitle());

        return convertView;
    }
}
