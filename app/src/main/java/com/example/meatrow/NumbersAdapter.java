package com.example.meatrow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.NumberViewHolder>{

    private static int viewHolderCount;
    private int numberItems; //Обще количество елементов
    public NumbersAdapter(int numberOfItems){
        numberItems = numberOfItems;
        viewHolderCount = 0;
    }

    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Context contex = parent.getContext();
        int layoutIdForListItem = R.layout.number_list_item; //id Loyaut

        LayoutInflater inflater = LayoutInflater.from(contex);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        NumberViewHolder viewHolder = new NumberViewHolder(view);
        viewHolder.viewHolderIndex.setText("View holer inex: " + viewHolderCount);

        viewHolderCount ++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        //За допомогою цього методу можна обновити значення створених вище приколівв
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder{

        TextView listItemNumberView;
        TextView viewHolderIndex;

        public NumberViewHolder(View itemView) {
            super(itemView);

            listItemNumberView = itemView.findViewById(R.id.tv_number_item);
            viewHolderIndex = itemView.findViewById(R.id.tv_view_holder_number);
        }

        void bind(int listInddex){
            listItemNumberView.setText(String.valueOf(listInddex));
        }
    }
}
