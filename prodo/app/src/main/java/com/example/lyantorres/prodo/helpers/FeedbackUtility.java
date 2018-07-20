package com.example.lyantorres.prodo.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class FeedbackUtility {

    public void showToastWith(Context _context, String _message, Integer _length){
        Toast.makeText(_context, _message, _length).show();
    }

    public void showAlertDialogWith(Context _context, String _title, String _message){
        AlertDialog alertDialog = new AlertDialog.Builder(_context).create();
        alertDialog.setTitle(_title);
        alertDialog.setMessage(_message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
