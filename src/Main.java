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
import java.util.TreeMap;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.Document;

/**
 * Created by jeremy on 3/5/16.
 */
public class Main implements ActionListener {

    private static final String weaponsFile = "data/weapons.txt";
    private static final String enemiesFile = "data/level1.txt";
    private static final String playersFile = "data/players.txt";
    private static TreeMap<String, Enemy> enemies = new TreeMap<>();
    private static TreeMap<String, Player> players = new TreeMap<>();
    private static TreeMap<String, Unit> units = new TreeMap<>();

    // All variables that were made necessary by the GUI
    private static JPanel textPanel, buttonPanel;
    private static JButton playersButton, enemiesButton, attackButton, healButton, saveButton, oneButton, twoButton;
    private static JTextArea output;
    private static boolean waitingForUnit1 = false, waitingForUnit2 = false, waitingForRange = false;
    private static Unit unit1 = null, unit2 = null;
    private static int range;

    public JPanel createContentPane() {
        JPanel bottom = new JPanel();
        bottom.setLayout(null);

        textPanel = new JPanel();
        textPanel.setLayout(null);
        textPanel.setLocation(10, 0);
        textPanel.setSize(600, 720);
        bottom.add(textPanel);

        output = new JTextArea("");
        output.setLocation(0, 0);
        output.setFont(new Font("monospaced", Font.PLAIN, 12));
        output.setSize(600, 720);
        output.setForeground(Color.black);
        textPanel.add(output);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setLocation(700, 50);
        buttonPanel.setSize(400, 670);
        bottom.add(buttonPanel);

        playersButton = new JButton("Players");
        playersButton.setSize(120, 30);
        playersButton.setLocation(0, 0);
        playersButton.setHorizontalAlignment(0);
        playersButton.addActionListener(this);
        buttonPanel.add(playersButton);

        int yPos = 50;
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            JButton temp = new JButton(entry.getValue().getName());
            temp.setSize(120, 30);
            temp.setLocation(0, yPos);
            temp.addActionListener(e -> {
                if (waitingForUnit1) {
                    unit1 = entry.getValue();
                    waitingForUnit1 = false;
                } else if (waitingForUnit2) {
                    unit2 = entry.getValue();
                    waitingForUnit2 = false;
                } else {
                    System.out.println(entry.getValue());
                    System.out.println();
                }
            });
            buttonPanel.add(temp);
            yPos += 40;
        }

        enemiesButton = new JButton("Enemies");
        enemiesButton.setSize(120, 30);
        enemiesButton.setLocation(250, 0);
        enemiesButton.setHorizontalAlignment(0);
        enemiesButton.addActionListener(this);
        buttonPanel.add(enemiesButton);

        yPos = 50;
        for (Map.Entry<String, Enemy> entry : enemies.entrySet()) {
            JButton temp = new JButton(entry.getValue().getName());
            temp.setSize(120, 30);
            temp.setLocation(250, yPos);
            temp.addActionListener(e -> {
                if (waitingForUnit1) {
                    unit1 = entry.getValue();
                    waitingForUnit1 = false;
                } else if (waitingForUnit2) {
                    unit2 = entry.getValue();
                    waitingForUnit2 = false;
                } else {
                    System.out.println(entry.getValue());
                    System.out.println();
                }
            });
            buttonPanel.add(temp);
            yPos += 40;
        }

        attackButton = new JButton("Attack");
        attackButton.setSize(120, 30);
        attackButton.setLocation(0, 500);
        attackButton.setHorizontalAlignment(0);
        attackButton.addActionListener(this);
        buttonPanel.add(attackButton);

        oneButton = new JButton("1");
        oneButton.setSize(60, 30);
        oneButton.setLocation(0, 550);
        oneButton.setHorizontalAlignment(0);
        oneButton.addActionListener(this);
        buttonPanel.add(oneButton);

        twoButton = new JButton("2");
        twoButton.setSize(60, 30);
        twoButton.setLocation(60, 550);
        twoButton.setHorizontalAlignment(0);
        twoButton.addActionListener(this);
        buttonPanel.add(twoButton);

        healButton = new JButton("Heal");
        healButton.setSize(120, 30);
        healButton.setLocation(250, 500);
        healButton.setHorizontalAlignment(0);
        healButton.addActionListener(this);
        buttonPanel.add(healButton);

        saveButton = new JButton("Save");
        saveButton.setSize(120, 30);
        saveButton.setLocation(250, 550);
        saveButton.addActionListener(this);
        buttonPanel.add(saveButton);

        bottom.setOpaque(true);

        return bottom;
    }


    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playersButton) {
            System.out.println("==================== ALL PLAYERS ====================");
            for (Map.Entry<String, Player> entry : players.entrySet()) {
                Player player = entry.getValue();
                System.out.printf("%-30s", player.getName());
                System.out.println("(" + player.getHp() + "/" + player.getMaxhp() + " hp)");
            }
        } else if (e.getSource() == enemiesButton) {
            System.out.println("==================== ALL ENEMIES ====================");
            for (Map.Entry<String, Enemy> entry : enemies.entrySet()) {
                Enemy enemy = entry.getValue();
                System.out.printf("%-30s", enemy.getName());
                System.out.println("(" + enemy.getHp() + "/" + enemy.getMaxhp() + " hp)");
            }
        } else if (e.getSource() == attackButton) {
            if ((unit1 == null) && (unit2 == null) && !waitingForUnit1 && !waitingForUnit2) {
                waitingForUnit1 = true;
                waitingForUnit2 = true;
                waitingForRange = true;
                System.out.println("Waiting for attacker/defender/range...");
            } else if (!((unit1 == null) || (unit2 == null)) && (range != 0)) {
                Combat.combat(unit1, unit2, range);
                unit1 = null;
                unit2 = null;
            } else if (waitingForUnit1 || waitingForUnit2 || waitingForRange){
                unit1 = null;
                unit2 = null;
                range = 0;
                waitingForUnit1 = false;
                waitingForUnit2 = false;
                waitingForRange = false;
                System.out.println("Combat cancelled.");
            }
        } else if (e.getSource() == oneButton && waitingForRange) {
            range = 1;
            waitingForRange = false;
        } else if (e.getSource() == twoButton && waitingForRange) {
            range = 2;
            waitingForRange = false;
        } else if (e.getSource() == healButton) {
            if ((unit1 == null) && (unit2 == null) && !waitingForUnit1 && !waitingForUnit2) {
                waitingForUnit1 = true;
                waitingForUnit2 = true;
                System.out.println("Waiting for healer/recipient...");
            } else if (!((unit1 == null) || (unit2 == null))) {
                Heal.heal(unit1, unit2);
                unit1 = null;
                unit2 = null;
            } else if (waitingForUnit1 || waitingForUnit2){
                unit1 = null;
                unit2 = null;
                waitingForUnit1 = false;
                waitingForUnit2 = false;
                System.out.println("Healing cancelled.");
            }
        } else if (e.getSource() == saveButton) {
            save();
        }
        System.out.println();
    }

    /**
     * The main function.
     * Calls init() to begin with.
     * Constructs a GUI based on the rest of this file and does everything in there.
     */
    public static void main(String[] args) {
        init();
        Main demo = new Main();
        demo.redirectSystemStreams();

        JFrame gui = new JFrame("Heroes of Asterfall");
        gui.setLocation(50, 25);
        gui.setContentPane(demo.createContentPane());
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(1280, 720);
        gui.setVisible(true);
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
                        new Weapon(firstlist[0],                   // Type of weapon (e.g. Sword)
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
                                new Weapon(list[10],    // Equipped weapon; initializes all stats to 0
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

    private void updateTextPane(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Document doc = output.getDocument();
                try {
                    doc.insertString(doc.getLength(), text, null);
                    while (doc.getText(0, doc.getLength()).split("\n").length >= 47) {
                        doc.remove(0, 1);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                output.setCaretPosition(doc.getLength() - 1);
            }
        });
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                updateTextPane(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextPane(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }
}
