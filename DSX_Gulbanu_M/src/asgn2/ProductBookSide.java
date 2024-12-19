package asgn2;
import dsx.InvalidOrderException;
import dsx.Price;
import java.util.*;
//Student: Gulbanu Madiyarova

public class ProductBookSide {

    private final BookSide side;
    private final TreeMap<Price, ArrayList<Tradable>> bookEntries;

    public ProductBookSide(BookSide side) throws InvalidInputException {
        if (side == null) {
            throw new InvalidInputException("Side cannot be empty");
        }
        this.side = side;

        if (this.side == BookSide.BUY) {
            this.bookEntries = new TreeMap<>(Collections.reverseOrder()); // Buy side (descending order)
        } else {
            this.bookEntries = new TreeMap<>(); // Sell side (ascending order)
        }
    }

    public TradableDTO add(Tradable o) throws InvalidInputException, DataValidationException {
        if (o == null) {
            throw new InvalidInputException("Order can't be empty ");
        }
        if (!this.bookEntries.containsKey(o.getPrice())) {
            this.bookEntries.put(o.getPrice(), new ArrayList<>());
        }
        this.bookEntries.get(o.getPrice()).add(o);
        TradableDTO tradableDTO = new TradableDTO(o);
        UserManager.getInstance().updateTradable(o.getUser(), tradableDTO);

        return tradableDTO;
    }

    public TradableDTO cancel(String tradableId) throws DataValidationException, InvalidInputException {
        Tradable tradableToCancel = null;
        Price priceToRemove = null;

        for (Map.Entry<Price, ArrayList<Tradable>> entry : this.bookEntries.entrySet()) {
            ArrayList<Tradable> tradables = entry.getValue();
            for (Tradable tradable : tradables) {
                if (tradable.getid().equals(tradableId)) {
                    tradableToCancel = tradable;
                    tradable.setCancelledVolume(tradable.getRemainingVolume());  // Mark as cancelled
                    tradable.setRemainingVolume(0);
                    // I don't need to keep looking, so break the loop
                    break;
                }
            }
            if (tradableToCancel != null) {
                tradables.remove(tradableToCancel);
                if (tradables.isEmpty()) {
                    priceToRemove = entry.getKey();
                } //TODO review: do I need else case with "do nothing"?
            } else {
                return null;
            }
        }
        // Remove the price entry if array is empty
        if (priceToRemove != null) {
            this.bookEntries.remove(priceToRemove);
        }
        TradableDTO cancelledTradable = new TradableDTO(tradableToCancel);
        UserManager.getInstance().updateTradable(tradableToCancel.getUser(), cancelledTradable );

        return cancelledTradable;
    }

    public TradableDTO removeQuotesForUser(String userName) throws DataValidationException, InvalidInputException {
        TradableDTO cancelledDTO = null;
        Price priceToRemove = null;

        for (Map.Entry<Price, ArrayList<Tradable>> entry : this.bookEntries.entrySet()) {
            ArrayList<Tradable> tradables = entry.getValue();

            for (Tradable tradableQuote : tradables) {
                if (tradableQuote instanceof QuoteSide && tradableQuote.getUser().equals(userName)) {
                    cancelledDTO = this.cancel(tradableQuote.getid()); // Cancel the quote
                    break;
                } //TODO review: what would the else case be?
            }
            if (tradables.isEmpty()) {
                priceToRemove = entry.getKey();
            }
            //break the loop if the quote for user has been found
            if (cancelledDTO != null) {
                break;
            }
        }
        if (priceToRemove != null) {
            this.bookEntries.remove(priceToRemove);
        }
        if (cancelledDTO != null) {
            UserManager.getInstance().updateTradable(userName, cancelledDTO);
        }
        return cancelledDTO;
    }

    public Price topOfBookPrice() {
        if (this.bookEntries.isEmpty()) {
            return null;
        } else {
            return bookEntries.firstKey();
        }
    }

    public int topOfBookVolume() {
        int topOfBookVolume = 0;

        if (this.bookEntries.isEmpty()) {
            return 0;
        } else {
            if (this.side == BookSide.BUY) {
                Map.Entry<Price, ArrayList<Tradable>> highestPriceEntry = this.bookEntries.firstEntry();

                for (Tradable trade : highestPriceEntry.getValue()) {
                    topOfBookVolume += trade.getRemainingVolume();
                }
            }
            else if (side == BookSide.SELL) {
                Map.Entry<Price, ArrayList<Tradable>> lowestPriceEntry = this.bookEntries.firstEntry();
                for (Tradable trade : lowestPriceEntry.getValue()) {
                    topOfBookVolume += trade.getRemainingVolume();
                }
            }
            return topOfBookVolume;
        }
    }

    public void tradeOut(Price price, int vol) throws InvalidOrderException, InvalidInputException, DataValidationException {
        Price topOfBookPrice = this.topOfBookPrice();
        StringBuilder sb = new StringBuilder();

        if (topOfBookPrice == null){
            return;
        } else {
            if (topOfBookPrice.greaterThan(price)){
                return;
            } else {
               ArrayList<Tradable> atPrice = bookEntries.get(topOfBookPrice);
               int totalVolAtPrice = 0;

               for(Tradable eachPrice : atPrice){
                   totalVolAtPrice = totalVolAtPrice + eachPrice.getRemainingVolume();
               }

               if (vol >= totalVolAtPrice){
                   for ( Tradable t : atPrice){
                       int rv = t.getRemainingVolume();
                       //Set the filled volume of tradable “t” to be “t”s original volume.
                       t.setFilledVolume(t.getOriginalVolume());
                       t.setRemainingVolume(0);

                       String tradableType = (t instanceof QuoteSide) ? "quote" : "order"; // Check if tradable is a Quote

                       sb.append("FULL FILL: ").append(" (").append(t.getSide()).append(" ")
                               .append(t.getOriginalVolume()).append(") ")
                               .append(t.getProduct()).append(" ").append(t.getSide()).append(" ")
                               .append(tradableType).append(" for ")
                               .append(t.getUser()).append(":").append(" Price: ")
                               .append(price).append(", Orig Vol: ")
                               .append(t.getOriginalVolume()).append(", Rem Vol: ")
                               .append(t.getRemainingVolume()).append(", Fill Vol: ")
                               .append(t.getFilledVolume()).append(", CXL Vol: ")
                               .append(t.getCancelledVolume()).append(", ID: ")
                               .append(t.getid()).append("\n");
                       UserManager.getInstance().updateTradable(t.getUser(), t.makeTradableDTO() );
                   }
                   System.out.println(sb);
                   //Remove the bookEntries ArrayList for the top of book price
                   bookEntries.remove(topOfBookPrice);
                   return;
               } else {
                   int remainder = vol;

                   for (Tradable t : atPrice) {
                       double ratio = (double) t.getRemainingVolume() / (double) totalVolAtPrice;
                       int toTrade = (int) Math.ceil((vol * ratio));
                       toTrade = Math.min(toTrade, remainder);
                       t.setFilledVolume(t.getFilledVolume() + toTrade);
                       t.setRemainingVolume(t.getRemainingVolume() - toTrade);

                       String tradableType = (t instanceof QuoteSide) ? "quote" : "order"; // Check if tradable is a Quote

                       sb.append("PARTIAL FILL: ").append(" (").append(t.getSide()).append(" ")
                               .append(toTrade).append(") ")
                               .append(t.getProduct()).append(" ").append(t.getSide()).append(" ")
                               .append(tradableType).append(" for ")
                               .append(t.getUser()).append(":").append(" Price: ")
                               .append(price).append(", Orig Vol: ")
                               .append(t.getOriginalVolume()).append(", Rem Vol: ")
                               .append(t.getRemainingVolume()).append(", Fill Vol: ")
                               .append(t.getFilledVolume()).append(", CXL Vol: ")
                               .append(t.getCancelledVolume()).append(", ID: ")
                               .append(t.getid()).append("\n");
                       remainder = remainder - toTrade;
                       UserManager.getInstance().updateTradable(t.getUser(), t.makeTradableDTO() );
                   }
                   System.out.println(sb);
                   return;
               }
            }
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        if (this.bookEntries.isEmpty()){
            sb.append("\t<Empty>\n");
        } else {
            for (Map.Entry<Price, ArrayList<Tradable>> entry : this.bookEntries.entrySet()) {

                Price price = entry.getKey();

                ArrayList<Tradable> tradables = entry.getValue();

                sb.append("        ").append(price).append(": ").append("\n");


                for (Tradable tradable : tradables) {

                    String tradableType = (tradable instanceof QuoteSide) ? "quote" : "order"; // Check if tradable is a Quote

                    sb.append("               ").append(tradable.getUser()).append(" ")
                            .append(tradable.getSide())
                            .append(" side ")
                            .append(tradableType).append(" for ")
                            .append(tradable.getProduct()).append(": ").append(" Price: ")
                            .append(price).append(", Orig Vol: ")
                            .append(tradable.getOriginalVolume()).append(", Rem Vol: ")
                            .append(tradable.getRemainingVolume()).append(", Fill Vol: ")
                            .append(tradable.getFilledVolume()).append(", CXL Vol: ")
                            .append(tradable.getCancelledVolume()).append(", ID: ")
                            .append(tradable.getid()).append("\n");
                }
            }
        }
        return sb.toString();
    }
}