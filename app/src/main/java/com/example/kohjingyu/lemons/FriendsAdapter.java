package com.example.kohjingyu.lemons;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by setia on 12/5/2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private JSONArray friendsJSONArray;
    Bitmap[] bitmaps;
    Context parentContext;

    FriendsAdapter(Context context, JSONArray friendsJSONArray, Bitmap[] bitmaps){
        this.parentContext = context;
        this.friendsJSONArray = friendsJSONArray;
        this.bitmaps = bitmaps;
    }

    public void update(JSONArray friendsJSONArray, Bitmap[] bitmaps){
        this.friendsJSONArray = friendsJSONArray;
        this.bitmaps = bitmaps;
    }


    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        View view = inflater.inflate(R.layout.friends_view_holder, parent, false);

        FriendsViewHolder friendsViewHolder = new FriendsViewHolder(view);

        return friendsViewHolder;
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return friendsJSONArray.length();
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView userInformationTextView;
        View v;
        FriendsViewHolder(View v){
            super(v);
            this.v = v;
        }

        public void bind(int position){
            userInformationTextView = this.v.findViewById(R.id.viewholder_textview);
            try {
                JSONObject temp = friendsJSONArray.getJSONObject(position);
                String name  = temp.getString("name");
                String username = temp.getString("username");
                userInformationTextView.setText(name + "\n" + username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (bitmaps[position] != null) {
                Bitmap avatar = bitmaps[position];
                avatarImageView = this.v.findViewById(R.id.viewholder_avatar);
                avatarImageView.setImageBitmap(avatar);
            }
        }
    }

}
