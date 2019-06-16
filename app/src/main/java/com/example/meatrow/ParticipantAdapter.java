package com.example.meatrow;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.PartViewHolder>{
    private static final String TAG = "Neko";
    public List<User> userList = new ArrayList<>();

    public PartViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Log.d(TAG, "onCreateViewHolder Start");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_item_view, parent, false);
        Log.d(TAG, "onCreateViewHolder End");
        return new PartViewHolder(view);
    }

    public void onBindViewHolder(PartViewHolder holder, int possition) {
        Log.d(TAG, "onBindViewHolder Start");
        Log.d(TAG, "oBVH possition: " + possition);
        //Log.d(TAG, "oBVH possition: " + meetList.get(possition).Time);
        holder.bind(userList.get(possition));
        Log.d(TAG, "onBindViewHolder False");
    }

    public int getItemCount() {
        Log.d(TAG, "GetItemCount");
        return userList.size();
    }

    public void updateReceiptsList(List<User> newlist) {
        userList.clear();
        userList.addAll(newlist);
        this.notifyDataSetChanged();
    }

    class PartViewHolder extends RecyclerView.ViewHolder{
        private TextView partNameView;

        public PartViewHolder(final View itemView) {
            super(itemView);

            partNameView = itemView.findViewById(R.id.tvName);
            Log.d(TAG, "MeetViewHolder");
        }

        public void bind(User user){
            Log.d(TAG, "bind Start");
            Log.d(TAG, "bind NAme: "+user.name);
            partNameView.setText(user.name);
            Log.d(TAG, "bind End");
        }
    }

    public void setItems(Collection<User> users){
        Log.d(TAG, "Set item Meet List Start");
        userList.addAll(users);
        notifyDataSetChanged();
        Log.d(TAG, "Set item Meet List Close");
    }

    public  void clearItems(){
        userList.clear();
        notifyDataSetChanged();
    }
}