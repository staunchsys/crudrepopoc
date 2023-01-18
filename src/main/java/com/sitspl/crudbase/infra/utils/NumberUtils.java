package com.sitspl.crudbase.infra.utils;

import java.math.BigDecimal;
import java.util.Random;

public class NumberUtils {

    private static NumberUtils me;

    public static NumberUtils getInstance() {
        if (me == null)
            me = new NumberUtils();

        return me;
    }

    public String buildDigitNumber(int number, int noOfDigits) {
        String numberStr = new Integer(number).toString();
        if (numberStr.length() == noOfDigits)
            return numberStr;
        else {
            StringBuffer ret = new StringBuffer();
            for (int i = 0; i < noOfDigits - numberStr.length(); i++) {
                ret.append("0");
            }
            ret.append(number);

            return ret.toString();
        }
    }

    /**
     * @param number 
     * @return perfect multiple of previous 100
     * i.e if number is 720.61 then it will be 700
     */
    public long buildNumberInPerfectMultipleOf100(double number) {
        long num = (long) number;
        if (num % 100 != 0) {
            num = (long) (Math.ceil(num / 100f) * 100f) - 100;
        }
        return num;
    }

    public boolean isMoreThanZero(BigDecimal n) {
        return n != null && n.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public String formatNumber(Object number, int precision) {
        return String.format("%,."+precision+"f", number);
    }

    public int getRandomNumber(int min, int max, Random random) {
        return random.nextInt((max - min) + 1) + min;
    }
}
