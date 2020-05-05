package kr.co.t_woori.market.naver_map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nhn.android.maps.NMapContext;

/**
 * Created by rladn on 2017-08-16.
 */

public class NMapActivity extends AppCompatActivity {

    protected NMapContext mapContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapContext = new NMapContext(this);
        mapContext.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapContext.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapContext.onResume();
    }

    @Override
    protected void onPause() {
        mapContext.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mapContext.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mapContext.onDestroy();
        super.onDestroy();
    }
}
