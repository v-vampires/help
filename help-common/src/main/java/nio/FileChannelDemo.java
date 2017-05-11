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
        while(channel.read(buffer) != -1){
            buffer.flip();
            while (buffer.hasRemaining()){
                System.out.print((char) buffer.get());
            }
            buffer.clear();
        }
        aFile.close();
        channel.close();
    }
}
