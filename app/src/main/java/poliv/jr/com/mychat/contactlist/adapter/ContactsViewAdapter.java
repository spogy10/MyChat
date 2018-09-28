package poliv.jr.com.mychat.contactlist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import poliv.jr.com.mychat.MyChat;
import poliv.jr.com.mychat.R;

public class ContactsViewAdapter extends RecyclerView.Adapter<ContactsViewAdapter.ViewHolder> {

    private List<String> contactUserNames;
    private Context context;
    private OnLongPressContactItemListener onLongPressContactItemListener;

    public ContactsViewAdapter(Context context, OnLongPressContactItemListener listener){
        contactUserNames = new LinkedList<>(MyChat.myUser.getContacts().keySet());
        this.context = context;
        onLongPressContactItemListener = listener;
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
        LinearLayout llContact;


        protected ViewHolder(View itemView) {
            super(itemView);
            cvProfilePic = itemView.findViewById(R.id.cvProfilePic);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            llContact = itemView.findViewById(R.id.llContact);
        }

        protected void onBindView(int position) {
            final String userName = contactUserNames.get(position);
            tvContactName.setText(userName);

            if(MyChat.myUser.getContacts().get(userName)){//if user online
                cvProfilePic.setBorderColor(ContextCompat.getColor(context, R.color.contact_online));
            }else{
                cvProfilePic.setBorderColor(ContextCompat.getColor(context, R.color.contact_offline));
            }

            llContact.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemView.setBackgroundColor(Color.GRAY);
                    onLongPressContactItemListener.onLongPress(userName, itemView);
                    return true;
                }
            });

        }
    }

    public interface OnLongPressContactItemListener {
        void onLongPress(String userName, View itemView);
    }
}
