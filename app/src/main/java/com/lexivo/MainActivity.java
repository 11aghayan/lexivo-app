package com.lexivo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lexivo.adapters.LanguagesArrayAdapter;
import com.lexivo.adapters.MyDictionariesAdapter;
import com.lexivo.data.Dictionary;
import com.lexivo.data.Language;
import com.lexivo.exception.DuplicateValueException;


public class MainActivity extends AppCompatActivity {
    private Button importDictionaryBtn, addDictionaryBtn, dismissLanguageModalBtn, saveLanguageBtn, importDictionaryByIdBtn, dismissDictionaryModalBtn, importDictionaryModalBtn, importDictionaryFromMemoryBtn;
    private LinearLayout importDictionaryBtnExpanded;
    private RecyclerView myDictionariesRecView;
    private Spinner languageSelector;
    private ConstraintLayout languageModal, importEditDictionaryModalBg;
    private String selectedLanguageInModal;
    private MyDictionariesAdapter myDictionariesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Language.init();
        initViews();
        handleMyDictionaries();
        handleImportDictionary();
        handleAddDictionaryModal();
    }

    private void handleAddDictionaryModal() {
        var adapter = new LanguagesArrayAdapter(MainActivity.this).adapter;
        adapter.setDropDownViewResource(R.layout.language_dropdown_item);
        languageSelector.setAdapter(adapter);

        addDictionaryBtn.setOnClickListener(v -> {
            languageModal.setVisibility(View.VISIBLE);
        });

        dismissLanguageModalBtn.setOnClickListener(v -> {
            languageModal.setVisibility(View.GONE);
        });

        saveLanguageBtn.setOnClickListener(v -> {
            if (selectedLanguageInModal != null) {
                try {
                    Dictionary dict = new Dictionary(Language.get(selectedLanguageInModal));
                    myDictionariesAdapter.addDictionary(dict);
                } catch (DuplicateValueException dve) {
                    Toast.makeText(MainActivity.this, getResources().getText(R.string.toast_dictionary_already_exists), Toast.LENGTH_SHORT).show();
                }
            }
            languageModal.setVisibility(View.GONE);
        });

        languageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguageInModal = ((String)parent.getItemAtPosition(position)).toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void initViews() {
        importDictionaryBtn = findViewById(R.id.importDictionaryBtn);
        importDictionaryBtnExpanded = findViewById(R.id.importDictionaryBtnExpanded);
        myDictionariesRecView = findViewById(R.id.myDictionariesRecView);
        languageSelector = findViewById(R.id.languageSelector);
        languageModal = findViewById(R.id.deleteModal);
        importEditDictionaryModalBg = findViewById(R.id.importEditDictionaryModalBg);
        dismissLanguageModalBtn = findViewById(R.id.dismissLanguageModalBtn);
        addDictionaryBtn = findViewById(R.id.addDictionaryBtn);
        saveLanguageBtn = findViewById(R.id.saveLanguageBtn);
        importDictionaryByIdBtn = findViewById(R.id.importDictionaryByIdBtn);
        dismissDictionaryModalBtn = findViewById(R.id.dismissDictionaryModalBtn);
        importDictionaryModalBtn = findViewById(R.id.importDictionaryModalBtn);
        importDictionaryFromMemoryBtn = findViewById(R.id.importDictionaryFromMemoryBtn);
    }

    private void handleImportDictionary() {
        importDictionaryBtn.setOnClickListener(v -> {
            importDictionaryBtn.setVisibility(View.GONE);
            importDictionaryBtnExpanded.setVisibility(View.VISIBLE);
        });

        importDictionaryByIdBtn.setOnClickListener(v -> {
            importEditDictionaryModalBg.setVisibility(View.VISIBLE);
        });

        dismissDictionaryModalBtn.setOnClickListener(v -> {
            importEditDictionaryModalBg.setVisibility(View.GONE);
        });

        importDictionaryModalBtn.setOnClickListener(v -> {
            //TODO
        });

        importDictionaryFromMemoryBtn.setOnClickListener(v -> {
            //TODO
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (importDictionaryBtn.getVisibility() == View.GONE) {
                    importDictionaryBtn.setVisibility(View.VISIBLE);
                    importDictionaryBtnExpanded.setVisibility(View.GONE);
                } else {
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void handleMyDictionaries() {
        myDictionariesAdapter = new MyDictionariesAdapter(findViewById(R.id.main), MainActivity.this);
        myDictionariesRecView.setAdapter(myDictionariesAdapter);
        myDictionariesRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        myDictionariesAdapter.notifyDataSetChanged();
    }
}