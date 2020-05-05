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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.List;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.GoodsGridItemBinding;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-09-08.
 */

public class GoodsGridAdapter extends BaseAdapter {

    private Context context;
    private FragmentManager fragmentManager;
    private List<Goods> goodsList;
    private HashMap<String, Drawable> imgHashMap;
    private OnGoodsGridAdapterListener onGoodsGridAdapterListener;

    public GoodsGridAdapter(Context context, FragmentManager fragmentManager, List<Goods> goodsList, OnGoodsGridAdapterListener onGoodsGridAdapterListener) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.goodsList = goodsList;
        this.imgHashMap = new HashMap<>();
        this.onGoodsGridAdapterListener = onGoodsGridAdapterListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final GoodsGridItemBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.goods_grid_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (GoodsGridItemBinding) convertView.getTag();
        }

        final Goods goods = goodsList.get(position);

        binding.nameTextView.setText(goods.getName());

        binding.priceTextView.setText(goods.getTotalPrice() + "원");

        binding.amountTextView.setText(Integer.toString(goods.getAmount()) + "개");

        binding.amountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoodsGridAdapterListener.onAmountBtnClick(GoodsGridAdapter.this, position);
            }
        });

        binding.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoodsGridAdapterListener.onDelBtnClick(GoodsGridAdapter.this, goods);
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

    public interface OnGoodsGridAdapterListener{
        void onDelBtnClick(BaseAdapter adapter, Goods goods);
        void onAmountBtnClick(BaseAdapter adapter, int position);
    }
}
