package com.example.nam_t.datingapp.Users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nam_t.datingapp.R;
import com.example.nam_t.datingapp.SelectedProfileActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nam_t on 13-Jan-18.
 */

public class user_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public String selectedID;
    public TextView mName,mAge;
    public CircleImageView mPic;
    public user_ViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        mName=itemView.findViewById(R.id.user_name);
        mAge=itemView.findViewById(R.id.user_age);
        mPic=itemView.findViewById(R.id.user_pic);
    }
    public void onClick(View view){
        Intent intent=new Intent(view.getContext(),SelectedProfileActivity.class);
        Bundle b=new Bundle();
        b.putString("SelectedID", selectedID);
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}