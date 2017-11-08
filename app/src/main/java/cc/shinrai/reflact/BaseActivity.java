package cc.shinrai.reflact;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Shinrai on 2017/11/8 0008.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public View findViewById(@IdRes int id) {
        View view = super.findViewById(id);
        // TODO
        return view;
    }

    public void reload() {
        // TODO
    }
}
