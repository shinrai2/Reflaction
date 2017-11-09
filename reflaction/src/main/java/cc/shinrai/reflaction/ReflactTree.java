package cc.shinrai.reflaction;


import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinrai on 2017/11/2 0002.
 */

public class ReflactTree {
    private Method _method;
    private Object _receiver;
    private ARGV[] _ARGV_OBJECTS;
    private CoreFunc coreFunc;

    private ReflactTree(String script, CoreFunc coreFunc) {
        this.coreFunc = coreFunc;
        try {
            this.buildOne(script);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void buildOne(String script)
            throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String[] assem = script.split(",\\n*");
        if(assem.length < 2) // if less than 2, must something wrong
            return;
        _receiver = coreFunc.get(assem[0]); // the function receiver

        List<Class<?>> classList = new ArrayList<>(); // for build method
        List<ARGV> argvList = new ArrayList<>();
        for(int i=0;i<assem.length-2;i++) {
            String[] c_v = assem[i+2].split(":"); // split argv:: class:value
            Class<?> cls = ClassTable._ClassForName(c_v[0]);
            Class<?> clsForInstance = ClassTable._ReloadClassName(cls); // basic class do not have instance method.
            ARGV argv;
            if(c_v[1].startsWith("(") && c_v[1].endsWith(")"))
                argv = new ARGV(false, null, new ReflactTree(c_v[1].substring(1, c_v[1].length()-1), coreFunc));
            else
                argv = new ARGV(true, clsForInstance.getDeclaredConstructor(String.class).newInstance(c_v[1]), null);

            classList.add(cls);
            argvList.add(argv);
        }

        Class<?>[] classes = new Class<?>[classList.size()];
        _ARGV_OBJECTS = new ARGV[argvList.size()];
        classList.toArray(classes);
        argvList.toArray(_ARGV_OBJECTS);

        Class<?> _root_class = _receiver.getClass();
        if(_receiver != null) // protect. if receiver is null, pointless.
            while(_method == null && _root_class != Object.class) {
                try { // must handle it in this loop, otherwise this loop will be break.
                    _method = _root_class.getDeclaredMethod(assem[1], classes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                _root_class = _root_class.getSuperclass();
            }
    }

    public Object exec() {
        if(_receiver == null || _method == null) // if have not receiver, do not exec.
            return null;
        Object result = null; // receive the return of func
        Object[] objects = new Object[_ARGV_OBJECTS.length];
        for(int i=0;i<_ARGV_OBJECTS.length;i++)
            objects[i] = _ARGV_OBJECTS[i].get();
        try {
            result = _method.invoke(_receiver, objects); // exec the method
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ReflactTree[] build(String script, CoreFunc coreFunc) {
        String[] scripts = split(script);
        ReflactTree[] reflactTrees = new ReflactTree[scripts.length];
        for(int i=0;i<scripts.length;i++)
            reflactTrees[i] = new ReflactTree(scripts[i], coreFunc);
        return reflactTrees;
    }

    private static String[] split(String script) {
        return script.split(";\\n*");
    }
}

/**
 * the inner function ARGV's class.
 */
class ARGV {
    private Object _object;
    private ReflactTree _tree;
    private boolean _flag;

    /**
     * the init func.
     * Rule:
     * @obj set flag true.
     * @tree set flag false.
     */
    public ARGV(boolean flag, Object obj, ReflactTree tree) {
        _flag = flag;
        if(flag)
            _object = obj;
        else
            _tree = tree;
    }

    public Object get() {
        if(_flag)
            return _object;
        else
            return _tree.exec();
    }
}

class ClassTable {
    public static Class<?> _ClassForName(String className) throws ClassNotFoundException {
        if(className.equals("String"))
            return String.class;
        if(className.equals("Integer"))
            return Integer.class;
        if(className.equals("Boolean"))
            return Boolean.class;
        if(className.equals("Float"))
            return Float.class;
        if(className.equals("Double"))
            return Double.class;
        if(className.equals("Long"))
            return Long.class;
        if(className.equals("int"))
            return int.class;
        if(className.equals("boolean"))
            return boolean.class;
        if(className.equals("float"))
            return float.class;
        if(className.equals("double"))
            return double.class;
        if(className.equals("long"))
            return long.class;
        return Class.forName(className);
    }

    public static Class<?> _ReloadClassName(Class<?> cls) {
        if(cls == int.class)
            return Integer.class;
        if(cls == boolean.class)
            return Boolean.class;
        if(cls == float.class)
            return Float.class;
        if(cls == double.class)
            return Double.class;
        if(cls == long.class)
            return Long.class;
        return cls; // not match, return origin.
    }
}
