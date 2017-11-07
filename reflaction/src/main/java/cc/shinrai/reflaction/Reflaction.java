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
    }
    public void put(String symbol, Object object) {
        __OBJECTS__.put(symbol, object);
    }
    public Object get(String symbol) {
        if(symbol.equals("__SELF__"))
            return __OBJECTS__;
        Object obj = __OBJECTS__.get(symbol);
        if(obj != null)
            return obj;
        else
            // TODO
        return null;
    }
    public void reload() {

    }
    public void reload(String script) {

    }
}
