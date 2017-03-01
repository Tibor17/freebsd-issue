import java.io.File;

public class Exec {
    public static void main(String... args) throws Exception {
        System.out.println("This Java application starts sleep sub-process waiting 3 seconds.");
        System.out.printf("%,3d millis Before ProcessBuilder.start().\n", System.currentTimeMillis());
        String[] cmd = {"/bin/sh", "-c", "sleep 3"};
        File userDir = new File(System.getProperty("user.dir")).getCanonicalFile();
        Process process = new ProcessBuilder(cmd)
                                  .directory(userDir)
                                  .start();
        System.out.printf("%,3d millis After ProcessBuilder.start() method has returned.\n", System.currentTimeMillis());
        process.getOutputStream();
        process.getInputStream();
        process.waitFor();
        System.out.printf("%,3d millis After sub-process has completed with error code: %d.\n",
                                 System.currentTimeMillis(), process.exitValue());
    }
}
