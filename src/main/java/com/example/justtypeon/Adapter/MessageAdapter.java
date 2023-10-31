package com.example.justtypeon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.justtypeon.Model.Chat;
import com.example.justtypeon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    static final int MSG_TYPE_LEFT =0;
    static final int MSG_TYPE_RIGHT =1;

    Context mContext;
    List<Chat> mchat;
    String imageURL;
    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mchat, String imageURL) {
        this.mContext = mContext;
        this.mchat = mchat;
        this.imageURL = imageURL;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_lefet,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mchat.get(position);
        holder.show_message.setText(chat.getMessage());
        if(imageURL.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(imageURL).into(holder.profile_image);
        }
        if(position==mchat.size()-1){
            if(chat.isIsseen()){
                holder.txt_seen.setText("seen");
            }else{
                holder.txt_seen.setText("Delivered");
            }
        }else{
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }
    class  ViewHolder extends RecyclerView.ViewHolder{
        TextView show_message;
        ImageView profile_image;
        TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mchat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;

        }else{
            return  MSG_TYPE_LEFT;
        }
    }

}
