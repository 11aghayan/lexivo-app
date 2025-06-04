package com.lexivo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lexivo.adapters.AllWordsAdapter;
import com.lexivo.data.Dictionary;
import com.lexivo.data.Gender;
import com.lexivo.data.Text;
import com.lexivo.data.Word;
import com.lexivo.data.WordType;
import com.lexivo.exception.DuplicateHashException;
import com.lexivo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AllWordsActivity extends AppCompatActivity {
    private RecyclerView allWordsRecView;
    private EditText inputSearch;
    private TextView textLanguage, textNoWords;
    private CardView languageFlag;
    private Button btnAddWord;
    private Intent intent;
    private Dictionary dictionary;
    private AllWordsAdapter allWordsAdapter;
    private ConstraintLayout deleteModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_words);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        initContent();
        handleAllWordsRecView();
        handleTextNoWords(dictionary.getWordCount());
    }

    private void initViews() {
        textLanguage = findViewById(R.id.textLanguage);
        allWordsRecView = findViewById(R.id.allWordsRecView);
        inputSearch = findViewById(R.id.inputSearch);
        btnAddWord = findViewById(R.id.btnAddWord);
        deleteModal = findViewById(R.id.deleteModal);
        textNoWords = findViewById(R.id.textNoWords);
        languageFlag = findViewById(R.id.languageFlag);
    }

    private void initContent() {
        intent = getIntent();
        String dictionaryId = intent.getStringExtra("dictionary_id");
        dictionary = Dictionary.getDictionaryById(dictionaryId);
        assert dictionary != null;
        textLanguage.setText(StringUtil.capitalize(dictionary.getLanguage().getLabel()));
        languageFlag.setForeground(ResourcesCompat.getDrawable(getResources(), dictionary.getLanguage().getFlag(), null));
    }

    private void handleTextNoWords(int wordCount) {
        if (wordCount == 0) {
            textNoWords.setVisibility(View.VISIBLE);
        } else {
            textNoWords.setVisibility(View.GONE);
        }
    }

    private void handleAllWordsRecView() {
        try {
            //TODO: remove the fake population
            dictionary.addWord(new Word(
                    dictionary,
                    new Text("der Zug", null),
                    new Text("Train", null),
                    null,
                    null,
                    null,
                    new ArrayList<>(List.of(WordType.NOUN)),
                    Gender.MASCULINE,
                    "die Züge"
            ));
            dictionary.addWord(new Word(
                    dictionary,
                    new Text("Um", "Akkusativ"),
                    new Text("At", null),
                    null,
                    null,
                    null,
                    new ArrayList<>(List.of(WordType.PRON_PREP))
            ));
            dictionary.addWord(new Word(
                    dictionary,
                    new Text("die Frau", null),
                    new Text("Wife", null),
                    null,
                    null,
                    null,
                    new ArrayList<>(List.of(WordType.NOUN)),
                    Gender.FEMININE,
                    "die Frauen"
            ));
            dictionary.addWord(new Word(
                    dictionary,
                    new Text("das Buch", null),
                    new Text("Book", null),
                    null,
                    null,
                    null,
                    new ArrayList<>(List.of(WordType.NOUN)),
                    Gender.NEUTRAL,
                    "die Bücher"
            ));
            dictionary.addWord(new Word(
                    dictionary,
                    new Text("Frankreich", null),
                    new Text("France", null),
                    null,
                    null,
                    null,
                    new ArrayList<>(List.of(WordType.NOUN)),
                    Gender.PERSONAL,
                    null
            ));
            dictionary.addWord(new Word(
                    dictionary,
                    new Text(null, null),
                    new Text("Furniture", null),
                    null,
                    null,
                    "In german the word furniture is only plural",
                    new ArrayList<>(List.of(WordType.NOUN)),
                    Gender.PLURAL,
                    "die Möbel"
            ));
            dictionary.addWord(new Word(
                    dictionary,
                    new Text("das Fitnessstudio", null),
                    new Text("Fitness Center", null),
                    null,
                    null,
                    null,
                    new ArrayList<>(List.of(WordType.NOUN)),
                    Gender.NEUTRAL,
                    "die Fitnessstudios"
            ));

        } catch (DuplicateHashException dhe) {
            Toast.makeText(this, "The word already exists", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(dhe);
        }
        allWordsAdapter = new AllWordsAdapter(dictionary, this, deleteModal, textNoWords);
        allWordsRecView.setAdapter(allWordsAdapter);
        allWordsRecView.setLayoutManager(new LinearLayoutManager(this));
        handleInputSearch(allWordsAdapter);
    }

    private void handleInputSearch(AllWordsAdapter allWordsAdapter) {
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Word> words = dictionary.searchWords(s);
                allWordsAdapter.setWords(words);
                handleTextNoWords(words.size());
                allWordsAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

//    @SuppressLint("NotifyDataSetChanged")
//    @Override
//    protected void onResume() {
//        super.onResume();
//        allWordsAdapter.notifyDataSetChanged();
//    }
}