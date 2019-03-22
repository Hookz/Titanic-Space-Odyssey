package Lambert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class FileUtil {

    /** Directory where output files should go by default. */
    private static File outputDir = deduceOutputDir();

    public static String file_separator() {
        return File.separator;
    }

    public static String getClassFilePath(String package_name,
                                          String class_name) {

        String path = null;
        try {
            Class c = Class.forName(package_name + "." + class_name);
            path = getClassFilePath(c);
        }
        catch (ClassNotFoundException e) {
            System.err.println("Could not locate class " + package_name + "." +
                    class_name);
            path = null;
        }
        return path;
    }


    public static String getClassFilePath(Class c) {
        String out = null;

        // check for Windoze
        boolean windoze = (File.separatorChar == '\\');

        URL url = null;
        String fullyQualifiedName = c.getCanonicalName();
        String shortName = c.getSimpleName();
        String classRes = "/" + fullyQualifiedName.replace('.', '/') + ".class";
        url = c.getResource(classRes);

        if (url.getProtocol().equals("file")) {
            try {

                out = url.toURI().getPath();
                if (out == null) {
                    out = url.toURI().getSchemeSpecificPart();
                }

                // chop off the unneeded stuff
                out = out.substring(0, out.length() - (shortName + ".class").length());
                if (windoze && out.startsWith("/")) {
                    out = out.substring(1);
                }
            } catch (URISyntaxException e) {
                // Something has gone awry.  All valid URLs are URIs.
                throw new RuntimeException("Could not find " + fullyQualifiedName +
                        " in classpath.");
            }

            out = out.replace('/', File.separatorChar);
        }
        else {
            // Not from a directory.  Return null.
            out = null;
        }
        return out;
    }

    private static File deduceOutputDir() {
        File outputDir = null;
        String fileUtilDir = getClassFilePath(FileUtil.class);
        if (fileUtilDir != null) {
            // We back up one directory for every "." in the class name.
            File rootDir = new File(fileUtilDir);
            String name = FileUtil.class.getCanonicalName();
            for(int ct = name.indexOf("."); ct >= 0; ct = name.indexOf(".", ct+1)) {
                rootDir = rootDir.getParentFile();
            }
            outputDir = rootDir;
        }
        else {
            // JAT is not deployed in a directory tree.  Use the working directory.
            outputDir = new File(".");
        }
        return outputDir;
    }

    public static OutputStream openOutputFile(String path)
            throws IOException {

        OutputStream strm = null;
        if (path.equals("-")) {
            strm = System.out;
        }
        else if (new File(path).isAbsolute()) {
            strm = new FileOutputStream(path);
        }
        else {
            path = outputDir.getAbsolutePath() + File.separator + path;
            strm = new FileOutputStream(path);
        }
        return strm;
    }
}
