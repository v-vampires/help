package dynamic.proxy;

/**
 * Created by fitz.li on 2017/1/18.
 */
public class Client {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        MyInvocationHandler handler = new MyInvocationHandler(userService);
        UserService proxy = (UserService) handler.getProxy();
        System.out.println(proxy);
        proxy.add();
    }
}
