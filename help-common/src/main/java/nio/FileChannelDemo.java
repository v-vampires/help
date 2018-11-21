package nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by fitz.li on 2017/3/2.
 */
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        readFile();
    }

    private static void readFile() throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("D:/fuzzy.sql","rw");
        FileChannel channel = aFile.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(128);
        while(channel.read(buffer) != -1){//从channel 读取到 buffer
            buffer.flip();//将buffer从写模式切换成读模式
            while (buffer.hasRemaining()){
                System.out.print((char) buffer.get());// read 1 byte at a time
            }
            buffer.clear();//make buffer ready for writing
        }
        aFile.close();
        channel.close();
    }
}
