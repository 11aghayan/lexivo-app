package com.lexivo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.lexivo.adapters.ArrayAdapters;
import com.lexivo.exception.DuplicateHashException;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Gender;
import com.lexivo.schema.Text;
import com.lexivo.schema.Word;
import com.lexivo.schema.WordType;
import com.lexivo.util.StringUtil;
import com.lexivo.util.ViewUtil;
import com.lexivo.databinding.ActivityAddEditWordBinding;

public class AddEditWordActivity extends AppCompatActivity {
    private Word word;
    private ActivityAddEditWordBinding binding;
    private CardView languageFlag;
    private Spinner spinnerWordType, spinnerWordGender;
    private EditText inputWordOriginal, inputWordOriginalDetails, inputWordPlural, inputWordPast1, inputWordPast2, inputWordNative, inputWordNativeDetails, inputWordComment;
    private TextView headerText;
    private WordType selectedWordType;
    private Gender selectedGender;
    private Button btnSave;
    private final StringBuilder
            wordOriginal = new StringBuilder(),
            wordOriginalDetails = new StringBuilder(),
            wordPlural = new StringBuilder(),
            wordPast1 = new StringBuilder(),
            wordPast2 = new StringBuilder(),
            wordNative = new StringBuilder(),
            wordNativeDetails = new StringBuilder(),
            wordComment = new StringBuilder();
    private String photo, audio;
    private LinearLayout spinnerWordGenderSection, inputWordPluralSection, pastTenseSection;
    private Dictionary dictionary;

    //        TODO: Add button delete in the header
    //        TODO: LATER: import, init and handle photo and audio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_word);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_word);

        initViews();
        populateStartingContent();
        handleSpinnerWordType();
        handleSpinnerWordGender();
        manageFieldsDependingOnWordType();
        handleGender();
        handleInputsChanges();
        handleSave();
    }

    private void initViews() {
        spinnerWordType = ViewUtil.getSpinner(findViewById(R.id.spinnerWordType));
        spinnerWordGender = ViewUtil.getSpinner(findViewById(R.id.spinnerWordGender));
        spinnerWordGenderSection = findViewById(R.id.spinnerWordGenderSection);
        inputWordPluralSection = findViewById(R.id.inputWordPluralSection);
        pastTenseSection = findViewById(R.id.pastTenseSection);
        languageFlag = findViewById(R.id.languageFlag);
        inputWordOriginal = findViewById(R.id.inputWordOriginal);
        inputWordOriginalDetails = findViewById(R.id.inputWordOriginalDetails);
        inputWordPlural = findViewById(R.id.inputWordPlural);
        inputWordPast1 = findViewById(R.id.inputWordPast1);
        inputWordPast2 = findViewById(R.id.inputWordPast2);
        inputWordNative = findViewById(R.id.inputWordNative);
        inputWordNativeDetails = findViewById(R.id.inputWordNativeDetails);
        inputWordComment = findViewById(R.id.inputWordComment);
        btnSave = findViewById(R.id.btnSave);
        headerText = findViewById(R.id.headerText);
    }

    private void populateStartingContent() {
        Intent intent = getIntent();
        String wordId = intent.getStringExtra("word_id");
        dictionary = Dictionary.getDictionaryById(intent.getStringExtra("dictionary_id"));
        if (wordId != null) {
            headerText.setText(getResources().getString(R.string.text_header_edit_word));
            assert dictionary != null;
            word = dictionary.getWordById(wordId);
            dictionary = word.getDictionary();
            initFieldsWithWordData(word);
        } else {
            headerText.setText(getResources().getString(R.string.text_header_add_word));
        }
        assert dictionary != null;
        binding.setLanguage(StringUtil.capitalize(dictionary.getLanguage().getLabel()));
        languageFlag.setForeground(ResourcesCompat.getDrawable(getResources(), dictionary.getLanguage().getFlag(), null));
    }

    private void handleSpinnerWordType() {
        var adapter = ArrayAdapters.wordTypeAdapter(this);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerWordType.setAdapter(adapter);
    }

    private void handleSpinnerWordGender() {
        var adapter = ArrayAdapters.wordGenderAdapter(this);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerWordGender.setAdapter(adapter);
    }

    private void manageFieldsDependingOnWordType() {
        spinnerWordType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWordType = WordType.fromString(((String)parent.getSelectedItem()).toLowerCase());
                if (selectedWordType.equals(WordType.NOUN)) {
                    spinnerWordGenderSection.setVisibility(View.VISIBLE);
                    inputWordPluralSection.setVisibility(View.VISIBLE);
                } else {
                    spinnerWordGenderSection.setVisibility(View.GONE);
                    inputWordPluralSection.setVisibility(View.GONE);
                    selectedGender = null;
                    StringUtil.setStringBuilderValue(wordPlural, "");
                    inputWordPlural.setText(null);
                }

                if (selectedWordType.equals(WordType.VERB)) {
                    pastTenseSection.setVisibility(View.VISIBLE);
                } else {
                    pastTenseSection.setVisibility(View.GONE);
                    StringUtil.setStringBuilderValue(wordPast1, "");
                    StringUtil.setStringBuilderValue(wordPast2, "");
                    inputWordPast1.setText(null);
                    inputWordPast2.setText(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void handleGender() {
        spinnerWordGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String genderString = (String)parent.getSelectedItem();
                selectedGender = Gender.valueOf(genderString.toUpperCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void handleInputsChanges() {
        inputWordOriginal.addTextChangedListener(new TextChangeListener(wordOriginal));
        inputWordOriginalDetails.addTextChangedListener(new TextChangeListener(wordOriginalDetails));
        inputWordPlural.addTextChangedListener(new TextChangeListener(wordPlural));
        inputWordPast1.addTextChangedListener(new TextChangeListener(wordPast1));
        inputWordPast2.addTextChangedListener(new TextChangeListener(wordPast2));
        inputWordNative.addTextChangedListener(new TextChangeListener(wordNative));
        inputWordNativeDetails.addTextChangedListener(new TextChangeListener(wordNativeDetails));
        inputWordComment.addTextChangedListener(new TextChangeListener(wordComment));
    }

    private void handleSave() {
        btnSave.setOnClickListener(v -> {
            if (StringUtil.getValueFromStringBuilder(wordOriginal) == null && StringUtil.getValueFromStringBuilder(wordPlural) == null) {
                Toast.makeText(this, "Both plural and singular cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else {
                Text original = new Text(StringUtil.getValueFromStringBuilder(wordOriginal), StringUtil.getValueFromStringBuilder(wordOriginalDetails));
                Text translation = new Text(StringUtil.getValueFromStringBuilder(wordNative), StringUtil.getValueFromStringBuilder(wordNativeDetails));
                String plural = StringUtil.getValueFromStringBuilder(wordPlural);
                String past1 =  StringUtil.getValueFromStringBuilder(wordPast1);
                String past2 = StringUtil.getValueFromStringBuilder(wordPast2);
                String comment = StringUtil.getValueFromStringBuilder(wordComment);

                if (word == null) {
                    try {
                        dictionary.addWord(new Word(dictionary, selectedWordType, selectedGender,original,plural,past1,past2,translation,photo,audio,comment));
                    }
                    catch (DuplicateHashException e) {
                        Toast.makeText(this, "The word already is in the dictionary", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    word.setType(selectedWordType);
                    word.setGender(selectedGender);
                    word.setOriginal(original);
                    word.setPlural(plural);
                    word.setPast1(past1);
                    word.setPast2(past2);
                    word.setTranslation(translation);
                    word.setPhoto(photo);
                    word.setAudio(audio);
                    word.setComment(comment);
                }
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private void initFieldsWithWordData(Word word) {
        StringUtil.setStringBuilderValue(wordOriginal, word.getOriginal().getValue());
        inputWordOriginal.setText(StringUtil.getValueFromStringBuilder(wordOriginal));

        StringUtil.setStringBuilderValue(wordOriginalDetails, word.getOriginal().getDetails());
        inputWordOriginalDetails.setText(StringUtil.getValueFromStringBuilder(wordOriginalDetails));

        StringUtil.setStringBuilderValue(wordPlural, word.getPlural());
        inputWordPlural.setText(StringUtil.getValueFromStringBuilder(wordPlural));

        StringUtil.setStringBuilderValue(wordPast1, word.getPast1());
        inputWordPast1.setText(StringUtil.getValueFromStringBuilder(wordPast1));

        StringUtil.setStringBuilderValue(wordPast2, word.getPast2());
        inputWordPast2.setText(StringUtil.getValueFromStringBuilder(wordPast2));

        StringUtil.setStringBuilderValue(wordNative, word.getTranslation().getValue());
        inputWordNative.setText(StringUtil.getValueFromStringBuilder(wordNative));

        StringUtil.setStringBuilderValue(wordNativeDetails, word.getTranslation().getDetails());
        inputWordNativeDetails.setText(StringUtil.getValueFromStringBuilder(wordNativeDetails));

        photo = word.getPhoto();
        audio = word.getAudio();

        StringUtil.setStringBuilderValue(wordComment, word.getComment());
        inputWordComment.setText(StringUtil.getValueFromStringBuilder(wordComment));
    }

    private static class TextChangeListener implements TextWatcher {
        private final StringBuilder sb;

        public TextChangeListener(StringBuilder sb) {
            this.sb = sb;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            StringUtil.setStringBuilderValue(sb, s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}