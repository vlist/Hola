package com.vlist.holaenhanced.Recommend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vlist.holaenhanced.R;

import java.util.List;

public class CardsAdapter extends ArrayAdapter<Cards> {

    Context context;

    public CardsAdapter(Context context, int resourceId, List<Cards> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_recommend, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.recommend_username);
        ImageView image = (ImageView) convertView.findViewById(R.id.recommend_image);

        name.setText(card_item.getName());
        switch (card_item.getProfileImageUrl()) {
            case "default":
                Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
                break;
            default:
                Glide.with(convertView.getContext()).clear(image);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }


        return convertView;

    }
}
