import Units.Player;

import java.io.*;
import java.util.TreeMap;

public class Test {
    public static void main(String [] args) {

        // The name of the file to open.
        String fileName = "players.txt";

        // This will reference one line at a time
        String line;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            // ACTUAL CODE RIGHT HERE
            TreeMap<String, Player> players = new TreeMap<>();
            while((line = bufferedReader.readLine()) != null) {
                String[] firstlist = line.split("\\s+");
                Integer[] list = new Integer[12];
                for (int i = 2; i < firstlist.length; i +=1) {
                    list[i-2] = Integer.parseInt(firstlist[i]);
                }
                //players.put(firstlist[0], new Player(firstlist[0], firstlist[1], list[0], list[1], list[2], list[3], list[4], list[5], list[6], list[7], list[8], list[9], list[10], list[11], list[12], null));
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }
}
