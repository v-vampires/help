package rpc;

/**
 * Created by fitz.li on 2018/12/3.
 * 暴露服务
 */
public class RpcProvider {
    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        RpcFramework.export(service, 12345);
    }
}
