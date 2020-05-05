package kr.co.t_woori.market.drawer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.ModMyInfoFragmentBinding;
import kr.co.t_woori.market.utilities.UserInfo;
import kr.co.t_woori.market.utilities.Utilities;

public class ModMyInfoFragment extends Fragment {

    private ModMyInfoFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.mod_my_info_fragment, container, false);

        binding.nameEdt.setText(UserInfo.getName());

        binding.addressEdt.setText(UserInfo.getAddress());

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modMyInfo(binding.nameEdt.getText().toString().trim(), binding.addressEdt.getText().toString().trim());
            }
        });

        return binding.getRoot();
    }

    private void modMyInfo(final String name, final String address){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(MyInfoAPIService.class).modMyInfo(name, UserInfo.getPhoneNum(), address)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                UserInfo.setName(name);
                UserInfo.setAddress(address);

                Utilities.showToast(getContext(), "정보 수정 완료");

                getActivity().onBackPressed();
            }
        };
        serverCommunicator.execute();
    }
}
