import Mechanics.Heal;
import Mechanics.LevelUp;
import Units.Enemy;
import Units.Player;
import Mechanics.Combat;
import Units.Unit;
import Units.Weapon;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Lots of testing and will eventually run the game program
 */
public class Main {

    private static final String weaponsFile = "src/weapons.txt";
    private static final String enemiesFile = "src/level1.txt";
    private static final String playersFile = "src/players.txt";
    private static TreeMap<String, Enemy> enemies = new TreeMap<>();
    private static TreeMap<String, Player> players = new TreeMap<>();
    private static TreeMap<String, Unit> units = new TreeMap<>();

    /**
     * The big guy.
     * Calls init() to begin with
     * Loops through and asks the user for commands
     * Does stuff based on those commands
     */
    public static void main(String[] args) {
        init();

        while (true) {
            Scanner console = new Scanner(System.in);
            System.out.printf("Enter command: ");
            String cmd = console.next();

            // Handles simple commands
            switch (cmd) {
                case ("attack"):
                    System.out.printf("Attacker: ");
                    String attacker = console.next();
                    System.out.printf("Defender: ");
                    String defender = console.next();
                    Combat.combat(units.get(attacker), units.get(defender));
                    break;

                case ("heal"):
                    System.out.printf("Healer: ");
                    String healer = console.next();
                    System.out.printf("Recipient: ");
                    String recipient = console.next();
                    Heal.heal(units.get(healer), units.get(recipient));
                    break;

                case ("players"):
                    System.out.println("==================== ALL PLAYERS ====================");
                    for (Map.Entry<String, Player> entry : players.entrySet()) {
                        Player player = entry.getValue();
                        System.out.printf("%-30s", player.getName());
                        System.out.println("(" + player.getHp() + "/" + player.getMaxhp() + " hp)");
                    }
                    break;

                case ("quit"):
                    return;
            }

            // Handles if the command is a player or enemy name
            if (units.containsKey(cmd)) {
                System.out.println(units.get(cmd));
            }

            System.out.println();
        } // Ends the while loop
    }

    /**
     * Does everything needed to start the game.
     * Populates HashMaps with players, enemies, weapons, etc.
     * Pulls that data from files specified in the Main class
     */
    public static void init() {

        // This will reference one line at a time, used for all file reading
        String line;

        try {

/////////////////////////////////////////////// WEAPON STUFF //////////////////////////////////////////////////////////
            // Initialize weapon list
            FileReader fileReader =
                    new FileReader(weaponsFile);
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



/////////////////////////////////////////////// ENEMY  STUFF //////////////////////////////////////////////////////////
            fileReader =
                    new FileReader(enemiesFile);
            bufferedReader =
                    new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {    // Loops through enemy list
                String[] list = line.split("\\s+");
                int[] statList = new int[9];
                for (int i = 1; i < 10; i += 1) {
                    statList[i-1] = Integer.parseInt(list[i]);
                }
                enemies.put(list[0].toLowerCase(),
                        new Enemy(list[0],
                                statList[0],
                                statList[0],
                                statList[1],
                                statList[2],
                                statList[3],
                                statList[4],
                                statList[5],
                                statList[6],
                                statList[7],
                                statList[8],
                                new Weapon(list[10],
                                        list[10], 0, 0, 0, 0, 0)));
            }



/////////////////////////////////////////////// PLAYER STUFF //////////////////////////////////////////////////////////
            // Initialize player list
            fileReader =
                    new FileReader(playersFile);
            bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {     // Loops through player list
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

            // Initializes level up stuff
            LevelUp.init();

            // Copies enemy and player TreeMaps into a general units TreeMap
            // Uses shallow copies so all changes happen to both copies of the unit
            for (Map.Entry<String, Player> entry : players.entrySet()) {
                units.put(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, Enemy> entry : enemies.entrySet()) {
                units.put(entry.getKey(), entry.getValue());
            }
        }


        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file.");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file.");
        }
        catch(Exception ex) {
            System.out.println("Something nonspecific went wrong.");
        }
    }
}
