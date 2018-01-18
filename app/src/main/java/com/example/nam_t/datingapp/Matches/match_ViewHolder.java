package com.example.nam_t.datingapp.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.nam_t.datingapp.Chat.ChatActivity;
import com.example.nam_t.datingapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nam_t on 16-Jan-18.
 */

public class match_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMatchName;
    public String mMatchId;
    public CircleImageView mMatchImage;
    public match_ViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchName = (TextView) itemView.findViewById(R.id.user_name);

        mMatchImage = (CircleImageView) itemView.findViewById(R.id.user_pic);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchId", mMatchId);
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
