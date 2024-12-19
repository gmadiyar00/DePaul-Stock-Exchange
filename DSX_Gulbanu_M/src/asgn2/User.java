package asgn2;
import java.util.HashMap;
import java.util.Map;
//Student: Gulbanu Madiyarova

public class User implements CurrentMarketObserver {

    private String userId;
    private HashMap<String, TradableDTO> tradables = new HashMap<>();
    private HashMap<String, CurrentMarketSide[]> currentMarkets = new HashMap<>();

    public User(String userId) throws InvalidInputException {
        setId(userId);
    }

    private void setId(String userId) throws InvalidInputException {
        if (!userId.matches("^[A-Z]{3}$")) {
            throw new InvalidInputException("Invalid user ID, user ID should be");
        }
        this.userId = userId;
    }

    public void updateTradable(TradableDTO o) {
        if (o == null) {
            return;
        } else {
            tradables.put(o.tradableId(), o);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\n" + "User Id: " + userId + "\n");

        for (TradableDTO tradable : tradables.values()) {
            result.append("         ").append(tradable.toString()).append("\n");
        }
        return result.toString();
    }

    @Override
    public void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        CurrentMarketSide[] arr = {buySide, sellSide};
        this.currentMarkets.put(symbol, arr);
    }

   public String getUserId(){

        return this.userId;
    }

    public String getCurrentMarkets() {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String, CurrentMarketSide[]> entry : this.currentMarkets.entrySet()) {
            String symbol = entry.getKey();
            CurrentMarketSide[] sides = entry.getValue();
            sb.append(symbol).append(" ").append(sides[0].toString()).append(" - ").append(sides[1].toString()).append("\n");
        }
        return sb.toString();
    }
}
