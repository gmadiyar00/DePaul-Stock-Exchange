package asgn2;
import dsx.Price;
// Student: Gulbanu Madiyarova

interface Tradable {

    String getid();
    int getRemainingVolume();
    void setCancelledVolume(int newVol);
    int getCancelledVolume();
    void setRemainingVolume(int newVol);
    TradableDTO makeTradableDTO();
    Price getPrice();
    void setFilledVolume(int newVol);
    int getFilledVolume();
    BookSide getSide();
    String getUser();
    String getProduct();
    int getOriginalVolume();
}
