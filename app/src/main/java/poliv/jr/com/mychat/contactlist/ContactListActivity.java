package poliv.jr.com.mychat.contactlist;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import model.Message;
import poliv.jr.com.mychat.MyChat;
import poliv.jr.com.mychat.R;
import poliv.jr.com.mychat.client.RequestSender;
import poliv.jr.com.mychat.client.listeners.ContactListListener;
import poliv.jr.com.mychat.contactlist.adapter.ContactsViewAdapter;
import poliv.jr.com.mychat.dialog.AddContactDialog;
import poliv.jr.com.mychat.dialog.OkDialog;
import poliv.jr.com.mychat.dialog.RemoveContactDialog;

public class ContactListActivity extends AppCompatActivity implements ContactListListener, ContactsViewAdapter.OnLongPressContactItemListener {


    private RecyclerView recyclerView;
    private ContactsViewAdapter adapter;
    private RequestSender rs = RequestSender.getInstance();
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(getMainLooper());
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddContactDialog addContactDialog = new AddContactDialog();
                addContactDialog.show(getFragmentManager(), "");
            }
        });

        recyclerView = findViewById(R.id.rvContactList);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        adapter = new ContactsViewAdapter(this, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        rs.setContactListListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        rs.removerContactListListener();
    }

    @Override
    public void onContactAdded(final String userName) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.contactAdded(userName);
            }
        });
    }

    @Override
    public void onContactRemoved(final String userName) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.contactRemoved(userName);
            }
        });
    }

    @Override
    public void onContactOnline(final String contactName) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.contactOnline(contactName);
            }
        });
    }

    @Override
    public void onContactOffline(final String contactName) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.contactOnline(contactName);
            }
        });

    }

    @Override
    public void messageReceived(Message receivedMessage) {

    }

    @Override
    public void onLongPress(String userName, View itemView) { //turn itemView back white after dialog answer
        Log.d("Paul", "remove: "+userName);
        RemoveContactDialog dialog = new RemoveContactDialog();
        dialog.setDialog(getFragmentManager(), userName, itemView, this);

        //rs.removeContact(userName);
    }
}
