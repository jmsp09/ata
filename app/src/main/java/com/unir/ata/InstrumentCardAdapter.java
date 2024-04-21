package com.unir.ata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private View lastTouchedView;

    public interface OnCardItemClickListener {
        //void onImageClick(int position);

        void onTextClick(int position);

        void onTextTouch(int position);
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

                }
            }
        });

        holder.cardItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (listener != null) {
                    listener.onTextTouch(position);

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            // Acción cuando se presiona el dedo sobre el layout
                            break;
                        case MotionEvent.ACTION_MOVE:
                            // Acción cuando se mueve el dedo sobre el layout

                            /*

                           Log.d("!!!!!!111" + v.getParent(), "!!!!!!!!1111");
                            Log.d("!!!!!!222" + (ViewGroup)v.getParent(), "!!!!!!!222");

                            View currentView = findViewAt((ViewGroup) v.getParent(),
                                    (int) event.getRawX(), (int) event.getRawY());

                            Log.d("!!!!!!aaaa!!!!!" + currentView, "!!!!!!!!!!!aaa" + currentView);

                            // Si la vista actual es diferente de la última vista, significa que el usuario ha pasado a un nuevo layout
                            if (currentView != null && currentView != lastTouchedView) {
                                listener.onTextTouch(position);
                                lastTouchedView = currentView;
                                Log.d("!!!!!!aaaa!!!!!", "!!!!!!!!!!!aaa");
                            }*/
                            break;
                        case MotionEvent.ACTION_UP:
                            // Acción cuando se levanta el dedo del layout
                            break;
                    }
                }
                return false; // Retornar true para indicar que se ha manejado el evento
            }
        });


    }

    @Override
    public int getItemCount() {
        return instrumentCardItemList.size();
    }
}
