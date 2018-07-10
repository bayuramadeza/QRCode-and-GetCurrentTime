package com.otret.absence.utilities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.otret.absence.R;
import com.otret.absence.interfaces.OnClickListener;
import com.otret.absence.interfaces.OnDialogButtonClickListener;
import com.otret.absence.interfaces.OnDialogWarningListener;

import java.util.Objects;

public class DialogsUtil {
    private Context context;
    private ProgressDialog pDialog;

    public DialogsUtil(Context context) {
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    public void showAbsenceDialog(String title, String postitiveBtnClick, String masuk, String check, String keterangan, final OnClickListener onClickListener){
        TextView tvMasuk, tvCheckIn, tvKeterangan;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(inflater).inflate(R.layout.content_info, null, false);
        tvMasuk = view.findViewById(R.id.tv_masuk);
        tvCheckIn = view.findViewById(R.id.tv_check_in);
        tvKeterangan = view.findViewById(R.id.tv_keterangan);

        tvMasuk.setText(masuk);
        tvCheckIn.setText(check);
        tvKeterangan.setText(ConstantPreferences.STATUS +keterangan);

        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(view);
        builder.setTitle(title)
                .setPositiveButton(postitiveBtnClick, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onClickListener.onClickListener();
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    public void showWarningDialog(String title, String message, String postitiveBtnClick, final OnDialogWarningListener onDialogWarningListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(postitiveBtnClick, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDialogWarningListener.onWarningDialog();
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    public void showQRWarning(String title, String message, String postitiveBtnClick, String negativeBtnClick, final OnDialogButtonClickListener onDialogButtonClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(postitiveBtnClick, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDialogButtonClickListener.onPositiveButtonClicked();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(negativeBtnClick, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDialogButtonClickListener.onNegativeButtonClicked();
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    public void pDialogShow(String message){
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(message);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void pDialogHide(){
        pDialog.dismiss();
    }
}
