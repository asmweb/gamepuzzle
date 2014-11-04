package com.myftiu.king.model;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by myftiu on 04/11/14.
 */
public class Score implements Comparator<Integer> {

    Map<Integer, Integer> base;

    public Score(Map<Integer, Integer> base) {
        this.base = base;
    }

    public int compare(Integer a, Integer b) {
        return -(base.get(a).compareTo(base.get(b)));
    }


}
