package com.example.nam_t.datingapp.Chat;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nam_t.datingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by khanhnguyen on 1/15/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHoler>{

    ArrayList<ChatObject> chatOnjects;

    Context context;

    public ChatAdapter(ArrayList<ChatObject> chatOnjects, Context context) {
        this.chatOnjects = chatOnjects;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(chatOnjects.get(position).isCurrentUser()) {
            return 1;
        }
        return 0;
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == 1) {
            View itemView = layoutInflater.inflate(R.layout.user_sent_message, parent, false);
            return new ViewHoler(itemView);
        }
        View itemView = layoutInflater.inflate(R.layout.user_received_message, parent, false);
        return new ViewHoler(itemView);

    }



    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        holder.txtMessage.setText(chatOnjects.get(position).getTextMessage());
        if(chatOnjects.get(position).getImageProfileUser() == null || chatOnjects.get(position).getImageProfileUser().toString().isEmpty()) {
            holder.imgMessageProfile.setImageResource(R.drawable.ic_person_black_48dp);
        } else {
            Picasso.with(context).load(chatOnjects.get(position).getImageProfileUser()).into(holder.imgMessageProfile);
        }
    }

    @Override
    public int getItemCount() {
        return chatOnjects.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        TextView txtMessage;
        CircleImageView imgMessageProfile;

        public ViewHoler(View itemView) {
            super(itemView);
            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            imgMessageProfile = (CircleImageView) itemView.findViewById(R.id.imgMessageProfile);
        }
    }
}