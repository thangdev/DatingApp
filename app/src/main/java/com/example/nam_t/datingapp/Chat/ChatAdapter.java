package com.example.nam_t.datingapp.Chat;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nam_t.datingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by khanhnguyen on 1/15/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    ArrayList<ChatObject> chatOnjects;

    Context context;

    public ChatAdapter(ArrayList<ChatObject> chatOnjects, Context context) {
        this.chatOnjects = chatOnjects;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(chatOnjects.get(position).isCurrentUser()) {
            if(chatOnjects.get(position).isImageMessage()) {
                return 11;
            }
            return 1;
        } else {
            if(chatOnjects.get(position).isImageMessage()) {
                return 22;
            } else {
                return 0;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == 1) {
            View itemView = layoutInflater.inflate(R.layout.user_sent_message, parent, false);
            return new ViewHolder(itemView);
        } else if(viewType == 11) {
            View itemView = layoutInflater.inflate(R.layout.user_sent_image_message, parent, false);
            return new ViewHolder(itemView);
        } else if(viewType == 22) {
            View itemView = layoutInflater.inflate(R.layout.user_received_image_message, parent, false);
            return new ViewHolder(itemView);
        } else {
            View itemView = layoutInflater.inflate(R.layout.user_received_message, parent, false);
            return new ViewHolder(itemView);
        }

    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.time=chatOnjects.get(position).getTime();
        if(chatOnjects.get(position).isImageMessage()) {
            Picasso.with(context).load(chatOnjects.get(position).getImageMessageDb()).into(holder.imgMessage);
        } else {
            holder.txtMessage.setText(chatOnjects.get(position).getTextMessage());
        }
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;
        CircleImageView imgMessageProfile;
        ImageView imgMessage;
        String time;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            imgMessageProfile = (CircleImageView) itemView.findViewById(R.id.imgMessageProfile);
            imgMessage = (ImageView) itemView.findViewById(R.id.imgMessage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"Sent at "+time,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}