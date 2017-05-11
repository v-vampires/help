package instrument;

public class Sample {
    static {
        System.loadLibrary("threadCPUTime");
    }

    public static native long getCurrentThreadCpuTime();

    public static void main(String[] args) {
        System.loadLibrary("threadCPUTime");
        helloWorld();
    }

    private static void helloWorld() {
        System.out.println("Hello world");
        String result = "";
        for (int i = 0; i < 10000; i++)
            result = result + (char) (i % 26 + 97);
    }
}