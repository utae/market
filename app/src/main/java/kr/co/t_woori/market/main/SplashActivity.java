package kr.co.t_woori.market.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import java.util.HashMap;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.SplashActivityBinding;
import kr.co.t_woori.market.utilities.UserInfo;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-24.
 */

public class SplashActivity extends AppCompatActivity {

    private SplashActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.splash_activity);

        Utilities.clearGlideCache(this);

        if (isPermissionGranted(Manifest.permission.READ_PHONE_STATE) && isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            savePhoneNum();
        } else {
            requestPermission();
        }
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    savePhoneNum();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("권한 오류").setMessage("권한을 얻어오는데 실패하였습니다.\n앱을 종료합니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                }
        }
    }

    private void savePhoneNum() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("권한 오류").setMessage("권한을 얻어오는데 실패하였습니다.\n앱을 종료합니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
            return;
        }
        @SuppressLint("HardwareIds") String phoneNum = telephonyManager != null ? telephonyManager.getLine1Number() : null;
        if(phoneNum == null || "".equals(phoneNum.trim())){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("인증 오류").setMessage("핸드폰 번호를 읽어오는데 실패하였습니다.\n앱을 종료합니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }else{
            if(phoneNum.startsWith("+82")){
                phoneNum = phoneNum.replace("+82", "0");
            }
            UserInfo.setPhoneNum(phoneNum);

            isUser(phoneNum);
        }
    }

    private void isUser(String phone){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(SplashAPIService.class).isUser(phone)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Intent intent;
                if("0".equals(results.get("count"))){
                    intent = new Intent(SplashActivity.this, SignupActivity.class);
                }else{
                    UserInfo.setName((String)results.get("name"));
                    UserInfo.setAddress((String)results.get("address"));

                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                if(getIntent().getExtras() != null){
                    intent.putExtras(getIntent().getExtras());
                }
                startActivity(intent);
            }
        };
        serverCommunicator.execute();
    }
}
