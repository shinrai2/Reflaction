package cc.shinrai.reflaction;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Shinrai on 2017/11/2 0002.
 */

public class ReflactTree {
    private static final char[][] signal_block = {
            {'(', ')'},
            {'\"', '\"'}
    };
    private Object _receiver;
    private String _method;
    private Class<?>[] _arg_class;
    private Object[] _arg_object;
    private boolean _AND_SIGN; // a sign, when method is static, set true.

    private CoreFunc coreFunc;

    private ReflactTree(String script, CoreFunc coreFunc) {
        _AND_SIGN = false; // default is false.
        this.coreFunc = coreFunc;
        try {
            this.buildOne(script);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void buildOne(String script)
            throws ClassNotFoundException {
        String[] assem = StringMethod.splitWithToken(script, ',', true, signal_block);
        if(assem.length < 2) // if less than 2, must something wrong
            return;
        Object inner_1 = innerLayer(assem[0]); // Object part: a tree or a object
        if(inner_1 instanceof ReflactTree)
            _receiver = inner_1;
        else
            _receiver = coreFunc.get((String) inner_1);
        if(assem[1].startsWith("&")) { // a static func sign.
            _AND_SIGN = true;
            _method = assem[1].substring(1);
            if(_receiver == null)
                _receiver = ClassTable._ClassForName((String) inner_1);
        } else
            _method = assem[1];
        // TODO field operating.

        List<Class<?>> classList = new ArrayList<>();
        List<Object> objectList = new ArrayList<>();
        for(int i = 0; i < assem.length-2; i++) {
            String[] c_v = StringMethod.splitWithToken(assem[i+2], ':', true, signal_block); // split argv:: class:value
            Class<?> cls;
            Object inner_2; // Argv part: a tree or a object
            if(c_v.length == 1) { // if do not have split token, class is String.
                cls = String.class;
                inner_2 = innerLayer(c_v[0]);
            } else {
                cls = ClassTable._ClassForName(c_v[0]);
                inner_2 = innerLayer(c_v[1]);
            }
            Object arg_object; // a tree or a instance.
            if(inner_2 instanceof ReflactTree)
                arg_object = inner_2;
            else
                arg_object = coreFunc.getInstance(cls, (String) inner_2);

            classList.add(cls);
            objectList.add(arg_object);
        }

        _arg_class = new Class<?>[classList.size()]; // Declare a Class<?> array
        _arg_object = new Object[objectList.size()]; // Declare a Object array
        classList.toArray(_arg_class); // Class<?> list to Class<?> array
        objectList.toArray(_arg_object); // Object list to Object array
    }

    private Object innerLayer(String inner) {
        if(inner.startsWith("(") && inner.endsWith(")"))
            return new ReflactTree(inner.substring(1, inner.length() - 1), coreFunc);
        else if(inner.startsWith("\"") && inner.endsWith("\""))
            return inner.substring(1, inner.length() - 1);
        else
            return inner;
    }

    public Object exec() {
        Object _receiver_; // _receiver is not the actual receiver, but _receiver_ is.
        Method _method_ = null;
        if(_receiver instanceof ReflactTree)
            _receiver_ = ((ReflactTree) _receiver).exec(); // exec the inner func in receiver part.
        else
            _receiver_ = _receiver;
        if(_receiver_ == null) // if have not receiver, do not exec.
            return null;
        Class<?> _root_class;
        if(_AND_SIGN) // if receiver is a class, do not use to call getClass()
            _root_class = (Class<?>) _receiver_;
        else
            _root_class = _receiver_.getClass();
        if(_receiver_ != null) // protect. if receiver is null, pointless.
            while(_method_ == null && _root_class != Object.class) {
                try { // must handle it in this loop, otherwise this loop will be break.
                    _method_ = _root_class.getDeclaredMethod(_method, _arg_class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                _root_class = _root_class.getSuperclass();
            }
        if(_method_ == null) // if have not method, do not exec.
            return null;
        Object result = null; // receive the return of func
        Object[] objects = new Object[_arg_object.length];
        for(int i = 0; i < _arg_object.length; i++) {
            if (_arg_object[i] instanceof ReflactTree)
                objects[i] = ((ReflactTree) _arg_object[i]).exec(); // exec the inner func in arg part.
            else
                objects[i] = _arg_object[i];
        }
        try {
            result = _method_.invoke(_receiver_, objects); // exec the method
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
        return StringMethod.splitWithToken(script, ';', true, signal_block);
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
        __class_map.put("Context", Context.class);
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
        Character first = null;
        for (char[] _pair : signal_block) {
            if(first == null)
                try {
                    first = _stack.getFirst();
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }
            if(_pair[1] == c && ((Character) _pair[0]).equals(first)) {
                _stack.removeFirst();
                first = null;
                break;
            } else if(_pair[0] == c) {
                _stack.addFirst(c);
                first = null;
                break;
            }
        }
    }
}
