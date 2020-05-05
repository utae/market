package kr.co.t_woori.market.notice;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.HashMap;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.NoticeInsertFragmentBinding;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-26.
 */

public class NoticeInsertFragment extends Fragment{

    private NoticeInsertFragmentBinding binding;
    private OnNoticeInsertedListener onNoticeInsertedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.notice_insert_fragment, container, false);

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        binding.insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllFormFilled()){
                    insertNotice(binding.titleTextView.getText().toString().trim(), binding.contentTextView.getText().toString().trim());
                }
            }
        });

        return binding.getRoot();
    }

    private boolean isAllFormFilled(){
        if(!isFilled(binding.titleTextView)){
            Utilities.showToast(getContext(), "제목을 입력하세요.");
        }else if(!isFilled(binding.contentTextView)){
            Utilities.showToast(getContext(), "내용을 입력하세요.");
        }else{
            return true;
        }
        return false;
    }

    private boolean isFilled(EditText editText){
        return editText.getText() != null && !"".equals(editText.getText().toString().trim());
    }

    private void insertNotice(String title, String content){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(NoticeAPIService.class).insertNotice(title, content)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "등록되었습니다.");
                if(onNoticeInsertedListener != null){
                    onNoticeInsertedListener.onInserted();
                }
                getFragmentManager().popBackStack();
            }
        };
        serverCommunicator.execute();
    }

    public void setOnNoticeInsertedListener(OnNoticeInsertedListener onNoticeInsertedListener) {
        this.onNoticeInsertedListener = onNoticeInsertedListener;
    }

    public interface OnNoticeInsertedListener{
        void onInserted();
    }
}
