package cc.shinrai.reflact;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

        String script = "{(fuck\\\\text,setVisibility,int:(hello_text,getVisibility,int:(hello_text,getVisibility,int:(hello_text,getVisibility,int:(hello_text,getVisibility,int:(hello_text,getVisibility,int:(hello_text,getVisibility,int:(hello_text,getVisibility,int:(hello_text,getVisibility,int:(hello_text,getVisibility,int:(hello_text,getVisibility)))))))))))}";
        load(script, new ScriptLoadedCallback() {
            @Override
            public void loaded() {
//                exec();
            }
        });
    }
}
class ForTest {
    public static PrintStream out() {
        return System.out;
    }
    public static Class aClass() {
        return ForTest.class;
    }
    public static String add(String a, String b) {
        return a+b;
    }
}
