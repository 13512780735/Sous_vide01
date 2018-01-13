package com.likeit.sous_vide.ui;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.likeit.sous_vide.R;
import com.likeit.sous_vide.listenter.OnLoginInforCompleted;
import com.machtalk.sdk.connect.MachtalkSDK;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetTimeFragment extends DialogFragment implements View.OnClickListener {

    private String time1;
    private TextView tv_cancel, tv_save;
    private String time;
    private NumberPickerView picker_hour;
    private NumberPickerView picker_minute;
    String deviceId="111110001000078288";

    private OnLoginInforCompleted mOnLoginInforCompleted;

    public void setOnLoginInforCompleted(OnLoginInforCompleted onLoginInforCompleted) {
        mOnLoginInforCompleted = onLoginInforCompleted;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.fragment_set_time, container, false);
        initView(view);
        MachtalkSDK.getInstance().setContext(getActivity());
        return view;
    }

    private void initView(View view) {
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_save = view.findViewById(R.id.tv_save);
        picker_hour = view.findViewById(R.id.picker_hour);
        picker_minute = view.findViewById(R.id.picker_minute);
        picker_hour.setOnClickListener(this);
        picker_minute.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        setData(picker_hour, 0, 99, 02);
        setData(picker_minute, 0, 59, 00);
    }

    private void setData(NumberPickerView picker, int minValue, int maxValue, int value) {
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setValue(value);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                // mlistener.onDialogClick("886");
                getDialog().dismiss();
                break;
            case R.id.tv_save:
                String h = picker_hour.getContentByCurrValue();
                String m = picker_minute.getContentByCurrValue();
                time = String.valueOf(Integer.valueOf(h) * 60 + Integer.valueOf(m));
                // mlistener.onDialogClick(time);
                Log.d("TAG", time);
                time1=String.valueOf(Integer.valueOf(m)/60);
                Log.d("TAG", "h-->"+h);
                Log.d("TAG", "m-->"+m);
                MachtalkSDK.getInstance().operateDevice(deviceId,new String[]{"113"},new String[]{m});
               MachtalkSDK.getInstance().operateDevice(deviceId,new String[]{"114"},new String[]{h});
                mOnLoginInforCompleted.inputLoginInforCompleted(time);
                getDialog().dismiss();
                break;
        }
    }

}
