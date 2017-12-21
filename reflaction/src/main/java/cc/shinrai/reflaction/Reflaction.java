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
    private BASIC rootScope;
    private Context mContext;

    public Reflaction(Context context) {
        __OBJECTS__ = new HashMap<>();
        mContext = context;
    }

    @Override
    public Object put(String symbol, Object object) {
        return __OBJECTS__.put(symbol, object);
    }

    @Override
    public Object get(String symbol) {
        if(symbol.equals("_")) // direct to self.
            return this;
        Object objInList = __OBJECTS__.get(symbol);
        if(objInList != null)
            return objInList;
        else
            return _Class._ClassForName(symbol);
    }

    @Override
    public Object rm(String symbol) {
        return __OBJECTS__.remove(symbol);
    }

    @Override
    public Object getInstance(String clsn, String value) {
        Class<?> cls;
        Object obj;
        cls = _Class._ClassForName(clsn);
        obj = getInstance(cls, value);
        return obj;
    }

    @Override
    public Object getInstance(Class<?> oriClass, String value) {
        Class<?> cls = _Class._ReloadClassName(oriClass);
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
    public Class<?> _ClassForName(String className) {
        return null;
    }

    @Override
    public Class<?> _ClassReload(Class<?> cls) {
        return null;
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
        return ((SCOPE) rootScope).execEach();
    }
    public void load(String script) {
//        mReflactTree = ReflactTree.build(script, this);
        // TODO parse the script string.
        BASIC current = null; // point to the current object.
        boolean _protect_next = false; // protect every char after '\\'.
        boolean _flag = false; // true:word end. false:METHOD or SCOPE end.
        StringBuilder stringBuilder = null;
        int _start = -1; // the first index of word.
        for(int i=0;i<script.length();i++) {
            if(_protect_next) { // the char next to the '\\'
                _protect_next = false;
                _start = i;
                continue; // next loop.
            }
            switch (script.charAt(i)) {
                /* protect char. The highest priority. */
                case '\\':
                    _protect_next = true;
                    if(stringBuilder == null)
                        stringBuilder = new StringBuilder();
                    if(_start != -1) {
                        stringBuilder.append(script.substring(_start, i));
                        _start = -1;
                    }
                    break;
                /* start of the func. */
                case '(':
                    current = new METHOD(current, this);
                    break;
                /* start of the scope. */
                case '{':
                    current = new SCOPE(current);
                    break;
                /* some ignored char. */
                case ' ':
                case '\n':
                case '\r':
                case '\t':
                case '\f':
                /* split the "class:object" with ':'. */
                case ':':
                /* a sign of split. */
                case ',':
                    _flag = true;
                /* end of the func. */
                case ')':
                /* end of the scope. */
                case '}':
                    if(_start != -1) {
                        if (stringBuilder == null)
                            current.put(script.substring(_start, i));
                        else { // use stringBuilder
                            stringBuilder.append(script.substring(_start, i));
                            current.put(stringBuilder.toString());
                        /* clean after use. */
                            stringBuilder = null;
                        }
                        _start = -1; // reset the _start.
                    }
                    if(_flag) {
                        _flag = false;
                        break;
                    }
                    BASIC parent = current.get_parent();
                    if(parent == null) { // current is the root.
                        current.fin();
                        break;
                    }
                    /* return the parent. */
                    parent.put(current.fin());
                    current = parent;
                    break;
                /* all of other common chars. */
                default:
                    if(_start == -1)
                        _start = i;
                    break;
            }
        }
        rootScope = current;
    }
}

class _Class {
    private static final HashMap<String, Class<?>> __class_map;
    private static final HashMap<Class<?>, Class<?>> __reload_map;
    static {
        __class_map = new HashMap<>();
        __class_map.put("String", String.class);
        __class_map.put("Integer", Integer.class);
        __class_map.put("Boolean", Boolean.class);
        __class_map.put("Float", Float.class);
        __class_map.put("Double", Double.class);
        __class_map.put("Long", Long.class);
        __class_map.put("int", int.class);
        __class_map.put("boolean", boolean.class);
        __class_map.put("float", float.class);
        __class_map.put("double", double.class);
        __class_map.put("long", long.class);
        __class_map.put("Context", Context.class);
        __reload_map = new HashMap<>();
        __reload_map.put(int.class, Integer.class);
        __reload_map.put(boolean.class, Boolean.class);
        __reload_map.put(float.class, Float.class);
        __reload_map.put(double.class, Double.class);
        __reload_map.put(long.class, Long.class);
    }

    public static Class<?> _ClassForName(String className) {
        Class<?> cls = __class_map.get(className);
        if(cls != null)
            return cls;
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> _ReloadClassName(Class<?> cls) {
        Class<?> reload_cls = __reload_map.get(cls);
        if(reload_cls != null)
            return reload_cls;
        return cls; // not match, return origin.
    }
}
