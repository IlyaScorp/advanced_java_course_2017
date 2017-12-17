package homework_1;


import edu.technopolis.FibonacciAlgorithm;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Kraken on 21.09.2017.
 */
public class FibonacciNumbers implements FibonacciAlgorithm{


    public static BigNumber startCalculate(int numb) {
        return getFibonacciNumber(numb);
    }

    /**
     * Наивный алгоритм вычисления чисел фиббоначи
     */
    public static BigNumber getFibonacciNumber(int index) {
        long startTime, finishTime;
        startTime = System.currentTimeMillis();

        BigNumber number1 = new BigNumber(1);
        BigNumber number2 = new BigNumber(1);
        BigNumber temp = new BigNumber();

        for (int i=0; i<index; i++) {
            temp.copy(number2);
            number2.add(number1);
            number1.copy(temp);
            number2.print();
        }



        finishTime = System.currentTimeMillis();
        System.out.println((finishTime - startTime) + " ms");

        return number2;
    }

    @Override
    public String evaluate(int index) {
        return getFibonacciNumber(index).toString();
    }
}
