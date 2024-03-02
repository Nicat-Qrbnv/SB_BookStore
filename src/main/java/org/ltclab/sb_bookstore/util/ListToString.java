package org.ltclab.sb_bookstore.util;

import org.ltclab.sb_bookstore.model.StoreElement;

import java.util.List;

public class ListToString {
    public static <T extends StoreElement> StringBuilder convert (List<T> list) {
        StringBuilder str = new StringBuilder();
        list.forEach(e -> str.append(e.toString().indent(4)));
        return str;
    }
}
