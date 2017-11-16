package cc.shinrai.reflact;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cc.shinrai.reflaction.Reflaction;

/**
 * Created by Shinrai on 2017/11/8 0008.
 */

public class BaseActivity extends AppCompatActivity {
    private Reflaction reflaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reflaction = new Reflaction(this);
    }

    @Override
    public View findViewById(@IdRes int id) {
        View view = super.findViewById(id);
        String resourceEntryName = this.getResources().getResourceEntryName(id);
        reflaction.put(resourceEntryName, view);
        return view;
    }

    public void reload() {
        // handle the call from the other looper
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                reflaction.reload();
            }
        });
    }

    public void load(final String script, final ScriptLoadedCallback scriptLoadedCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                reflaction.load(script);
                scriptLoadedCallback.loaded();
            }
        }).start();
    }

    interface ScriptLoadedCallback {
        void loaded();
    }
}
