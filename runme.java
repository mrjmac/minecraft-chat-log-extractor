import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.Arrays;


public class runme {
    public static void main(String args[]) throws IOException {

        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter the absolute path of your .minecraft logs folder :: ");
        String path = r.readLine();

        System.out.print("Enter your minecraft username :: ");
        String name = r.readLine();

        System.out.print("Would you like to save your logs to a file? y/n :: ");
        String response = r.readLine();
        boolean save = (response.equals("y") || response.equals("Y") ? true : false);

        System.out.print("Would you like to print your logs to output? y/n :: ");
        response = r.readLine();
        boolean print = (response.equals("y") || response.equals("Y") ? true : false);

        // Grab the names of all log files
        File folder = new File(path);
        File[] files = folder.listFiles();
        Arrays.sort(files);
        
        // Create starter files
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].getName().indexOf("debug") != -1 || files[i].getName().indexOf("latest") != -1 | files[i].getName().indexOf(".txt") != -1)
            {
                continue;
            }
            
            Path source = Paths.get(files[i].getAbsolutePath());
            Path target = Paths.get(files[i].getAbsolutePath().substring(0, files[i].getAbsolutePath().length() - 3) + ".txt");
            decompressGzip(source, target);
        }   

        folder = new File(path);
        files = folder.listFiles();
        Arrays.sort(files);

        PrintWriter pw = new PrintWriter("logs.txt");

        for (int i = 0; i < files.length; i++)
        {
            if (files[i].getName().indexOf("txt") != -1 && files[i].getName().indexOf("debug") == -1)
            {
                BufferedReader br = new BufferedReader(new FileReader(files[i]));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("CHAT") && line.contains(name + ":"))
                    {
                        if (print)
                        {
                            System.out.println(line);
                        }
                        if (save)
                        {
                            pw.println(line);
                        }
                    }
                }
                files[i].delete();
                br.close();
            }
        }

        pw.close();
        if (save == false)
        {
            File saved = new File("logs.txt");
            saved.delete();
        }
        
    }

    public static void decompressGzip(Path source, Path target) throws IOException {

        try (GZIPInputStream gis = new GZIPInputStream(
                                      new FileInputStream(source.toFile()));
             FileOutputStream fos = new FileOutputStream(target.toFile())) {

            // Copy GZIPInputStream to FileOutputStream
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

        }

    }
}
