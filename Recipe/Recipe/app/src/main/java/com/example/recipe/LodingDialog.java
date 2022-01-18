package com.example.recipe;



import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;


public class LodingDialog extends Dialog {

    public LodingDialog(Context context, String strText) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
        setContentView(R.layout.popup_loading);

        TextView tvText = (TextView) findViewById(R.id.tvtitle);
        tvText.setText(strText);
    }
}