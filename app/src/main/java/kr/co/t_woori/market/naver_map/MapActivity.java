package kr.co.t_woori.market.naver_map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.Location;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.MapActivityBinding;

/**
 * Created by rladn on 2017-10-26.
 */

public class MapActivity extends NMapActivity {

    private MapActivityBinding binding;

    private final int PERMISSIONS_REQUEST_LOCATION = 100;
    private final double MARKET_LONGITUDE = 127.3339284;
    private final double MARKET_LATITUDE = 36.357748;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.map_activity);

        NMapView mapView = binding.mapView;
        mapView.setClientId(getString(R.string.naver_map_client_id));
        mapContext.setupMapView(mapView);
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();
        NMapController mapController = mapView.getMapController();
        mapController.setMapCenter(MARKET_LONGITUDE, MARKET_LATITUDE, 14);

        NMapViewerResourceProvider mapResourceProvider = new NMapViewerResourceProvider(this);
        NMapOverlayManager overlayManager = new NMapOverlayManager(this, mapView, mapResourceProvider);

        NMapPOIdata poidata = new NMapPOIdata(1, mapResourceProvider);
        poidata.beginPOIdata(1);
        poidata.addPOIitem(MARKET_LONGITUDE, MARKET_LATITUDE, "00마트", NMapPOIflagType.PIN, 0, 0);
        poidata.endPOIdata();

        NMapPOIdataOverlay poidataOverlay = overlayManager.createPOIdataOverlay(poidata, null);

        binding.navigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationPermission();
            }
        });
    }

    private void navigate(){
        Location destination = Location.newBuilder("OO마트", MARKET_LONGITUDE, MARKET_LATITUDE).build();
        NaviOptions options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).build();
        KakaoNaviParams params = KakaoNaviParams.newBuilder(destination).setNaviOptions(options).build();
        KakaoNaviService.navigate(this, params);
    }

    private void requestLocationPermission(){
        if(!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_LOCATION);
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_LOCATION);
            }
        }else{
            navigate();
        }
    }

    private boolean isPermissionGranted(String permission){
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_LOCATION :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    navigate();
                }else{
                    Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
