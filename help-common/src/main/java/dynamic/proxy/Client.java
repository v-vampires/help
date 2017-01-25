package dynamic.proxy;

/**
 * Created by fitz.li on 2017/1/18.
 * 会自动调用InvocationHandlerImpl的invoke方法?
 * 因为JDK生成的最终真正的代理类，它继承自Proxy并实现了我们定义的UserService接口，
 * 在实现UserService接口方法的内部，调用了InvocationHandlerImpl的invoke方法。
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
