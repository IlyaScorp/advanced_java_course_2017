import java.math.BigDecimal;
import java.math.BigInteger;

public class Fibonaccis implements FibonacciAlgorithm{
    private static String fib(long value) {
        BigNumeric first = new BigNumeric(0L);
        BigNumeric second = new BigNumeric(1L);
        if (value == 0)
            return first.toBigInteger().toString();
        if (value == 1)
            return second.toBigInteger().toString();
        BigNumeric result = new BigNumeric(0L);
        for(int i = 2; i <= value; i++){
            result.reset();
            result.add(first);
            result.add(second);
            first.set(second);
            second.set(result);
        }
        return result.toBigInteger().toString();
    }

    @Override
    public String evaluate(int index) {
        return fib(index);
    }
}
