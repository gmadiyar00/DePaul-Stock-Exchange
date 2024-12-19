package asgn2;
import dsx.Price;
// Student: Gulbanu Madiyarova

public class QuoteSide implements Tradable {

    private String user;
    private String product;
    private Price price;
    private BookSide side;
    private int originalVolume;
    private int cancelledVolume = 0;
    private int filledVolume = 0;
    private final String id;
    private int remainingVolume;

    public QuoteSide(String user, Price price, String product, BookSide side, int originalVolume) throws InvalidInputException {
        this.setUser(user);
        this.setProduct(product);
        this.setPrice(price);
        this.setOriginalVolume(originalVolume);
        this.setSide(side);
        this.remainingVolume = this.originalVolume;
        this.id = this.user + this.product + this.price + System.nanoTime();
    }

    private void setUser(String user) throws InvalidInputException {
        if (!user.matches("^[A-Z]{3}$")) {
            throw new InvalidInputException("Invalid user name");
        } else {
            this.user = user;
        }
    }

    private void setProduct(String product) throws InvalidInputException {
        if (product == null || product.isEmpty()) {
            throw new InvalidInputException("Invalid symbol");
        } else {
            if (product.length() <= 5 && !product.matches("[^a-zA-Z0-9.]+")) {
                this.product = product;
            } else {
                throw new InvalidInputException("Invalid symbol");
            }
        }
    }

    private void setPrice(Price price) throws InvalidInputException {
        if (price == null) {
            throw new InvalidInputException("Invalid price");
        } else {
            this.price = price;
        }
    }

    private void setOriginalVolume(int originalVolume) throws InvalidInputException {
        if (originalVolume > 10000 || originalVolume < 0) {
            throw new InvalidInputException("Invalid Original Volume");
        } else {
            this.originalVolume = originalVolume;
        }
    }

    private void setSide(BookSide side) throws InvalidInputException {
        if (side == null) {
            throw new InvalidInputException("Invalid side");
        } else {
            this.side = side;
        }
    }

    @Override
    public String getid() {
        return this.id;
    }

    @Override
    public int getRemainingVolume() {
        return this.remainingVolume;
    }

    @Override
    public void setCancelledVolume(int newVol) {
        this.cancelledVolume = newVol;
    }

    @Override
    public int getCancelledVolume() {
        return this.cancelledVolume;
    }

    @Override
    public void setRemainingVolume(int newVol) {
        this.remainingVolume = newVol;
    }

    @Override
    public Price getPrice() {
        return this.price;
    }

    @Override
    public void setFilledVolume(int newVol) {
        this.filledVolume = newVol;
    }

    @Override
    public int getFilledVolume() {
        return this.filledVolume;
    }

    @Override
    public BookSide getSide() {
        return this.side;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public String getProduct() {
        return this.product;
    }

    @Override
    public int getOriginalVolume() {
        return this.originalVolume;
    }

    @Override
    public String toString() {
        return this.getUser() + " " + this.getSide() + " side quote for " + this.getProduct() + ": " + this.getPrice() + ", Orig Vol: "
                + this.getOriginalVolume() + ", Rem Vol: " + this.getRemainingVolume() +
                ", Fill Vol: " + this.getFilledVolume() + ", CXL Vol: " + this.getCancelledVolume() + ", ID: " + this.getid();
    }

    @Override
    public TradableDTO makeTradableDTO() {
        return new TradableDTO(this);
    }
}
