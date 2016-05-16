package Mechanics;

import Units.Player;

import java.util.Random;
import java.util.TreeMap;

/**
 * Simply creates class objects to hold probabilities of a stat increasing upon a leveling up.
 * Each player class has their own LevelUp object created in the init() method.
 * init() should be called by the main function during the initialization routine.
 * @author Jeremy Lefurge
 */
public class LevelUp {
    // These are all pretty self-explanatory. The exact number is the percent chance of an increase.
    private int hpChance;
    private int strChance;
    private int magChance;
    private int skillChance;
    private int spdChance;
    private int defenseChance;
    private int resChance;
    private int masteryChance;
    // Used to store stat increase chances
    private static TreeMap<String, LevelUp> levelUps = new TreeMap<>();

    /**
     * Constructor
     * All of the parameters are percent chances for the stat to increase upon leveling up
     * @param hpChance      Max hp
     * @param strChance     Strength
     * @param magChance     Magic
     * @param skillChance   Skill
     * @param spdChance     Speed
     * @param defenseChance Defense
     * @param resChance     Resistance
     * @param masteryChance Mastery
     */
    public LevelUp(int hpChance, int strChance, int magChance, int skillChance, int spdChance,
                   int defenseChance, int resChance, int masteryChance) {
        this.hpChance = hpChance;
        this.strChance = strChance;
        this.magChance = magChance;
        this.skillChance = skillChance;
        this.spdChance = spdChance;
        this.defenseChance = defenseChance;
        this.resChance = resChance;
        this.masteryChance = masteryChance;
    }

    /**
     * Levels up a player, increasing their stats according to the percent chances
     * @param player    Player object to level up
     */
    public static void levelUp(Player player) {
        Random rng = new Random();
        LevelUp levelUpStats = levelUps.get(player.getRole());
        String statsUpString = player.getName() + " leveled up! ";
        if (rng.nextInt(100) < levelUpStats.hpChance) {
            player.setMaxhp(player.getMaxhp() + 1);
            statsUpString += "Max HP up! ";
        }
        if (rng.nextInt(100) < levelUpStats.strChance) {
            player.setStr(player.getStr() + 1);
            statsUpString += "Strength up! ";
        }
        if (rng.nextInt(100) < levelUpStats.magChance) {
            player.setMag(player.getMag() + 1);
            statsUpString += "Magic up! ";
        }
        if (rng.nextInt(100) < levelUpStats.skillChance) {
            player.setSkill(player.getSkill() + 1);
            statsUpString += "Skill up! ";
        }
        if (rng.nextInt(100) < levelUpStats.spdChance) {
            player.setSpd(player.getSpd() + 1);
            statsUpString += "Speed up! ";
        }
        if (rng.nextInt(100) < levelUpStats.defenseChance) {
            player.setDefense(player.getDefense() + 1);
            statsUpString += "Defense up! ";
        }
        if (rng.nextInt(100) < levelUpStats.resChance) {
            player.setRes(player.getRes() + 1);
            statsUpString += "Resistance up! ";
        }
        if (rng.nextInt(100) < levelUpStats.masteryChance) {
            player.setMastery(player.getMastery() + 1);
            statsUpString += "Mastery up! ";
        }
        System.out.println(statsUpString);
    }

    /**
     * Creates the LevelUp classes for each player role/class
     */
    public static void init() {
        levelUps.put("Acolyte", new LevelUp(50, 10, 80, 70, 50, 20, 70, 10));
        levelUps.put("Adept", new LevelUp(60, 50, 50, 70, 70, 30, 30, 15));
        levelUps.put("Nomad", new LevelUp(65, 60, 20, 75, 75, 40, 25, 10));
        levelUps.put("Hunter", new LevelUp(50, 60, 10, 80, 80, 30, 40, 15));
        levelUps.put("Warrior", new LevelUp(70, 70, 30, 40, 45, 70, 30, 10));
    }
}
