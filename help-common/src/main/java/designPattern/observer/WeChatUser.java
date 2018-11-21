package designPattern.observer;

/**
 * Created by fitz.li on 2018/11/21.
 */
public class WeChatUser implements Observer{
    private String name;
    private String message;

    public WeChatUser(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        this.message = message;
        read();
    }

    private void read() {
        System.out.println(name + " 收到的消息：" + message);
    }
}
