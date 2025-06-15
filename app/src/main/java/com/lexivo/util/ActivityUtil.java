package com.lexivo.util;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;

import com.lexivo.MainActivity;
import com.lexivo.R;

public abstract class ActivityUtil {
    public static void handleBackBtn(Activity activity) {
        Button btnBack = activity.findViewById(R.id.btnBack);
        LinearLayout usernameAndActiveMinutesContainer = activity.findViewById(R.id.usernameAndActiveMinutesContainer);
        LayoutParams layoutParams = (LayoutParams) usernameAndActiveMinutesContainer.getLayoutParams();
        int initialMargin = layoutParams.getMarginStart();

        btnBack.setOnClickListener(v -> activity.finish());
        if (activity instanceof MainActivity) {
            btnBack.setVisibility(View.GONE);
            layoutParams.setMarginStart(0);
        }
        else
            layoutParams.setMarginStart(initialMargin);

        usernameAndActiveMinutesContainer.setLayoutParams(layoutParams);

    }
}
