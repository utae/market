package kr.co.t_woori.market.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import kr.co.t_woori.market.goods.GoodsListFragment;
import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.HomeGridHeaderBinding;
import kr.co.t_woori.market.databinding.HomePageFragmentBinding;
import kr.co.t_woori.market.main.MainActivity;
import kr.co.t_woori.market.utilities.RefreshFragment;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-23.
 */

public class HomePageFragment extends RefreshFragment {

    private HomePageFragmentBinding binding;
    private HomeGridHeaderBinding homeGridHeaderBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_page_fragment, container, false);

        homeGridHeaderBinding = DataBindingUtil.inflate(inflater, R.layout.home_grid_header, null, false);

        homeGridHeaderBinding.eventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.main_container, GoodsListFragment.createEventList()).addToBackStack(null).commit();
            }
        });
        binding.gridView.addHeaderView(homeGridHeaderBinding.getRoot());
        binding.gridView.setAdapter(new HomeGridAdapter(getContext()));
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 23){
                    if(parent instanceof GridView){
                        getFragmentManager().beginTransaction().add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(position))).addToBackStack(null).commit();
                    }
                }
            }
        });

        initBanner();

        return binding.getRoot();
    }

    private void initBanner(){
        HomeBannerAdapter bannerAdapter = new HomeBannerAdapter(getContext(), getFragmentManager(), homeGridHeaderBinding.viewPagerIndicator);
        homeGridHeaderBinding.viewPager.setAdapter(bannerAdapter);
        homeGridHeaderBinding.viewPager.addOnPageChangeListener(bannerAdapter.getOnBannerChangeListener());
    }

    @Override
    public void refreshFragment() {
        homeGridHeaderBinding.viewPager.getAdapter().notifyDataSetChanged();
    }
}
