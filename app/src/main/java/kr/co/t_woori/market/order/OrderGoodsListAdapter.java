package kr.co.t_woori.market.order;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.List;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.OrderGoodsListItemBinding;
import kr.co.t_woori.market.goods.Goods;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-25.
 */

public class OrderGoodsListAdapter extends BaseAdapter{

    private Context context;
    private List<Goods> goodsList;
    private HashMap<String, Drawable> imgHashMap;


    public OrderGoodsListAdapter(Context context, List<Goods> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
        this.imgHashMap = new HashMap<>();
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final OrderGoodsListItemBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_goods_list_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (OrderGoodsListItemBinding) convertView.getTag();
        }

        final Goods goods = goodsList.get(position);

        binding.nameTextView.setText(goods.getName());

        if(goods.getDiscountPrice() == null){
            binding.priceTextView.setText(goods.getPrice() + "원");
        }else{
            binding.priceTextView.setText(goods.getDiscountPrice() + "원");
        }

        binding.amountTextView.setText("(" + goods.getAmount() + "개)");

        if(goods.hasImg()){
            if(!imgHashMap.containsKey(goods.getBarcode())){
                Glide.with(context).asDrawable().load(Utilities.getGoodsImgDir() + goods.getBarcode() + ".jpg")
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                binding.imgView.setImageResource(R.drawable.img_loading);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                imgHashMap.put(goods.getBarcode(), resource);
                                return false;
                            }
                        }).into(binding.imgView);
            }else{
                binding.imgView.setImageDrawable(imgHashMap.get(goods.getBarcode()));
            }
        }else{
            binding.imgView.setImageResource(R.drawable.img_loading);
        }

        return convertView;
    }
}
