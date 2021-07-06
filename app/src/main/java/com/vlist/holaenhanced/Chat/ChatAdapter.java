package com.vlist.holaenhanced.Chat;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vlist.holaenhanced.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders> {
    private List<ChatObject> chatList;
    private Context context;

    private final int SELF_MSG = 1;
    private final int FRIEND_MSG = 2;

    private Bitmap selfProfileImage;
    private Bitmap friendProfileImage;

    private String selfProfileImageUrl;
    private String friendProfileImageUrl;

    public void setSelfProfileImageUrl(String selfProfileImageUrl) {
        this.selfProfileImageUrl = selfProfileImageUrl;
        //setSelfProfileImage(returnBitMap(selfProfileImageUrl));
    }

    public void setFriendProfileImageUrl(String friendProfileImageUrl) {
        this.friendProfileImageUrl = friendProfileImageUrl;
        //setFriendProfileImage(returnBitMap(friendProfileImageUrl));
    }

    public void setSelfProfileImage(Bitmap selfProfileImage) {
        this.selfProfileImage = selfProfileImage;
    }

    public void setFriendProfileImage(Bitmap friendProfileImage) {
        this.friendProfileImage = friendProfileImage;
    }

    public ChatAdapter(List<ChatObject> matchesList, Context context) {
        this.chatList = matchesList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (chatList.get(position).getCurrentUser()) {
            return SELF_MSG;
        } else {
            return FRIEND_MSG;
        }
    }

    @Override
    public ChatViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = null;
        if (viewType == SELF_MSG) {
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_self, null, false);
        } else {
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_friend, null, false);
        }
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new ChatViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolders holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage());
        if (chatList.get(position).getCurrentUser()) {
            holder.mMessage.setTextColor(ContextCompat.getColor(context, R.color.textColorDark));
            holder.mCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));

            if (selfProfileImageUrl != null) {
                Glide.with(context).load(selfProfileImageUrl).into(holder.mProfile);
            }
        } else {
            holder.mMessage.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.mCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

            if (friendProfileImageUrl != null) {
                Glide.with(context).load(friendProfileImageUrl).into(holder.mProfile);
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }

    /*
     *    get image from network
     *    @param [String]imageURL
     *    @return [BitMap]image
     */
    public Bitmap returnBitMap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
