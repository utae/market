package kr.co.t_woori.market.main;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.PopupDialogBinding;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2018-01-02.
 */

public class PopupDialog extends DialogFragment {

    private PopupDialogBinding binding;
    private String curDate;

    public static PopupDialog create(String curDate){
        PopupDialog popupDialog = new PopupDialog();
        Bundle args = new Bundle();
        args.putString("curDate", curDate);
        popupDialog.setArguments(args);
        return popupDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curDate = getArguments().getString("curDate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.popup_dialog, container, false);

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        Glide.with(getContext()).asDrawable().load(Utilities.getGoodsImgDir() + "popup.jpg").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                binding.imgView.setImageResource(R.drawable.img_loading);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(binding.imgView);

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.checkbox.isChecked()){
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    prefs.edit().putString("popup", curDate).apply();
                }
                dismiss();
            }
        });

        return binding.getRoot();
    }
}
