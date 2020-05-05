package kr.co.t_woori.market.goods;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.GoodsDetailFragmentBinding;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-23.
 */

public class GoodsDetailFragment extends Fragment {

    private GoodsDetailFragmentBinding binding;

    private Goods goods;

    public static GoodsDetailFragment create(Goods goods){
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("goods", goods);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goods = (Goods)getArguments().getSerializable("goods");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.goods_detail_fragment, container, false);

        binding.nameTextView.setText(goods.getName());

        binding.capacityTextView.setText(goods.getCapacity());

        if(goods.getDiscountPrice() == null){
            binding.priceTextView.setText(goods.getPrice());
        }else{
            binding.priceTextView.setText(goods.getDiscountPrice());
        }

        binding.saveCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goods.setAmount(1);
                Utilities.addGoodsInCart(getContext(), goods);
            }
        });

        if(goods.hasImg()){
            Glide.with(getContext()).asDrawable().load(Utilities.getGoodsImgDir() + goods.getBarcode() + ".jpg")
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            binding.imgView.setImageResource(R.drawable.img_loading);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(binding.imgView);
        }else {
            binding.imgView.setImageResource(R.drawable.img_loading);
        }

        return binding.getRoot();
    }
}
