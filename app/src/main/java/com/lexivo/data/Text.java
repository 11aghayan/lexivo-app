package com.lexivo.data;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Text {
    private String value;
    private String details;

    public Text(String value, String details) {
        this.value = value;
        this.details = details;
    }

    public Text(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return Objects.equals(value, text.value) && Objects.equals(details, text.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, details);
    }

    @NonNull
    @Override
    public String toString() {
        return "Text{" +
                "value='" + value + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
