package com.unir.ata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
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

        void onTextClick(int position);
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
        /*holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageClick(position);
                }
            }
        });*/

        // Handle click on description
        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onTextClick(position);
                    Vibrator vibrator = (Vibrator) holder.cardItem.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    // Verificar si el dispositivo soporta la vibración y si el permiso está concedido
                    if (vibrator != null && vibrator.hasVibrator()) {
                        // Vibrar durante 100 milisegundos
                        /*vibrator.vibrate(VibrationEffect
                                .createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));*/
                        //TODO https://developer.android.com/develop/ui/views/haptics/actuators
                        long[] timings = new long[] { 40, 50, 20 }; //TODO Vibración corta para onTouch, investigar una larga para onClick
                        int[] amplitudes = new int[] { 17, 29, 44};
                        int repeatIndex = -1; // Do not repeat.

                        vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex));
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return instrumentCardItemList.size();
    }
}
