package poliv.jr.com.mychat.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import poliv.jr.com.mychat.R;

public class AddContactDialog extends DialogFragment {

    Button btnOk, btnCancel;
    EditText editText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_contact);

        LayoutInflater i = getActivity().getLayoutInflater();
        View view = i.inflate(R.layout.add_contact, null);
        editText = view.findViewById(R.id.editText);
        btnOk = view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okButtonOnCLick();
            }
        });
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(view);


        return builder.create();
    }

    private void okButtonOnCLick(){
        Log.d("Paul", "Button works");
    }
}
