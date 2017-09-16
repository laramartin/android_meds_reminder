package eu.laramartin.medsreminder.common;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.firebase.FirebaseUtility;
import eu.laramartin.medsreminder.meds.MedsAdapterItem;

public class DialogsUtility {

    public static void showRemoveMedDialog(Context context, final MedsAdapterItem medsAdapterItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.dialog_delete_med_description);
        builder.setPositiveButton(R.string.dialog_delete_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FirebaseUtility.removeMed(medsAdapterItem);
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showInviteFriendDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_permissions_title);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        builder.setMessage(R.string.dialog_give_permissions_to_friend);
        builder.setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // TODO: 16.09.17 Lara: assert that input is email

                // TODO: 16.09.17 Lara: save email to db
                // TODO: 16.09.17 Lara: invite friend
                String email = input.getText().toString();
                Log.i("Permissions dialog", "Email: " + email);
                FirebaseUtility.writePermissionOnDb(email);
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
