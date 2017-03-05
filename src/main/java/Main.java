import org.apache.maven.shared.utils.cli.CommandLineUtils;
import org.apache.maven.shared.utils.cli.Commandline;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.io.File.separator;

public class Main
{
    public static void main(String... args) throws Exception {
        File javaHome = new File(System.getProperty("java.home")).getCanonicalFile();
        final File executable = new File(javaHome, "bin" + separator + "java").getCanonicalFile();
        final File userDir = new File(System.getProperty("user.dir")).getCanonicalFile();

        System.out.println("This Java application forks JVM.");

        final AtomicInteger idx = new AtomicInteger();
        //runFork(executable, userDir, idx);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                runFork(executable, userDir, idx);
            }
        });
        executor.submit(new Runnable() {
            @Override
            public void run() {
                runFork(executable, userDir, idx);
            }
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

    }

    private static void runFork(File executable, File userDir, AtomicInteger idx) {
        try {
            System.out.printf("%,3d millis Before ProcessBuilder.start().\n", System.currentTimeMillis());
            Process process = startProcess(executable, userDir, idx.incrementAndGet());
            System.out.printf("%,3d millis After ProcessBuilder.start() method has returned.\n", System.currentTimeMillis());

/*System.out.println("Type a character to exit.");
redirectTo(process.getOutputStream(), process.getInputStream());*/
            //process.getInputStream();
            //process.destroy();
            process.waitFor();
            System.out.printf("%,3d millis After process.waitFor()\n", System.currentTimeMillis());
            process.getOutputStream().close();
            process.getInputStream().close();
            process.getErrorStream().close();
            System.out.printf("%,3d millis After sub-process has completed with error code: %d.\n",
                    System.currentTimeMillis(), process.exitValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Process startProcess(File executable, File userDir, int idx) throws IOException {
        String jar = new File(userDir, "exec-1.0-fork" + idx + ".jar").getCanonicalPath();
        String[] cmd =
                {
                        /*
                        "/bin/sh",
                        "-c",
                        */
                        "CMD",
                        "/X",
                        "/C",

                        "cd",
                        quoteIfHasSpace(userDir.getAbsolutePath()),
                        "&&",
                        quoteIfHasSpace(executable.getAbsolutePath()),
                        "-jar",
                        quoteIfHasSpace(jar),
                        "/usr/home/osipovmi/Projekte/maven-surefire/surefire-integration-tests/target/ForkModeIT_testForkCountTwoReuse/target/surefire/",
                        "2017-03-04T11-14-12_766-jvmRun2",
                        "surefire3463035433514775088tmp",
                        "surefire_11427250543530051033tmp",
                };

        Commandline cli = new Commandline();
        cli.setExecutable(executable.getCanonicalPath());//"/usr/local/openjdk7/jre/bin/java"
        cli.setWorkingDirectory(userDir);
        cli.createArg().setValue("-jar");
        cli.createArg().setValue(jar);
        // dummy arguments
        cli.createArg().setValue("/usr/home/osipovmi/Projekte/maven-surefire/surefire-integration-tests/target/ForkModeIT_testForkCountTwoReuse/target/surefire/");
        cli.createArg().setValue("2017-03-04T11-14-12_766-jvmRun2");
        cli.createArg().setValue("surefire3463035433514775088tmp");
        cli.createArg().setValue("surefire_11427250543530051033tmp");
        //CommandLineUtils.executeCommandLine()
        List<String> cliCmd = cli.getShell().getShellCommandLine(cli.getArguments());
        cmd = cliCmd.toArray(new String[cliCmd.size()]);

        System.out.println(Arrays.toString(cmd));

        return new ProcessBuilder(cmd)
                .directory(userDir)
                .start();
    }

    private static String quoteIfHasSpace(String s) {
        return s.contains(" ") ? "\"" + s + "\"" : s;
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
