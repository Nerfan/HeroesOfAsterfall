package Mechanics;

import Units.Player;

import java.util.Random;
import java.util.TreeMap;

/**
 * Created by jeremy on 3/2/16.
 */
public class LevelUp {
    private String name;
    private int hpChance;
    private int strChance;
    private int magChance;
    private int skillChance;
    private int spdChance;
    private int defenseChance;
    private int resChance;
    private int masteryChance;

    private static TreeMap<String, LevelUp> levelUps = new TreeMap<>();

    public LevelUp(String name, int hpChance, int strChance, int magChance, int skillChance, int spdChance,
                   int defenseChance, int resChance, int masteryChance) {
        this.name = name;
        this.hpChance = hpChance;
        this.strChance = strChance;
        this.magChance = magChance;
        this.skillChance = skillChance;
        this.spdChance = spdChance;
        this.defenseChance = defenseChance;
        this.resChance = resChance;
        this.masteryChance = masteryChance;
    }

    public static void levelUp(Player player) {
        Random rng = new Random();
        LevelUp levelUpStats = levelUps.get(player.getRole());
        String statsUpString = "";
        if (rng.nextInt(100) < levelUpStats.hpChance) {
            player.setHp(player.getHp() + 1);
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

    public static void init() {
        levelUps.put("Acolyte", new LevelUp("Acolyte", 50, 10, 80, 70, 50, 20, 70, 10));
        levelUps.put("Adept", new LevelUp("Adept", 60, 50, 50, 70, 70, 30, 30, 15));
        levelUps.put("Nomad", new LevelUp("Nomad", 65, 60, 20, 75, 75, 40, 25, 10));
        levelUps.put("Hunter", new LevelUp("Hunter", 50, 60, 10, 80, 80, 30, 40, 15));
        levelUps.put("Warrior", new LevelUp("Warrior", 70, 70, 30, 40, 45, 70, 30, 10));
    }
}
