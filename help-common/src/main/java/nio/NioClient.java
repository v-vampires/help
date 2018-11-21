package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * Created by fitz.li on 2018/11/14.
 */
public class NioClient {
    private Selector selector;
    public void init(String ip, int port) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        this.selector = SelectorProvider.provider().openSelector();
        channel.connect(new InetSocketAddress(ip, port));
        channel.register(selector, SelectionKey.OP_CONNECT);
    }

    public static void main(String[] args) throws IOException {
        NioClient nioClient = new NioClient();
        nioClient.init("localhost",8000);
        nioClient.working();
    }

    public void working() throws IOException {
        while(true){
            if(!selector.isOpen()){
                break;
            }
            selector.select();
            Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey sk = it.next();
                it.remove();
                //连接事件发生
                if(sk.isConnectable()){
                    connect(sk);
                }else if(sk.isReadable()){
                    read(sk);
                }
            }
        }
    }

    private void read(SelectionKey sk) throws IOException {
        SocketChannel channel = (SocketChannel) sk.channel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        channel.read(buffer);
        String msg = new String(buffer.array()).trim();
        System.out.println("客户端收到消息：" + msg);
        channel.close();
        sk.selector().close();
    }

    /**
     * 先判断连接是否建立，完成连接的建立
     * 向channel写数据
     * 再为channel向Selector注册感兴趣事件为读事件
     * @param sk
     * @throws IOException
     */
    private void connect(SelectionKey sk) throws IOException {
        SocketChannel channel = (SocketChannel) sk.channel();
        if(channel.isConnectionPending()){
            channel.finishConnect();
        }
        channel.configureBlocking(false);
        channel.write(ByteBuffer.wrap(new String("hello server!\r\n").getBytes()));
        channel.register(this.selector, SelectionKey.OP_READ);
    }
}
