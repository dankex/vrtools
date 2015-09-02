package fibonacci.samsung.com.fibonacci;

import android.util.Log;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Something to get CPU cores busy
 */
public class Fibonacci {
    private static final String TAG = "Fibonacci";

    public static interface FibListener {
        void onUpdate(BigInteger i, BigInteger n);
    }

    FibListener mListener;
    BigInteger mNextNumber;

    public Fibonacci(FibListener listener) {
        mListener = listener;
        mNextNumber = BigInteger.ONE;
    }

    public static BigInteger fib(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) <= 0)
            return n;

        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;

        while (n.compareTo(BigInteger.valueOf(2)) >= 0) {
            BigInteger c = a.add(b);
            a = b;
            b = c;

            n = n.subtract(BigInteger.ONE);
        }

        return b;
    }

    public synchronized BigInteger getNext() {
        BigInteger rv = mNextNumber;
        mNextNumber = mNextNumber.add(BigInteger.ONE);
        return rv;
    }

    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int j = 0; j < 10; j++) {
            executorService.submit(new Runnable() {
                public void run() {
                    while (true) {
                        BigInteger nextNumber = getNext();
                        BigInteger result = fib(nextNumber);
                        mListener.onUpdate(nextNumber, result);
                    }
                }
            });
        }
    }
}
