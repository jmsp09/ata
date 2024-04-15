package com.unir.ata;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InstrumentsActivity extends AppCompatActivity
        implements InstrumentCardAdapter.OnCardItemClickListener {

    private RecyclerView recyclerView;
    private InstrumentCardAdapter cardAdapter;
    private List<InstrumentCardItem> instrumentCardItems;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_instruments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Prepare card items
        instrumentCardItems = new ArrayList<>();
        instrumentCardItems.add(new InstrumentCardItem(R.drawable.violin,
                getString(R.string.violin), getString(R.string.violin_description)));
        instrumentCardItems.add(new InstrumentCardItem(R.drawable.clarinete,
                getString(R.string.clarinete), getString(R.string.clarinete_description)));
        instrumentCardItems.add(new InstrumentCardItem(R.drawable.bombardino,
                getString(R.string.trompeta), getString(R.string.trompeta_description)));

        // Set up adapter for RecyclerView
        cardAdapter = new InstrumentCardAdapter(this, instrumentCardItems, this);
        recyclerView.setAdapter(cardAdapter);
    }

    @Override
    public void onImageClick(int position) {
        // Handle click on image
        InstrumentCardItem clickedItem = instrumentCardItems.get(position);
        Toast.makeText(this, "Clicked image of " + clickedItem.getDescription(), Toast.LENGTH_SHORT).show();
        // Add your custom action here (e.g., open detailed view)
    }

    @Override
    public void onDescriptionClick(int position) {
        // Handle click on description
        InstrumentCardItem clickedItem = instrumentCardItems.get(position);
        Toast.makeText(this, "Clicked description of " + clickedItem.getDescription(), Toast.LENGTH_SHORT).show();
        // Add your custom action here (e.g., show more info)
    }
}