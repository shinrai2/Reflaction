package cc.shinrai.reflaction;

import android.content.Context;

/**
 * Created by Shinrai on 2017/11/8 0008.
 */

public interface CoreFunc {
    void put(String symbol, Object object);
    Object get(String symbol);
    void rm(String symbol);
    Object getInstance(String className, String value);
    Object getInstance(Class<?> oriClass, String value);
    Context getContext();

    // some practicality func
    Object NIL();
//    void judgif(boolean condition, Object satisfied, Object not_satisfied);
//    void loop(boolean condition, Object loop_body);
}
