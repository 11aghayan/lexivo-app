package com.lexivo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.lexivo.schema.Gender;
import com.lexivo.schema.Word;
import com.lexivo.schema.WordType;
import com.lexivo.util.IntentUtil;
import com.lexivo.util.OnSwipeTouchListener;
import com.lexivo.util.SystemUtil;
import com.lexivo.util.ViewUtil;

import java.util.LinkedList;

public class PracticeWordsActivity extends AppCompatActivity {
    private final float NEXT_CARD_INITIAL_SCALE = 0.4f;
    private Dictionary dictionary;
    private LinkedList<Word> words;
    private CardView languageFlag, cardOnTop, cardNext;
    private RelativeLayout cardsContainer;
    private ConstraintLayout layoutNoMoreWords;
    private TextView textNoWordsInDictionary;
    private Button btnShuffleAndStartAgain;
    private int nextWordIndex = 0;
    private int screenWidth;
    private boolean flipped = false;
    private int editedWordIndex = -1;

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
    private void addTouchListeners(CardView card, Word word) {
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
                flipCard(card, word);
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
                    cardNext.animate().setDuration(100).scaleX(1).scaleY(1).withEndAction(() -> shiftActiveCards());
                }
            }

            private void addNextCard() {
                if (cardNext != null) {
                    addCardToCardsContainer(cardNext, 1);
                }
                else {
                    showNoMoreWordsLayout();
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

    private void shiftActiveCards() {
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

    private void flipCard(CardView card, Word word) {
        if (flipped) return;
        flipped = true;
        card.animate().setDuration(130).scaleX(0.2f).rotationYBy(-90).withEndAction(() -> {
            card.setRotationY(90);
            expandAllData(card, word);
            card.animate().setDuration(130).scaleX(1).rotationYBy(-90);
        });
    }

    private void setCardData(CardView card, Word word) {
        ConstraintLayout innerLayout = (ConstraintLayout) card.getChildAt(0);
        TextView textType = innerLayout.findViewById(R.id.wordType);
        TextView textGender = innerLayout.findViewById(R.id.wordGender);
        TextView wordSingular = innerLayout.findViewById(R.id.wordSingular);
        TextView wordSingularAddition = innerLayout.findViewById(R.id.wordSingularAddition);
        TextView wordPlural = innerLayout.findViewById(R.id.wordPlural);
        TextView wordPast1 = innerLayout.findViewById(R.id.wordPast1);
        TextView wordPast2 = innerLayout.findViewById(R.id.wordPast2);
        TextView wordTranslation = innerLayout.findViewById(R.id.wordTranslation);
        TextView wordTranslationAddition = innerLayout.findViewById(R.id.wordTranslationAddition);
        TextView comment = innerLayout.findViewById(R.id.comment);
        Button btnEditWord = innerLayout.findViewById(R.id.btnEditWord);

        textType.setText(word.getType().toString());
        if (WordType.NOUN.equals(word.getType()) && word.getGender() != null) {
            String[] genderData = ViewUtil.getGenderStringAndColorArray(this, word.getGender());
            textGender.setText(genderData[0]);
            textGender.setTextColor(Integer.parseInt(genderData[1]));
            textGender.setVisibility(View.VISIBLE);
        }
        else
            textGender.setVisibility(View.GONE);

        if (word.getOriginal().getValue() != null && !(Gender.PLURAL.equals(word.getGender())))
            setTextMain(wordSingular, word.getOriginal().getValue());
        else
            setTextMain(wordSingular, word.getPlural());

        if (word.getOriginal().getDetails() != null) {
            setTextAddition(wordSingularAddition, word.getOriginal().getDetails());
            wordSingularAddition.setVisibility(View.VISIBLE);
        }
        else
            wordSingularAddition.setVisibility(View.GONE);

        setTextMain(wordPlural, word.getPlural());
        setTextMain(wordPast1, word.getPast1());
        setTextMain(wordPast2, word.getPast2());
        setTextMain(wordTranslation, word.getTranslation().getValue());
        setTextAddition(wordTranslationAddition, word.getTranslation().getDetails());
        comment.setText(word.getComment());

        btnEditWord.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditWordActivity.class);
            intent.putExtra(IntentUtil.KEY_WORD_ID, word.getId());
            intent.putExtra(IntentUtil.KEY_DICTIONARY_ID, dictionary.getId());
            editedWordIndex = words.indexOf(word);
            startActivity(intent);
        });
    }

    private void expandAllData(CardView card, Word word) {
        ConstraintLayout innerLayout = (ConstraintLayout) card.getChildAt(0);
        LinearLayout wordTranslationContainer = innerLayout.findViewById(R.id.wordTranslationContainer);
        LinearLayout commentContainer = innerLayout.findViewById(R.id.commentContainer);
        TextView wordPlural = innerLayout.findViewById(R.id.wordPlural);
        TextView wordPast1 = innerLayout.findViewById(R.id.wordPast1);
        TextView wordPast2 = innerLayout.findViewById(R.id.wordPast2);
        TextView wordTranslationAddition = innerLayout.findViewById(R.id.wordTranslationAddition);
        Button btnEditWord = innerLayout.findViewById(R.id.btnEditWord);

        if (word.getTranslation().getValue() != null) {
            wordTranslationContainer.setVisibility(View.VISIBLE);
            if (word.getTranslation().getDetails() != null) {
                wordTranslationAddition.setVisibility(View.VISIBLE);
            }
            else
                wordTranslationAddition.setVisibility(View.GONE);}
        else
            wordTranslationContainer.setVisibility(View.GONE);

        if (word.getComment() != null)
            commentContainer.setVisibility(View.VISIBLE);
        else
            commentContainer.setVisibility(View.GONE);

        if (WordType.NOUN.equals(word.getType()) && word.getPlural() != null && word.getOriginal().getValue() != null)
            wordPlural.setVisibility(View.VISIBLE);
        else
            wordPlural.setVisibility(View.GONE);

        if (WordType.VERB.equals(word.getType()) && word.getPast1() != null)
            wordPast1.setVisibility(View.VISIBLE);
        else
            wordPast1.setVisibility(View.GONE);

        if (WordType.VERB.equals(word.getType()) && word.getPast2() != null)
            wordPast2.setVisibility(View.VISIBLE);
        else
            wordPast2.setVisibility(View.GONE);

        btnEditWord.setVisibility(View.VISIBLE);
    }

    private void addCardToCardsContainer(CardView card, int index) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cardsContainer.addView(card, index, lp);
    }

    private CardView generateCard(Word word) {
        if (word == null) return null;

        @SuppressLint("InflateParams")
        CardView card = (CardView) LayoutInflater.from(this).inflate(R.layout.card_word, null);
        setCardData(card, word);
        addTouchListeners(card, word);
        return card;
    }

    private void setTextMain(TextView textView, String text) {
        if (text == null) {
            textView.setText(null);
            return;
        }

        textView.setText(text);

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
    private void setTextAddition(TextView textView, String text) {
        if (text == null) {
            textView.setText(null);
            return;
        }

        textView.setText("(" + text + ")");

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

    private void showNoMoreWordsLayout() {
        layoutNoMoreWords.setVisibility(View.VISIBLE);
        if (!btnShuffleAndStartAgain.hasOnClickListeners())
            btnShuffleAndStartAgain.setOnClickListener(v -> shuffleAndStartAgain());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (editedWordIndex == -1) return;
        try {
            Word word = dictionary.getWordById(words.get(editedWordIndex).getId());
            System.out.println(word.toString());
            words.set(editedWordIndex, word);
            setCardData(cardOnTop, word);
            expandAllData(cardOnTop, word);
        }
        catch (IndexOutOfBoundsException obe) {
            words.remove(editedWordIndex);
            cardsContainer.removeAllViewsInLayout();
            if (cardNext != null) {
                shiftActiveCards();
                cardOnTop.setScaleX(1);
                cardOnTop.setScaleY(1);
                addCardToCardsContainer(cardOnTop, 0);
            }
            else
                showNoMoreWordsLayout();

            flipped = false;
        }
        finally {
            editedWordIndex = -1;
        }
    }
}