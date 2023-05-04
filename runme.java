import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Scanner;


public class runme {
    public static void main(String args[]) throws IOException {

        // Grab the names of all log files
        Path currentRelativePath = Paths.get("scrape2");
        File folder = new File(currentRelativePath.toAbsolutePath().toString());
        File[] files = folder.listFiles();
        Arrays.sort(files);

        //create starter files
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].getName().indexOf("runme") != -1 || files[i].getName().startsWith(".") == true || files[i].getName().indexOf("README.md") != -1 )
            {
                continue;
            }
            
            BufferedReader br = new BufferedReader(new FileReader(files[i]));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("CHAT") && line.contains("[INSERT USERNAME]"))
                {
                    System.out.println(line);
                }
            }
        }
    }
}