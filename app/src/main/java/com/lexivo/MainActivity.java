package com.lexivo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lexivo.adapters.ArrayAdapters;
import com.lexivo.adapters.DictionaryAdapter;
import com.lexivo.exception.UnableToSaveException;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Language;
import com.lexivo.exception.DuplicateValueException;
import com.lexivo.util.ActivityUtil;
import com.lexivo.util.Memory;
import com.lexivo.util.SystemUtil;
import com.lexivo.util.ToastUtil;
import com.lexivo.util.ViewUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ACTIVITY_MAIN";
    private Button
            importDictionaryBtn, addDictionaryBtn, dismissLanguageModalBtn, saveLanguageBtn, importDictionaryByIdBtn, dismissDictionaryModalBtn, importDictionaryModalBtn, importDictionaryFromMemoryBtn, btnDeleteDictionary;
    private LinearLayout importDictionaryBtnExpanded;
    private RecyclerView myDictionariesRecView;
    private Spinner languageSelector;
    private ConstraintLayout modalAddDictionary, modalImportDictionary;
    private String selectedLanguageInModal;
    private DictionaryAdapter myDictionariesAdapter;

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
        SystemUtil.hideSystemUi(getWindow());

        Language.init(this);
        Dictionary.setDictionaries(Memory.retrieveData(this));

        ActivityUtil.handleBackBtn(this);

        initViews();
        handleMyDictionaries();
        handleImportDictionary();
        handleAddDictionaryModal();
        handleOnBackPressed();
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
        btnDeleteDictionary = findViewById(R.id.btnDeleteDictionary);
    }

    private void handleAddDictionaryModal() {
        btnDeleteDictionary.setVisibility(View.GONE);
        var adapter = ArrayAdapters.languagesAdapter(MainActivity.this);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        languageSelector.setAdapter(adapter);

        addDictionaryBtn.setOnClickListener(v -> ViewUtil.openModal(modalAddDictionary));

        dismissLanguageModalBtn.setOnClickListener(v -> ViewUtil.closeModal(modalAddDictionary));

        saveLanguageBtn.setOnClickListener(v -> {
            if (selectedLanguageInModal != null) {
                try {
                    Dictionary dict = new Dictionary(Language.get(selectedLanguageInModal));
                    myDictionariesAdapter.addDictionary(dict);
                    toggleNoItemsText();
                }
                catch (DuplicateValueException dve) {
                    Toast.makeText(MainActivity.this, getResources().getText(R.string.toast_dictionary_already_exists), Toast.LENGTH_SHORT).show();
                }
                catch (UnableToSaveException use) {
                    ToastUtil.unableToSave(this);
                }
            }
            ViewUtil.closeModal(modalAddDictionary);
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

    private void handleImportDictionary() {
        importDictionaryBtn.setOnClickListener(v -> {
            importDictionaryBtnExpanded.setVisibility(View.VISIBLE);
            importDictionaryBtn.setVisibility(View.GONE);
        });

        importDictionaryByIdBtn.setOnClickListener(v -> ViewUtil.openModal(modalImportDictionary));

        dismissDictionaryModalBtn.setOnClickListener(v -> ViewUtil.closeModal(modalImportDictionary));

        importDictionaryModalBtn.setOnClickListener(v -> {
            //TODO: implement import by id
        });

        importDictionaryFromMemoryBtn.setOnClickListener(v -> Memory.importDictionary(this));
    }

    private void handleMyDictionaries() {
        myDictionariesAdapter = new DictionaryAdapter(MainActivity.this);
        myDictionariesRecView.setAdapter(myDictionariesAdapter);
        myDictionariesRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void toggleNoItemsText() {
        findViewById(R.id.noDictionariesText).setVisibility(
                Dictionary.getDictionaries().isEmpty() ? View.VISIBLE : View.GONE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                if (uri == null) return;
                try(InputStream inputStream = getContentResolver().openInputStream(uri);
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))
                ) {
                    Gson gson = new Gson();
                    String dataJson = br.lines().reduce("", (acc, val) -> acc + val);
                    Dictionary dictionary = gson.fromJson(dataJson, Dictionary.class);
                    Dictionary.addDictionary(dictionary, this);
                    ToastUtil.dictionaryImported(this);
                }
            }
            catch (IOException ioe) {
                ToastUtil.dictionaryNotImported(this);
                Log.e(TAG, "onActivityResult: " + ioe.getMessage());
            }
            catch (DuplicateValueException dve) {
                ToastUtil.dictionaryNotImported(this, true);
            }
            catch (UnableToSaveException utse) {
                ToastUtil.unableToSave(this);
            }
            finally {
                getOnBackPressedDispatcher().onBackPressed();
            }
        }
    }

    private void handleOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (modalImportDictionary.getVisibility() == View.VISIBLE){
                    ViewUtil.closeModal(modalImportDictionary);
                }
                else if (modalAddDictionary.getVisibility() == View.VISIBLE) {
                    ViewUtil.closeModal(modalAddDictionary);
                }
                else if (importDictionaryBtn.getVisibility() == View.GONE) {
                    importDictionaryBtn.setVisibility(View.VISIBLE);
                    importDictionaryBtnExpanded.setVisibility(View.GONE);
                }
                else
                    finish();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        toggleNoItemsText();
        myDictionariesAdapter.notifyDataSetChanged();
    }
}