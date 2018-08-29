package poliv.jr.com.mychat.contactlist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import poliv.jr.com.mychat.MyChat;
import poliv.jr.com.mychat.R;

public class ContactListActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: add contact
            }
        });

        recyclerView = findViewById(R.id.rvContactList);
        Toast.makeText(this, "Username: "+MyChat.myUser.getUserName()+", Passsword: "+MyChat.myUser.getPassword(), Toast.LENGTH_SHORT).show();

    }

}
