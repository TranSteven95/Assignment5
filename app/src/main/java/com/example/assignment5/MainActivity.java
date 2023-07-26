package com.example.assignment5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button startGameButton;
    private TextView winsTextView;
    private TextView lossesTextView;
    private TextView gamesPlayedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        winsTextView = findViewById(R.id.winsTextView);
        lossesTextView = findViewById(R.id.lossesTextView);
        gamesPlayedTextView = findViewById(R.id.gamesPlayedTextView);

        int wins = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("wins", 0);
        int losses = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("losses", 0);
        int gamesPlayed = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("gamesPlayed", 0);

        winsTextView.setText("Wins: " + wins);
        lossesTextView.setText("Losses: " + losses);
        gamesPlayedTextView.setText("Games Played: " + gamesPlayed);

        startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start the game activity
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
}