package kr.co.t_woori.market.notice;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.NoticeListFragmentBinding;
import kr.co.t_woori.market.main.MainActivity;
import kr.co.t_woori.market.main.PopupDialog;
import kr.co.t_woori.market.utilities.UserInfo;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-26.
 */

public class NoticeListFragment extends Fragment {

    private NoticeListFragmentBinding binding;
    private ArrayList<Notice> noticeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.notice_list_fragment, container, false);

        noticeList = new ArrayList<>();

        binding.emptyView.emptyTextView.setText("등록된 공지사항이 없습니다.");

        getNotice();

        if(UserInfo.isMaster()){
            binding.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NoticeInsertFragment fragment = new NoticeInsertFragment();
                    fragment.setOnNoticeInsertedListener(new NoticeInsertFragment.OnNoticeInsertedListener() {
                        @Override
                        public void onInserted() {
                            getNotice();
                        }
                    });
                    getFragmentManager().beginTransaction().add(R.id.main_container, fragment).addToBackStack(null).commit();
                }
            });
            binding.addBtn.setVisibility(View.VISIBLE);
        }

        binding.popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupDialog.create(Utilities.convertTimeMillisToTimeFormat(System.currentTimeMillis())).show(getFragmentManager(), "popup");
            }
        });

        return binding.getRoot();
    }

    private void initListView(){
        NoticeListAdapter adapter = new NoticeListAdapter(getContext(), noticeList);
        binding.listView.setAdapter(adapter);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getAdapter().getItem(position) instanceof Notice){
                    Notice notice = (Notice) parent.getAdapter().getItem(position);
                    getFragmentManager().beginTransaction().add(R.id.main_container, NoticeDetailFragment.create(notice)).addToBackStack(null).commit();
                }
            }
        });

        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                if(UserInfo.isMaster()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("공지사항을 삭제하시겠습니까?")
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    delNotice(noticeList.get(position).getNoticeNum());
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
    }

    private void getNotice(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(NoticeAPIService.class).getNotice()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(!noticeList.isEmpty()){
                    noticeList.clear();
                }
                if("0".equals(results.get("count"))){
                    binding.listView.setEmptyView(binding.emptyView.emptyTextView);
                }else{
                    if(results.get("notice") instanceof List){
                        for(Object object : (List)results.get("notice")){
                            if(object instanceof Map){
                                Map map = (Map)object;
                                noticeList.add(new Notice((String)map.get("noticeNum"), (String)map.get("time"), (String)map.get("title"), (String)map.get("content"), "Y".equals(map.get("img"))));
                            }
                        }
                    }
                    initListView();
                }
            }
        };
        serverCommunicator.execute();
    }

    private void delNotice(String noticeNum){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(NoticeAPIService.class).delNotice(noticeNum)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "삭제 되었습니다.");
                getNotice();
            }
        };
        serverCommunicator.execute();
    }

    public void showNoticeDetailFragmentByNoticeNum(String noticeNum){

    }
}
