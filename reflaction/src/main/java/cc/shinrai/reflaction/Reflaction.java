package cc.shinrai.reflaction;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by Shinrai on 2017/11/2 0002.
 */

public class Reflaction {
    private HashMap<String, Object> __OBJECTS__;
    private ReflactTree mReflactTree;

    public Reflaction() {
        __OBJECTS__ = new HashMap<>();
        put("__SELF__", __OBJECTS__);
    }
    public void put(String symbol, Object object) {
        __OBJECTS__.put(symbol, object);
    }
    public Object get(String symbol) {
        return __OBJECTS__.get(symbol);
    }
    public void reload() {
        Object obj = __OBJECTS__.get("__SELF__");
        try {
            Class<?>[] classes = new Class<?>[] {};
//            classes[]
            Method setname = obj.getClass().getDeclaredMethod("setName", String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void reload(String script) {

    }
}
