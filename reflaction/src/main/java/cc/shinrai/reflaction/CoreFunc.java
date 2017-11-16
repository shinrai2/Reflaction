package cc.shinrai.reflaction;

/**
 * Created by Shinrai on 2017/11/8 0008.
 */

public interface CoreFunc {
    void put(String symbol, Object object);
    Object get(String symbol);
    void rm(String symbol);
}
