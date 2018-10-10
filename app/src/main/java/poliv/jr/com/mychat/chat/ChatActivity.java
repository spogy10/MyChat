package poliv.jr.com.mychat.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import poliv.jr.com.mychat.R;

public class ChatActivity extends AppCompatActivity {

    private String contactName;
    public final static String CONTACT_NAME = "CONTACT NAME";

    private TextView tvContactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        contactName = intent.getStringExtra(CONTACT_NAME);
        tvContactName = findViewById(R.id.tvContactName);
        tvContactName.setText(contactName);
    }
}
