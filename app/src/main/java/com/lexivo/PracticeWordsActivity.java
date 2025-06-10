package com.lexivo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class PracticeWordsActivity extends AppCompatActivity {
    private final float NEXT_CARD_INITIAL_SCALE = 0.4f;
    private Dictionary dictionary;
    private List<Word> words;
    private CardView languageFlag, cardOnTop, cardNext;
    private RelativeLayout cardsContainer;
    private ConstraintLayout layoutNoMoreWords;
    private TextView textNoWordsInDictionary;
    private Button btnShuffleAndStartAgain;
    private int nextWordIndex = 0;
    private int screenWidth;
    private boolean flipped = false;

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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        initViews();
        initStartingContent();
        populateCards();
    }

    private void initViews() {
        languageFlag = findViewById(R.id.languageFlag);
        cardsContainer = findViewById(R.id.cardsContainer);
        layoutNoMoreWords = findViewById(R.id.layoutNoMoreWords);
        btnShuffleAndStartAgain = findViewById(R.id.btnShuffleAndStartAgain);
        textNoWordsInDictionary = findViewById(R.id.textNoWordsInDictionary);
    }

    private void initStartingContent() {
        Intent intent = getIntent();
        dictionary = Dictionary.getDictionaryById(intent.getStringExtra(IntentUtil.KEY_DICTIONARY_ID));
        languageFlag.setForeground(ResourcesCompat.getDrawable(getResources(), dictionary.getLanguage().getFlag(), null));
        if (dictionary.getAllWordsCount() == 0)
            textNoWordsInDictionary.setVisibility(View.VISIBLE);

        words = dictionary.getAllWordsShuffled();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTouchListeners(View card) {
        card.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeRight() {
                animateAndUpdate('r');
            }

            @Override
            public void onSwipeLeft() {
                animateAndUpdate('l');
            }

            @Override
            public void onClick() {
                    if (flipped) return;
                    card.animate().setDuration(130).scaleX(0.2f).rotationYBy(-90).withEndAction(() -> {
                    card.setRotationY(90);
                    card.animate().setDuration(130).scaleX(1).rotationYBy(-90);
                    flipped = true;
                });
            }

            private void animateAndUpdate(char direction) {
                flipped = false;
                int rotation = direction == 'r' ? -15 : 15;
                int translation = direction == 'r' ? screenWidth : -screenWidth;
                addNextCard();
                card.animate().setDuration(250).rotation(-rotation).translationX(translation).withEndAction(this::updateLogic);
            }

            private void updateLogic() {
                cardsContainer.removeView(cardOnTop);
                if (cardNext != null) {
                    cardNext.animate().setDuration(100).scaleX(1).scaleY(1).withEndAction(() -> updateCardStackByOne());
                }
            }

            private void addNextCard() {
                if (cardNext != null) {
                    addCardToCardsContainer(cardNext, 1);
                }
                else {
                    layoutNoMoreWords.setVisibility(View.VISIBLE);
                    if (!btnShuffleAndStartAgain.hasOnClickListeners())
                        btnShuffleAndStartAgain.setOnClickListener(v -> shuffleAndStartAgain());
                }
                cardsContainer.bringChildToFront(cardOnTop);
            }
        });
    }

    private void populateCards() {
        cardsContainer.removeAllViewsInLayout();
        if (!words.isEmpty()) {
            cardOnTop = generateCard(words.get(0));
            addCardToCardsContainer(cardOnTop, 0);
            nextWordIndex = 1;
        }
        if (words.size() > 1) {
            cardNext = generateCard(words.get(nextWordIndex++));
            cardNext.setScaleX(NEXT_CARD_INITIAL_SCALE);
            cardNext.setScaleY(NEXT_CARD_INITIAL_SCALE);
        }
    }

    private void updateCardStackByOne() {
        cardOnTop = cardNext;
        try {
            cardNext = generateCard(words.get(nextWordIndex++));
            cardNext.setScaleX(NEXT_CARD_INITIAL_SCALE);
            cardNext.setScaleY(NEXT_CARD_INITIAL_SCALE);
        } catch (IndexOutOfBoundsException obe) {
            cardNext = null;
        }
    }

    private void shuffleAndStartAgain() {
        layoutNoMoreWords.setVisibility(View.GONE);
        cardsContainer.removeAllViewsInLayout();
        words = dictionary.getAllWordsShuffled();
        nextWordIndex = 0;
        flipped = false;
        populateCards();
    }

    private void addCardToCardsContainer(CardView card, int index) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cardsContainer.addView(card, index, lp);
    }

    private CardView generateCard(Word word) {
        if (word == null) return null;

//        TODO: Properly style the card

        @SuppressLint("InflateParams")
        CardView card = (CardView) LayoutInflater.from(this).inflate(R.layout.card_word, null);
        ConstraintLayout innerLayout = (ConstraintLayout) card.getChildAt(0);
        TextView textType = innerLayout.findViewById(R.id.wordType);
        TextView textGender = innerLayout.findViewById(R.id.wordGender);
        TextView wordOriginalFront = innerLayout.findViewById(R.id.wordOriginalFront);
        TextView wordOriginalAdditionFront = innerLayout.findViewById(R.id.wordOriginalAdditionFront);
        textType.setText(word.getType().toString());
        textGender.setText(word.getGender() == null ? null : word.getGender().toString().toLowerCase());
        setWordOriginalFront(wordOriginalFront, word.getOriginal().getValue());
        setWordOriginalAdditionFront(wordOriginalAdditionFront, word.getOriginal().getDetails());
        addTouchListeners(card);
        return card;
    }

    private void setWordOriginalFront(TextView textView, String text) {
        textView.setText(text);

        if (text == null) return;

        if (text.length() > 30) {
            textView.setTextSize(8);
        }
        else if (text.length() > 26) {
            textView.setTextSize(12);
        }
        else if (text.length() > 20){
            textView.setTextSize(16);
        }
        else if (text.length() > 14) {
            textView.setTextSize(24);
        } else {
            textView.setTextSize(40);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setWordOriginalAdditionFront(TextView textView, String text) {
        textView.setText("(" + text + ")");

        if (text == null) return;

        if (text.length() > 26) {
            textView.setTextSize(8);
        }
        else if (text.length() > 20){
            textView.setTextSize(10);
        }
        else if (text.length() > 14) {
            textView.setTextSize(16);
        } else {
            textView.setTextSize(30);
        }
    }
}