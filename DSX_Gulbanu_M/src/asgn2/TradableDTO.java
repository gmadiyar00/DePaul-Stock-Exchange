package asgn2;
import dsx.Price;
//Student: Gulbanu Madiyarova

public record TradableDTO(Tradable tradable, String user, String product, Price price, int originalVolume, int remainingVolume,
                          int cancelledVolume, int filledVolume, BookSide side, String tradableId ) {

    public TradableDTO(Tradable tradable) {
        this(tradable, tradable.getUser(), tradable.getProduct(), tradable.getPrice(), tradable.getOriginalVolume(), tradable.getRemainingVolume(),
                tradable.getCancelledVolume(), tradable.getFilledVolume(), tradable.getSide(), tradable.getid());
    }
    @Override
    public String toString() {
        return "Product: " + product + ", Price: " + price + ", Orig Vol: "
                + originalVolume + ", Rem Vol: " + this.remainingVolume +
                ", Fill Vol: " + filledVolume + ", CXL Vol: " + cancelledVolume + ", ID: " + tradableId;
    }
}
