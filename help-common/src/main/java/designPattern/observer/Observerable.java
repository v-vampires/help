package designPattern.observer;

/**
 * Created by fitz.li on 2018/11/21.
 * 抽象被观察者接口
 */
public interface Observerable {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObserver();
}
