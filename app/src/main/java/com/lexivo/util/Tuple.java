package com.lexivo.util;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Tuple<T, K> {
    private T firstElement;
    private K secondElement;

    public Tuple(T firstElement, K secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    public T getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(T firstElement) {
        this.firstElement = firstElement;
    }

    public K getSecondElement() {
        return secondElement;
    }

    public void setSecondElement(K secondElement) {
        this.secondElement = secondElement;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(firstElement, tuple.firstElement) && Objects.equals(secondElement, tuple.secondElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstElement, secondElement);
    }

    @NonNull
    @Override
    public String toString() {
        return "[" + firstElement + ", " + secondElement + "]";
    }
}
