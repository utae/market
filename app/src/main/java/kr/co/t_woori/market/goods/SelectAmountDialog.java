package kr.co.t_woori.market.goods;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.databinding.SelectAmountDialogBinding;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-23.
 */

public class SelectAmountDialog extends DialogFragment {

    private SelectAmountDialogBinding binding;
    private Goods goods;
    private int amount;
    private String positiveBtnText;

    private OnSelectedAmountListener onSelectedAmountListener;

    public static SelectAmountDialog create(Goods goods, int amount, String positiveBtnText){
        SelectAmountDialog dialog = new SelectAmountDialog();
        Bundle args = new Bundle();
        args.putSerializable("goods", goods);
        args.putInt("amount", amount);
        args.putString("positiveBtnText", positiveBtnText);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goods = (Goods) getArguments().getSerializable("goods");
        amount = getArguments().getInt("amount");
        positiveBtnText = getArguments().getString("positiveBtnText");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.select_amount_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.positiveBtn.setText(positiveBtnText);

        binding.numberPicker.setMinValue(1);
        binding.numberPicker.setMaxValue(30);

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSelectedAmountListener != null){
                    onSelectedAmountListener.onSelected(binding.numberPicker.getValue());
                }
                dismiss();
            }
        });

        return binding.getRoot();
    }

    public SelectAmountDialog setOnSelectedAmountListener(OnSelectedAmountListener onSelectedAmountListener) {
        this.onSelectedAmountListener = onSelectedAmountListener;
        return this;
    }

    public interface OnSelectedAmountListener{
        void onSelected(int amount);
    }
}
