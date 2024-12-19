package asgn2;
import dsx.Price;
//Student: Gulbanu Madiyarova Date: 10/15/2024

public class Order implements Tradable {
    private String user;
    private String product;
    private Price price;
    private BookSide side;
    private int originalVolume;
    private int cancelledVolume;
    private int filledVolume;
    private final String id;
    private int remainingVolume;

    public Order(String user, String product, Price price, int originalVolume, BookSide side) throws InvalidInputException {
        this.setUser(user);
        this.setProduct(product);
        this.setPrice(price);
        this.setSide(side);
        this.setOrigVolume(originalVolume);
        this.filledVolume = 0;
        this.cancelledVolume = 0;
        this.remainingVolume = this.originalVolume;
        this.id = this.user + this.product + this.price + System.nanoTime();
    }

    private void setUser(String u) throws InvalidInputException {
        if (!u.matches("^[A-Z]{3}$")) {
            throw new InvalidInputException("Invalid user name");
        } else {
            this.user = u;
        }
    }

    private void setProduct(String p) throws InvalidInputException {
        if (p == null || p.isEmpty()) {
            throw new InvalidInputException("Invalid product");
        } else{
            if (p.length() <= 5 && !p.matches("[^a-zA-Z0-9.]+")){
                this.product = p;
            } else {
                throw new InvalidInputException("Invalid product");
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

    private void setSide(BookSide s ) throws InvalidInputException {
        if (s == null) {
            throw new InvalidInputException("Invalid side");
        } else {
            this.side = s;
        }
    }

    private void setOrigVolume(int v) throws InvalidInputException {
        if (v > 10000 || v < 0) {
            throw new InvalidInputException("Invalid Original Volume");
        } else {
            this.originalVolume = v;
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
        return this.side + "order: " + this.product + " at " + this.price + ", Orig Vol: "
                + this.originalVolume + ", Rem Vol: " + this.remainingVolume +
                ", Fill Vol: " + this.filledVolume + ", CXL Vol: " + this.cancelledVolume + ", ID: " + this.id;
    }
    @Override
    public TradableDTO makeTradableDTO() {
        return new TradableDTO(this);
    }
}
