package com.unir.ata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InstrumentCardAdapter extends RecyclerView.Adapter<InstrumentCardAdapter.CardViewHolder> {

    private final List<InstrumentCardItem> instrumentCardItemList;
    private final OnCardItemClickListener listener;
    private Context context;

    public interface OnCardItemClickListener {
        //void onImageClick(int position);

        void onClick(int position);

        void onLongClick(int position);
    }

    public InstrumentCardAdapter(Context context, List<InstrumentCardItem> instrumentCardItemList,
                                 OnCardItemClickListener listener) {
        this.instrumentCardItemList = instrumentCardItemList;
        this.listener = listener;
        this.context = context;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout cardItem;
        public ImageView imageView;
        public TextView textView;

        public CardViewHolder(View itemView) {
            super(itemView);
            cardItem = itemView.findViewById(R.id.cardItem);
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

    @SuppressLint({"RecyclerView", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, final int position) {
        final InstrumentCardItem instrumentCardItem = instrumentCardItemList.get(position);

        // Set image and description
        holder.imageView.setImageResource(instrumentCardItem.getImageResourceId());
        holder.imageView.setContentDescription(instrumentCardItem.getText());
        holder.textView.setText(instrumentCardItem.getText());
        holder.textView.setContentDescription(instrumentCardItem.getDescription());
        holder.cardItem.setContentDescription(instrumentCardItem.getDescription());
        holder.cardItem.setTag(instrumentCardItem.getDescription());


        holder.cardItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(position);
            }
        });
        holder.cardItem.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onLongClick(position);
            }
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return instrumentCardItemList.size();
    }
}
