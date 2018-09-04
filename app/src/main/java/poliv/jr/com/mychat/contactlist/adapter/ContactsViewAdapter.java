package poliv.jr.com.mychat.contactlist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import poliv.jr.com.mychat.MyChat;
import poliv.jr.com.mychat.R;

public class ContactsViewAdapter extends RecyclerView.Adapter<ContactsViewAdapter.ViewHolder> {

    private List<String> contactUserNames;
    private Context context;

    public ContactsViewAdapter(Context context){
        contactUserNames = new LinkedList<>(MyChat.myUser.getContacts().keySet());
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(position);
    }

    @Override
    public int getItemCount() {
        return MyChat.myUser.getContacts().size();
    }

    public void contactOnline(String userName){
        notifyItemChanged(contactUserNames.indexOf(userName));
    }

    public void contactAdded(String userName){
        contactUserNames.add(userName);
        notifyItemInserted(contactUserNames.size() - 1);
    }

    public void contactRemoved(String userName){
        int position = contactUserNames.indexOf(userName);
        contactUserNames.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,contactUserNames.size());
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView cvProfilePic;
        TextView tvContactName;


        protected ViewHolder(View itemView) {
            super(itemView);
            cvProfilePic = itemView.findViewById(R.id.cvProfilePic);
            tvContactName = itemView.findViewById(R.id.tvContactName);
        }

        protected void onBindView(int position) {
            String userName = contactUserNames.get(position);
            tvContactName.setText(userName);

            if(MyChat.myUser.getContacts().get(userName)){//if user online
                cvProfilePic.setBorderColor(ContextCompat.getColor(context, R.color.contact_online));
            }else{
                cvProfilePic.setBorderColor(ContextCompat.getColor(context, R.color.contact_offline));
            }
        }
    }
}
