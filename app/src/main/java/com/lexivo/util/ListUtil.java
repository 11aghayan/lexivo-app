package com.lexivo.util;

import com.lexivo.schema.ObjectContainingId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public abstract class ListUtil {
    public static int getIndexById(List<? extends ObjectContainingId> list, String id) throws NoSuchElementException {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    public static String joinElementsIntoString(List<String> list, String concatenator) {
        StringBuilder result = new StringBuilder();
        int index = 0;
        for (String elm : list) {
            result.append(elm);
            if (++index != list.size()) {
                result.append(concatenator);
            }
        }
        return result.toString();
    }

    public static String joinElementsIntoString(String[] list, String concatenator) {
        return joinElementsIntoString(Arrays.stream(list).collect(Collectors.toList()), concatenator);
    }

    public static <T> List<T> copyOfList(List<T> list) {
        List<T> copyOfList = new ArrayList<>(list.size());
        copyOfList.addAll(list);
        return copyOfList;
    }
}
