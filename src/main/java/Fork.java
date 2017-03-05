import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.lang.System.getProperty;
import static java.lang.System.nanoTime;

public class Fork
{
    private static final String LINE = "01234567890123456789012345678901234567890123456789\n";
    private static final Logger LOG = Logger.getLogger(Fork.class.getName());

    static {
        try {
            File log = new File(getProperty("user.dir"), nanoTime() + ".log");
            FileHandler fh = new FileHandler(log.getCanonicalPath());
            LOG.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            throw new IllegalStateException(e.getLocalizedMessage(), e);
        }
    }

    public static void main(String... args) throws Exception {
        LOG.info(System.currentTimeMillis() + " main() started");

        for (int i = 0; i < 500; i++) {
            // 50 x 500 chars = 25000 characters
            System.out.print(LINE);
            System.out.flush();
        }

        Thread.sleep(3_000);

        /*final Semaphore semaphore = new Semaphore(0);
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
        semaphore.acquire();*/
        //int read = System.in.read();
        //read = System.in.read();
        //System.exit(0);
        //Runtime.getRuntime().halt(0);
    }
}
