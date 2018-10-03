package poliv.jr.com.mychat.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import communication.DC;
import communication.DataCarrier;
import poliv.jr.com.mychat.MyChat;
import poliv.jr.com.mychat.R;
import poliv.jr.com.mychat.client.RequestSender;
import poliv.jr.com.mychat.client.listeners.ContactAddedListener;

public class RemoveContactDialog extends DialogFragment {

    String userName = "";
    View itemView;
    ContactAddedListener cal;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.remove_contact)
                .setMessage(getString(R.string.remove_contact_confirmation, userName))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestSender rs = RequestSender.getInstance();
                        DataCarrier response = rs.removeContact(userName);

                        if(RequestSender.responseCheck(response) && response.getData() != null && ( (Boolean) response.getData()) ){//todo add confirmation message
                            OkDialog.setDialog(getFragmentManager(), getString(R.string.contact_removed));
                        }else if(response.getInfo().equals(DC.USERNAME_DOES_NOT_EXIST)){

                            MyChat.myUser.getContacts().remove(userName);
                            OkDialog.setDialog(getFragmentManager(), getString(R.string.user_does_not_exist));
                            Log.d("Paul", userName+" is now removed");
                            if(cal != null)
                                cal.onContactRemoved(userName);
                        }else {
                            OkDialog.setDialog(getFragmentManager(), getString(R.string.error_removing_contact));
                        }
                    }
                })
                .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d("Paul", "dismissed");
        itemView.setBackgroundColor(Color.WHITE);

        super.onDismiss(dialog);
    }

    public void setDialog(FragmentManager fragmentManager, String userName, View itemView, @Nullable ContactAddedListener cal) {
        this.userName = userName;
        this.itemView = itemView;
        this.cal = cal;
        show(fragmentManager, "");
    }
}
