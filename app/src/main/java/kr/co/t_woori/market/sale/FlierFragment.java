package kr.co.t_woori.market.sale;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import kr.co.t_woori.market.main.MainActivity;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-11-24.
 */

public class FlierFragment extends DialogFragment {

    private FlierBinding binding;

    private Flier flier;

    private ArrayList<TextView> nameTextViewList;
    private ArrayList<TextView> priceTextViewList;
    private ArrayList<ImageView> imgViewList;

    public static FlierFragment create(Flier flier){
        FlierFragment fragment = new FlierFragment();
        Bundle args = new Bundle();
        args.putSerializable("flier", flier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flier = (Flier)(getArguments().getSerializable("flier"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.flier, container, false);

        initViewList();

        binding.titleTextView.setText(flier.getTitle());
        binding.timeTextView.setText(Utilities.convertStringToDateFormat(flier.getTime(), "yyyyMMddHHmmss", "yyyy년 MM월 dd일 HH:mm"));
        binding.contentTextView.setText(flier.getContent());

        getFlierGoods(flier.getId());

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(getActivity() instanceof MainActivity){
                    ((MainActivity)getActivity()).getMainViewPager().setCurrentItem(3);
                }
            }
        });

        return binding.getRoot();
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

    private void getFlierGoods(String id){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(FlierAPIService.class).getFlierGoods(id)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(results.get("goods") instanceof List){
                    for(int i = 0; i < ((List)results.get("goods")).size(); i++){
                        Object object = ((List)results.get("goods")).get(i);
                        if(object instanceof Map){
                            Map map = (Map)object;

                            if("Y".equals(map.get("img"))){
                                final int tempIndex = i;
                                Glide.with(getContext()).asDrawable().load(Utilities.getGoodsImgDir() + map.get("barcode") + ".jpg")
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

                            nameTextViewList.get(i).setText((String)map.get("name"));

                            if(map.get("discount") == null){
                                priceTextViewList.get(i).setText(map.get("price") + "원");
                            }else{
                                priceTextViewList.get(i).setText(map.get("discount") + "원");
                            }
                        }
                    }
                }
            }
        };
        serverCommunicator.execute();
    }
}
