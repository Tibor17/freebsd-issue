import java.io.IOException;
import java.util.concurrent.Semaphore;

public class Fork
{
    private static final String LINE = "01234567890123456789012345678901234567890123456789\n";

    public static void main(String... args) throws Exception {
        for (int i = 0; i < 200; i++) {
            // 50  200 chars = 10 000 characters
            System.out.print(LINE);
            System.out.flush();
        }

        final Semaphore semaphore = new Semaphore(0);
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    for (;;) {
                        int read = System.in.read();
                        semaphore.release();
                    }
                } catch (IOException e) {}
            }
        };
        t.setDaemon(true);
        t.start();
        semaphore.acquire();
        //int read = System.in.read();
        //read = System.in.read();
        //System.exit(0);
        //Runtime.getRuntime().halt(0);
    }
}
