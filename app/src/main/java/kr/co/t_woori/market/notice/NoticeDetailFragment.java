package kr.co.t_woori.market.notice;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.NoticeDetailFragmentBinding;
import kr.co.t_woori.market.databinding.OrderHistoryDetailFragmentBinding;
import kr.co.t_woori.market.order.OrderGoodsListAdapter;
import kr.co.t_woori.market.order.OrderHistory;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-25.
 */

public class NoticeDetailFragment extends Fragment {

    private NoticeDetailFragmentBinding binding;
    private Notice notice;
    private String noticeNum;

    public static NoticeDetailFragment create(Notice notice){
        NoticeDetailFragment fragment = new NoticeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("notice", notice);
        fragment.setArguments(args);
        return fragment;
    }

    public static NoticeDetailFragment createById(String noticeNum){
        NoticeDetailFragment fragment = new NoticeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("noticeNum", noticeNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notice = (Notice) getArguments().getSerializable("notice");
        noticeNum = getArguments().getString("noticeNum");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.notice_detail_fragment, container, false);

        if(notice != null){
            setNoticeInfo(notice);
        }else{
            getNoticeById(noticeNum);
        }

        return binding.getRoot();
    }

    private void setNoticeInfo(Notice notice){
        binding.titleTextView.setText(notice.getTitle());
        binding.contentTextView.setText(notice.getContent());
        if(notice.hasImg()){
            binding.imgContainer.setVisibility(View.VISIBLE);
            Glide.with(getContext()).asDrawable().load(Utilities.getGoodsImgDir() + "notice/" + notice.getNoticeNum() + ".jpg")
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(binding.imgView);
        }
    }

    private void getNoticeById(final String noticeNum){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(NoticeAPIService.class).getNoticeById(noticeNum)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(results.get("notice") instanceof List){
                    for(Object object : (List)results.get("notice")){
                        if(object instanceof Map){
                            Map map = (Map)object;
                            notice = new Notice((String)map.get("noticeNum"), (String)map.get("time"), (String)map.get("title"), (String)map.get("content"), "Y".equals(map.get("img")));
                            setNoticeInfo(notice);
                        }
                    }
                }
            }
        };
        serverCommunicator.execute();
    }
}
