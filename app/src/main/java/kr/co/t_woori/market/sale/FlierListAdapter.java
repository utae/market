package kr.co.t_woori.market.sale;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.FlierListItemBinding;
import kr.co.t_woori.market.databinding.NoticeListItemBinding;
import kr.co.t_woori.market.notice.Notice;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-25.
 */

public class FlierListAdapter extends BaseAdapter {

    private Context context;
    private List<Flier> flierList;

    public FlierListAdapter(Context context, List<Flier> flierList) {
        this.context = context;
        this.flierList = flierList;
    }

    @Override
    public int getCount() {
        return flierList.size();
    }

    @Override
    public Object getItem(int position) {
        return flierList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FlierListItemBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.flier_list_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (FlierListItemBinding) convertView.getTag();
        }

        Flier flier = flierList.get(position);

        binding.timeTextView.setText(Utilities.convertStringToDateFormat(flier.getTime(), "yyyyMMddHHmmss", "yyyy년 MM월 dd일 HH:mm"));
        binding.titleTextView.setText(flier.getTitle());

        return convertView;
    }
}
