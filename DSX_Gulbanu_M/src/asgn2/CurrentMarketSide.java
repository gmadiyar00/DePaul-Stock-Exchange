package asgn2;
import dsx.Price;
// Student: Gulbanu Madiyarova

public class CurrentMarketSide {

    private Price price;
    private int volume;

    public CurrentMarketSide(Price p, int v){
        setPrice(p);
        setVolume(v);
    }

    private void setPrice(Price price){
        this.price = price;
    }

    private void setVolume(int volume){
        this.volume = volume;
    }

    public String toString(){
        return price + "x" + volume;
    }
}
