import Units.Player;
import Mechanics.Combat;
import Units.Weapon;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jeremy on 2/22/16.
 */
public class Main {
    public static void main(String [] args) {

        // This will reference one line at a time
        String line;

        try {

            // Initialize weapon list
            FileReader fileReader =
                    new FileReader("src/weapons.txt");
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            TreeMap<String, Weapon> weapons = new TreeMap<>();
            while((line = bufferedReader.readLine()) != null) {
                String[] firstlist = line.split("\\s+");
                Integer[] list = new Integer[6];
                for (int i = 2; i < firstlist.length; i +=1) {
                    if (i != 4) {
                        list[i - 2] = Integer.parseInt(firstlist[i]);
                    }
                }
                weapons.put(firstlist[1].toLowerCase() + " " + firstlist[0].toLowerCase(), new Weapon(firstlist[0], firstlist[1], list[0], list[1], list[4], list[3], list[5]));
            }
            bufferedReader.close();

            // Initialize player list
            fileReader =
                    new FileReader("src/players.txt");
            bufferedReader =
                    new BufferedReader(fileReader);
            TreeMap<String, Player> players = new TreeMap<>();
            while((line = bufferedReader.readLine()) != null) {
                String[] firstlist = line.split("\\s+");
                Integer[] list = new Integer[13];
                for (int i = 2; i < 15; i +=1) {
                    list[i-2] = Integer.parseInt(firstlist[i]);
                }
                players.put(firstlist[0].toLowerCase(),
                        new Player(firstlist[0], firstlist[1], // Name and role
                                list[0], list[1], list[2], list[3], list[4], list[5], list[6], list[7], list[8], list[9], list[10], list[11], list[12],
                                weapons.get(firstlist[15].toLowerCase() + " " + firstlist[16].toLowerCase())));
            }
            bufferedReader.close();


            for (Map.Entry<String, Player> player:players.entrySet()) {
                System.out.println(player.getKey() + " " + player.getValue().toString());
            }
            Combat.combat(players.get("nerfan"), players.get("ghost"));
        } // END ACTUAL CODE


        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }
}
