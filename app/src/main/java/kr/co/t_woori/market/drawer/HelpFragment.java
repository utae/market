package kr.co.t_woori.market.drawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.HelpFragmentBinding;
import kr.co.t_woori.market.utilities.UserInfo;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-23.
 */

public class HelpFragment extends Fragment {

    private HelpFragmentBinding binding;

    private long versionPressedCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.help_fragment, container, false);

        binding.versionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versionPressedCount++;
                if(versionPressedCount == 5){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder.setMessage("관리자 암호를 입력하세요.")
                            .setView(R.layout.master_login_dialog)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(dialog instanceof AlertDialog){
                                        EditText editText = (EditText)((AlertDialog)dialog).findViewById(R.id.edit_text);
                                        if(editText != null && "tw1109".equals(editText.getText().toString())){
                                            UserInfo.setMaster();
                                            Utilities.showToast(getContext(), "관리자 기능이 활성화 되었습니다.");
                                            dialog.dismiss();
                                        }else{
                                            Utilities.showToast(getContext(), "비밀번호가 틀렸습니다.");
                                            dialog.dismiss();
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    versionPressedCount = 0;
                }
            }
        });

        binding.modMyInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().add(R.id.main_container, new ModMyInfoFragment()).addToBackStack(null).commit();
            }
        });

        binding.privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse("http://112.217.229.162/private.html"));
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }
}
