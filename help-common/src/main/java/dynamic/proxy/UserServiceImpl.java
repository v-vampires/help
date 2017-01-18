package dynamic.proxy;

/**
 * Created by fitz.li on 2017/1/18.
 */
public class UserServiceImpl implements UserService {
    @Override
    public void add() {
        System.out.println("--------------------add---------------");
    }
}
