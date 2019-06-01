package com.example.meatrow;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MeetAdapter extends RecyclerView.Adapter<MeetAdapter.MeetViewHolder>{
    private static final String TAG = "Neko";
    public List<Meet> meetList = new ArrayList<>();

    public MeetViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Log.d(TAG, "onCreateViewHolder Start");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meet_item_view, parent, false);
        Log.d(TAG, "onCreateViewHolder End");
        return new MeetViewHolder(view);
    }

    public void onBindViewHolder(MeetViewHolder holder, int possition) {
        Log.d(TAG, "onBindViewHolder Start");
        Log.d(TAG, "oBVH possition: " + possition);
        //Log.d(TAG, "oBVH possition: " + meetList.get(possition).Time);
        holder.bind(meetList.get(possition));
        Log.d(TAG, "onBindViewHolder False");
    }

    public int getItemCount() {
        Log.d(TAG, "GetItemCount");
        return meetList.size();
    }

    public void updateReceiptsList(List<Meet> newlist) {
        meetList.clear();
        meetList.addAll(newlist);
        this.notifyDataSetChanged();
    }

    class MeetViewHolder extends RecyclerView.ViewHolder{
        private ImageView meetAvatarView;
        private TextView meetNameView;
        private TextView meetDescriptionView;
        private TextView meetDateView;
        private TextView meetTimeView;
        private TextView meetUserCountView;

        public MeetViewHolder(final View itemView) {
            super(itemView);
            meetAvatarView = itemView.findViewById(R.id.meet_avatar_view);
            meetNameView = itemView.findViewById(R.id.meet_name_view);
            meetDescriptionView = itemView.findViewById(R.id.meet_description_view);
            meetDateView = itemView.findViewById(R.id.meet_date_view);
            meetTimeView = itemView.findViewById(R.id.meet_time_view);
            meetUserCountView = itemView.findViewById(R.id.meet_count_view);

            //Обработчик натиснень
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String res = meetList.get(position).id;
                    Log.d(TAG, res);

                    Intent intent = new Intent(v.getContext(), MeetProfile.class);
                    intent.putExtra("meetId", res);
                    v.getContext().startActivity(intent);
                }
            });

            Log.d(TAG, "MeetViewHolder");
        }

        public void bind(Meet meet){
            Log.d(TAG, "bind Start");
            Log.d(TAG, "bind NAme: "+meet.name);
            meetNameView.setText(meet.name);
            Log.d(TAG, "bind Desc: "+meet.description);
            meetDescriptionView.setText(meet.description);
            Log.d(TAG, "bind Count: "+meet.meetStart);
            meetUserCountView.setText(meet.meetStart);
            //meetAvatarView.
            Picasso.get().load(meet.getAvatar()).into(meetAvatarView);
            //Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/meet-f86fb.appspot.com/o/uploads%2Fimages%2Fd40376b4-f54e-4487-8c20-416782459c10?alt=media&token=cb89bb54-a963-474e-8886-03a7e0c827a4").into(meetAvatarView);
            //Log.d(TAG, "mmmmmm: " + meet.getAvatarHref());
            Log.d(TAG, "bind End");
        }
    }

    public void setItems(Collection<Meet> meets){
        Log.d(TAG, "Set item Meet List Start");
        meetList.addAll(meets);
        notifyDataSetChanged();
        Log.d(TAG, "Set item Meet List Close");
    }

    public  void clearItems(){
        meetList.clear();
        notifyDataSetChanged();
    }
}
