package dsx;
import java.util.TreeMap;
//Student: Gulbanu Madiyarova Date: 09/19/2024

public abstract class PriceFactory {

    private static final TreeMap<Integer, Price> priceEntries = new TreeMap<>();

    public static Price makePrice(int value) {
        if (!priceEntries.containsKey(value)) {
            priceEntries.put(value, new Price(value));
        }
        return priceEntries.get(value);
    }

    public static Price makePrice(String valueIn) throws InvalidOrderException {

        if (valueIn == null || valueIn.trim().isEmpty()) {
            if (!priceEntries.containsKey(0)) {
                Price result = new Price(0);
                priceEntries.put(0, result);
            }
            return priceEntries.get(0);
        }
        valueIn = valueIn.replaceAll(",", "");
        valueIn = valueIn.replaceAll("\\$", "");
        String[] parts = valueIn.split("\\.");

        int dollars = 0;

        if (!parts[0].isEmpty() && !parts[0].equals("-")) {
            dollars = Integer.parseInt(parts[0]);
        }

        int cent = 0;
        if (parts.length > 1) {
            String centsPart = parts[1];
            if (centsPart.length() == 1 || centsPart.length() > 2) {
                throw new InvalidOrderException("Invalid price String value: " + valueIn);
            }

            cent = Integer.parseInt(centsPart);
            if (parts[0].equals("-")) {
                cent = -cent;
            }
        }
        int computed;

        Price result = null;

        if (dollars < 0) {
            computed = (dollars * 100) - cent;
        } else {
            computed = (dollars * 100) + cent;
        }
        if (!priceEntries.containsKey(computed)) {
            result = new Price(computed);

            priceEntries.put(computed, result);
            return priceEntries.get(computed);

        } else {
            result = priceEntries.get(computed);
        }
        return result;
    }
}
