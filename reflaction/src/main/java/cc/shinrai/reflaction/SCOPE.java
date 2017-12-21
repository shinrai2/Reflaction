package cc.shinrai.reflaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinrai on 2017/11/27 0027.
 */

class SCOPE implements BASIC {
    private METHOD[] _methods = null;
    private Object[] _rec = null;
    /* help build. */
    private List<METHOD> methodList;

    private BASIC _parent;

    public SCOPE(BASIC parent) {
        set_parent(parent);
        this.methodList = new ArrayList<>();
    }

    @Override
    public Object exec() {
        return this;
    }
    @Override
    public BASIC fin() {
        this._methods = new METHOD[methodList.size()];
        this.methodList.toArray(_methods);
        /* clean */
        this.methodList = null;

        return this;
    }
    @Override
    public void put(Object _obj) {
        methodList.add((METHOD) _obj);
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
     * the actually exec func.
     */
    Object execEach() {
        Object result = null;
        for(METHOD method : _methods)
            result = method.exec();
        return result;
    }
}
