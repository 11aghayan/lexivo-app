package com.lexivo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Word;
import com.lexivo.util.IntentUtil;
import com.lexivo.util.OnSwipeTouchListener;
import com.lexivo.util.SystemUtil;

import java.util.List;

//TODO: if word is long make text size smaller, if short bigger; 12sp - 40sp
public class PracticeWordsActivity extends AppCompatActivity {
    private Dictionary dictionary;
    private List<Word> words;
    private CardView languageFlag;
    private LinearLayout cardsContainer;


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
        SystemUtil.hideSystemUi(getWindow());

        initViews();
        initStartingContent();
        populateCards();
    }

    private void initViews() {
        languageFlag = findViewById(R.id.languageFlag);
        cardsContainer = findViewById(R.id.cardsContainer);
    }

    private void initStartingContent() {
        Intent intent = getIntent();
        dictionary = Dictionary.getDictionaryById(intent.getStringExtra(IntentUtil.KEY_DICTIONARY_ID));
        languageFlag.setForeground(ResourcesCompat.getDrawable(getResources(), dictionary.getLanguage().getFlag(), null));
        words = dictionary.getAllWordsShuffled();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTouchListeners(View card) {
        card.setOnTouchListener(new OnSwipeTouchListener(this){
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
                    card.animate().setDuration(130).scaleX(0.2f).rotationYBy(-90).withEndAction(() -> {
                    card.setRotationY(90);
                    card.animate().setDuration(130).scaleX(1).rotationYBy(-90);
                });
            }
        });
    }

    private void populateCards() {
        cardsContainer.removeAllViewsInLayout();
        for (Word word : words) {
            @SuppressLint("InflateParams")
            CardView cardWord = (CardView) LayoutInflater.from(this).inflate(R.layout.card_word, null);
            ConstraintLayout innerLayout = (ConstraintLayout) cardWord.getChildAt(0);
            TextView textType = (TextView) innerLayout.getChildAt(0);
            TextView textGender = (TextView) innerLayout.getChildAt(1);
            TextView textWord = (TextView) innerLayout.getChildAt(2);
            textType.setText(word.getType().toString());
            textGender.setText(word.getGender() == null ? null : word.getGender().toString().toLowerCase());
            textWord.setText(word.getOriginal().getValue());
            addTouchListeners(cardWord);
            cardsContainer.addView(cardWord, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            cardsContainer.bringChildToFront(cardWord);
        }
    }
}