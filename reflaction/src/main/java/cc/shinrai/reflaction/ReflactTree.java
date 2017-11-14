package cc.shinrai.reflaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shinrai on 2017/11/2 0002.
 */

public class ReflactTree {
    private static final char[][] signal_block = {
            {'(', ')'},
            {'\"', '\"'}
    };
    private Method _method;
    private Object _receiver;
    private InnerObject[] _INNER_OBJECTS;
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
        String[] assem = StringMethod.splitWithToken(script, ',', true, signal_block);
        if(assem.length < 2) // if less than 2, must something wrong
            return;
        _receiver = coreFunc.get(assem[0]); // the function receiver

        List<Class<?>> classList = new ArrayList<>(); // for build method
        List<InnerObject> innerList = new ArrayList<>();
        for(int i = 0; i < assem.length-2; i++) {
            String[] c_v = StringMethod.splitWithToken(assem[i+2], ':', true, signal_block); // split argv:: class:value
            Class<?> cls = ClassTable._ClassForName(c_v[0]);
            Class<?> clsForInstance = ClassTable._ReloadClassName(cls); // basic class do not have instance method.
            InnerObject argv;
            if(c_v[1].startsWith("(") && c_v[1].endsWith(")"))
                argv = new InnerObject(false, null, new ReflactTree(c_v[1].substring(1, c_v[1].length()-1), coreFunc));
            else if(c_v[1].startsWith("\"") && c_v[1].endsWith("\""))
                argv = new InnerObject(true, clsForInstance.getDeclaredConstructor(String.class).newInstance(c_v[1].substring(1, c_v[1].length()-1)), null);
            else
                argv = new InnerObject(true, clsForInstance.getDeclaredConstructor(String.class).newInstance(c_v[1]), null);

            classList.add(cls);
            innerList.add(argv);
        }

        Class<?>[] classes = new Class<?>[classList.size()]; // Declare a Class<?> array
        _INNER_OBJECTS = new InnerObject[innerList.size()]; // Declare a ARGV array
        classList.toArray(classes); // Class<?> list to Class<?> array
        innerList.toArray(_INNER_OBJECTS); // ARGV list to ARGV array

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
        Object[] objects = new Object[_INNER_OBJECTS.length];
        for(int i = 0; i < _INNER_OBJECTS.length; i++)
            objects[i] = _INNER_OBJECTS[i].get();
        try {
            result = _method.invoke(_receiver, objects); // exec the method
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
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
        return StringMethod.splitWithToken(script, ';', true);
    }
}

/**
 * the inner function ARGV's class.
 */
class InnerObject {
    private Object _object;
    private ReflactTree _tree;
    private boolean _flag;

    /**
     * the init func.
     * Rule:
     * @obj set flag true.
     * @tree set flag false.
     */
    public InnerObject(boolean flag, Object obj, ReflactTree tree) {
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
        __reload_map = new HashMap<>();
        __reload_map.put(int.class, Integer.class);
        __reload_map.put(boolean.class, Boolean.class);
        __reload_map.put(float.class, Float.class);
        __reload_map.put(double.class, Double.class);
        __reload_map.put(long.class, Long.class);
    }

    public static Class<?> _ClassForName(String className) throws ClassNotFoundException {
        Class<?> cls = __class_map.get(className);
        if(cls != null)
            return cls;
        return Class.forName(className);
    }

    public static Class<?> _ReloadClassName(Class<?> cls) {
        Class<?> reload_cls = __reload_map.get(cls);
        if(reload_cls != null)
            return reload_cls;
        return cls; // not match, return origin.
    }
}

class StringMethod {
    private static final char[] block_whitespace = new char[] {' ', '\n', '\r', '\t', '\f'};

    /**
     * Split the String by the token char.
     * @param origin the String which is wanted to be split.
     * @param token
     * @param is_block_whitespace if you do not want to block whitespace, set false. But... actually I have not tested it. :)
     * @param signal_block
     * @return
     */
    public static String[] splitWithToken(String origin, char token, boolean is_block_whitespace, char[]... signal_block) {
        List<String> out = new ArrayList<>();
        LinkedList<Character> _protect_stack = new LinkedList<>();
        int _start = -1;
        int i;
        for(i=0;i<origin.length();i++) {
            char _single = origin.charAt(i);
            if(signal_block.length != 0)
                isProtect(_single, _protect_stack, signal_block);
            if(isWhitespaceOrToken(_single, token, is_block_whitespace) && _protect_stack.isEmpty()) { // this is a meaningless whitespace
                if (_start != -1 && _single == token) { // avoid split on every whitespace
                    out.add(origin.substring(_start, backTrack(origin, i, is_block_whitespace)));
                    _start = -1;
                }
                continue;
            }
            if(_start != -1)
                continue;
            else
                _start = i;
        }
        if(_start != -1)
            out.add(origin.substring(_start, backTrack(origin, i, is_block_whitespace)));

        String[] outArr = new String[out.size()];
        out.toArray(outArr);
        return outArr;
    }

    /**
     * Backtracking forward, and find the last non-whitespace
     * @param origin
     * @param i
     * @return
     */
    private static int backTrack(String origin, int i, boolean is_block_whitespace) {
        int bti = i;
        if(is_block_whitespace)
            while(isWhitespace(origin.charAt(bti-1)))
                bti--;
        return bti;
    }

    /**
     * Determine whether the char is a whitespace or a token
     * @param c
     * @param token
     * @param _is_block
     * @return
     */
    private static boolean isWhitespaceOrToken(char c, char token, boolean _is_block) {
        if(!_is_block) // if do not block, always return false.
            return false;
        if(c == token)
            return true;
        return isWhitespace(c);
    }

    private static boolean isWhitespace(char c) {
        for (int i = 0; i < block_whitespace.length; i++) {
            if(block_whitespace[i] == c)
                return true;
        }
        return false;
    }

    private static void isProtect(char c, LinkedList<Character> _stack, char[][] signal_block) {
        for (char[] _pair : signal_block) {
            if(_stack.isEmpty() || _pair[0] == _stack.getFirst()) {
                if(_pair[0] == c) {
                    _stack.addFirst(c);
                    break;
                }
                if (_pair[1] == c && !_stack.isEmpty()) {
                    _stack.removeFirst();
                    break;
                }
            }
        }
    }
}
