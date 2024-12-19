package asgn2;
import dsx.Price;
//Student: Gulbanu Madiyarova

public class Quote {

    private String user;
    private String product;
    private final QuoteSide buySide;
    private final QuoteSide sellSide;

    public Quote(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume, String userName) throws InvalidInputException {
        this.setUser(userName);
        this.setProduct(symbol);
        this.buySide = new QuoteSide(this.user, buyPrice, this.product, BookSide.BUY, buyVolume);
        this.sellSide = new QuoteSide(this.user, sellPrice, this.product, BookSide.SELL, sellVolume);
    }

    private void setUser(String user) throws InvalidInputException {
        if (!user.matches("^[A-Z]{3}$")) {
            throw new InvalidInputException("Invalid user name");
        } else {
            this.user = user;
        }
    }

    private void setProduct(String productSymbol) throws InvalidInputException {
        if (productSymbol == null || productSymbol.isEmpty()) {
            throw new InvalidInputException("Invalid symbol");
        } else {
            if (productSymbol.length() <= 5 && !productSymbol.matches("[^a-zA-Z0-9.]+")){
                this.product = productSymbol;
            } else {
                throw new InvalidInputException("Invalid symbol");
            }
        }
    }

    public QuoteSide getQuoteSide(BookSide sideIn) throws InvalidInputException {
        if ( sideIn == BookSide.SELL) {
            return this.sellSide;
        }
        if (sideIn == BookSide.BUY) {
            return this.buySide;
        } else  {
            throw new InvalidInputException("Invalid side");
        }
    }

    public String getSymbol() {
        //symbol is same as product
        return this.product;
    }

    public String getUser() {
        return this.user;
    }
}
