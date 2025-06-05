package com.lexivo.schema;

import androidx.annotation.NonNull;

public enum WordType {
    NOUN,
    ADJ_ADV,
    VERB,
    PRON_PREP,
    CONJUNCTION,
    INTERJECTION,
    QUESTION_WORD,
    NUMERAL;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case NOUN:
                return "noun";
            case ADJ_ADV:
                return "adjective/adverb";
            case VERB:
                return "verb";
            case PRON_PREP:
                return "pronoun/preposition";
            case CONJUNCTION:
                return "conjunction";
            case INTERJECTION:
                return "interjection";
            case QUESTION_WORD:
                return "question word";
            default:
                return "numeral";
        }
    }

    public static WordType fromString(String s) {
        switch(s) {
            case "noun":
                return NOUN;
            case "adjective/adverb":
                return ADJ_ADV;
            case "verb":
                return VERB;
            case "pronoun/preposition":
                return PRON_PREP;
            case "conjunction":
                return CONJUNCTION;
            case "interjection":
                return INTERJECTION;
            case "question word":
                return QUESTION_WORD;
            default:
                return NUMERAL;
        }
    }
}