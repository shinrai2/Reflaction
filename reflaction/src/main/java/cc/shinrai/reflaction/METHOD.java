package cc.shinrai.reflaction;

/**
 * Created by Shinrai on 2017/11/27 0027.
 */

class METHOD {
    private Object _receiver        = null;
    private String _method          = null;
    private String _field           = null;
    private String[] _classOfArgs   = null;
    private Object[] _objectOfArgs  = null;

    private METHOD() {
    }

    public Object exec() {
        if(_field != null)
            ; // TODO get field.
        // TODO run method.
        return null;
    }

    /**
     * protect the METHOD class, avoid to be write twice.
     */
    class Builder {
        private METHOD method;

        private Object _parent;

        public Builder() {
            method = new METHOD();
        }

        public METHOD getMethod() {
            return method;
        }

        public Builder set_receiver(Object _receiver) {
            method.set_receiver(_receiver);
            return this;
        }
        public Builder set_method(String _method) {
            method.set_method(_method);
            return this;
        }
        public Builder set_field(String _field) {
            method.set_field(_field);
            return this;
        }
        public Builder set_classOfArgs(String[] _classOfArgs) {
            method.set_classOfArgs(_classOfArgs);
            return this;
        }
        public Builder set_objectOfArgs(Object[] _objectOfArgs) {
            method.set_objectOfArgs(_objectOfArgs);
            return this;
        }
        public void set_parent(Object _parent) {
            this._parent = _parent;
        }
        public Object get_parent() {
            return _parent;
        }
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

    private void set_field(String _field) {
        this._field = _field;
    }

    private void set_classOfArgs(String[] _classOfArgs) {
        this._classOfArgs = _classOfArgs;
    }

    private void set_objectOfArgs(Object[] _objectOfArgs) {
        this._objectOfArgs = _objectOfArgs;
    }
}
