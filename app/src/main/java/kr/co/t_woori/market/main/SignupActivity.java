package kr.co.t_woori.market.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.SignupActivityBinding;
import kr.co.t_woori.market.utilities.UserInfo;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-12-20.
 */

public class SignupActivity extends AppCompatActivity {

    private SignupActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signup_activity);

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllFormFilled()){
                    String name = binding.nameEdt.getText().toString().trim();
                    String address = binding.addressEdt.getText().toString().trim();

                    signup(name, UserInfo.getPhoneNum(), address);
                }
            }
        });
    }

    private boolean isAllFormFilled(){
        if(!isFilled(binding.nameEdt)){
            Utilities.showToast(this, "이름을 입력하세요.");
        }else if(!Utilities.isOnlyKorean(binding.nameEdt.getText().toString().trim())){
            Utilities.showToast(this, "이름은 한글만 입력가능합니다.");
        }else if(!isFilled(binding.addressEdt)){
            Utilities.showToast(this, "주소를 입력하세요.");
        }else{
            return true;
        }
        return false;
    }

    private boolean isFilled(EditText editText){
        return isFilled(editText, 1);
    }

    private boolean isFilled(EditText editText, int length){
        return editText.getText().toString().trim().length() >= length;
    }

    private void signup(final String name, String phone, final String address){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(SplashAPIService.class).signup(name, phone, address)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                UserInfo.setName(name);
                UserInfo.setAddress(address);

                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
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
