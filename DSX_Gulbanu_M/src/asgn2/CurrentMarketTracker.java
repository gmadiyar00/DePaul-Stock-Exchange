package asgn2;
import dsx.InvalidOrderException;
import dsx.Price;
import dsx.PriceFactory;
//Student: Gulbanu Madiyarova

public final class CurrentMarketTracker {

    private static CurrentMarketTracker instance;

    public static synchronized CurrentMarketTracker getInstance() {
        if (instance == null) {
            instance = new CurrentMarketTracker();
        }
        return instance;
    }
    private CurrentMarketTracker() {};

    public void updateMarket(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws InvalidOrderException {
        Price marketWidth;
        if (buyPrice == null || sellPrice == null) {
            marketWidth = PriceFactory.makePrice(0);
        } else {
            marketWidth = sellPrice.subtract(buyPrice);
        }
        if (buyPrice == null) {
            buyPrice = PriceFactory.makePrice(0);
        }
        if(sellPrice == null) {
            sellPrice = PriceFactory.makePrice(0);
        }
        CurrentMarketSide buyMarketSide = new CurrentMarketSide(buyPrice, buyVolume);
        CurrentMarketSide sellMarketSide = new CurrentMarketSide(sellPrice, sellVolume);

        StringBuilder sb = new StringBuilder();
        sb.append("*********** Current Market ***********").append("\n")
                .append("* ").append(symbol).append(" ").append(buyPrice)
                .append("x").append(buyVolume)
                .append(" - ").append(sellPrice).append("x").append(sellVolume).append(" [")
                .append(marketWidth).append("]").append("\n")
                .append("**************************************");
        System.out.println(sb);

        CurrentMarketPublisher.getInstance().acceptCurrentMarket(symbol, buyMarketSide, sellMarketSide);
    }
}
