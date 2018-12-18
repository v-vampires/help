package rpc;

/**
 * Created by fitz.li on 2018/12/3.
 * 实现服务
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello" + name;
    }
}
