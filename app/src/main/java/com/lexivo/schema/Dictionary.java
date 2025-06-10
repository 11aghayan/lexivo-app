package com.lexivo.schema;

import androidx.annotation.NonNull;

import com.lexivo.exception.DuplicateValueException;
import com.lexivo.exception.DuplicateHashException;
import com.lexivo.util.HashUtil;
import com.lexivo.util.ListUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Dictionary implements ObjectContainingId {
    private static final List<Dictionary> dictionaries = new ArrayList<>();
    private final List<Word> words = new ArrayList<>();
    private List<Word> wordsFiltered;
    private final List<Expression> expressions = new ArrayList<>();
    private final String id;
    private Language language;

    public Dictionary(Language language) {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        this.language = language;
        this.wordsFiltered = ListUtil.copyOfList(words);
    }

    public String getId() {
        return id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public int getAllWordsCount() {
        return words.size();
    }

    public int getFilteredWordsCount() { return wordsFiltered.size(); }

    public int getExpressionCount() {
        return expressions.size();
    }

    public List<Word> getAlWords() {
        return words;
    }

    public List<Word> getWordsFiltered() { return wordsFiltered; }

    public LinkedList<Word> getAllWordsShuffled() {
        LinkedList<Word> copyOfWords = new LinkedList<>(words);
        Collections.shuffle(copyOfWords);
        return copyOfWords;
    }

    public Word getWordById(String id) throws IndexOutOfBoundsException {
        return getAlWords().stream().filter(w -> w.getId().equals(id)).collect(Collectors.toList()).get(0);
    }

    public List<Word> searchWords(CharSequence s) {
        String searchWord = s.toString().toLowerCase();
        List<Word> result = new ArrayList<>();
        for (Word word : words) {
            if (
                    (word.getOriginal().getValue() != null && word.getOriginal().getValue().toLowerCase().contains(searchWord))
                    || (word.getTranslation().getValue() != null && word.getTranslation().getValue().toLowerCase().contains(searchWord))
                    || (word.getPlural() != null && word.getPlural().toLowerCase().contains(searchWord))
                    || (word.getPast1() != null && word.getPast1().toLowerCase().contains(searchWord))
                    || (word.getPast2() != null && word.getPast2().toLowerCase().contains(searchWord))
            ) {
                result.add(word);
            }
        }
        return result;
    }

    public void addWord(Word w) throws DuplicateHashException {
        if (HashUtil.isHashInList(words, w.getHash())) {
            throw new DuplicateHashException();
        }
        wordsFiltered.add(w);
        words.add(w);
    }

    public void deleteWord(Word w) {
        words.remove(w);
        wordsFiltered.remove(w);
    }

    public void deleteWord(int pos) {
        Word w = wordsFiltered.get(pos);
        words.remove(w);
        wordsFiltered.remove(w);
    }

    public void setWordsFiltered(List<Word> wordsFiltered) { this.wordsFiltered = wordsFiltered; }

    public List<Expression> getAllExpressions() {
        return expressions;
    }

    public Expression getExpressionById(String id) {
        return getAllExpressions().stream().filter(e -> e.getId().equals(id)).collect(Collectors.toList()).get(0);
    }

    public void addExpression(Expression e) {
        expressions.add(e);
    }

    public void deleteExpression(Expression e) {
        expressions.remove(e);
    }

    public static void addDictionary(Dictionary dictionary) throws DuplicateValueException {
        boolean isDuplicate = false;
        for (var d : dictionaries) {
            isDuplicate = Objects.equals(dictionary, d);
        }
        if (isDuplicate) {
            throw new DuplicateValueException();
        }
        dictionaries.add(dictionary);
    }

    public static List<Dictionary> getDictionaries() {
        return dictionaries;
    }

    public static Dictionary getDictionaryById(String id) {
        for (Dictionary d : dictionaries) {
            if (d.getId().equals(id)) {
                return d;
            }
        }

        return null;
    }

    public static void deleteDictionary(Dictionary d) {
        dictionaries.remove(d);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dictionary that = (Dictionary) o;
        return Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, language);
    }

    @NonNull
    @Override
    public String toString() {
        return "Dictionary{" +
                "language='" + language.getLabel() + '\'' +
                '}';
    }
}
