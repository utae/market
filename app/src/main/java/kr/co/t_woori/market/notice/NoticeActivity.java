package kr.co.t_woori.market.notice;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

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
import kr.co.t_woori.market.main.SplashActivity;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2018-02-25.
 */

public class NoticeActivity extends Activity {

    private NoticeDetailFragmentBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        binding = DataBindingUtil.setContentView(this, R.layout.notice_detail_fragment);

        binding.buttonContainer.setVisibility(View.VISIBLE);

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                if(getIntent().getExtras() != null){
                    intent.putExtras(getIntent().getExtras());
                }
                startActivity(intent);
            }
        });

        getNoticeById(getIntent().getStringExtra("id"));

    }

    private void setNoticeInfo(Notice notice){
        binding.titleTextView.setText(notice.getTitle());
        binding.contentTextView.setText(notice.getContent());
        if(notice.hasImg()){
            binding.imgContainer.setVisibility(View.VISIBLE);
            Glide.with(this).asDrawable().load(Utilities.getGoodsImgDir() + "notice/" + notice.getNoticeNum() + ".jpg")
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
                this, APICreator.create(NoticeAPIService.class).getNoticeById(noticeNum)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(results.get("notice") instanceof List){
                    for(Object object : (List)results.get("notice")){
                        if(object instanceof Map){
                            Map map = (Map)object;
                            Notice notice = new Notice((String)map.get("noticeNum"), (String)map.get("time"), (String)map.get("title"), (String)map.get("content"), "Y".equals(map.get("img")));
                            setNoticeInfo(notice);
                        }
                    }
                }
            }
        };
        serverCommunicator.execute();
    }

    @Override
    protected void onStop() {
        Glide.with(this).onStop();
        super.onStop();
    }
}
