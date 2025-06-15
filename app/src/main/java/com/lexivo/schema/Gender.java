package com.lexivo.schema;

import androidx.annotation.NonNull;

public enum Gender {
    MASCULINE,
    FEMININE,
    NEUTER,
    PERSONAL,
    PLURAL,
    NO_GENDER;

    @NonNull
    @Override
    public String toString() {
        if (this.equals(NO_GENDER)) {
            return "NO GENDER";
        }
        else {
            return super.toString();
        }
    }
}