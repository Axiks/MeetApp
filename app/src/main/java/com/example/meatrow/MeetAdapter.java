package com.example.meatrow;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MeetAdapter extends RecyclerView.Adapter<MeetAdapter.MeetViewHolder>{
    private static final String TAG = "Neko";
    private List<Meet> meetList = new ArrayList<>();

    public MeetViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Log.d(TAG, "onCreateViewHolder Start");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meet_item_view, parent, false);
        Log.d(TAG, "onCreateViewHolder End");
        return new MeetViewHolder(view);
    }

    public void onBindViewHolder(MeetViewHolder holder, int possition) {
        Log.d(TAG, "onBindViewHolder Start");
        Log.d(TAG, "oBVH possition: " + possition);
        Log.d(TAG, "oBVH possition: " + meetList.get(possition).Time);
        holder.bind(meetList.get(possition));
        Log.d(TAG, "onBindViewHolder False");
    }

    public int getItemCount() {
        Log.d(TAG, "GetItemCount");
        return meetList.size();
    }

    class MeetViewHolder extends RecyclerView.ViewHolder{
        private ImageView meetAvatarView;
        private TextView meetNameView;
        private TextView meetDescriptionView;
        private TextView meetDateView;
        private TextView meetTimeView;
        private TextView meetUserCountView;

        public MeetViewHolder(View itemView) {
            super(itemView);
            meetAvatarView = itemView.findViewById(R.id.meet_avatar_view);
            meetNameView = itemView.findViewById(R.id.meet_name_view);
            meetDescriptionView = itemView.findViewById(R.id.meet_description_view);
            meetDateView = itemView.findViewById(R.id.meet_date_view);
            meetTimeView = itemView.findViewById(R.id.meet_time_view);
            meetUserCountView = itemView.findViewById(R.id.meet_count_view);

            Log.d(TAG, "MeetViewHolder");
        }

        public void bind(Meet meet){
            Log.d(TAG, "bind Start");
            Log.d(TAG, "bind NAme: "+meet.Name);
            meetNameView.setText(meet.Name);
            Log.d(TAG, "bind Desc: "+meet.Description);
            meetDescriptionView.setText(meet.Description);
            Log.d(TAG, "bind Count: "+meet.UserCount);
            meetUserCountView.setText(meet.UserCount.toString());

            Log.d(TAG, "bind End");
        }
    }

    public void setItems(Collection<Meet> meets){
        Log.d(TAG, "Set item Meet List Start");
        meetList.addAll(meets);
        notifyDataSetChanged();
        Log.d(TAG, "Set item Meet List Close");
    }

    public void setTest(){
        notifyDataSetChanged();
        Log.d(TAG, "Set item Test Meet List");
    }

    public  void clearItems(){
        meetList.clear();
        notifyDataSetChanged();
    }
}
