public class Exec {
    public static void main(String... args) throws Exception {
        System.out.println("This Java application starts sleep sub-process waiting 3 seconds.");
        System.out.printf("%,3d millis Before forking sub-process.\n", System.currentTimeMillis());
        Process process = Runtime.getRuntime().exec("/bin/sh -c \"sleep 3\"");
        System.out.printf("%,3d millis After Runtime.exec() method has returned.\n", System.currentTimeMillis());
        process.getOutputStream();
        process.getInputStream();
        process.waitFor();
        System.out.printf("%,3d millis After sub-process has completed.\n", System.currentTimeMillis());
    }
}
