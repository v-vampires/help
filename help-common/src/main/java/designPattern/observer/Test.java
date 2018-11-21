package designPattern.observer;

/**
 * Created by fitz.li on 2018/11/21.
 */
public class Test {

    public static void main(String[] args) {
        WeChatServer server = new WeChatServer();
        Observer zhansan = new WeChatUser("zhangsan");
        Observer lisi = new WeChatUser("lisi");
        Observer wangfu = new WeChatUser("wangfu");
        server.registerObserver(zhansan);
        server.registerObserver(lisi);
        server.registerObserver(wangfu);
        server.setInfomation("PHP是世界上最好用的语言！");
        System.out.println("----------------------------------------------");
        server.removeObserver(zhansan);
        server.setInfomation("java是世界上最好用的语言！");
    }
}
