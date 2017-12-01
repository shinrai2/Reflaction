package cc.shinrai.reflaction;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by Shinrai on 2017/11/2 0002.
 */

public class Reflaction implements CoreFunc {
    private HashMap<String, Object> __OBJECTS__;
//    private ReflactTree[] mReflactTree;
    private SCOPE rootScope;
    private Context mContext;

    public Reflaction(Context context) {
        __OBJECTS__ = new HashMap<>();
        mContext = context;
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

    @Override
    public void rm(String symbol) {
        __OBJECTS__.remove(symbol);
    }

    @Override
    public Object getInstance(String clsn, String value) {
        Class<?> cls;
        Object obj = null;
        try {
            cls = ClassTable._ClassForName(clsn);
            obj = getInstance(cls, value);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public Object getInstance(Class<?> oriClass, String value) {
        Class<?> cls = ClassTable._ReloadClassName(oriClass);
        Object obj = null;
        try {
            obj = cls.getDeclaredConstructor(String.class).newInstance(value);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public Object exec() {
//        if(mReflactTree != null && mReflactTree.length != 0)
//            for(int i = 0; i < mReflactTree.length; i++)
//                mReflactTree[i].exec();
//        return null;
        return rootScope.execEach();
    }
    public void load(String script) {
//        mReflactTree = ReflactTree.build(script, this);
        // TODO parse the script string.
        Object parent = null;
        for()
    }
}
