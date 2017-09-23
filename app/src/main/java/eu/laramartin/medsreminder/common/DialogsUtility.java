package eu.laramartin.medsreminder.common;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.firebase.FirebaseUtility;
import eu.laramartin.medsreminder.meds.MedsAdapterItem;
import eu.laramartin.medsreminder.model.Permission;

public class DialogsUtility {

    public static void showRemoveMedDialog(Context context, final MedsAdapterItem medsAdapterItem) {
        new AlertDialog.Builder(context)
                .setMessage(R.string.dialog_delete_med_description)
                .setPositiveButton(R.string.dialog_delete_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseUtility.removeMed(medsAdapterItem);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                })
                .create()
                .show();
    }

    public static void showInviteFriendDialog(Context context) {
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        new AlertDialog.Builder(context)
            .setTitle(R.string.dialog_permissions_title)
            .setView(input)
                .setMessage(R.string.dialog_give_permissions_to_friend)
                .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: 16.09.17 Lara: invite friend via intent email
                        String email = input.getText().toString();
                        Permission permission = new Permission();
                        permission.setEmail(email);
                        FirebaseUtility.writePermissionOnDb(permission);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                })
                .create()
                .show();
    }

    public static void showRevokePermissionDialog(Context context, final Permission permission) {
        new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.dialog_delete_permission_description, permission.getEmail()))
                .setPositiveButton(R.string.dialog_delete_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseUtility.removePermission(permission);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                })
                .create()
                .show();
    }
}
