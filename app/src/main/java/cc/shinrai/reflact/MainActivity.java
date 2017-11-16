package cc.shinrai.reflact;

import android.os.Bundle;
import android.widget.TextView;

import java.io.PrintStream;

public class MainActivity extends BaseActivity {
    private TextView helloText;
    private TextView fuckText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloText = (TextView) findViewById(R.id.hello_text);
        fuckText = (TextView) findViewById(R.id.fuck_text);
//        System.out.println("method chaining test.");

        String script = "(cc.shinrai.reflact.ForTest\n,&out),println,String:\"method chaining test.\";(__SELF__,get,String:\"hello_text\"),setVisibility,int:\"8\";(__SELF__,get,String:fuck_text),setVisibility,int:(hello_text,getVisibility);";
        load(script, new ScriptLoadedCallback() {
            @Override
            public void loaded() {
                reload();
            }
        });
    }
}
class ForTest {
    public static PrintStream out() {
        return System.out;
    }
}
