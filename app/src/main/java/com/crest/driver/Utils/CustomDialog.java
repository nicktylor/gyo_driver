package com.crest.driver.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.crest.driver.R;


/**
 * Created by annie on 1/2/17.
 */

public class CustomDialog {

    Dialog dialog;

    public CustomDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public void show(){
        dialog.show();
    }

    public void hide(){
        dialog.dismiss();
    }
}
