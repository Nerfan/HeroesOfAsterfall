import Units.Player;
import Mechanics.Combat;
import Units.Weapon;

import java.io.*;
import java.util.HashMap;
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
                weapons.put(firstlist[1].toLowerCase() + " " + firstlist[0].toLowerCase(),
                        new Weapon(firstlist[0], // Type of weapon (e.g. Sword)
                                firstlist[1] + " " + firstlist[0], // Full name (e.g. Iron Sword)
                                list[0],    // Strength
                                list[1],    // Magic
                                list[4],    // Hit chance
                                list[3],    // Durability
                                list[5]     // Cost
                        )
                );
            } // End weapon list loop
            bufferedReader.close();

            // Initialize player list
            fileReader =
                    new FileReader("src/players.txt");
            bufferedReader =
                    new BufferedReader(fileReader);
            TreeMap<String, Player> players = new TreeMap<>();

            while((line = bufferedReader.readLine()) != null) { // Loops through player list
                String[] firstlist = line.split("\\s+");
                Integer[] list = new Integer[13];
                for (int i = 2; i < 15; i +=1) {
                    list[i-2] = Integer.parseInt(firstlist[i]);
                }
                // Create Inventory
                HashMap<String, Weapon> inventory = new HashMap<>();
                for (int i = 17; i < firstlist.length; i += 3) {
                    String name = firstlist[i].toLowerCase() + " " + firstlist[i+1].toLowerCase();
                    inventory.put(name, new Weapon(weapons.get(name)));
                    inventory.get(name).setDurability(Integer.parseInt(firstlist[i+2]));
                }
                // Create the player object
                players.put(firstlist[0].toLowerCase(),
                        new Player(firstlist[0], firstlist[1], // Name and role
                                list[0],    // Level
                                list[1],    // Experience
                                list[2],    // Max HP
                                list[3],    // Current HP
                                list[4],    // Move
                                list[5],    // Strength
                                list[6],    // Magic
                                list[7],    // Skill
                                list[8],    // Speed
                                list[9],    // Defense
                                list[10],   // Resistance
                                list[11],   // Mastery
                                list[12],   // Gold
                                inventory,  // Inventory (Created above)
                                inventory.get(firstlist[15].toLowerCase() + " " + firstlist[16].toLowerCase())
                        )
                );
            } // End player list loop
            bufferedReader.close();


            System.out.println(players.get("nerfan"));
            System.out.println(players.get("ghost"));

            Combat.combat(players.get("nerfan"), players.get("ghost"));

            System.out.println(players.get("nerfan"));
            System.out.println(players.get("ghost"));


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
