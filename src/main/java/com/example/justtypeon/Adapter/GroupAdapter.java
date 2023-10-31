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
import com.example.justtypeon.Model.GroupChat;
import com.example.justtypeon.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<GroupChat> mGroupChat;
    private String imageURL; // Group image or icon (if available)
    private String currentUserId; // ID of the current user

    public GroupAdapter(Context mContext, List<GroupChat> mGroupChat, String imageURL, String currentUserId) {
        this.mContext = mContext;
        this.mGroupChat = mGroupChat;
        this.imageURL = imageURL;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_lefet, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupChat groupChat = mGroupChat.get(position);
        holder.show_message.setText(groupChat.getMessage());
        holder.member_name.setText(groupChat.getSenderName());

        // Load the group image if available
        if (imageURL != null && !imageURL.isEmpty()) {
            Glide.with(mContext).load(imageURL).into(holder.group_image);
        }

        // Display seen/delivered status if applicable
        if (position == mGroupChat.size() - 1) {
            if (groupChat.isSeen()) {
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mGroupChat.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView show_message;
        TextView member_name;
        ImageView group_image;
        TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            member_name = itemView.findViewById(R.id.member_name);

            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        GroupChat groupChat = mGroupChat.get(position);
        return groupChat.getSenderId().equals(currentUserId) ? MSG_TYPE_RIGHT : MSG_TYPE_LEFT;
    }
}
