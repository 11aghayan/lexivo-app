package com.lexivo.data;

public enum WordType {
    NOUN,
    ADJ_ADV,
    VERB,
    PRON_PREP,
    CONJUNCTION,
    INTERJECTION,
    QUESTION_WORD,
    NUMERAL;

    public String getString() {
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
}