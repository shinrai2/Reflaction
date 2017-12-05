package cc.shinrai.reflaction;

/**
 * Created by Shinrai on 2017/12/5 0005.
 */

public interface BASIC {
    Object exec();
    void put(Object _obj);
    BASIC fin();
    void set_parent(BASIC _basic);
    BASIC get_parent();
}
