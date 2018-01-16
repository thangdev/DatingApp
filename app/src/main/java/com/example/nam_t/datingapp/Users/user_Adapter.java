package com.example.nam_t.datingapp.Users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nam_t.datingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nam_t on 13-Jan-18.
 */

public class user_Adapter extends RecyclerView.Adapter<user_ViewHolder> {
    private List<user_object> user_list;
    private Context context;
    public user_Adapter(List<user_object> user_list, Context context){
        this.user_list=user_list;
        this.context=context;
    }
    @Override
    public user_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,null,false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams((lp));
        user_ViewHolder rcv=new user_ViewHolder(layoutView);
        return rcv;
    }
    @Override
    public void onBindViewHolder(user_ViewHolder holder,int position){
        holder.selectedID = user_list.get(position).getUserID();
        holder.mName.setText(user_list.get(position).getUser_name());
        holder.mAge.setText("Age: "+ user_list.get(position).getUser_age());
        if(!user_list.get(position).getUser_ProfileImgURL().equals("")) {
            Picasso.with(context).load(user_list.get(position).getUser_ProfileImgURL()).into(holder.mPic);
        }else {
            Picasso.with(context).load(R.drawable.ic_person_black_48dp).into(holder.mPic);
        }
    }
    public int getItemCount(){return this.user_list.size();}
}