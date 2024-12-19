package asgn2;
import dsx.InvalidOrderException;
import dsx.Price;
//Student: Gulbanu Madiyarova

public class ProductBook {

    private String product;
    private final ProductBookSide buySide;
    private final ProductBookSide sellSide;

    public ProductBook(String product) throws InvalidInputException {
        this.setProduct(product);
        this.buySide = new ProductBookSide(BookSide.BUY);
        this.sellSide = new ProductBookSide(BookSide.SELL);
    }

    private void setProduct(String product) throws InvalidInputException {
        if (product == null || product.isEmpty()) {
            throw new InvalidInputException("Invalid product");
        } else {
            if (product.length() <= 5 && !product.matches("[^a-zA-Z0-9.]+")) {
                this.product = product;
            } else {
                throw new InvalidInputException("Invalid product");
            }
        }
    }

    public TradableDTO add(Tradable t) throws InvalidInputException, dsx.InvalidOrderException, DataValidationException, NotFoundException {
        if (t == null) {
            throw new InvalidInputException("The order is invalid");
        } else {
            TradableDTO addedProduct;

            if (t.getSide() == BookSide.BUY) {
                addedProduct = this.buySide.add(t);
            } else if (t.getSide() == BookSide.SELL) {
                addedProduct = this.sellSide.add(t);
            } else {
                throw new InvalidInputException(" Invalid side: " + t.getSide());
            }
            this.tryTrade();
            this.updateMarket();
            return addedProduct;
        }
    }

    public TradableDTO[] add(Quote qte) throws InvalidInputException, dsx.InvalidOrderException, DataValidationException, NotFoundException {
        if (qte == null) {
            throw new InvalidInputException("Quote cannot be empty");
        } else {
            this.removeQuotesForUser(qte.getUser());
            TradableDTO buyDTO = buySide.add(qte.getQuoteSide(BookSide.BUY));
            TradableDTO sellDTO = sellSide.add(qte.getQuoteSide(BookSide.SELL));
            this.tryTrade();
            return new TradableDTO[]{buyDTO, sellDTO};
        }
    }

    public TradableDTO cancel(BookSide side, String orderId) throws InvalidInputException, DataValidationException, InvalidOrderException, NotFoundException {
        TradableDTO result;

        if (side == BookSide.SELL) {
            result = sellSide.cancel(orderId);
        } else if (side == BookSide.BUY) {
            result = buySide.cancel(orderId);
        } else {
            throw new InvalidInputException("Side is invalid");
        }

        this.updateMarket();
        return result;
    }

    public TradableDTO[] removeQuotesForUser(String userName) throws DataValidationException, InvalidOrderException, InvalidInputException, NotFoundException {
        TradableDTO removeBuyQuote = this.buySide.removeQuotesForUser(userName);
        TradableDTO removeSellQuote = this.sellSide.removeQuotesForUser(userName);
        this.updateMarket();
        return new TradableDTO[]{removeBuyQuote, removeSellQuote};
    }

    public void tryTrade() throws dsx.InvalidOrderException, DataValidationException, NotFoundException, InvalidInputException {
        if (this.buySide.topOfBookPrice() == null || this.sellSide.topOfBookPrice() == null) {
            return;
        } else {
            int totalToTrade = Math.max(this.buySide.topOfBookVolume(), this.sellSide.topOfBookVolume());

            while (true) {
                if (totalToTrade > 0) {
                    Price topBuyPrice = this.buySide.topOfBookPrice();
                    Price topSellPrice = this.sellSide.topOfBookPrice();

                    if (topBuyPrice == null || topSellPrice == null) {
                        return;
                    } else {
                        if (topSellPrice.greaterThan(topBuyPrice)) {
                            return;
                        } else {
                            int volToTrade = Math.min(this.buySide.topOfBookVolume(), this.sellSide.topOfBookVolume());
                            this.buySide.tradeOut(topBuyPrice, volToTrade);
                            this.sellSide.tradeOut(topBuyPrice, volToTrade);
                            totalToTrade = totalToTrade - volToTrade;
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    private void updateMarket() throws InvalidOrderException {

        Price buyPrice = this.buySide.topOfBookPrice();
        Price sellPrice = this.sellSide.topOfBookPrice();
        int buyVol = this.buySide.topOfBookVolume();
        int sellVol = this.sellSide.topOfBookVolume();

        CurrentMarketTracker.getInstance().updateMarket(this.product, buyPrice, buyVol, sellPrice, sellVol);
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------\n");

        sb.append("Product Book: ").append(product).append("\n");

        if (buySide == null) {
            sb.append("Side: BUY\n\t<Empty>\n");
        } else {
            sb.append("Side: BUY\n");
            sb.append(buySide);
        }

        if (sellSide == null) {
            sb.append("Side: SELL\n\t<Empty>\n");
        } else {
            sb.append("Side: SELL\n");
            sb.append(sellSide);
        }
        sb.append("--------------------------------------------\n");

        return sb.toString();
    }

    public String getTopOfBookString(BookSide side) {
        if (side == BookSide.BUY) {
            return "Top of BUY book: " +
                    (buySide.topOfBookPrice() == null ? "$0.00" : buySide.topOfBookPrice())
                    + " x " + buySide.topOfBookVolume();
        } else {
            return "Top of SELL book: " +
                    (sellSide.topOfBookPrice() == null ? "$0.00" : sellSide.topOfBookPrice())
                    + " x " + sellSide.topOfBookVolume();
        }
    }
}

