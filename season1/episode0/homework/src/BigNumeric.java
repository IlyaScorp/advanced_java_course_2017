import java.math.BigInteger;
import java.util.ArrayList;

class BigNumeric{
    private static final Long MAX_VALUE_FOR_SET = new Long(999999999999999999L);

    private ArrayList<Long> value_list;
    private int length;

    BigNumeric(){
        this.value_list = new ArrayList<>();
        this.value_list.add(0L);
        this.length = 1;
    }

    BigNumeric(final String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) throw new NumberFormatException();
        }

        this.value_list = new ArrayList<>();
        this.value_list.add(0L);
        this.length = this.value_list.size();

        long multiplier = 1;
        int setNumber = 0;
        int counter = 0;
        for (int i = value.length() - 1; i >= 0; i--) {
            counter++;
            if (counter == 19) {
                multiplier = 1;
                this.value_list.add(0L);
                setNumber++;
                this.value_list.set(setNumber, this.value_list.get(setNumber) + (Long.parseLong("" + value.charAt(i)) * multiplier));
                multiplier *= 10;
                counter = 1;
            } else {
                this.value_list.set(setNumber, this.value_list.get(setNumber) + (Long.parseLong("" + value.charAt(i)) * multiplier));
                multiplier *= 10;
            }
        }
        this.length = this.value_list.size();
    }

    BigNumeric(final BigNumeric object){
        value_list = new ArrayList<>();
        for(int i = 0; i < object.size(); i++){
            this.value_list.add(object.getValuePart(i));
            this.length++;
        }
    }

    BigNumeric(final long value){
        this.value_list = new ArrayList<>();
        long amountOfIncludesOfMaxValueForSet = value / MAX_VALUE_FOR_SET;
        if (amountOfIncludesOfMaxValueForSet > 1){
            this.value_list.add(0L);
            this.value_list.add(0L);
            this.value_list.set(1, amountOfIncludesOfMaxValueForSet);
            this.value_list.set(0,(value-(amountOfIncludesOfMaxValueForSet*MAX_VALUE_FOR_SET)) - amountOfIncludesOfMaxValueForSet);
        }
        else this.value_list.add(value);
        this.length = this.value_list.size();
    }

    private int size(){
        return this.value_list.size();
    }

    private void addEmptySet(){
        this.value_list.add(0L);
        this.length++;
    }

    private Long getValuePart(final int partNumber){

        return this.value_list.get(partNumber);

    }

    public void add(final BigNumeric value){
        long mem = 0;
        for(int i = 0; i < value.size(); i++){
            if (this.size() <= i)
                this.addEmptySet();
            long temp = this.getValuePart(i) + value.getValuePart(i) + mem;
            if (temp > MAX_VALUE_FOR_SET){
                long amountOFIncludesOfMaxValueOfSet = temp / MAX_VALUE_FOR_SET;
                mem = amountOFIncludesOfMaxValueOfSet;
                this.value_list.set(i,(temp - amountOFIncludesOfMaxValueOfSet*MAX_VALUE_FOR_SET) - amountOFIncludesOfMaxValueOfSet);
            }else{
                this.value_list.set(i,temp);
                mem = 0;
            }
        }
        if (mem != 0) this.value_list.add(mem);
    }

    public void reset(){
        this.value_list.clear();
        this.value_list.add(0L);
        this.length = 1;
    }

    public void set(final BigNumeric value){
        this.value_list.clear();
        this.length = 0;
        for(int i = 0; i < value.size(); i++){
            this.value_list.add(value.getValuePart(i));
        }
        this.length = value.size();
    }

    public BigInteger toBigInteger(){
        BigInteger summary = new BigInteger("0");
        BigInteger multiplierBase = new BigInteger("1000000000000000000");
        BigInteger multiplier = new BigInteger("1");

        for(int i = 0; i < this.size(); i++){
            BigInteger temp = new BigInteger(Long.toString(this.value_list.get(i)));
            temp = temp.multiply(multiplier);
            multiplier = multiplier.multiply(multiplierBase);
            summary = summary.add(temp);
        }

        return summary;
    }
}
