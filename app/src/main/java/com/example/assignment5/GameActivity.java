package com.example.assignment5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private Deck deck;
    private List<Card> playerCards, dealerCards;

    private TextView playerCardsTextView, dealerCardsTextView;
    private LinearLayout playerCardsLayout, dealerCardsLayout;
    private Button hitButton, stayButton;

    private ImageView winningImageView;
    private ImageView losingImageView;
    private ImageView dealerCard1ImageView;
    private ImageView dealerCard2ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playerCardsTextView = findViewById(R.id.playerCardsTextView);
        dealerCardsTextView = findViewById(R.id.dealerCardsTextView);
        playerCardsLayout = findViewById(R.id.playerCardsLayout);
        dealerCardsLayout = findViewById(R.id.dealerCardsLayout);
        hitButton = findViewById(R.id.hitButton);
        stayButton = findViewById(R.id.stayButton);
        Button restartButton = findViewById(R.id.restartButton);
        Button mainMenuButton = findViewById(R.id.mainMenuButton);
        winningImageView = findViewById(R.id.winningImageView);
        losingImageView = findViewById(R.id.losingImageView);
        // Initialize the deck and deal the initial cards
        deck = new Deck();
        playerCards = new ArrayList<>();
        dealerCards = new ArrayList<>();
        dealInitialCards();

        // Set up the click listeners for the hit and stay buttons
        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hit();
            }
        });

        stayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stay();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void dealInitialCards() {
        // Deal two cards to the player and dealer
        for (int i = 0; i < 2; i++) {
            playerCards.add(deck.drawCard());
            dealerCards.add(deck.drawCard());
        }

        // Display the player's cards
        displayPlayerCards();

        // Display the dealer's cards
        displayDealerCards();
    }

    private void hit() {
        // Draw a card and add it to the player's hand
        Card card = deck.drawCard();
        playerCards.add(card);

        // Display the player's cards
        displayPlayerCards();

        // Check if the player busts
        if (getHandValue(playerCards) > 21) {
            displayDealerafterCards();
            endGame("You bust!");

        }
    }

    private void stay() {
        // Dealer hits until their hand is at least 17
        while (getHandValue(dealerCards) < 17) {
            Card card = deck.drawCard();
            dealerCards.add(card);
        }

        // Display the dealer's cards
        displayDealerafterCards();

        // Check if the dealer busts
        if (getHandValue(dealerCards) > 21) {
            endGame("Dealer busts!");
            winningImageView.setVisibility(View.VISIBLE);

        } else {
            // Determine the winner
            int playerHandValue = getHandValue(playerCards);
            int dealerHandValue = getHandValue(dealerCards);

            if (playerHandValue > dealerHandValue) {
                endGame("You win!");
            } else if (playerHandValue == dealerHandValue) {
                endGame("Push!");
            } else {
                endGame("Dealer wins!");
            }
        }
    }

    private void restartGame() {
        // Reset the game variables
        winningImageView.setVisibility(View.INVISIBLE);
        losingImageView.setVisibility(View.INVISIBLE);
        playerCards.clear();
        dealerCards.clear();
        deck = new Deck();
        deck.shuffle();

        // Hide the game over text and the dealer's second card
        dealerCardsLayout.getChildAt(1).setVisibility(View.INVISIBLE);

        // Deal new cards and update the display
        dealInitialCards();
        displayPlayerCards();
        displayDealerCards();

        // Enable the hit and stay buttons
        hitButton.setEnabled(true);
        stayButton.setEnabled(true);
    }
    private int getHandValue(List<Card> hand) {
        int handValue = 0;
        boolean hasAce = false;

        // Calculate the value of the hand
        for (Card card : hand) {
            if (card.getRank().getValue() == 1) {
                hasAce = true;
                handValue += 11;
            } else {
                handValue += card.getRank().getValue();
            }
        }

        // Adjust the value of the ace if necessary
        if (hasAce && handValue > 21) {
            handValue -= 10;
        }

        return handValue;
    }

    private void displayPlayerCards() {
        // Clear the player's cards layout
        playerCardsLayout.removeAllViews();

        // Iterate over the player's cards and add TextViews for each card
        int handValue = 0;
        int aceCount = 0;
        for (Card card : playerCards) {
            // Create a TextView for the card
            TextView cardTextView = new TextView(this);
            cardTextView.setText(card.toString());
            playerCardsLayout.addView(cardTextView);

            // Update the hand value
            int cardValue = card.getRank().getValue();
            if (cardValue == 1) {
                aceCount++;
                handValue += 11;
            } else {
                handValue += cardValue;
            }
        }

        // If the hand value is over 21 and there are aces in the hand, convert the value of the aces from 11 to 1.
        while (handValue > 21 && aceCount > 0) {
            handValue -= 10;
            aceCount--;
        }

        // Display the hand value
        playerCardsTextView.setText("Your cards (" + handValue + "):");
    }

    private void displayDealerCards() {
        // Clear the dealer's cards layout
        dealerCardsLayout.removeAllViews();

        // Create a TextView for the first card
        Card firstCard = dealerCards.get(0);
        TextView firstCardTextView = new TextView(this);
        firstCardTextView.setText(firstCard.toString());
        dealerCardsLayout.addView(firstCardTextView);

        // Create a TextView for the second card (hidden)
        TextView secondCardTextView = new TextView(this);
        secondCardTextView.setText("Hidden");
        dealerCardsLayout.addView(secondCardTextView);

        // Display the dealer's cards
        dealerCardsTextView.setText("Dealer's cards:");
    }
    private void displayDealerafterCards() {
        // Clear the dealer's cards layout
        dealerCardsLayout.removeAllViews();

        // Iterate over the dealer's cards and add TextViews for each card
        int handValue = 0;
        for (Card card : dealerCards) {
            // Create a TextView for the card
            TextView cardTextView = new TextView(this);
            cardTextView.setText(card.toString());
            dealerCardsLayout.addView(cardTextView);

            // Update the hand value
            handValue += card.getRank().getValue();
        }

        // Display the hand value
        dealerCardsTextView.setText("Dealer's cards (" + handValue + "):");
    }
    private void endGame(String message) {
        // Determine the winner and update the win/loss statistics
        int playerHandValue = getHandValue(playerCards);
        int dealerHandValue = getHandValue(dealerCards);
        if (playerHandValue > dealerHandValue && playerHandValue <= 21) {
            // Player wins
            winningImageView.setVisibility(View.VISIBLE);
            int wins = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("wins", 0);
            getSharedPreferences("PREFS", MODE_PRIVATE).edit().putInt("wins", wins + 1).apply();

        } else if (playerHandValue == dealerHandValue) {
            // Push
        } else {
            // Dealer wins
            losingImageView.setVisibility(View.VISIBLE);
            int losses = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("losses", 0);
            getSharedPreferences("PREFS", MODE_PRIVATE).edit().putInt("losses", losses + 1).apply();
        }

        // Increment the games played
        int gamesPlayed = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("gamesPlayed", 0);
        getSharedPreferences("PREFS", MODE_PRIVATE).edit().putInt("gamesPlayed", gamesPlayed + 1).apply();

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        hitButton.setEnabled(false);
        stayButton.setEnabled(false);
    }
}
