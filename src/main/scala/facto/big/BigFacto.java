package facto.big;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

public class BigFacto {
    public static void main(String[] args) {
        BigInteger bi = facto(new BigInteger(args[0]));
        String formattedResult = NumberFormat.getNumberInstance(Locale.US).format(bi);
        System.out.println("!" + args[0] + " = " + formattedResult);
    }

    public static BigInteger facto(BigInteger entry) {
        if (entry.intValue() <= 1) return new BigInteger("1");
        else return entry.multiply(
                facto(entry.subtract(new BigInteger("1")))
        );
    }
}