package com.humanity.vs.cards.cardsvshumanity.utils;

import java.util.List;

/**
 * Created by robot on 08.11.15.
 */
public class EmptyUtils {
    public static boolean isEmpty(String text) {
        return text == null || text.equals("") || text.trim().equals("");
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

}
