package com.lexivo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.lexivo.adapters.ArrayAdapters;
import com.lexivo.exception.DuplicateHashException;
import com.lexivo.exception.UnableToSaveException;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Gender;
import com.lexivo.schema.Text;
import com.lexivo.schema.Word;
import com.lexivo.schema.WordType;
import com.lexivo.util.ActivityUtil;
import com.lexivo.util.IntentUtil;
import com.lexivo.util.ListUtil;
import com.lexivo.util.Memory;
import com.lexivo.util.StringUtil;
import com.lexivo.util.SystemUtil;
import com.lexivo.util.ToastUtil;
import com.lexivo.util.ViewUtil;
import com.lexivo.databinding.ActivityAddEditWordBinding;

public class AddEditWordActivity extends AppCompatActivity {
    private Word word;
    private ActivityAddEditWordBinding binding;
    private ArrayAdapter<String> adapterWordType;
    private ArrayAdapter<String> adapterWordGender;
    private CardView languageFlag;
    private Spinner spinnerWordType, spinnerWordGender;
    private EditText inputWordOriginal, inputWordOriginalDetails, inputWordPlural, inputWordPast1, inputWordPast2, inputWordNative, inputWordNativeDetails, inputWordComment;
    private TextView headerText, btnOpenDeleteModal;
    private WordType selectedWordType;
    private Gender selectedGender;
    private Button btnSave;
    private ConstraintLayout modalDelete;
    private final StringBuilder
            wordOriginal = new StringBuilder(),
            wordOriginalDetails = new StringBuilder(),
            wordPlural = new StringBuilder(),
            wordPast1 = new StringBuilder(),
            wordPast2 = new StringBuilder(),
            wordNative = new StringBuilder(),
            wordNativeDetails = new StringBuilder(),
            wordComment = new StringBuilder();
    private LinearLayout spinnerWordGenderSection, inputWordPluralSection, pastTenseSection, inputWordOriginalLabelContainer;
    private Dictionary dictionary;

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
        SystemUtil.hideSystemUi(getWindow());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_word);

        ActivityUtil.handleBackBtn(this);

        initViews();
        handleSpinnerWordType();
        handleSpinnerWordGender();
        initStartingContent();
        handleInputsChanges();
        handleSave();
        handleDelete();
        handleOnBackPressed();
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
        btnOpenDeleteModal = findViewById(R.id.btnOpenDeleteModal);
        modalDelete = findViewById(R.id.modalDelete);
        inputWordOriginalLabelContainer = findViewById(R.id.inputWordOriginalLabelContainer);
    }

    private void initStartingContent() {
        Intent intent = getIntent();
        String wordId = intent.getStringExtra(IntentUtil.KEY_WORD_ID);
        dictionary = Dictionary.getDictionaryById(intent.getStringExtra(IntentUtil.KEY_DICTIONARY_ID));
        if (wordId != null) {
            headerText.setText(getResources().getString(R.string.text_header_edit_word));
            assert dictionary != null;
            word = dictionary.getWordById(wordId);
            initFieldsWithWordData(word);
            btnOpenDeleteModal.setVisibility(View.VISIBLE);
        }
        else {
            headerText.setText(getResources().getString(R.string.text_header_add_word));
            btnOpenDeleteModal.setVisibility(View.GONE);
        }
        assert dictionary != null;
        binding.setLanguage(StringUtil.capitalize(dictionary.getLanguage().getLabel()));
        languageFlag.setForeground(dictionary.getLanguage().getFlag(this));
    }

    private void handleSpinnerWordType() {
        adapterWordType = ArrayAdapters.wordTypeAdapter(this);
        adapterWordType.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerWordType.setAdapter(adapterWordType);

        spinnerWordType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWordType = WordType.fromString(((String)parent.getSelectedItem()).toLowerCase());
                if (WordType.NOUN.equals(selectedWordType)) {
                    spinnerWordGenderSection.setVisibility(View.VISIBLE);
                    inputWordPluralSection.setVisibility(View.VISIBLE);
                }
                else {
                    spinnerWordGenderSection.setVisibility(View.GONE);
                    inputWordPluralSection.setVisibility(View.GONE);
                    selectedGender = null;
                    StringUtil.setStringBuilderValue(wordPlural, "");
                    inputWordPlural.setText(null);
                }

                if (WordType.VERB.equals(selectedWordType)) {
                    pastTenseSection.setVisibility(View.VISIBLE);
                }
                else {
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

    private void handleSpinnerWordGender() {
        adapterWordGender = ArrayAdapters.wordGenderAdapter(this);
        adapterWordGender.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerWordGender.setAdapter(adapterWordGender);

        spinnerWordGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String genderString = (String)parent.getSelectedItem();
                selectedGender = Gender.valueOf(ListUtil.joinElementsIntoString(genderString.split(" "), "_").toUpperCase());

                if (Gender.PLURAL.equals(selectedGender)) {
                    inputWordOriginal.setVisibility(View.GONE);
                    inputWordOriginalLabelContainer.setVisibility(View.GONE);
                    StringUtil.setStringBuilderValue(wordOriginal, "");
                }
                else {
                    inputWordOriginal.setVisibility(View.VISIBLE);
                    inputWordOriginalLabelContainer.setVisibility(View.VISIBLE);
                }
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
                String past1 = StringUtil.getValueFromStringBuilder(wordPast1);
                String past2 = StringUtil.getValueFromStringBuilder(wordPast2);
                String comment = StringUtil.getValueFromStringBuilder(wordComment);

                if (word == null) {
                    try {
                        dictionary.addWord(new Word(selectedWordType, selectedGender, original, plural, past1, past2, translation, comment), this);
                    } catch (DuplicateHashException e) {
                        Toast.makeText(this, "The word already is in the dictionary", Toast.LENGTH_SHORT).show();
                    } catch (UnableToSaveException utse) {
                        ToastUtil.unableToSave(this);
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
                    word.setComment(comment);

                    if (!Memory.saveData(this)) {
                        ToastUtil.unableToSave(this);
                        return;
                    }
                }
            }
            finish();
        });
    }

    private void handleDelete() {
        btnOpenDeleteModal.setOnClickListener(v -> {
            ViewUtil.openModal(modalDelete);
            findViewById(R.id.btnDelete).setOnClickListener(_v -> {
                try {
                    dictionary.deleteWord(word, this);
                    finish();
                }
                catch (UnableToSaveException utse) {
                    ToastUtil.unableToSave(this);
                }
                finally {
                    ViewUtil.closeModal(modalDelete);
                }
            });
            findViewById(R.id.btnCancel).setOnClickListener(_v -> ViewUtil.closeModal(modalDelete));
        });
    }

    private void initFieldsWithWordData(Word word) {
        selectedWordType = word.getType();
        spinnerWordType.setSelection(adapterWordType.getPosition(StringUtil.capitalize(selectedWordType.toString())));

        selectedGender = word.getGender();
        if (selectedGender != null) {
            spinnerWordGender.setSelection(adapterWordGender.getPosition(StringUtil.capitalize(selectedGender.toString().toLowerCase())));
        }

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

        StringUtil.setStringBuilderValue(wordComment, word.getComment());
        inputWordComment.setText(StringUtil.getValueFromStringBuilder(wordComment));
    }

    private void handleOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (modalDelete.getVisibility() == View.VISIBLE){
                    ViewUtil.closeModal(modalDelete);
                }
                else
                    finish();
            }


        });
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