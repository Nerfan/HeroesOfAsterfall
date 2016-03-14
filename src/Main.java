import Mechanics.Combat;
import Mechanics.Heal;
import Mechanics.LevelUp;
import Units.Enemy;
import Units.Player;
import Units.Unit;
import Units.Weapon;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.Document;

/**
 * The main file to run that runs the game itself
 */
public class Main implements ActionListener {

    private static final String weaponsFile = "data/weapons.txt";
    private static final String enemiesFile = "data/level1.txt";
    private static final String playersFile = "data/players.txt";
    private static TreeMap<String, Enemy> enemies = new TreeMap<>();
    private static TreeMap<String, Player> players = new TreeMap<>();

    // All variables made necessary by the GUI
    private static JButton playersButton, enemiesButton, attackButton, healButton, saveButton, oneButton, twoButton;
    private static JTextArea output;
    private static boolean inCombat = false, inHealing = false;
    private static Unit attacker = null, defender = null, healer = null, patient = null;
    private static int range;

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
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gui.setSize(1280, 720);
        gui.setVisible(true);
    }

    /**
     * Constructs the GUI and returns it as an object
     * Everything that affects how the GUI looks goes in here
     * @return the GUI
     */
    public JPanel createContentPane() {
        JPanel bottom = new JPanel();
        bottom.setLayout(null);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(null);
        textPanel.setLocation(10, 0);
        textPanel.setSize(600, 720);
        bottom.add(textPanel);

        // Retruns the text that would normally be sent to System.out
        output = new JTextArea("");
        output.setLocation(0, 0);
        output.setFont(new Font("monospaced", Font.PLAIN, 12));
        output.setSize(600, 720);
        output.setForeground(Color.black);
        output.setEditable(false);
        textPanel.add(output);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setLocation(700, 50);
        buttonPanel.setSize(400, 670);
        bottom.add(buttonPanel);

        playersButton = new JButton("Players");
        playersButton.setSize(120, 30);
        playersButton.setLocation(0, 0);
        playersButton.addActionListener(this);
        buttonPanel.add(playersButton);

        int yPos = 50;
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            JButton temp = new JButton(entry.getValue().getName());
            temp.setSize(120, 30);
            temp.setLocation(0, yPos);
            temp.addActionListener(e -> {
                if (inCombat) {
                    if (attacker == null) {
                        attacker = entry.getValue();
                    } else if (defender == null) {
                        defender = entry.getValue();
                    }
                    if (!(attacker == null || defender == null || range == 0)) {
                        Combat.combat(attacker, defender, range);
                        System.out.println();
                        attacker = null;
                        defender = null;
                        range = 0;
                        inCombat = false;
                    }
                } else if (inHealing) {
                    if (healer == null) {
                        healer = entry.getValue();
                    } else if (patient == null) {
                        patient = entry.getValue();
                    }
                    if (!(healer == null || patient == null)) {
                        Heal.heal(healer, patient);
                        System.out.println();
                        healer = null;
                        patient = null;
                        inHealing = false;
                    }
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
        enemiesButton.addActionListener(this);
        buttonPanel.add(enemiesButton);

        yPos = 50;
        for (Map.Entry<String, Enemy> entry : enemies.entrySet()) {
            JButton temp = new JButton(entry.getValue().getName());
            temp.setSize(120, 30);
            temp.setLocation(250, yPos);
            temp.addActionListener(e -> {
                if (inCombat) {
                    if (attacker == null) {
                        attacker = entry.getValue();
                    } else if (defender == null) {
                        defender = entry.getValue();
                    }
                    if (!(attacker == null || defender == null || range == 0)) {
                        Combat.combat(attacker, defender, range);
                        System.out.println();
                        attacker = null;
                        defender = null;
                        range = 0;
                        inCombat = false;
                    }
                } else if (inHealing) {
                    if (healer == null) {
                        healer = entry.getValue();
                    } else if (patient == null) {
                        patient = entry.getValue();
                    }
                    if (!(healer == null || patient == null)) {
                        Heal.heal(healer, patient);
                        System.out.println();
                        healer = null;
                        patient = null;
                        inHealing = false;
                    }
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
        attackButton.addActionListener(this);
        buttonPanel.add(attackButton);

        oneButton = new JButton("1");
        oneButton.setSize(60, 30);
        oneButton.setLocation(0, 550);
        oneButton.addActionListener(this);
        buttonPanel.add(oneButton);

        twoButton = new JButton("2");
        twoButton.setSize(60, 30);
        twoButton.setLocation(60, 550);
        twoButton.addActionListener(this);
        buttonPanel.add(twoButton);

        healButton = new JButton("Heal");
        healButton.setSize(120, 30);
        healButton.setLocation(250, 500);
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
     * Governs what happens when button are pressed, excluding the buttons for individual Units.
     * The Unit actions are handled with lambdas when they are constructed in createContentPane().
     * @param e The action, cased by a button press in this case
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
            if (inHealing) {
                inHealing = false;
                System.out.println("Healing cancelled.\n");
            }
            if (!inCombat) {
                inCombat = true;
                System.out.println("Waiting for attacker/defender/range...");
            } else {
                inCombat = false;
                System.out.println("Combat cancelled.");
            }
        } else if (e.getSource() == oneButton) {
            if (inCombat) {
                range = 1;
                if (!(attacker == null || defender == null)) {
                    Combat.combat(attacker, defender, range);
                    System.out.println();
                    attacker = null;
                    defender = null;
                    range = 0;
                    inCombat = false;
                }
            }
            return;
        } else if (e.getSource() == twoButton) {
            if (inCombat) {
                range = 2;
                if (!(attacker == null || defender == null)) {
                    Combat.combat(attacker, defender, range);
                    System.out.println();
                    attacker = null;
                    defender = null;
                    range = 0;
                    inCombat = false;
                }
            }
            return;
        } else if (e.getSource() == healButton) {
            if (inCombat) {
                inCombat = false;
                System.out.println("Combat cancelled.\n");
            }
            if (!inHealing) {
                inHealing = true;
                System.out.println("Waiting for healer/patient...");
            } else {
                inHealing = false;
                System.out.println("Healing cancelled.");
            }
        } else if (e.getSource() == saveButton) {
            save();
        }
        System.out.println();
    }

    /**
     * Does everything needed to start the game.
     * Populates TreeMaps with players, enemies, weapons, etc.
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
                TreeMap<String, Weapon> inventory = new TreeMap<>();
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

    // These two at the bottom redirect System.out to the GUI.
    // I don't know how they work, but they work, so I'm fine with that.

    private void updateTextPane(final String text) {
        SwingUtilities.invokeLater(() -> {
            Document doc = output.getDocument();
            try {
                doc.insertString(doc.getLength(), text, null);
                while (doc.getText(0, doc.getLength()).split("\n").length >= 47) {
                    doc.remove(0, 1);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
            output.setCaretPosition(doc.getLength() - 1);
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
