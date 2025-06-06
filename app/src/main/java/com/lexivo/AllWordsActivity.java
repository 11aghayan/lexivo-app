package com.lexivo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import com.lexivo.adapters.WordAdapter;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Word;
import com.lexivo.util.StringUtil;

import java.util.List;

public class AllWordsActivity extends AppCompatActivity {
    private RecyclerView allWordsRecView;
    private EditText inputSearch;
    private TextView textLanguage, textNoWords;
    private CardView languageFlag;
    private Button btnAddWord;
    private Intent intent;
    private Dictionary dictionary;
    private WordAdapter wordAdapter;
    private ConstraintLayout modalDelete;

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
        initData();
        handleAllWordsRecView();
        handleTextNoWords(dictionary.getWordCount());
        handleAddWord();
    }

    private void initViews() {
        textLanguage = findViewById(R.id.textLanguage);
        allWordsRecView = findViewById(R.id.allWordsRecView);
        inputSearch = findViewById(R.id.inputSearch);
        btnAddWord = findViewById(R.id.btnAddWord);
        modalDelete = findViewById(R.id.modalDelete);
        textNoWords = findViewById(R.id.textNoWords);
        languageFlag = findViewById(R.id.languageFlag);
    }

    private void initData() {
        intent = getIntent();
        dictionary = Dictionary.getDictionaryById(intent.getStringExtra("dictionary_id"));
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
        wordAdapter = new WordAdapter(dictionary, this, modalDelete, textNoWords);
        allWordsRecView.setAdapter(wordAdapter);
        allWordsRecView.setLayoutManager(new LinearLayoutManager(this));
        handleInputSearch(wordAdapter);
    }

    private void handleInputSearch(WordAdapter allWordsAdapter) {
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

    private void handleAddWord() {
        btnAddWord.setOnClickListener(v -> {
            intent = new Intent(this, AddEditWordActivity.class);
            intent.putExtra("dictionary_id", dictionary.getId());
            intent.putExtra("activity", "activity_all_words");
            startActivity(intent);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        wordAdapter.notifyDataSetChanged();
        handleTextNoWords(dictionary.getWordCount());
    }
}