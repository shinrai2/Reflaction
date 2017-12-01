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
    void put(String symbol, Object object);
    Object get(String symbol);
    void rm(String symbol);
    Context getContext();

    /**
     * get the instance of the specified class.
     */
    Object getInstance(String className, String value);
    Object getInstance(Class<?> oriClass, String value);

    // some practicality func
//    Object NIL();
//    void judgif(boolean condition, Object satisfied, Object not_satisfied);
//    void loop(boolean condition, Object loop_body);
}
