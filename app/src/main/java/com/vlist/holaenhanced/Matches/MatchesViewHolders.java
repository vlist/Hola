package com.vlist.holaenhanced.Matches;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vlist.holaenhanced.R;

import androidx.recyclerview.widget.RecyclerView;

import com.vlist.holaenhanced.Chat.ChatActivity;

import java.io.ByteArrayOutputStream;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mMatchId, mMatchName;
    public ImageView mMatchImage;

    public MatchesViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = (TextView) itemView.findViewById(R.id.match_id);
        mMatchName = (TextView) itemView.findViewById(R.id.match_name);
        mMatchImage = (ImageView) itemView.findViewById(R.id.match_image);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);

        Bundle b = new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        b.putString("username", mMatchName.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }

}
