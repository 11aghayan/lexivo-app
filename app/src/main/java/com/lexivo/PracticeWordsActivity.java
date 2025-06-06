package com.lexivo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Word;
import com.lexivo.util.OnSwipeTouchListener;

import java.util.List;

//TODO: if word is long make text size smaller, if short bigger; 12sp - 40sp
public class PracticeWordsActivity extends AppCompatActivity {
    private Dictionary dictionary;
    private List<Word> words;
    private CardView languageFlag;
    private MaterialCardView wordCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practice_words);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initStartingContent();
        handleWordCardInteractions();
    }

    private void initViews() {
        languageFlag = findViewById(R.id.languageFlag);
        wordCard = findViewById(R.id.wordCard);
    }

    private void initStartingContent() {
        Intent intent = getIntent();
        dictionary = Dictionary.getDictionaryById(intent.getStringExtra("dictionary_id"));
        languageFlag.setForeground(ResourcesCompat.getDrawable(getResources(), dictionary.getLanguage().getFlag(), null));
        words = dictionary.getAllWordsShuffled();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void handleWordCardInteractions() {
        wordCard.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeRight() {
//                TODO: Implement functionality
                Toast.makeText(PracticeWordsActivity.this, "Swipe right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
//                TODO: Implement functionality
                Toast.makeText(PracticeWordsActivity.this, "Swipe left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClick() {
                wordCard.animate().setDuration(130).scaleX(0.2f).rotationYBy(-90).withEndAction(() -> {
                    wordCard.setRotationY(90);
                    wordCard.animate().setDuration(130).scaleX(1).rotationYBy(-90);
                });
            }
        });
    }
}