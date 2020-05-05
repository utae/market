package kr.co.t_woori.market.order;

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
import kr.co.t_woori.market.databinding.TimePickerDialogBinding;

/**
 * Created by rladn on 2017-10-23.
 */

public class TimePickerDialog extends DialogFragment {

    private TimePickerDialogBinding binding;

    private OnSelectedListener onSelectedListener;

    public static TimePickerDialog create(){
        TimePickerDialog dialog = new TimePickerDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.time_picker_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSelectedListener != null){
                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                        onSelectedListener.onSelected(binding.timePicker.getHour(), binding.timePicker.getMinute());
                    }else{
                        onSelectedListener.onSelected(binding.timePicker.getCurrentHour(), binding.timePicker.getCurrentMinute());
                    }
                }
                dismiss();
            }
        });

        return binding.getRoot();
    }

    public TimePickerDialog setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
        return this;
    }

    public interface OnSelectedListener{
        void onSelected(int hour, int min);
    }
}
