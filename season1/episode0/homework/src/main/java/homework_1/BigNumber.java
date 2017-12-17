package homework_1;

/**
 * Created by Kraken on 21.09.2017.
 *
 * Класс, предоставляющий хранение и действия над большими беззнаковыми целыми числами,
 * хранящихся в виде массива типа long длинны SIZE
 */
public class BigNumber {

    //По условию задания - 4 элемента
    private int SIZE = 1;
    //Максимальное значение лонга - 18 символов
    private final long maxLong = 999999999999999999L;
    //private final long maxLong = 9L;

    private long[] elements;

    public BigNumber() {
        elements = new long[SIZE];

        //Забиваем все нулями
        for (long el : elements) {
            el = 0;
        }

    }

    public BigNumber(long number) {
        elements = new long[SIZE];

        //Забиваем все нулями
        for (long el : elements) {
            el = 0;
        }
        elements[0] = number;
    }

    public int size() {
        return SIZE;
    }

    public long getElement(int index) {
        return elements[index];
    }

    /**
     * Суррогат конструктора копирования
     * @param number
     */
    public void copy(BigNumber number) {
        SIZE = number.size();
        elements = new long[SIZE];

        for (int i=0; i<SIZE; i++) {
            elements[i] = number.getElement(i);
        }
    }

    /**
     * Вывод числа на экран
     */
    public void print() {
        System.out.println(toString());
    }

    public String toString() {

        String result = "";
        boolean startNumber = false;
        for (int i=SIZE-1; i>=0; i--) {
            if (!startNumber && elements[i] > 0)
                startNumber = true;
            if (startNumber) {
                result += elements[i];
            }
        }

        return result;
    }


    //Сложение

    /**
     * Сложение двух больших чисел
     * @param number
     */
    public void add(BigNumber number) {
        //Ленивый и в худшем случае более долгий вариант суммирования
        //Для каждого элемента
        for (int i=0; i<number.size(); i++) {
            //Если дошли до нулевого элемента - суммирование закончено
                additionElement(number, i);
        }
    }

    /**
     * Сложение двух заданных элементов с рекурсивным переносом
     * @param number
     * @param index
     */
    private void additionElement(BigNumber number, int index) {
        //Проверяем, будет ли при сложении текущих элементов переполнение
        if (maxLong - number.getElement(index) < elements[index]) {
            //Если будет - производим сложение с переносом

            //При переносе разряда используем формулу A + B = (A - (maxLong - B)) - 1
            //Пример для maxLong = 99: 84 + 77 = (84 - (99 - 77)) - 1 = 61
            elements[index] = (elements[index] - (maxLong - number.getElement(index))) - 1;

            try {
                dischargeTransfer(index + 1, number);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Выделенный массив переполнен, перенос не произошел");
            }
        } else {
            //Если переноса не будет - просто складываем элементы
            elements[index]+=number.getElement(index);
        }
    }

    /**
     * Рекурсивный перенос разряда на заданный элемент
     * @param index
     */
    private void dischargeTransfer(int index, BigNumber number) {
        //Если произойдет перенос перенос при... переносе - производим перенос
        if (index == SIZE) {
            increaseElementsArray(this);
            increaseElementsArray(number);
        }
        if (maxLong  == elements[index]) {
            elements[index] = 0;

            try {
                dischargeTransfer(index + 1, number);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Выделенный массив переполнен, перенос разряда не произошел");
            }

        } else {
            //Иначе просто увеличиваем элемент и забываем об этом
            elements[index]++;
        }
    }

    /**
     * При необходимости
     */
    private void increaseElementsArray(BigNumber number) {
        long[] temp = new long[number.SIZE];

        for (int i=0; i<number.SIZE; i++) {
            temp[i] = number.elements[i];
        }
        number.elements = new long[number.SIZE + 1];
        for (int i=0; i<number.SIZE; i++) {
            number.elements[i] = temp[i];
        }
        number.SIZE++;
    }
}
