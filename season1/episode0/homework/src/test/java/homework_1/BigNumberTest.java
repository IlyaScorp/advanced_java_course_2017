package homework_1;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by Kraken on 25.09.2017.
 */
public class BigNumberTest {
    /**
     * Тестируем сложение больших чисел сравнением с BigDecimal
     */
    @Test
    public void additionTest() {
        BigNumber testNumber = FibonacciNumbers.startCalculate(400);
        BigDecimal testDecimal = getFibonacci(400);

        Assert.assertEquals(testDecimal.toString(),testNumber.toString());

    }

    /**
     * Возвращает число Фибоначчи с заданным номером, используя BigDecimal
     * @param index
     * @return
     */
    public BigDecimal getFibonacci(int index) {
        BigDecimal number1 = new BigDecimal(1);
        BigDecimal number2 = new BigDecimal(1);
        BigDecimal temp;
        for (int i=0; i<index; i++) {
            temp = new BigDecimal(0);
            temp = temp.add(number2);
            number2 = number2.add(number1);
            number1 = new BigDecimal(0);
            number1 = number1.add(temp);
        }

        return number2;
    }

}
