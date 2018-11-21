package http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by fitz.li on 2018/8/13.
 */
public class PortCheck {
    public static void main(String[] args) {
        //System.out.println(isHostReachable("23.43.48.28", 1000));
        //System.out.println(isHostConnectable("23.43.48.28", 8080));
        System.out.println(isHostConnectable("23.43.48.28", 80, 5000));
    }

    public static boolean isHostConnectable(String host, int port, int timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), timeout);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean isHostReachable(String host, Integer timeOut) {
        try {
            return InetAddress.getByName(host).isReachable(timeOut);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
