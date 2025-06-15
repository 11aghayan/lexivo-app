package com.lexivo;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.lexivo.adapters.ArrayAdapters;
import com.lexivo.exception.UnableToSaveException;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Language;
import com.lexivo.util.ActivityUtil;
import com.lexivo.util.IntentUtil;
import com.lexivo.util.Memory;
import com.lexivo.util.StringUtil;
import com.lexivo.util.SystemUtil;
import com.lexivo.util.ToastUtil;
import com.lexivo.util.ViewUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class DictionaryActivity extends AppCompatActivity {
    private static final String TAG = "ACTIVITY_DICTIONARY";
    private Dictionary dictionary;
    private Intent intent;
    private Language currentLanguage, selectedLanguage;
    private CardView languageFlag, btnPracticeWordsLang, btnPracticeWordsEnglish, btnPracticeExpressionsLang, btnPracticeExpressionsEnglish;
    private TextView textLanguage;
    private Button btnExportDictionary, dismissLanguageModalBtn, saveLanguageBtn, btnCopyId, btnExportJson, btnWords, btnExpressions, btnRules, btnDeleteDictionary;
    private RelativeLayout languageRelativeLayout, modalExportDictionary;
    private LinearLayout exportDictionaryModalContent;
    private ConstraintLayout modalEditDictionary, modalDelete, modalConfirmDeleteDictionary;
    private GridLayout actionButtonsGridLayout;
    private Spinner languageSelector;

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

        ActivityUtil.handleBackBtn(this);

        initViews();
        handleContent();
        handleLanguageChange();
        handleExportDictionary();
        setActionLabels();
        handleNavigation();
        handleDelete();
        handleOnBackPressed();
        handleActionButtonsGridLayout();
    }

    private void initViews() {
        languageFlag = findViewById(R.id.languageFlag);
        textLanguage = findViewById(R.id.textLanguage);
        btnExportDictionary = findViewById(R.id.btnExportDictionary);
        languageRelativeLayout = findViewById(R.id.language);
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
        btnDeleteDictionary = findViewById(R.id.btnDeleteDictionary);
        modalDelete = findViewById(R.id.modalDelete);
        modalConfirmDeleteDictionary = findViewById(R.id.modalConfirmDeleteDictionary);
        actionButtonsGridLayout = findViewById(R.id.actionButtonsGridLayout);
        btnPracticeWordsLang = findViewById(R.id.btnPracticeWordsLang);
        btnPracticeWordsEnglish = findViewById(R.id.btnPracticeWordsEnglish);
        btnPracticeExpressionsLang = findViewById(R.id.btnPracticeExpressionsLang);
        btnPracticeExpressionsEnglish = findViewById(R.id.btnPracticeExpressionsEnglish);
    }

    private void handleContent() {
        intent  = getIntent();
        String dictionaryId = intent.getStringExtra(IntentUtil.KEY_DICTIONARY_ID);
        dictionary = Dictionary.getDictionaryById(dictionaryId);
        assert dictionary != null;
        currentLanguage = dictionary.getLanguage();
        languageFlag.setForeground(dictionary.getLanguage().getFlag(this));
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
            ToastUtil.copiedToClipboard(this);
            ViewUtil.closeModal(modalExportDictionary);
        });
        btnExportJson.setOnClickListener(v -> Memory.exportDictionary(this, dictionary.getLanguage().getLabel()));
    }

    private void setActionLabels() {
        ((TextView)(btnPracticeWordsLang.findViewById(R.id.btnPracticeLang))).setText(StringUtil.capitalize(currentLanguage.getLabel()));
        ((TextView)(btnPracticeWordsEnglish.findViewById(R.id.btnPracticeLang))).setText(StringUtil.capitalize("english"));
        ((TextView)(btnPracticeExpressionsLang.findViewById(R.id.btnPracticeLang))).setText(StringUtil.capitalize(currentLanguage.getLabel()));
        ((TextView)(btnPracticeExpressionsEnglish.findViewById(R.id.btnPracticeLang))).setText(StringUtil.capitalize("english"));
    }

    private void handleNavigation() {
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
        btnPracticeWordsLang.findViewById(R.id.button).setOnClickListener(v -> {
            Intent intent = new Intent(this, PracticeWordsActivity.class);
            intent.putExtra(IntentUtil.KEY_DICTIONARY_ID, dictionary.getId());
            intent.putExtra(IntentUtil.KEY_PRACTICE_TYPE, IntentUtil.VALUE_PRACTICE_TYPE_LANG);
            startActivity(intent);
        });

        btnPracticeWordsEnglish.findViewById(R.id.button).setOnClickListener(v -> {
            com.lexivo.util.Toast.success(this, "Practice Words English Clicked");
//            TODO: Navigate to activity
        });
        btnPracticeExpressionsLang.findViewById(R.id.button).setOnClickListener(v -> {
            com.lexivo.util.Toast.warning(this, "Practice Expressions Lang Clicked");
//            TODO: Navigate to activity
        });
        btnPracticeExpressionsEnglish.findViewById(R.id.button).setOnClickListener(v -> {
            com.lexivo.util.Toast.error(this, "Practice Expressions English Clicked");
//            TODO: Navigate to activity
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
        languageRelativeLayout.setOnClickListener(v -> ViewUtil.openModal(modalEditDictionary));
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

    private void handleActionButtonsGridLayout() {
        actionButtonsGridLayout.post(() -> {
            int currentColumnCount = 5;
            float buttonSize = getResources().getDimension(R.dimen.action_button_size) + 10;
            float buttonsWidth = currentColumnCount * buttonSize;
            actionButtonsGridLayout.setColumnCount(currentColumnCount);
            while((actionButtonsGridLayout.getWidth() < buttonsWidth) && (currentColumnCount > 1)) {
                actionButtonsGridLayout.setColumnCount(--currentColumnCount);
                buttonsWidth = currentColumnCount * buttonSize;
            }
        });
    }

    private void handleOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (modalExportDictionary.getVisibility() == View.VISIBLE){
                    ViewUtil.closeModal(modalExportDictionary);
                }
                else if (modalEditDictionary.getVisibility() == View.VISIBLE) {
                    ViewUtil.closeModal(modalEditDictionary);
                }
                else
                    finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                if (uri == null) return;
                try(OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream))
                ) {
                    Gson gson = new Gson();
                    String dataJson = gson.toJson(dictionary);
                    bw.write(dataJson);
                    ToastUtil.dictionaryExported(this);
                }
            }
            catch (IOException ioe) {
                ToastUtil.dictionaryNotExported(this);
                Log.e(TAG, "onActivityResult: " + ioe.getMessage());
            }
            finally {
                ViewUtil.closeModal(modalExportDictionary);
            }
        }
    }
}
