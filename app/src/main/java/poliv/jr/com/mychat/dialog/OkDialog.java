package poliv.jr.com.mychat.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;

public class OkDialog extends DialogFragment {

    public static OkDialog dialog = new OkDialog();

    public OkDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String message = args.getString("message", "");

        return new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setMessage(message)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    public static void setDialog(FragmentManager fragmentManager, String message) {
        Bundle args = new Bundle();
        args.putString("message", message);
        dialog.setArguments(args);
        dialog.show(fragmentManager, "");
    }
}