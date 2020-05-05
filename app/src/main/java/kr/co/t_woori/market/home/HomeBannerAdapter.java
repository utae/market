package kr.co.t_woori.market.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.BannerImageUploader;
import kr.co.t_woori.market.custom_view.ViewPagerIndicator;
import kr.co.t_woori.market.databinding.HomeBannerBinding;
import kr.co.t_woori.market.goods.GoodsListFragment;
import kr.co.t_woori.market.utilities.UserInfo;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-09-08.
 */

public class HomeBannerAdapter extends PagerAdapter {

    private Context context;
    private FragmentManager fragmentManager;
    private ViewPagerIndicator indicator;
    private HashMap<String, Drawable> imgHashMap;
    private String bannerName;

    public HomeBannerAdapter(Context context, FragmentManager fragmentManager, ViewPagerIndicator indicator) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.indicator = indicator;
        this.indicator.createDotPanel(2, R.drawable.ic_indicator_off, R.drawable.ic_indicator_on);
        this.imgHashMap = new HashMap<>();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final HomeBannerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_banner, null, false);

        switch (position){
            case 0 :
                bannerName = "popular";
                break;
            case 1 :
                bannerName = "bargain";
                break;
        }

        if(!imgHashMap.containsKey(bannerName)){
            Glide.with(context).asDrawable().load(Utilities.getGoodsImgDir() + bannerName + ".jpg").listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    binding.imageView.setImageResource(R.drawable.img_loading);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    imgHashMap.put(bannerName, resource);
                    return false;
                }
            }).into(binding.imageView);
        }else{
            binding.imageView.setImageDrawable(imgHashMap.get(bannerName));
        }

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0 :
                        fragmentManager.beginTransaction().add(R.id.main_container, GoodsListFragment.createPopularList()).addToBackStack(null).commit();
                        break;
                    case 1 :
                        fragmentManager.beginTransaction().add(R.id.main_container, GoodsListFragment.createBargainList()).addToBackStack(null).commit();
                        break;
                    case 2 :
                        fragmentManager.beginTransaction().add(R.id.main_container, GoodsListFragment.createSaleList()).addToBackStack(null).commit();
                        break;
                }
            }
        });

        binding.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(UserInfo.isMaster()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("배너 이미지를 변경 하시겠습니까?")
                            .setPositiveButton("변경", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, BannerImageUploader.class);
                                    switch (position){
                                        case 0 :
                                            intent.putExtra("bannerName", "popular");
                                            break;
                                        case 1 :
                                            intent.putExtra("bannerName", "bargain");
                                            break;
                                        case 2 :
                                            intent.putExtra("bannerName", "sale");
                                            break;
                                    }
                                    context.startActivity(intent);
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

        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    public OnBannerChangeListener getOnBannerChangeListener(){
        return new OnBannerChangeListener(indicator);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    private class OnBannerChangeListener extends ViewPager.SimpleOnPageChangeListener{

        private ViewPagerIndicator indicator;

        public OnBannerChangeListener(ViewPagerIndicator indicator) {
            this.indicator = indicator;
        }

        @Override
        public void onPageSelected(int position) {
            indicator.selectDot(position);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
