package designPattern.observer;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by fitz.li on 2018/11/21.
 * 被观察者，也就是微信公众号服务
 */
public class WeChatServer implements Observerable {

    private List<Observer> observers = Lists.newArrayList();

    private String meesage;

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObserver() {
        observers.forEach(o -> o.update(meesage));
    }

    public void setInfomation(String meesage){
        this.meesage = meesage;
        notifyObserver();
    }
}
