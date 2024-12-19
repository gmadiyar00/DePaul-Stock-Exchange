package asgn2;
import java.util.ArrayList;
import java.util.HashMap;
//Student: Gulbanu Madiyarova

public final class CurrentMarketPublisher {

    private static CurrentMarketPublisher instance;

    static synchronized CurrentMarketPublisher getInstance() {
        if (instance == null) {
            instance = new CurrentMarketPublisher();
        }
        return instance;
    }

    private CurrentMarketPublisher() {};

    HashMap<String, ArrayList<CurrentMarketObserver>> filters =  new HashMap<>();

    public void subscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) {
        if (!this.filters.containsKey(symbol)) {
            this.filters.put(symbol, new ArrayList<>());
        }
        ArrayList<CurrentMarketObserver> list = this.filters.get(symbol);
        list.add(cmo);
    }

    public void unSubscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) {
        if (filters.containsKey(symbol)) {
        ArrayList<CurrentMarketObserver> list = filters.get(symbol);
        list.remove(cmo);
        }
    }

    void acceptCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide){
        if (filters.containsKey(symbol)) {
            ArrayList<CurrentMarketObserver> list = filters.get(symbol);
            for (CurrentMarketObserver cmo : list) {
                cmo.updateCurrentMarket(symbol, buySide, sellSide);
            }
        }
    }
}
