package edu.technopolis;

public class MySuchBigFibonacci implements FibonacciAlgorithm {

    @Override
    public String evaluate(int index) {
        if (index == 1 || index == 2) return "1";

        SuchBigInteger prev = new SuchBigInteger("1");
        SuchBigInteger current = new SuchBigInteger("1");
        SuchBigInteger t;
        for (int i = 2; i < index; i++) {
            t = current;
            current = current.sum(prev);
            prev = t;
        }
        return current.toString();
    }
}