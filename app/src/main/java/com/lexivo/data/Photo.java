package com.lexivo.data;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Photo {
    private static final List<Photo> photoList = new LinkedList<>();

    private final String id;
    private String data;

    public Photo(String data) {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        this.data = data;
        photoList.add(this);
    }

    public static Photo get(String id) {
        return photoList.stream().filter(p -> p.id.equals(id)).collect(Collectors.toList()).get(0);
    }

    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(id, photo.id) && Objects.equals(data, photo.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data);
    }

    @NonNull
    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
