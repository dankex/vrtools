package fibonacci.samsung.com.fibonacci;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static fibonacci.samsung.com.fibonacci.Fibonacci.*;

public class FibonacciActivity extends AppCompatActivity {

    Fibonacci fib;
    ScrollView mScrollView;
    TextView mTextMsg;
    String mDisplay = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibonacci);

        mScrollView = (ScrollView) findViewById(R.id.scroll_view);

        mTextMsg = (TextView) findViewById(R.id.text_msg);
        mTextMsg.setMovementMethod(ScrollingMovementMethod.getInstance());

        fib = new Fibonacci(new FibListener() {
            @Override
            public synchronized void onUpdate(BigInteger i, BigInteger n) {
                String msg = "fib(" + i + ")=" + n + "\n";
                mDisplay = mDisplay + msg;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextMsg.setText(mDisplay);
                        mScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });

        fib.run();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fibonacci, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
