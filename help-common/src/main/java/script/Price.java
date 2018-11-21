package script;

/**
 * Created by fitz.li on 2017/9/14.
 */
public class Price {

    private String flightNo;

    private String wrapperId;

    private int price;


    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getWrapperId() {
        return wrapperId;
    }

    public void setWrapperId(String wrapperId) {
        this.wrapperId = wrapperId;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Price(String flightNo, String wrapperId, int price) {
        this.flightNo = flightNo;
        this.wrapperId = wrapperId;
        this.price = price;
    }

    public void doSomething(Object args){
        System.out.println("doSomething:" + this.getWrapperId()+",args:" + args);
    }
}
