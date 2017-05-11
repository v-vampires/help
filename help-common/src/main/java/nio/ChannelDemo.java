package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by fitz.li on 2017/3/1.
 */
public class ChannelDemo {
    public static void main(String[] args) throws IOException {
        ReadableByteChannel source = Channels.newChannel(System.in);
        WritableByteChannel dest = Channels.newChannel(System.out);
        channelCopy(source, dest);
        source.close();
        dest.close();
    }

    private static void channelCopy(ReadableByteChannel source, WritableByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(16*1024);
        while (source.read(buffer)!=-1){
            buffer.flip();
            while (buffer.hasRemaining()){
                dest.write(buffer);
            }
            buffer.clear();
        }
    }
}
