package com.lexivo.data;

import androidx.annotation.NonNull;

import com.lexivo.exception.DuplicateValueException;
import com.lexivo.exception.HashAlreadyExistsException;
import com.lexivo.util.HashUtil;
import com.lexivo.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Dictionary implements ObjectContainingId {
    private final List<Word> words = new ArrayList<>();
    private final List<Expression> expressions = new ArrayList<>();
    private final String id;
    private String language;

    public Dictionary(Language language) {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        this.language = language.getLabel();
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getWordCount() {
        return words.size();
    }

    public long getExpressionCount() {
        return expressions.size();
    }

    public List<Word> getAlWords() {
        return words;
    }

    public Word getWordById(String id) {
        return getAlWords().stream().filter(w -> w.getId().equals(id)).collect(Collectors.toList()).get(0);
    }

    public void addWord(Word w) throws HashAlreadyExistsException {
        if (HashUtil.isHashInList(words, w.getHash())) {
            throw new HashAlreadyExistsException();
        }
        words.add(w);
    }

    public void updateWord(Word w) throws NoSuchElementException {
        int index = ListUtil.getIndexById(words, w.getId());
        words.set(index, w);
    }

    public void deleteWord(Word w) {
        words.remove(w);
    }

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

    public void updateExpression(Expression e) throws NoSuchElementException {
        int index = ListUtil.getIndexById(expressions, e.getId());
        expressions.set(index, e);
    }

    public void changeLanguage(String languageId) {
        language = languageId;
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
                "id='" + id + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
