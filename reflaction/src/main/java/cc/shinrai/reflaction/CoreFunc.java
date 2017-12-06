package cc.shinrai.reflaction;

import android.content.Context;

/**
 * Created by Shinrai on 2017/11/8 0008.
 */

public interface CoreFunc {
    /**
     * the basic operation of REFLACTION.
     * put obj to   list.
     * get obj from list.
     * rem obj from list.
     * get context from field.
     */
    Object put(String symbol, Object object);
    Object get(String symbol);
    Object rm(String symbol);
    Context getContext();

    /**
     * get the instance of the specified class.
     */
    Object getInstance(String className, String value);
    Object getInstance(Class<?> oriClass, String value);
    Class<?> _ClassForName(String className);
    Class<?> _ClassReload(Class<?> cls);
}
