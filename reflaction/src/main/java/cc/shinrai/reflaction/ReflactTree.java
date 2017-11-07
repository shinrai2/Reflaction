package cc.shinrai.reflaction;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Shinrai on 2017/11/2 0002.
 */

public class ReflactTree {
    private Method _method;
    private Object _receiver;
    private ARGV[] _ARGV_OBJECTS;

    private ReflactTree(String script) {
        // TODO
    }

    public Object exec() {
        Object result = null;
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

    public static ReflactTree[] build(String script) {
        // TODO
        return null;
    }
}

/**
 * the inner function ARGV's class.
 */
class ARGV {
    private Object _object;
    private ReflactTree _tree;
    private boolean flag;

    /**
     * Rule:
     * @obj set flag true.
     * @tree set flag false;
     */
    public ARGV(boolean flag, Object obj, ReflactTree tree) {
        if(flag)
            _object = obj;
        else
            _tree = tree;
    }

    public Object get() {
        if(flag)
            return _object;
        else
            return _tree.exec();
    }
}
