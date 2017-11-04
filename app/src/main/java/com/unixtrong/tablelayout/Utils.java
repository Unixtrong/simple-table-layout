package com.unixtrong.tablelayout;

import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * Author(s): danyun
 * Date: 2017/11/3
 */
public class Utils {

    public static List<Field> getAllDeclaredFields(Class<?> clazz) {
        LinkedList<Field> fieldList;
        for (fieldList = new LinkedList<>(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (!isInvalid(field)) {
                    fieldList.addLast(field);
                }
            }
        }

        return fieldList;
    }

    public static boolean isInvalid(Field f) {
        return Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()) || f.isSynthetic();
    }

    public static void debug(String msg) {
        Log.d("unixtrong", msg);
    }
}
