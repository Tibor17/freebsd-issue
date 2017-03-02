import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main
{
    public static void main(String... args) throws Exception {
        System.out.println("This Java application forks JVM.");
        System.out.printf("%,3d millis Before ProcessBuilder.start().\n", System.currentTimeMillis());

        String[] cmd = {"CMD", "/C", "java -jar fork.jar"};
        File userDir = new File(System.getProperty("user.dir")).getCanonicalFile();
        Process process = new ProcessBuilder(cmd)
                                  .directory(userDir)
                                  .start();

        System.out.printf("%,3d millis After ProcessBuilder.start() method has returned.\n", System.currentTimeMillis());
        System.out.println("Type a character to exit.");
        redirectTo(process.getOutputStream(), process.getInputStream());
        //process.getInputStream();
        //process.destroy();
        process.waitFor();
        System.out.printf("%,3d millis After process.waitFor()\n", System.currentTimeMillis());
        process.getOutputStream().close();
        process.getInputStream().close();
        process.getErrorStream().close();
        System.out.printf("%,3d millis After sub-process has completed with error code: %d.\n",
                                 System.currentTimeMillis(), process.exitValue());
    }

    private static void redirectTo(final OutputStream in, final InputStream out) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    int read = System.in.read();
                    System.out.printf("Dispatched '%s' to fork.jar.\n", (char) read);
                    in.write(read);
                    in.flush();

                    while (out.read() != -1) {
                    }
                } catch (IOException e) {}
                System.out.println("Daemon Thread finished.");
            }
        };
        t.setDaemon(true);
        t.start();
    }
}
