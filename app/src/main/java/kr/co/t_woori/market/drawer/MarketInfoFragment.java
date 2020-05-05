package kr.co.t_woori.market.drawer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.MarketInfoFragmentBinding;
import kr.co.t_woori.market.naver_map.MapActivity;

/**
 * Created by rladn on 2017-10-23.
 */

public class MarketInfoFragment extends Fragment {

    private MarketInfoFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.market_info_fragment, container, false);

        binding.locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        binding.callBtn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0426256157")));
            }
        });

        binding.callBtn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0426736511")));
            }
        });

        return binding.getRoot();
    }
}
