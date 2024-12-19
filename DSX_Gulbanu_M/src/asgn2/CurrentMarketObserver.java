package asgn2;
// Student: Gulbanu Madiyarova

public interface CurrentMarketObserver {
    void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide);
}
