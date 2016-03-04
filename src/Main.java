import Mechanics.Combat;
import Mechanics.Heal;
import Mechanics.LevelUp;
import Units.Enemy;
import Units.Player;
import Units.Unit;
import Units.Weapon;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.IntBinaryOperator;

/**
 * Lots of testing and will eventually run the game program
 */
public class Main {

    private static final String weaponsFile = "data/weapons.txt";
    private static final String enemiesFile = "data/level1.txt";
    private static final String playersFile = "data/players.txt";
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
            String[] line = console.nextLine().split("\\s+");
            String cmd = line[0].toLowerCase();

            // Handles simple commands
            switch (cmd) {
                case ("attack"):    // Sets up combat
                    String attacker;
                    String defender;
                    int distance;

                    if (line.length == 4) {
                        attacker = line[1];
                        defender = line[2];
                        distance = Integer.parseInt(line[3]);
                    } else {
                        System.out.printf("Attacker: ");
                        attacker = console.next();
                        System.out.printf("Defender: ");
                        defender = console.next();
                        System.out.printf("Distance: ");
                        distance = console.nextInt();
                    }
                    Combat.combat(units.get(attacker.toLowerCase()), units.get(defender.toLowerCase()), distance);
                    break;

                case ("heal"):      // Sets up healing
                    String healer;
                    String recipient;

                    if (line.length == 3) {
                        healer = line[1];
                        recipient = line[2];
                    } else {
                        System.out.printf("Healer: ");
                        healer = console.next();
                        System.out.printf("Recipient: ");
                        recipient = console.next();
                    }
                    Heal.heal(units.get(healer.toLowerCase()), units.get(recipient.toLowerCase()));
                    break;

                case ("players"):   // Prints all players and their hp
                    System.out.println("==================== ALL PLAYERS ====================");
                    for (Map.Entry<String, Player> entry : players.entrySet()) {
                        Player player = entry.getValue();
                        System.out.printf("%-30s", player.getName());
                        System.out.println("(" + player.getHp() + "/" + player.getMaxhp() + " hp)");
                    }
                    break;

                case("enemies"):    // Prints all enemies and their hp
                    System.out.println("==================== ALL ENEMIES ====================");
                    for (Map.Entry<String, Enemy> entry : enemies.entrySet()) {
                        Enemy enemy = entry.getValue();
                        System.out.printf("%-30s", enemy.getName());
                        System.out.println("(" + enemy.getHp() + "/" + enemy.getMaxhp() + " hp)");
                    }
                    break;

                case ("save"):      // Save players to a file
                    save();
                    break;

                case ("healall"):
                    for (Map.Entry<String, Player> entry : players.entrySet()) {
                        Player player = entry.getValue();
                        player.setHp(player.getMaxhp());
                    }
                    System.out.println("All players restored to full health.");
                    break;

                case ("help"):
                    System.out.println("Commands:\n" +
                            "attack\n" +
                            "heal\n" +
                            "players\n" +
                            "enemies\n" +
                            "save\n" +
                            "healall\n" +
                            "quit\n");

                case ("quit"):
                    return;
            }

            // Handles if the command is a player
            if (players.containsKey(cmd)) {
                System.out.println(players.get(cmd));
                if (line.length != 1) {
                    switch (line[1]) {
                        case ("-s"):
                            System.out.print("What weapon would you like to switch to? ");
                            Scanner weapon = new Scanner(System.in);
                            String weaponName = weapon.nextLine();
                            players.get(cmd).switchWeapon(weaponName);
                            break;
                        case ("-i"):
                            System.out.println(players.get(cmd).inventoryToString());
                            break;
                    }
                }
            }

            // Handles if the command is an enemy
            if (enemies.containsKey(cmd)) {
                System.out.println(enemies.get(cmd));
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
                        new Enemy(list[0],      // Name
                                statList[0],    // Max HP
                                statList[0],    // HP
                                statList[1],    // Move
                                statList[2],    // Str
                                statList[3],    // Mag
                                statList[4],    // Skill
                                statList[5],    // Spd
                                statList[6],    // Defense
                                statList[7],    // Res
                                statList[8],    // Mastery
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

    /**
     * Saves player data to the players.txt file
     */
    public static void save() {
        try {
            FileWriter saver = new FileWriter(playersFile);
            for (Map.Entry<String, Player> entry : players.entrySet()) {
                Player player = entry.getValue();
                saver.write(player.getName() + "\t" + player.getRole() + "\t" + player.getLevel() + "\t" +
                        player.getXp() + "\t" + player.getMaxhp() + "\t" + player.getHp() + "\t" + player.getMove() + "\t" +
                        player.getStr() + "\t" + player.getMag() + "\t" + player.getSkill() + "\t" +
                        player.getSpd() + "\t" + player.getDefense() + "\t" + player.getRes() + "\t" +
                        player.getMastery() +  "\t" + player.getGold() + "\t" +
                        player.getEquipped().getName() + "\t");
                for (Map.Entry<String, Weapon> weaponentry : player.getInventory().entrySet()) {
                    Weapon weapon = weaponentry.getValue();
                    saver.write(weapon.getName() + " " + weapon.getDurability() + " ");
                }
                saver.write(System.lineSeparator());
            }
            saver.close();
            System.out.println("Player data saved to " + playersFile);
        }
        catch(Exception ex) {
            System.out.println("Unable to save.");
        }
    }
}
