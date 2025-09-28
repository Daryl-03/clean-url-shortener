package dev.richryl.shortlink.adapaters.services;

public class Base62 {
    private static final String BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encode(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();
        long decimalRepresentationOfTheString = Math.abs(byteArrayToLong(bytes));

        int reminder;
        long quotient;
        while (decimalRepresentationOfTheString > 0) {
            reminder = (int) (decimalRepresentationOfTheString % 62);
            quotient = decimalRepresentationOfTheString / 62;
            stringBuilder.append(BASE62_ALPHABET.charAt(reminder));
            decimalRepresentationOfTheString = quotient;
        }

        return stringBuilder.reverse().toString();
    }

    public static long byteArrayToLong(byte[] bytes){
        long result = 0;
        for (byte b : bytes) {
            result = (result << 8) | (b & 0xFF);
        }
        return result;
    }
}
