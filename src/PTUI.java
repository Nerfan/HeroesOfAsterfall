import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Plain text UI for the game
 *
 * @author Jeremy Lefurge
 */
public class PTUI implements Observer {

    /** The underlying model */
    private HoAModel model;

    @Override
    public void update(Observable observable, Object o) {
        // Does nothing; nothing needs to update in the PTUI
    }

    /**
     * Prompts in console for input files, then takes those and makes a new game model with them
     * Also adds self as an observer, though this doesn't really mean anything as far as functionality goes
     */
    private PTUI() {
        Scanner console = new Scanner(System.in);

        System.out.print("File for players? (No input defaults to data/players.txt) ");
        String playersFile = console.nextLine();
        if (playersFile.equals("")) {
            playersFile = "data/players.txt";
        }

        System.out.print("File for enemies? (No input defaults to data/level1.txt) ");
        String enemiesFile = console.nextLine();
        if (enemiesFile.equals("")) {
            enemiesFile = "data/level1.txt";
        }

        System.out.print("File for weapons? (No input defaults to data/weapons.txt) ");
        String weaponsFile = console.nextLine();
        if (weaponsFile.equals("")) {
            weaponsFile = "data/weapons.txt";
        }

        this.model = new HoAModel(playersFile, enemiesFile, weaponsFile);
        this.model.addObserver(this);
    }

    private void repl() {
        while (true) {
            Scanner console = new Scanner(System.in);
            System.out.printf("Enter command: ");
            String[] tokens = console.nextLine().split("\\s+");
            String cmd = tokens[0].toLowerCase();

            // Handles simple commands
            switch (cmd) {
                case ("attack"):    // Sets up combat
                    // These need to be initialized up here to keep Java happy
                    String attacker = "";
                    String defender = "";
                    int distance = 1;

                    if (tokens.length == 4) {
                        try {
                            attacker = tokens[1];
                            defender = tokens[2];
                            distance = Integer.parseInt(tokens[3]);
                        }
                        catch (Exception ex) {
                            System.out.println("Error: " + ex);
                        }
                    } else {
                        System.out.printf("Attacker: ");
                        attacker = console.next();
                        System.out.printf("Defender: ");
                        defender = console.next();
                        System.out.printf("Distance: ");
                        distance = console.nextInt();
                    }
                    model.combat(attacker, defender, distance);
                    break;

                case ("heal"):      // Sets up healing
                    String healer;
                    String recipient;

                    if (tokens.length == 3) {
                        healer = tokens[1];
                        recipient = tokens[2];
                    } else {
                        System.out.printf("Healer: ");
                        healer = console.next();
                        System.out.printf("Recipient: ");
                        recipient = console.next();
                    }
                    model.heal(healer, recipient);
                    break;

                case ("players"):   // Prints all players and their hp
                    System.out.print(model.playersToString());
                    break;

                case("enemies"):    // Prints all enemies and their hp
                    System.out.print(model.enemiesToString());
                    break;

                case ("save"):      // Save players to a file
                    model.save();
                    break;

                case ("healall"):   // Fully heal all players
                    model.healAll();
                    break;

                case ("help"):      // Display all commands
                    System.out.println("Commands:\n" +
                            "attack\n" +
                            "heal\n" +
                            "players\n" +
                            "enemies\n" +
                            "save\n" +
                            "healall\n" +
                            "quit\n");

                case ("quit"):      // Exit the game
                    System.exit(0);
            }

            // Handles if the command is a player
            if (this.model.hasPlayer(cmd)) {
                System.out.println(model.getPlayer(cmd));
                if (tokens.length != 1) {
                    switch (tokens[1]) {
                        case ("-s"):
                            System.out.print("What weapon would you like to switch to? ");
                            Scanner scanner = new Scanner(System.in);
                            String weaponName = scanner.nextLine();
                            this.model.switchWeapon(cmd, weaponName);
                            break;
                        case ("-i"):
                            System.out.println(this.model.getPlayer(cmd).inventoryToString());
                            break;
                    }
                }
            }

            // Handles if the command is an enemy
            if (this.model.hasEnemy(cmd)) {
                System.out.println(this.model.getEnemy(cmd));
            }

            System.out.println();
        } // Ends the while loop
    }

    public static void main(String[] args) {
        PTUI game = new PTUI();
        game.repl();
    }
}
