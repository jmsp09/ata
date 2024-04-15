package com.unir.ata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class InstrumentCardAdapter extends RecyclerView.Adapter<InstrumentCardAdapter.CardViewHolder> {

    private final List<InstrumentCardItem> instrumentCardItemList;
    private final OnCardItemClickListener listener;

    public interface OnCardItemClickListener {
        void onImageClick(int position);

        void onDescriptionClick(int position);
    }

    public InstrumentCardAdapter(Context context, List<InstrumentCardItem> instrumentCardItemList,
                                 OnCardItemClickListener listener) {
        this.instrumentCardItemList = instrumentCardItemList;
        this.listener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public CardViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.instrument_card_item, parent, false);
        return new CardViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, final int position) {
        final InstrumentCardItem instrumentCardItem = instrumentCardItemList.get(position);

        // Set image and description
        holder.imageView.setImageResource(instrumentCardItem.getImageResourceId());
        holder.imageView.setContentDescription(instrumentCardItem.getText());
        holder.textView.setText(instrumentCardItem.getText());
        holder.textView.setContentDescription(instrumentCardItem.getDescription());

        // Handle click on image
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageClick(position);
                }
            }
        });

        // Handle click on description
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDescriptionClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return instrumentCardItemList.size();
    }
}
