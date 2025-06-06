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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lexivo.adapters.ArrayAdapters;
import com.lexivo.adapters.MyDictionariesAdapter;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Language;
import com.lexivo.exception.DuplicateValueException;
import com.lexivo.util.ViewUtil;


public class MainActivity extends AppCompatActivity {
    private Button importDictionaryBtn, addDictionaryBtn, dismissLanguageModalBtn, saveLanguageBtn, importDictionaryByIdBtn, dismissDictionaryModalBtn, importDictionaryModalBtn, importDictionaryFromMemoryBtn;
    private LinearLayout importDictionaryBtnExpanded;
    private RecyclerView myDictionariesRecView;
    private Spinner languageSelector;
    private ConstraintLayout modalAddDictionary, modalImportDictionary;
    private String selectedLanguageInModal;
    private MyDictionariesAdapter myDictionariesAdapter;
    private CardView modalAddDictionaryContent, modalImportDictionaryContent;

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
        var adapter = ArrayAdapters.languagesAdapter(MainActivity.this);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        languageSelector.setAdapter(adapter);

        addDictionaryBtn.setOnClickListener(v -> {
            modalAddDictionary.setVisibility(View.VISIBLE);
            modalAddDictionaryContent.setScaleX(0);
            modalAddDictionaryContent.setScaleY(0);
            modalAddDictionaryContent.animate().setDuration(100).scaleX(1).scaleY(1);
        });

        dismissLanguageModalBtn.setOnClickListener(v -> {
            modalAddDictionaryContent.animate().setDuration(100).scaleX(0).scaleY(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    modalAddDictionary.setVisibility(View.GONE);
                }
            });
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
            modalAddDictionary.setVisibility(View.GONE);
        });

        languageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguageInModal = ((String)parent.getSelectedItem()).toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void initViews() {
        importDictionaryBtn = findViewById(R.id.importDictionaryBtn);
        importDictionaryBtnExpanded = findViewById(R.id.importDictionaryBtnExpanded);
        myDictionariesRecView = findViewById(R.id.myDictionariesRecView);
        languageSelector = ViewUtil.getSpinner(findViewById(R.id.languageSelector));
        modalAddDictionary = findViewById(R.id.modalAddEditDictionary);
        modalImportDictionary = findViewById(R.id.modalImportDictionary);
        dismissLanguageModalBtn = findViewById(R.id.dismissLanguageModalBtn);
        addDictionaryBtn = findViewById(R.id.addDictionaryBtn);
        saveLanguageBtn = findViewById(R.id.saveLanguageBtn);
        importDictionaryByIdBtn = findViewById(R.id.importDictionaryByIdBtn);
        dismissDictionaryModalBtn = findViewById(R.id.dismissDictionaryModalBtn);
        importDictionaryModalBtn = findViewById(R.id.importDictionaryModalBtn);
        importDictionaryFromMemoryBtn = findViewById(R.id.importDictionaryFromMemoryBtn);
        modalAddDictionaryContent = findViewById(R.id.modalAddEditDictionaryContent);
        modalImportDictionaryContent = findViewById(R.id.modalImportDictionaryContent);
    }

    private void handleImportDictionary() {
        importDictionaryBtn.setOnClickListener(v -> {
            importDictionaryBtn.setVisibility(View.GONE);
            importDictionaryBtnExpanded.setVisibility(View.VISIBLE);
        });

        importDictionaryByIdBtn.setOnClickListener(v -> {
            modalImportDictionary.setVisibility(View.VISIBLE);
            modalImportDictionaryContent.setScaleX(0);
            modalImportDictionaryContent.setScaleY(0);
            modalImportDictionaryContent.animate().setDuration(100).scaleX(1).scaleY(1);
        });

        dismissDictionaryModalBtn.setOnClickListener(v -> {
            modalImportDictionaryContent.animate().setDuration(100).scaleX(0).scaleY(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    modalImportDictionary.setVisibility(View.GONE);
                }
            });
        });

        importDictionaryModalBtn.setOnClickListener(v -> {
            //TODO: implement import by id
        });

        importDictionaryFromMemoryBtn.setOnClickListener(v -> {
            //TODO: implement import from memory
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