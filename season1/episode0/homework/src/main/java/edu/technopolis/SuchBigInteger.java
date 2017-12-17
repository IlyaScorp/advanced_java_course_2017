package edu.technopolis;

import java.util.ArrayList;
import java.util.List;

public class SuchBigInteger {

    private List<Long> number = new ArrayList<>();
    public SuchBigInteger(String number){
        for (int i = 0; i < number.length(); i += 18) {
            int temp = number.length() - i;
            if (temp < 18)
                this.number.add(0, Long.parseLong(number.substring(0, temp)));
            else
                this .number.add(0, Long.parseLong(number.substring(temp-18, temp)));
        }
    }

    public SuchBigInteger(List<Long> blockOfNumbers){
        this.number = blockOfNumbers;
    }

    public SuchBigInteger sum(SuchBigInteger sbi){
        long base = 1_000_000_000_000_000_000L;
        List result = new ArrayList<Long>();
        boolean isOverflow = false;
        long a;
        long b;
        long temp;
        for (int i = 0; i < Math.max(this.getBlocksOfNumber().size(), sbi.getBlocksOfNumber().size()); i++) {
            if (i == sbi.getBlocksOfNumber().size()){
                sbi.getBlocksOfNumber().add(0, 0L);
            }
            a = number.get(number.size() - i - 1);
            b = sbi.getBlocksOfNumber().get(sbi.getBlocksOfNumber().size() - i - 1);
            temp = isOverflow ? a + b + 1 : a + b;
            isOverflow = longSize(temp) == 19;
            if (isOverflow) temp -= base;
            result.add(0, temp);
        }
        if (isOverflow) result.add(0,1L);
        return new SuchBigInteger(result);
    }

    public List<Long> getBlocksOfNumber(){
        return number;
    }

    private int longSize(long x) {
        long p = 10;
        for (int i=1; i<19; i++) {
            if (x < p)
                return i;
            p = 10*p;
        }
        return 19;
    }

    @Override
    public String toString() {
        StringBuilder numberString = new StringBuilder("");
        numberString.append(number.get(0));
        for (int i = 1; i < number.size(); i++) {
            long x = number.get(i);
            if (number.size() > 1 && longSize(x) < 18) {
                for (int j = 0; j < 18-longSize(x); j++) {
                    numberString.append("0");
                }
            }
            numberString.append(x);
        }
        return numberString.toString();
    }
}
