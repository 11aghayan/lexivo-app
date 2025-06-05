package com.lexivo;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lexivo.adapters.ArrayAdapters;
import com.lexivo.util.ViewUtil;

public class AddEditWordActivity extends AppCompatActivity {
    Spinner spinnerWordType, spinnerWordGender;

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

        initViews();
        handleSpinnerWordType();
    }

    private void initViews() {
        spinnerWordType = ViewUtil.getSpinner(findViewById(R.id.spinnerWordType));
        spinnerWordGender = ViewUtil.getSpinner(findViewById(R.id.spinnerWordGender));
    }

    private void handleSpinnerWordType() {
        var adapter = ArrayAdapters.wordTypeAdapter(this);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerWordType.setAdapter(adapter);
    }
}