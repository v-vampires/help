package nio;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fitz.li on 2018/11/14.
 */
public class NioEchoServer {
    private Selector selector;
    private ExecutorService tp = Executors.newWorkStealingPool();
    public static Map<Socket ,Long> timestat = Maps.newHashMap();

    public static void main(String[] args) throws IOException {
        new NioEchoServer().startServer();
    }

    private void startServer() throws IOException {
        selector = SelectorProvider.provider().openSelector();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress(8000));
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        for(;;){
            selector.select();
            Set<SelectionKey> readKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = readKeys.iterator();
            long e = 0;
            while(it.hasNext()){
                SelectionKey sk = it.next();
                it.remove();

                if(sk.isAcceptable()){
                    doAccept(sk);
                }else if(sk.isValid() && sk.isReadable()){
                    Socket socket = ((SocketChannel) sk.channel()).socket();
                    if(!timestat.containsKey(socket)){
                        timestat.put(socket, System.currentTimeMillis());
                    }
                    doRead(sk);
                }else if(sk.isValid() && sk.isWritable()){
                    Socket socket = ((SocketChannel) sk.channel()).socket();
                    doWrite(sk);
                    e = System.currentTimeMillis();
                    long b = timestat.remove(socket);
                    System.out.println("spend:" + (e - b));
                }
            }
        }
    }

    private void doWrite(SelectionKey sk) {
        SocketChannel clientChannel = (SocketChannel) sk.channel();
        EchoClient echoClient = (EchoClient) sk.attachment();
        LinkedList<ByteBuffer> outq = echoClient.getOutq();
        ByteBuffer last = outq.getLast();
        try {
            int len = clientChannel.write(last);
            if(len == -1){
                disconnect(sk);
                return;
            }
            if(last.remaining() ==0){
                outq.removeLast();
            }
        } catch (IOException e) {
            e.printStackTrace();
            disconnect(sk);
        }
        if(outq.size() == 0){
            sk.interestOps(SelectionKey.OP_READ);
        }
    }

    private void disconnect(SelectionKey sk) {

    }

    private void doRead(SelectionKey sk) {
        SocketChannel clientChannel = (SocketChannel) sk.channel();
        ByteBuffer bb = ByteBuffer.allocate(8192);
        int len;
        try {
            len = clientChannel.read(bb);
            if(len < 0){
                disconnect(sk);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            disconnect(sk);
            return;
        }
        bb.flip();
        tp.execute(new HandleMsg(sk, bb));
    }

    private void doAccept(SelectionKey sk) {
        ServerSocketChannel server = (ServerSocketChannel) sk.channel();
        SocketChannel clientChanel = null;
        try {
            clientChanel = server.accept();
            clientChanel.configureBlocking(false);
            SelectionKey clientKey = clientChanel.register(selector, SelectionKey.OP_READ);
            EchoClient echoClient = new EchoClient();
            clientKey.attach(echoClient);
            InetAddress clientAddress = clientChanel.socket().getInetAddress();
            System.out.println("Accepted connection from " + clientAddress.getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class EchoClient{
        private LinkedList<ByteBuffer> outq;

        public EchoClient() {
            this.outq = new LinkedList<ByteBuffer>();
        }

        public LinkedList<ByteBuffer> getOutq() {
            return outq;
        }
        public void enqueue(ByteBuffer bb){
            outq.addFirst(bb);
        }
    }

    class HandleMsg implements Runnable{

        SelectionKey sk;
        ByteBuffer bb;

        public HandleMsg(SelectionKey sk, ByteBuffer bb) {
            this.sk = sk;
            this.bb = bb;
        }

        @Override
        public void run() {
            EchoClient echoClient = (EchoClient) sk.attachment();
            echoClient.enqueue(bb);
            sk.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
            selector.wakeup();
        }
    }
}
