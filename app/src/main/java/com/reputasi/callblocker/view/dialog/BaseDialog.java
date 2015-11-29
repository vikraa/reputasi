package com.reputasi.callblocker.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;

/**
 * Created by vikraa on 5/17/2015.
 */
public class BaseDialog extends DialogFragment {

    public interface BaseDialogListener {
        void OnItemClick(BaseDialog dialog, int optionId);
    }

    protected BaseDialogListener mListener;

    public void setListener(BaseDialogListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
