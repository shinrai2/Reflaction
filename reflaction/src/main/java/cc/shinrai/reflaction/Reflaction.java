package cc.shinrai.reflaction;

import java.util.HashMap;

/**
 * Created by Shinrai on 2017/11/2 0002.
 */

public class Reflaction {
    private HashMap<String, Object> __OBJECTS__;
    private ReflactTree[] mReflactTree;

    public Reflaction() {
        __OBJECTS__ = new HashMap<>();
    }

    public void put(String symbol, Object object) {
        __OBJECTS__.put(symbol, object);
    }

    public Object get(String symbol) {
        if(symbol.equals("__SELF__")) // direct to HashMap self.
            return __OBJECTS__;
        Object obj = __OBJECTS__.get(symbol);
        if(obj != null)
            return obj;
        else
            return null;
    }
    public void reload() {
        if(mReflactTree != null && mReflactTree.length != 0)
            for(int i=0;i<mReflactTree.length;i++)
                mReflactTree[i].exec();
    }
    public void load(String script) {
        mReflactTree = ReflactTree.build(script);
    }
}
