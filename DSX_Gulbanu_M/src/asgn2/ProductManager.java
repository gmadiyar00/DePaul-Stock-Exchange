package asgn2;
import dsx.InvalidOrderException;
import java.util.*;
//Student: Gulbanu Madiyarova

public final class ProductManager {

    private static ProductManager instance;
    //the String product symbol; the value is a ProductBook object
    private HashMap<String, ProductBook> products = new HashMap<>();
    private ProductManager() {}

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public void addProduct(String symbol) throws InvalidInputException, DataValidationException, InvalidOrderException {
        if (symbol == null || !symbol.matches("^[A-Za-z0-9]{1,5}(\\.[A-Za-z0-9]{1,5})?$")) {
            throw new DataValidationException("Invalid product symbol");
        } else {
            this.products.put(symbol, new ProductBook(symbol));
        }
    }

    public ProductBook getProductBook(String symbol) throws DataValidationException {
        if (symbol == null) {
            throw new DataValidationException("Symbol cannot be null");
        } else {
            ProductBook productBook = this.products.get(symbol);
            if (productBook == null ) {
                throw new DataValidationException( "Product not found for symbol: "+ symbol);
            } else {
                return productBook;
            }
        }
    }

    String getRandomProduct() throws DataValidationException {
        if (this.products.isEmpty()) {
            throw new DataValidationException("No products available");
        } else {
            List<String> productSymbols = new ArrayList<>(this.products.keySet());
            Random random = new Random();
            int randomIndex = random.nextInt(productSymbols.size());
            return productSymbols.get(randomIndex);
        }
    }

    TradableDTO addTradable(Tradable o) throws DataValidationException, InvalidInputException, InvalidOrderException, NotFoundException {
        if (o == null) {
            throw new DataValidationException("Passed in null");
        } else {
            ProductBook productBook = getProductBook(o.getProduct());
            TradableDTO tradableDTO = productBook.add(o);
            UserManager.getInstance().updateTradable(o.getProduct(), tradableDTO);

            return tradableDTO;
        }
    }

    public TradableDTO[] addQuote(Quote q) throws InvalidInputException, DataValidationException, InvalidOrderException, NotFoundException {
        if (q == null) {
            throw new DataValidationException("Quote cannot be build");
        } else {
            ProductBook b = getProductBook(q.getSymbol());
            b.removeQuotesForUser(q.getUser());
            TradableDTO buySideDTO = this.addTradable(q.getQuoteSide(BookSide.BUY));
            TradableDTO sellSideDTO = this.addTradable(q.getQuoteSide(BookSide.SELL));
            return new TradableDTO[] {buySideDTO, sellSideDTO};
        }
    }

    public TradableDTO cancel(TradableDTO o) throws DataValidationException, InvalidInputException, InvalidOrderException, NotFoundException {
        if (o == null) {
            throw new DataValidationException("TradableDTO can't be null");
        } else {
            ProductBook productB = getProductBook(o.product());
            TradableDTO cancelledDTO = productB.cancel(o.side(), o.tradableId());
            if (cancelledDTO == null) {
                System.out.println("Failed to cancel: " + o);
                return null;
            } else {
                return cancelledDTO;
            }
        }
    }
    public TradableDTO[] cancelQuote(String symbol, String user) throws DataValidationException, InvalidOrderException, InvalidInputException, NotFoundException {
        if (this.products.isEmpty()) {
            throw new DataValidationException("Failed to cancel quote");
        } else {
            if (symbol == null || user == null) {
                throw new DataValidationException("Symbol and user cannot be null");
            } else {
                ProductBook productB = getProductBook(symbol);
                return productB.removeQuotesForUser(user);
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ProductBook> entry : products.entrySet()) {
            sb.append(entry.getValue().toString()).append("\n");
        }
        return sb.toString();
    }
}
