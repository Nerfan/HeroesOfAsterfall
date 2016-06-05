import Mechanics.Combat;
import Mechanics.Heal;
import Mechanics.LevelUp;
import Units.Enemy;
import Units.Player;
import Units.Unit;
import Units.Weapon;

import java.io.*;
import java.util.*;

/**
 * The model of the game for the Model View Controller (MVC) design pattern
 * Everything that happens in the game can be called from this class.
 * However, nothing should prompt for input at any point during the methods contained in this file.
 *
 * @author Jeremy Lefurge
 */
public class HoAModel extends Observable {
    private enum Phase {
        PLAYER, ENEMY
    }
    /** Tells what units are able to act: players or enemies */
    private Phase phase;
    /** Number of turns that have passed */
    private int turncount;
    /** Path to the file where the weapons should be read from */
    private String weaponsFile;
    /** Path to the file where the enemies should be read from */
    private String enemiesFile;
    /** Path to the file where the players should be read from */
    private String playersFile;

    // Keys for all Maps are lowercase
    /** All enemies stored by name */
    private TreeMap<String, Enemy> enemies;
    /** All players stored by name */
    private TreeMap<String, Player> players;
    /** All units stored by name */
    private TreeMap<String, Unit> units;
    /** All weapons stored by name */
    private TreeMap<String, Weapon> weapons;

    /**
     * Constructs a new model.
     * Populates all TreeMaps from files specified as parameters
     *
     * @param playersFile Path to file to read players from
     * @param enemiesFile Path to file to read enemies from
     * @param weaponsFile Path to file to read weapons from
     */
    public HoAModel(String playersFile, String enemiesFile, String weaponsFile) {
        // Simple constructor stuff
        this.weaponsFile = weaponsFile;
        this.enemiesFile = enemiesFile;
        this.playersFile = playersFile;
        this.turncount = 0;

        // Initializes all of these as empty TreeMaps
        this.enemies = new TreeMap<>();
        this.players = new TreeMap<>();
        this.units = new TreeMap<>();
        this.weapons = new TreeMap<>();

        String line; // This will reference one line at a time, used for all file reading

        try {
/////////////////////////////////////////////// WEAPON STUFF //////////////////////////////////////////////////////////
            // Initialize weapon list
            FileReader fileReader =
                    new FileReader(weaponsFile);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                String[] firstList = line.split("\\s+");
                int[] list = new int[6];
                for (int i = 2; i < firstList.length; i +=1) {
                    if (i != 4) {
                        list[i - 2] = Integer.parseInt(firstList[i]);
                    }
                }
                this.weapons.put(firstList[1].toLowerCase() + " " + firstList[0].toLowerCase(),
                        new Weapon(firstList[0],                   // Type of weapon (e.g. Sword)
                                firstList[1] + " " + firstList[0], // Full name (e.g. Iron Sword)
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
                this.enemies.put(list[0].toLowerCase(),
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
                                new Weapon(list[10],    // Equipped weapon; initializes all stats to 0
                                        list[10], 0, 0, 0, 0, 0)));
            } // End enemy list loop
            bufferedReader.close();



/////////////////////////////////////////////// PLAYER STUFF //////////////////////////////////////////////////////////
            // Initialize player list
            fileReader =
                    new FileReader(playersFile);
            bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {     // Loops through player list
                String[] firstlist = line.split("\\s+");
                int[] list = new int[13];
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
                this.players.put(firstlist[0].toLowerCase(),
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
                                // Currently equipped weapon
                                inventory.get(firstlist[15].toLowerCase() + " " + firstlist[16].toLowerCase())
                        )
                );
            } // End player list loop
            bufferedReader.close();

            // Initializes level up stuff
            LevelUp.init();

            // Copies enemy and player TreeMaps into a general units TreeMap
            // Uses shallow copies so all changes happen to both copies of the unit
            // Basically gives two ways to call each unit
            for (Map.Entry<String, Player> entry : this.players.entrySet()) {
                this.units.put(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, Enemy> entry : this.enemies.entrySet()) {
                this.units.put(entry.getKey(), entry.getValue());
            }
            this.phase = Phase.ENEMY;
            this.nextPhase(); // Starts the first player phase
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
            System.out.println(ex);
        }

    }

    /**
     * Advances the game to the next phase
     */
    public void nextPhase() {
        switch (this.phase) {
            case ENEMY:
                this.phase = Phase.PLAYER;
                players.values().forEach(Unit::newTurn);
                this.turncount++;
                break;
            case PLAYER:
                this.phase = Phase.ENEMY;
                enemies.values().forEach(Unit::newTurn);
                break;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Tells how many turns have passed
     * @return the turn count
     */
    public int getTurnCount() {
        return this.turncount;
    }

    public Phase getPhase() {
        return this.phase;
    }

    /**
     * Calls for combat between two units
     *
     * @param attackerName Name of the attacker
     * @param defenderName Name of the defender
     * @param distance Distance from one combatant to the other
     */
    public void combat(String attackerName, String defenderName, int distance) {
        Combat.combat(units.get(attackerName.toLowerCase()), units.get(defenderName.toLowerCase()), distance);
        setChanged();
        notifyObservers();
    }

    /**
     * Calls for one unit to heal another
     *
     * @param healerName    Name of the healer
     * @param recipientName Name of the unit being healed
     */
    public void heal(String healerName, String recipientName) {
        Heal.heal(units.get(healerName.toLowerCase()), units.get(recipientName.toLowerCase()));
        setChanged();
        notifyObservers();
    }

    /**
     * Switches a player's weapon
     *
     * @param playerName Name of player
     * @param weaponName Name of weapon to switch to
     */
    public void switchWeapon(String playerName, String weaponName) {
        players.get(playerName.toLowerCase()).switchWeapon(weaponName.toLowerCase());
        setChanged();
        notifyObservers();
    }

    // Class abilities

    /**
     * Saint ability: heal two other units at the same time for half as much as normal
     * @param healerName     Name of healer, should be of Saint class
     * @param recipientNames List of names of recipients; should have a size of 2
     */
    public void linkHeal(String healerName, List<String> recipientNames) {
        Unit healer = units.get(healerName.toLowerCase());
        List<Unit> recipients = new ArrayList<>();
        for (String name : recipientNames) {
            recipients.add(units.get(name.toLowerCase()));
        }
        Heal.linkHeal(healer, recipients);
        setChanged();
        notifyObservers();
    }

    /**
     * Marksman ability; deal half damage to up to four targets in range
     * @param attackerName Name of Marksman
     * @param targetNames  List of names of targets
     */
    public void pierce(String attackerName, List<String> targetNames) {
        Unit attacker = units.get(attackerName.toLowerCase());
        List<Unit> targets = new ArrayList<>();
        for (String name : targetNames) {
            targets.add(units.get(name.toLowerCase()));
        }
        Combat.pierce(attacker, targets);
        setChanged();
        notifyObservers();
    }

    /**
     * Marksman ability; deal full damage to two targets in a line
     * @param attackerName Name of Marksman
     * @param targetNames  List of names of targets
     */
    public void multiShot(String attackerName, List<String> targetNames) {
        Unit attacker = units.get(attackerName.toLowerCase());
        List<Unit> targets = new ArrayList<>();
        for (String name : targetNames) {
            targets.add(units.get(name.toLowerCase()));
        }
        Combat.multiShot(attacker, targets);
        setChanged();
        notifyObservers();
    }

    /**
     * Strategist ability; uses strong points of surrounding allies to buff oneself before combat.
     * For each adjacent ally, the Strategist takes their highest stat and increases his/her own by 5.
     * For example, an adjacent ally whose highest stat is Strength will result
     * in the Strategist's Strength increasing by 5 for the battle
     * @param attackerName Name of the attacker; should be a Strategist
     * @param defenderName Name of the defender
     * @param distance Distance between attacker and defender
     * @param adjacentNames A list of names of adjacent allies
     */
    public void adaptability(String attackerName, String defenderName, int distance, List<String> adjacentNames) {
        Unit attacker = units.get(attackerName.toLowerCase());
        Unit defender = units.get(defenderName.toLowerCase());
        ArrayList<Unit> adjacent = new ArrayList<>();
        for (String name : adjacentNames) {
            adjacent.add(units.get(name.toLowerCase()));
        }
        Combat.adaptability(attacker, defender, distance, adjacent);
        setChanged();
        notifyObservers();
    }

    /**
     * Assassin ability; deals double damage (from behind, but there is currently no way to check for that)
     * @param attackerName Name of the attacker; should be an Assassin
     * @param defenderName Name of the defender
     * @param distance Distance between units
     */
    public void backstab(String attackerName, String defenderName, int distance) {
        Unit attacker = units.get(attackerName.toLowerCase());
        Unit defender = units.get(defenderName.toLowerCase());
        Combat.backstab(attacker, defender, distance);
        setChanged();
        notifyObservers();
    }

    /**
     * Sorcerer Fire Tome ability; deals splash damage around main target in a 9-block area.
     * Each affected unit takes half damage
     * @param attackerName Name of the attacker; should be a Sorcerer with Fire Tome equipped
     * @param targetNames List of names of the targets; first target is able to retaliate
     * @param distance Distance between the attacker and primary target
     */
    public void supernova(String attackerName, List<String> targetNames, int distance) {
        Unit attacker = units.get(attackerName.toLowerCase());
        List<Unit> targets = new ArrayList<>();
        for (String name : targetNames) {
            targets.add(units.get(name.toLowerCase()));
        }
        Combat.supernova(attacker, targets, distance);
        setChanged();
        notifyObservers();
    }

    /**
     * Shaman ability; uses a Tome to empower a mace strike.
     * Increases damage by the Tome's magic stat.
     * @param attackerName Name of the attacker; should be a Shaman with a Mace equipped
     * @param tomeName Name of the Tome the attacker will be using to empower the mace strike
     * @param defenderName Name of the defender
     * @param distance Distance between attacker and defender
     */
    public void empoweredStrike(String attackerName, String tomeName, String defenderName, int distance) {
        Unit attacker = units.get(attackerName.toLowerCase());
        Weapon tome = attacker.getInventory().get(tomeName.toLowerCase());
        Unit defender = units.get(defenderName.toLowerCase());
        Combat.empoweredStrike(attacker, tome, defender, distance);
        setChanged();
        notifyObservers();
    }

    // End class abilities

    /**
     * Returns a string listing all of the players and their health
     *
     * @return String listing all players with their health
     */
    public String playersToString() {
        String playersList = "==================== ALL PLAYERS ====================\n";
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            Player player = entry.getValue();
            playersList += String.format("%-30s", player.getName());
            playersList += "(" + player.getHp() + "/" + player.getMaxhp() + " hp)\n";
        }
        return playersList;
    }

    /**
     * Returns a string listing all of the enemies and their health
     *
     * @return String listing all enemies with their health
     */
    public String enemiesToString() {
        String enemiesList = "==================== ALL ENEMIES ====================\n";
        for (Map.Entry<String, Enemy> entry : enemies.entrySet()) {
            Enemy enemy = entry.getValue();
            enemiesList += String.format("%-30s", enemy.getName());
            enemiesList += "(" + enemy.getHp() + "/" + enemy.getMaxhp() + " hp)\n";
        }
        return enemiesList;
    }

    /**
     * Tells whether or not the players Map contains a certain key (i.e. name)
     *
     * @param playerName Name of the player to look for
     * @return True if the player is in the players Map, false if not
     */
    public boolean hasPlayer(String playerName) {
        return this.players.containsKey(playerName.toLowerCase());
    }

    /**
     * Tells whether or not the enemies Map contains a certain key (i.e. name)
     *
     * @param enemyName Name of the enemy to look for
     * @return True if the enemy is in the enemies Map, false if not
     */
    public boolean hasEnemy(String enemyName) {
        return this.enemies.containsKey(enemyName.toLowerCase());
    }

    /**
     * Returns a player by name
     *
     * @param playerName Name of the player to retrieve
     * @return The player object from the Map
     */
    public Player getPlayer(String playerName) {
        return this.players.get(playerName.toLowerCase());
    }

    /**
     * Returns an enemy by name
     *
     * @param enemyName Name of the enemy to retrieve
     * @return The enemy object from the Map
     */
    public Enemy getEnemy(String enemyName) {
        return this.enemies.get(enemyName.toLowerCase());
    }

    /**
     * Restores all players to full health
     */
    public void healAll() {
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            Player player = entry.getValue();
            player.setHp(player.getMaxhp());
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Gives a player a specified amount of gold
     * @param playerName Name of the player to give gold to
     * @param gold How much gold to give
     */
    public void giveGold(String playerName, int gold) {
        Player player = players.get(playerName.toLowerCase());
        player.setGold(player.getGold() + gold);
        System.out.println(player.getName() + " has been given " + gold + " gold. Total: " + player.getGold());
        setChanged();
        notifyObservers();
    }

    /**
     * Saves player data to the players.txt file
     */
    public void save() {
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
        setChanged();
        notifyObservers();
    }

    /**
     * Uses a player's gold to buy a weapon
     * @param playerName Name of the player purchasing a weapon
     * @param weaponName Name of the weapon being purchased
     */
    public void buy(String playerName, String weaponName) {
        Weapon weapon = new Weapon(weapons.get(weaponName.toLowerCase()));
        Player player = players.get(playerName);
        if (player.getGold() < weapon.getCost()) {
            System.out.println("Not enough gold!");
            return;
        }
        player.setGold(player.getGold() - weapon.getCost());
        player.getInventory().put(weaponName.toLowerCase(), weapon);
        setChanged();
        notifyObservers();
    }

    // Simple getters

    /**
     * Returns the list of players
     * @return TreeMap of players
     */
    public TreeMap<String, Player> getPlayers() { return this.players; }

    /**
     * Returns the list of enemies
     * @return TreeMap of enemies
     */
    public TreeMap<String, Enemy> getEnemies() { return this.enemies; }

    /**
     * Return  the list of all units
     * @return TreeMap of all units
     */
    public TreeMap<String, Unit> getUnits() { return units; }

    /**
     * Return the list of weapons
     * @return TreeMap of weapons
     */
    public TreeMap<String, Weapon> getWeapons() { return weapons; }

    /**
     * Returns the path to the weapons file
     * @return Path to weapons file
     */
    public String getWeaponsFile() { return weaponsFile; }

    /**
     * Returns the path to the enemies file
     * @return Path to enemies file
     */
    public String getEnemiesFile() { return enemiesFile; }

    /**
     * Returns the path to the players file
     * @return Path to players file
     */
    public String getPlayersFile() { return playersFile; }
}
