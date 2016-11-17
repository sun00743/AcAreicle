/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static ArrayList<Integer> toList(int...arr){
        ArrayList<Integer> list = new ArrayList<>(arr.length);
//        for(int i=0 ; i<arr.length; i++){
//            list.add(Integer.valueOf(arr[i]));
//        }
        for (int i:arr) {
            list.add(i);
        }
        return list;
    }

    public static long[] toLongArray(List<Long> list){
        if(list==null || list.isEmpty()) return null;
        long[] arr = new long[list.size()];
        for(int i=0;i<list.size();i++){
            arr[i] = list.get(i).longValue();
        }
        return arr;
    }
    public static <E> ArrayList<E> newArrayList(){
        return new ArrayList<>();
    }

    public static <E> boolean validate(List<E> list){
        return list != null && !list.isEmpty();
    }
    public static <E> SparseArray<E> putAll(SparseArray<E> source, SparseArray<E> dest){
        if(dest == null)
            dest = source;
        else if(source != null)
            for(int i=0 ; i<source.size();i++){
                int key = source.keyAt(i);
                E value = source.valueAt(i);
                dest.put(key, value);
            }
        return dest;
    }

    public static ArrayList<Integer> asList(int... arr){
        ArrayList<Integer> list = new ArrayList<>(arr.length);
        for(int i=0;i<arr.length;i++){
            list.add(Integer.valueOf(arr[i]));
        }
        return list;

    }
}
