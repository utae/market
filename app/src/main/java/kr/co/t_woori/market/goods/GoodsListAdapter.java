package kr.co.t_woori.market.goods;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.NumberPicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.List;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.GoodsListItemBinding;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-08-16.
 */

public class GoodsListAdapter extends BaseAdapter {

    private Context context;
    private FragmentManager fragmentManager;
    private List<Goods> goodsList;
    private HashMap<String, Drawable> imgHashMap;


    public GoodsListAdapter(Context context, FragmentManager fragmentManager, List<Goods> goodsList) {
        this.context = context;
        this.fragmentManager = fragmentManager;
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
        final GoodsListItemBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.goods_list_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (GoodsListItemBinding) convertView.getTag();
        }

        final Goods goods = goodsList.get(position);

        binding.nameTextView.setText(goods.getName());
        binding.capacityTextView.setText(goods.getCapacity());

        if(goods.getDiscountPrice() != null){
            binding.priceTextView.setText(goods.getDiscountPrice() + "원");
            binding.originalPriceTextView.setVisibility(View.VISIBLE);
            binding.originalPriceTextView.setPaintFlags(binding.priceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.originalPriceTextView.setText(goods.getPrice() + "원");
        }else{
            binding.priceTextView.setText(goods.getPrice() + "원");
        }

        if(goods.getAmount() > 0){
            binding.amountBtn.setText(Integer.toString(goods.getAmount()));
        }

        binding.amountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectAmountDialog.create(goods, Integer.parseInt(binding.amountBtn.getText().toString()), "담기")
                        .setOnSelectedAmountListener(new SelectAmountDialog.OnSelectedAmountListener() {
                            @Override
                            public void onSelected(int amount) {
                                goods.setAmount(amount);
                                Utilities.addGoodsInCart(context, goods);
                                binding.amountBtn.setText(Integer.toString(amount));
                            }
                        })
                        .show(fragmentManager, "select amount dialog");
            }
        });

        binding.saveCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goods.setAmount(Integer.parseInt(binding.amountBtn.getText().toString()));
                Utilities.addGoodsInCart(context, goods);
            }
        });

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
