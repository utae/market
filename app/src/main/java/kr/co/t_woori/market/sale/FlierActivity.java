package kr.co.t_woori.market.sale;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.FlierBinding;
import kr.co.t_woori.market.goods.Goods;
import kr.co.t_woori.market.main.SplashActivity;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-11-24.
 */

public class FlierActivity extends Activity {

    private FlierBinding binding;

    private ArrayList<TextView> nameTextViewList;
    private ArrayList<TextView> priceTextViewList;
    private ArrayList<ImageView> imgViewList;
    private ArrayList<Goods> goodsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        binding = DataBindingUtil.setContentView(this, R.layout.flier);

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FlierActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                if(getIntent().getExtras() != null){
                    intent.putExtras(getIntent().getExtras());
                }
                startActivity(intent);
            }
        });

        binding.goods01.setOnClickListener(new OnGoodsClickListener());
        binding.goods02.setOnClickListener(new OnGoodsClickListener());
        binding.goods03.setOnClickListener(new OnGoodsClickListener());
        binding.goods04.setOnClickListener(new OnGoodsClickListener());
        binding.goods05.setOnClickListener(new OnGoodsClickListener());
        binding.goods06.setOnClickListener(new OnGoodsClickListener());
        binding.goods07.setOnClickListener(new OnGoodsClickListener());
        binding.goods08.setOnClickListener(new OnGoodsClickListener());
        binding.goods09.setOnClickListener(new OnGoodsClickListener());
        binding.goods10.setOnClickListener(new OnGoodsClickListener());

        initViewList();

        goodsList = new ArrayList<>();

        if(getIntent().getStringExtra("id") != null && !"".equals(getIntent().getStringExtra("id"))){
            getFlier(getIntent().getStringExtra("id"));
        }else{
            getLatestFlier();
        }
    }

    private void initViewList(){
        nameTextViewList = new ArrayList<>();
        priceTextViewList = new ArrayList<>();
        imgViewList = new ArrayList<>();

        nameTextViewList.add(binding.nameTextView01);
        nameTextViewList.add(binding.nameTextView02);
        nameTextViewList.add(binding.nameTextView03);
        nameTextViewList.add(binding.nameTextView04);
        nameTextViewList.add(binding.nameTextView05);
        nameTextViewList.add(binding.nameTextView06);
        nameTextViewList.add(binding.nameTextView07);
        nameTextViewList.add(binding.nameTextView08);
        nameTextViewList.add(binding.nameTextView09);
        nameTextViewList.add(binding.nameTextView10);

        priceTextViewList.add(binding.priceTextView01);
        priceTextViewList.add(binding.priceTextView02);
        priceTextViewList.add(binding.priceTextView03);
        priceTextViewList.add(binding.priceTextView04);
        priceTextViewList.add(binding.priceTextView05);
        priceTextViewList.add(binding.priceTextView06);
        priceTextViewList.add(binding.priceTextView07);
        priceTextViewList.add(binding.priceTextView08);
        priceTextViewList.add(binding.priceTextView09);
        priceTextViewList.add(binding.priceTextView10);

        imgViewList.add(binding.imgView01);
        imgViewList.add(binding.imgView02);
        imgViewList.add(binding.imgView03);
        imgViewList.add(binding.imgView04);
        imgViewList.add(binding.imgView05);
        imgViewList.add(binding.imgView06);
        imgViewList.add(binding.imgView07);
        imgViewList.add(binding.imgView08);
        imgViewList.add(binding.imgView09);
        imgViewList.add(binding.imgView10);
    }

    private void getFlier(String id){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(FlierAPIService.class).getFlierGoods(id)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                binding.titleTextView.setText((String)results.get("title"));
                binding.timeTextView.setText(Utilities.convertStringToDateFormat((String)results.get("time"), "yyyyMMddHHmmss", "yyyy년 MM월 dd일 HH:mm"));
                binding.contentTextView.setText((String)results.get("content"));
                if(results.get("goods") instanceof List){
                    for(int i = 0; i < ((List)results.get("goods")).size(); i++){
                        Object object = ((List)results.get("goods")).get(i);
                        if(object instanceof Map){
                            Map map = (Map)object;

                            String barcode = (String)map.get("barcode");
                            String name = (String)map.get("name");
                            String price = (String)map.get("price");
                            String capacity = (String)map.get("capacity");
                            String discount;
                            if(map.get("discount") == null){
                                discount = null;
                            }else{
                                discount = (String)map.get("discount");
                            }
                            boolean hasImg = "Y".equals(map.get("img"));

                            if(discount == null){
                                goodsList.add(new Goods(barcode, name, price, capacity, hasImg));
                            }else{
                                goodsList.add(new Goods(barcode, name, price, capacity, hasImg, discount));
                            }

                            if(hasImg){
                                final int tempIndex = i;
                                Glide.with(FlierActivity.this).asDrawable().load(Utilities.getGoodsImgDir() + barcode + ".jpg")
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                imgViewList.get(tempIndex).setImageResource(R.drawable.img_loading);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                return false;
                                            }
                                        }).into(imgViewList.get(i));
                            }else{
                                imgViewList.get(i).setImageResource(R.drawable.img_loading);
                            }

                            nameTextViewList.get(i).setText(name);

                            if(map.get("discount") == null){
                                priceTextViewList.get(i).setText(price + "원");
                            }else{
                                priceTextViewList.get(i).setText(discount + "원");
                            }
                        }
                    }
                }
            }
        };
        serverCommunicator.execute();
    }

    private void getLatestFlier(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(FlierAPIService.class).getLatestFlierGoods()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                binding.titleTextView.setText((String)results.get("title"));
                binding.timeTextView.setText(Utilities.convertStringToDateFormat((String)results.get("time"), "yyyyMMddHHmmss", "yyyy년 MM월 dd일 HH:mm"));
                binding.contentTextView.setText((String)results.get("content"));
                if(results.get("goods") instanceof List){
                    for(int i = 0; i < ((List)results.get("goods")).size(); i++){
                        Object object = ((List)results.get("goods")).get(i);
                        if(object instanceof Map){
                            Map map = (Map)object;

                            String barcode = (String)map.get("barcode");
                            String name = (String)map.get("name");
                            String price = (String)map.get("price");
                            String capacity = (String)map.get("capacity");
                            String discount;
                            if(map.get("discount") == null){
                                discount = null;
                            }else{
                                discount = (String)map.get("discount");
                            }
                            boolean hasImg = "Y".equals(map.get("img"));

                            if(discount == null){
                                goodsList.add(new Goods(barcode, name, price, capacity, hasImg));
                            }else{
                                goodsList.add(new Goods(barcode, name, price, capacity, hasImg, discount));
                            }

                            if(hasImg){
                                final int tempIndex = i;
                                Glide.with(FlierActivity.this).asDrawable().load(Utilities.getGoodsImgDir() + barcode + ".jpg")
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                imgViewList.get(tempIndex).setImageResource(R.drawable.img_loading);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                return false;
                                            }
                                        }).into(imgViewList.get(i));
                            }else{
                                imgViewList.get(i).setImageResource(R.drawable.img_loading);
                            }

                            nameTextViewList.get(i).setText(name);

                            if(map.get("discount") == null){
                                priceTextViewList.get(i).setText(price + "원");
                            }else{
                                priceTextViewList.get(i).setText(discount + "원");
                            }
                        }
                    }
                }
            }
        };
        serverCommunicator.execute();
    }

    private class OnGoodsClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent;
            Utilities.logD("Test", "flier activity goods click");
            switch (view.getId()){
                case R.id.goods_01 :
                    intent = new Intent(FlierActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tag", "flier");
                    intent.putExtra("goods", goodsList.get(0));
                    startActivity(intent);
                    break;

                case R.id.goods_02 :
                    intent = new Intent(FlierActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tag", "flier");
                    intent.putExtra("goods", goodsList.get(1));
                    startActivity(intent);
                    break;

                case R.id.goods_03 :
                    intent = new Intent(FlierActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tag", "flier");
                    intent.putExtra("goods", goodsList.get(2));
                    startActivity(intent);
                    break;

                case R.id.goods_04 :
                    intent = new Intent(FlierActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tag", "flier");
                    intent.putExtra("goods", goodsList.get(3));
                    startActivity(intent);
                    break;

                case R.id.goods_05 :
                    intent = new Intent(FlierActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tag", "flier");
                    intent.putExtra("goods", goodsList.get(4));
                    startActivity(intent);
                    break;

                case R.id.goods_06 :
                    intent = new Intent(FlierActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tag", "flier");
                    intent.putExtra("goods", goodsList.get(5));
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        Glide.with(this).onStop();
        super.onStop();
    }
}
