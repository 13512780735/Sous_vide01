package com.likeit.sous_vide.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.likeit.sous_vide.R;


public class LoaddingDialog extends Dialog {


    public LoaddingDialog(@NonNull Context context) {
        super(context, R.style.dialogStyle);
        setContentView(R.layout.dialog_loadding);
    }
}
