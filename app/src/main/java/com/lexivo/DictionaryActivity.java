package com.lexivo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lexivo.adapters.ArrayAdapters;
import com.lexivo.exception.UnableToSaveException;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Language;
import com.lexivo.util.IntentUtil;
import com.lexivo.util.StringUtil;
import com.lexivo.util.SystemUtil;
import com.lexivo.util.ToastUtil;
import com.lexivo.util.ViewUtil;

public class DictionaryActivity extends AppCompatActivity {
    private Dictionary dictionary;
    private Intent intent;
    private Language currentLanguage, selectedLanguage;
    private CardView languageFlag, modalChooseWordsOrExpressionsContent;
    private TextView textLanguage;
    private Button
            btnExportDictionary, dismissLanguageModalBtn, saveLanguageBtn, btnCopyId, btnExportJson, btnWords, btnExpressions, btnRules, btnPractice, btnQuiz, btnChooseWords, btnChooseExpressions, btnDeleteDictionary;
    private RelativeLayout language, modalExportDictionary, modalChooseWordsOrExpressions;
    private LinearLayout exportDictionaryModalContent;
    private ConstraintLayout modalEditDictionary, modalDelete, modalConfirmDeleteDictionary;
    private Spinner languageSelector;
    private boolean isQuiz = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dictionary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SystemUtil.hideSystemUi(getWindow());

        initViews();
        handleContent();
        handleLanguageChange();
        handleExportDictionary();
        handleNavigation();
        handleDelete();
        handleOnBackPressed();
    }

    private void initViews() {
        languageFlag = findViewById(R.id.languageFlag);
        textLanguage = findViewById(R.id.textLanguage);
        btnExportDictionary = findViewById(R.id.btnExportDictionary);
        language = findViewById(R.id.language);
        modalEditDictionary = findViewById(R.id.modalAddEditDictionary);
        languageSelector = ViewUtil.getSpinner(findViewById(R.id.languageSelector));
        dismissLanguageModalBtn = findViewById(R.id.dismissLanguageModalBtn);
        saveLanguageBtn = findViewById(R.id.saveLanguageBtn);
        btnCopyId = findViewById(R.id.btnCopyId);
        btnExportJson = findViewById(R.id.btnExportJson);
        modalExportDictionary = findViewById(R.id.modalExportDictionary);
        exportDictionaryModalContent = findViewById(R.id.exportDictionaryModalContent);
        btnWords = findViewById(R.id.btnWords);
        btnExpressions = findViewById(R.id.btnExpressions);
        btnRules = findViewById(R.id.btnRules);
        btnPractice = findViewById(R.id.btnPractice);
        btnQuiz = findViewById(R.id.btnQuiz);
        modalChooseWordsOrExpressions = findViewById(R.id.modalChooseWordsOrExpressions);
        modalChooseWordsOrExpressionsContent = findViewById(R.id.modalChooseWordsOrExpressionsContent);
        btnChooseWords = findViewById(R.id.btnChooseWords);
        btnChooseExpressions = findViewById(R.id.btnChooseExpressions);
        btnDeleteDictionary = findViewById(R.id.btnDeleteDictionary);
        modalDelete = findViewById(R.id.modalDelete);
        modalConfirmDeleteDictionary = findViewById(R.id.modalConfirmDeleteDictionary);
    }

    private void handleContent() {
        intent  = getIntent();
        String dictionaryId = intent.getStringExtra(IntentUtil.KEY_DICTIONARY_ID);
        dictionary = Dictionary.getDictionaryById(dictionaryId);
        assert dictionary != null;
        currentLanguage = dictionary.getLanguage();
        languageFlag.setForeground(ResourcesCompat.getDrawable(getResources(), currentLanguage.getFlag(), null));
        textLanguage.setText(StringUtil.capitalize(currentLanguage.getLabel()));
    }

    private void handleExportDictionary() {
        btnExportDictionary.setOnClickListener(v -> ViewUtil.openModal(modalExportDictionary));
        modalExportDictionary.setOnClickListener(v -> ViewUtil.closeModal(modalExportDictionary));
        exportDictionaryModalContent.setOnClickListener(v->{});
        btnCopyId.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("dictionary_id", dictionary.getId());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, getResources().getText(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
            modalExportDictionary.setVisibility(View.GONE);
        });
        btnExportJson.setOnClickListener(v -> {
            //TODO: handle export as json
        });
    }

    private void handleNavigation() {
        btnPractice.setOnClickListener(v -> {
            isQuiz = false;
            ViewUtil.openModal(modalChooseWordsOrExpressions);
        });
        btnQuiz.setOnClickListener(v -> {
            isQuiz = true;
            modalChooseWordsOrExpressions.setVisibility(View.VISIBLE);
        });
        handleModalChooseWordsOrExpressions();
        btnWords.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllWordsActivity.class);
            intent.putExtra(IntentUtil.KEY_DICTIONARY_ID, dictionary.getId());
            this.startActivity(intent);
        });
        btnExpressions.setOnClickListener(v -> {
            //TODO: redirect to expressions activity
        });
        btnRules.setOnClickListener(v -> {
            //TODO: redirect to rules activity
        });
    }

    private void handleModalChooseWordsOrExpressions() {
        modalChooseWordsOrExpressions.setOnClickListener(v -> ViewUtil.closeModal(modalChooseWordsOrExpressions));
        modalChooseWordsOrExpressionsContent.setOnClickListener(v -> {});

        btnChooseWords.setOnClickListener(v -> {
            if (!isQuiz) {
                Intent intent = new Intent(this, PracticeWordsActivity.class);
                intent.putExtra(IntentUtil.KEY_DICTIONARY_ID, dictionary.getId());
                ViewUtil.closeModal(modalChooseWordsOrExpressions);
                startActivity(intent);
            }
            else {
//                TODO: handle quiz
            }
        });

        btnChooseExpressions.setOnClickListener(v -> {
            if (!isQuiz) {
//                TODO: uncomment the code below
//                Intent intent = new Intent(this, PracticeExpressionsActivity.class);
//                intent.putExtra("dictionary_id", dictionary.getId());
//                ViewUtil.closeModal(modalChooseWordsOrExpressions);
//                startActivity(intent);
            }
            else {
//                TODO: handle quiz
            }
        });
    }

    private void handleLanguageChange() {
        btnDeleteDictionary.setVisibility(View.VISIBLE);
        saveLanguageBtn.setText(R.string.save);
        var adapter = ArrayAdapters.languagesAdapter(this);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        languageSelector.setAdapter(adapter);
        languageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String[] languages = getResources().getStringArray(R.array.languages);
                if (selectedLanguage == null) {
                    int pos = -1;
                    for (int i = 0; i < languages.length; i++) {
                        if (languages[i].toLowerCase().equals(dictionary.getLanguage().getLabel())) {
                            pos = i;
                            selectedLanguage = dictionary.getLanguage();
                            break;
                        }
                    }
                    parent.setSelection(pos);
                }
                else {
                    selectedLanguage = Language.get(languages[position].toLowerCase());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        language.setOnClickListener(v -> ViewUtil.openModal(modalEditDictionary));
        dismissLanguageModalBtn.setOnClickListener(v -> {
            selectedLanguage = currentLanguage;
            ViewUtil.closeModal(modalEditDictionary);
        });
        saveLanguageBtn.setOnClickListener(v -> {
            dictionary.setLanguage(selectedLanguage);
            ViewUtil.closeModal(modalEditDictionary);
            finish();
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }

    private void handleDelete() {
        btnDeleteDictionary.setOnClickListener(v -> ViewUtil.openModal(modalDelete));
        modalDelete.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            ViewUtil.closeModal(modalDelete);
            ViewUtil.openModal(modalConfirmDeleteDictionary);
        });
        modalDelete.findViewById(R.id.btnCancel).setOnClickListener(v -> ViewUtil.closeModal(modalDelete));

        Button btnConfirmDelete = modalConfirmDeleteDictionary.findViewById(R.id.btnConfirmDelete);

        ((EditText) modalConfirmDeleteDictionary.findViewById(R.id.input)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("delete dictionary".equals(s.toString().trim())) {
                    btnConfirmDelete.setEnabled(true);
                    btnConfirmDelete.setBackgroundColor(getColor(R.color.btn_delete));
                }
                else {
                    btnConfirmDelete.setEnabled(false);
                    btnConfirmDelete.setBackgroundColor(getColor(R.color.light_gray_low));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnConfirmDelete.setOnClickListener(v -> {
            Toast.makeText(DictionaryActivity.this, StringUtil.capitalize(dictionary.getLanguage().getLabel()) + " dictionary deleted", Toast.LENGTH_SHORT).show();
            try {
                Dictionary.deleteDictionary(dictionary, this);
                ViewUtil.closeModal(modalDelete);
                finish();
            } catch (UnableToSaveException utse) {
                ToastUtil.unableToSave(this);
            }
        });

        modalConfirmDeleteDictionary.findViewById(R.id.btnDismissDelete).setOnClickListener(v-> ViewUtil.closeModal(modalConfirmDeleteDictionary));
    }

    private void handleOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (modalExportDictionary.getVisibility() == View.VISIBLE){
                    ViewUtil.closeModal(modalExportDictionary);
                }
                else if (modalChooseWordsOrExpressions.getVisibility() == View.VISIBLE) {
                    ViewUtil.closeModal(modalChooseWordsOrExpressions);
                }
                else if (modalEditDictionary.getVisibility() == View.VISIBLE) {
                    ViewUtil.closeModal(modalEditDictionary);
                }
                else
                    finish();
            }
        });
    }
}
