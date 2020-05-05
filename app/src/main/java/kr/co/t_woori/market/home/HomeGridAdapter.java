package kr.co.t_woori.market.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.HomeGridItemBinding;

/**
 * Created by rladn on 2017-08-16.
 */

public class HomeGridAdapter extends BaseAdapter {

    private Context context;
    private LinkedHashMap<String, ArrayList<Integer>> linkedHashMap;
    private Object[] itemList;
    private ArrayList<Integer> imgResList;

    public HomeGridAdapter(Context context) {
        this.context = context;
        this.linkedHashMap = Category.getCategory();
        this.itemList = linkedHashMap.keySet().toArray();
        this.imgResList = Category.getCategoryImgList();
    }


    @Override
    public int getCount() {
        return itemList.length;
    }

    @Override
    public Object getItem(int position) {
        return itemList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeGridItemBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_grid_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (HomeGridItemBinding) convertView.getTag();
        }
        binding.imageView.setImageResource(imgResList.get(position));

        return convertView;
    }
}
