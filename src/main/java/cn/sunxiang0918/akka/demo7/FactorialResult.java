package cn.sunxiang0918.akka.demo7;

import java.io.Serializable;
import java.math.BigInteger;

public class FactorialResult implements Serializable {
    public final int n;
    public final BigInteger factorial;

    FactorialResult(int n, BigInteger factorial) {
        this.n = n;
        this.factorial = factorial;
    }

    @Override
    public String toString() {
        return "FactorialResult{" +
                "n=" + n +
                ", factorial=" + factorial +
                '}';
    }
}