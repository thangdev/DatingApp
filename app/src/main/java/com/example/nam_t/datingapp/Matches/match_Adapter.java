package com.example.nam_t.datingapp.Matches;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nam_t.datingapp.R;
import com.example.nam_t.datingapp.Users.user_ViewHolder;
import com.example.nam_t.datingapp.Users.user_object;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nam_t on 16-Jan-18.
 */

public class match_Adapter extends RecyclerView.Adapter<match_ViewHolder>{
    private List<match_Object> matchesList;
    private Context context;


    public match_Adapter(List<match_Object> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

    @Override
    public match_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        match_ViewHolder rcv = new match_ViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(match_ViewHolder holder, int position) {
        holder.mMatchId=matchesList.get(position).getUserId();
        holder.mMatchName.setText(matchesList.get(position).getName());
        if(!matchesList.get(position).getProfileImageUrl().equals("")) {
            Picasso.with(context).load(matchesList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
        }else {
            Picasso.with(context).load(R.drawable.ic_person_black_48dp).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.matchesList.size();
    }
}
