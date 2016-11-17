/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {

    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .generateNonExecutableJson()
            // TODO
            //.registerTypeAdapter()
            .create();

    private GsonHelper() {}

    public static Gson get() {
        return GSON;
    }
}
