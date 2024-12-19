package dsx;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
//Student: Gulbanu Madiyarova Date: 09/19/2024

public class Price implements Comparable<Price> {

    final int cents;

    Price(int priceIn) {
        cents = priceIn;
    }

    public boolean isNegative() {
        return cents < -1;
    }

    public Price add(Price p) throws dsx.InvalidOrderException {
        if (p == null) {
            throw new dsx.InvalidOrderException("Cannot add null to a Price object");
        } else {
            int totalCents = this.cents + p.cents;
            return PriceFactory.makePrice(totalCents);
        }
    }

    public Price subtract(Price p) throws dsx.InvalidOrderException {
        if (p == null) {
            throw new dsx.InvalidOrderException("Cannot subtract null from a Price object");
        } else {
            int subtractedCents = this.cents - p.cents;
            return PriceFactory.makePrice(subtractedCents);
        }
    }

    public Price multiply(int n) {
        int result = this.cents * n;
        return PriceFactory.makePrice(result);
    }

    public boolean greaterOrEqual(Price p) throws dsx.InvalidOrderException {
        if (p == null) {
            throw new dsx.InvalidOrderException("Cannot check greater than or equal with a null");
        } else {
            return this.cents >= p.cents;
        }
    }

    public boolean lessOrEqual(Price p) throws dsx.InvalidOrderException {
        if (p == null) {
            throw new dsx.InvalidOrderException("Cannot check less than or equal with a null");
        } else {
            return this.cents <= p.cents;
        }
    }

    public boolean greaterThan(Price p) throws dsx.InvalidOrderException {
        if (p == null) {
            throw new dsx.InvalidOrderException("Cannot check greater than with a null");
        } else {
            return this.cents > p.cents;
        }
    }

    public boolean lessThan(Price p) throws dsx.InvalidOrderException {
        if (p == null) {
            throw new InvalidOrderException("Cannot check less than with a null");
        } else {
            return this.cents < p.cents;

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return this.cents == price.cents;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.cents);
    }

    @Override
    public int compareTo(Price o) {
        if (o == null) {
            return this.cents;
        } else {
            return this.cents - o.cents;
        }
    }

    @Override
    public String toString() {
        int dollars = cents / 100;
        int centA = cents % 100;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        currencyFormat.setMaximumFractionDigits(2);

        // Format the amount as currency, removing any currency symbol for custom formatting
        return currencyFormat.format(dollars + centA / 100.0);

    }
}

