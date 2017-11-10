package cc.shinrai.reflact;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
    private TextView helloText;
    private TextView fuckText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloText = (TextView) findViewById(R.id.hello_text);
        fuckText = (TextView) findViewById(R.id.fuck_text);

        String script = " \t hello_text ,\n setVisibility , int : \"8\"; \t fuck_text , setVisibility , int : ( hello_text ,getVisibility \n\t) \n; \n";
        load(script);
        reload();
    }
}
