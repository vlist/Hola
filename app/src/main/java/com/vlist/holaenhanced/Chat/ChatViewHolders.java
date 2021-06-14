package com.vlist.holaenhanced.Chat;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vlist.holaenhanced.R;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mMessage;
    public CardView mCard;
    public RelativeLayout mContainer;

    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMessage = itemView.findViewById(R.id.text_message);
        mCard = itemView.findViewById(R.id.message_container_card);
        mContainer = itemView.findViewById(R.id.message_container);
    }

    @Override
    public void onClick(View view) {
    }
}
