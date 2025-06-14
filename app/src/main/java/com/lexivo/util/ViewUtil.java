package com.lexivo.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.google.android.material.card.MaterialCardView;
import com.lexivo.R;
import com.lexivo.schema.Gender;

public abstract class ViewUtil {

    public static Spinner getSpinner(View v) {
        return (Spinner) ((MaterialCardView) v).getChildAt(0);
    }

    public static void openModal(ViewGroup modal) {
        modal.setVisibility(View.VISIBLE);
        View content = modal.getChildAt(0);
        content.setScaleX(0);
        content.setScaleY(0);
        content.animate().setDuration(100).scaleX(1).scaleY(1);
    }

    public static void closeModal(ViewGroup modal) {
        modal.setVisibility(View.GONE);
    }

    public static String[] getGenderStringAndColorArray(Context context, Gender gender) {
        return switch (gender) {
            case MASCULINE ->
                    new String[]{context.getString(R.string.text_gender_masculine), String.valueOf(context.getColor(R.color.gender_male))};
            case FEMININE ->
                    new String[]{context.getString(R.string.text_gender_feminine), String.valueOf(context.getColor(R.color.gender_female))};
            case NEUTER ->
                    new String[]{context.getString(R.string.text_gender_neutral), String.valueOf(context.getColor(R.color.gender_neutral))};
            case PERSONAL ->
                    new String[]{context.getString(R.string.text_gender_personal), String.valueOf(context.getColor(R.color.gender_personal))};
            case NO_GENDER -> new String[]{null, null};
            default ->
                    new String[]{context.getString(R.string.text_gender_plural), String.valueOf(context.getColor(R.color.gender_plural))};
        };
    }

}
