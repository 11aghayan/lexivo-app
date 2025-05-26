package com.lexivo.util;

import com.lexivo.data.ObjectContainingId;

import java.util.List;
import java.util.NoSuchElementException;

public abstract class ListUtil {
    public static int getIndexById(List<? extends ObjectContainingId> list, String id) throws NoSuchElementException {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }
}
