package com.lexivo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import com.lexivo.adapters.ArrayAdapters;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Language;
import com.lexivo.util.StringUtil;
import com.lexivo.util.ViewUtil;

public class DictionaryActivity extends AppCompatActivity {
    private Dictionary dictionary;
    private Intent intent;
    private Language currentLanguage, selectedLanguage;
    private CardView languageFlag;
    private TextView textLanguage;
    private Button btnExportDictionary, dismissLanguageModalBtn, saveLanguageBtn, btnCopyId, btnExportJson, btnWords, btnExpressions, btnRules;
    private RelativeLayout language, exportDictionaryModal;
    private LinearLayout exportDictionaryModalContent;
    private ConstraintLayout languageModal;
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
        initViews();
        handleContent();
        handleLanguageChange();
        handleExportDictionary();
        handleNavigation();

    }

    private void initViews() {
        languageFlag = findViewById(R.id.languageFlag);
        textLanguage = findViewById(R.id.textLanguage);
        btnExportDictionary = findViewById(R.id.btnExportDictionary);
        language = findViewById(R.id.language);
        languageModal = findViewById(R.id.deleteModal);
        languageSelector = ViewUtil.getSpinner(findViewById(R.id.languageSelector));
        dismissLanguageModalBtn = findViewById(R.id.dismissLanguageModalBtn);
        saveLanguageBtn = findViewById(R.id.saveLanguageBtn);
        btnCopyId = findViewById(R.id.btnCopyId);
        btnExportJson = findViewById(R.id.btnExportJson);
        exportDictionaryModal = findViewById(R.id.exportDictionaryModal);
        exportDictionaryModalContent = findViewById(R.id.exportDictionaryModalContent);
        btnWords = findViewById(R.id.btnWords);
        btnExpressions = findViewById(R.id.btnExpressions);
        btnRules = findViewById(R.id.btnRules);
    }

    private void handleContent() {
        intent  = getIntent();
        String dictionaryId = intent.getStringExtra("dictionary_id");
        dictionary = Dictionary.getDictionaryById(dictionaryId);
        assert dictionary != null;
        currentLanguage = dictionary.getLanguage();
        languageFlag.setForeground(ResourcesCompat.getDrawable(getResources(), currentLanguage.getFlag(), null));
        textLanguage.setText(StringUtil.capitalize(currentLanguage.getLabel()));
    }

    private void handleExportDictionary() {
        btnExportDictionary.setOnClickListener(v -> exportDictionaryModal.setVisibility(View.VISIBLE));
        exportDictionaryModal.setOnClickListener(v -> {
            exportDictionaryModal.setVisibility(View.GONE);
        });
        exportDictionaryModalContent.setOnClickListener(v->{});
        btnCopyId.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("dictionary_id", dictionary.getId());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, getResources().getText(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
            exportDictionaryModal.setVisibility(View.GONE);
        });
        btnExportJson.setOnClickListener(v -> {
            //TODO
        });
    }

    private void handleNavigation() {
        btnWords.setOnClickListener(v -> {
            intent = new Intent(this, AllWordsActivity.class);
            intent.putExtra("dictionary_id", dictionary.getId());
            this.startActivity(intent);
        });
        btnExpressions.setOnClickListener(v -> {
            //TODO
        });
        btnRules.setOnClickListener(v -> {
            //TODO
        });
    }

    private void handleLanguageChange() {
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
        language.setOnClickListener(v -> languageModal.setVisibility(View.VISIBLE));
        dismissLanguageModalBtn.setOnClickListener(v -> {
            selectedLanguage = currentLanguage;
            languageModal.setVisibility(View.GONE);
        });
        saveLanguageBtn.setOnClickListener(v -> {
            dictionary.setLanguage(selectedLanguage);
            languageModal.setVisibility(View.GONE);
            finish();
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}
