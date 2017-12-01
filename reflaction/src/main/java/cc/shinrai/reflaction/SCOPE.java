package cc.shinrai.reflaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinrai on 2017/11/27 0027.
 */

class SCOPE {
    private METHOD[] methods = null;

    public Object execEach() {
        Object result = null;
        for(METHOD method : methods)
            result = method.exec();
        return result;
    }

    class Builder {
        private SCOPE scope;
        private List<METHOD> methodList;

        private Object _parent;

        public Builder() {
            scope = new SCOPE();
            methodList = new ArrayList<>();
        }

        public SCOPE getScope() {
            setMethods(methodList);
            return scope;
        }
    }

    public void setMethods(List<METHOD> list) {
        methods = new METHOD[list.size()];
        list.toArray(methods);
    }
}
