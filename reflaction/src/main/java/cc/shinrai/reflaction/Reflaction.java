package cc.shinrai.reflaction;

import java.util.HashMap;

/**
 * Created by Shinrai on 2017/11/2 0002.
 */

public class Reflaction implements CoreFunc {
    private HashMap<String, Object> __OBJECTS__;
    private ReflactTree[] mReflactTree;

    public Reflaction() {
        __OBJECTS__ = new HashMap<>();
    }

    @Override
    public void put(String symbol, Object object) {
        __OBJECTS__.put(symbol, object);
    }

    @Override
    public Object get(String symbol) {
        if(symbol.equals("__SELF__")) // direct to self.
            return this;
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
        mReflactTree = ReflactTree.build(script, this);
    }
}
