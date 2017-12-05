package cc.shinrai.reflaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinrai on 2017/11/27 0027.
 */

class METHOD implements BASIC {
    enum POINT {
        RECEIVER, METHOD, CLASS, OBJECT
    }

    private Object _receiver        = null;
    private String _method          = null;
    private String[] _classOfArgs   = null;
    private Object[] _objectOfArgs  = null;

    private List<String> classList;
    private List<Object> objectList;

    private BASIC _parent;
    private POINT point;

    public METHOD(BASIC parent) {
        set_parent(parent);
        this.point = POINT.RECEIVER;
        this.classList = new ArrayList<>();
        this.objectList = new ArrayList<>();
    }
    @Override
    public Object exec() {
        // TODO
        return null;
    }
    @Override
    public BASIC fin() {
        this._classOfArgs = new String[classList.size()];
        this.classList.toArray(_classOfArgs);
        this._objectOfArgs = new Object[objectList.size()];
        this.objectList.toArray(_objectOfArgs);
        /* clean */
        this.classList = null;
        this.objectList = null;

        return this;
    }
    @Override
    public void put(Object _obj) {
        switch (point) {
            case RECEIVER: // set receiver
                set_receiver(_obj);
                point = POINT.METHOD;
                break;
            case METHOD: // set method
                set_method((String) _obj);
                point = POINT.CLASS;
                break;
            case CLASS: // add class
                add_classOfArgs((String) _obj);
                point = POINT.OBJECT;
                break;
            case OBJECT: // add object
                add_objectOfArgs(_obj);
                point = POINT.CLASS;
                break;
        }
    }
    @Override
    public void set_parent(BASIC _basic) {
        this._parent = _basic;
    }
    @Override
    public BASIC get_parent() {
        return _parent;
    }

    /**
     * private setters of METHOD class.
     */
    private void set_receiver(Object _receiver) {
        this._receiver = _receiver;
    }

    private void set_method(String _method) {
        this._method = _method;
    }

    private void add_classOfArgs(String _class) {
        classList.add(_class);
    }
    private void add_objectOfArgs(Object _object) {
        objectList.add(_object);
    }
}
