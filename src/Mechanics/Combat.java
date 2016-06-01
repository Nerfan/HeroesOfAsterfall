package Mechanics;

import Units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles everything related to dealing damage
 * @author Jeremy Lefurge
 */
public class Combat {

    /**
     * Goes through the motions of combat for two units
     * @param attacker  The unit attacking
     * @param defender  The unit defending
     */
    public static void combat(Unit attacker, Unit defender, int distance) {
        try { // Make sure things don't crash
            if (attacker.getAttackType().equals("heal")) { // Check for healing item
                System.out.println("Error: Healing item equipped");
                return;
            } else if (attacker.getHp() <= 0) { // Make sure the attacker is alive
                System.out.println(attacker.getName() + " is dead!");
                return;
            } else if (defender.getHp() <= 0) { // Make sure the defender is alive
                System.out.println(defender.getName() + " is dead!");
                return;
            } else if (!attacker.inRange(distance)) {
                System.out.println("Out of range!");
                return;
            }

            // Blademaster ability
            boolean alreadyHit = false;
            if (defender.isRole("Blademaster")) {
                damage(defender, attacker);
                alreadyHit = true;
            }

            damage(attacker, defender);

            if (defender.getAttackType().equals("heal") || defender.getHp() <= 0 || !defender.inRange(distance)) {
                System.out.println("No retaliation from " + defender.getName() + "!");
            } else if (!alreadyHit) {
                damage(defender, attacker);
            }

            System.out.println(attacker.getName() + " has " + attacker.getHp() + " hp remaining.");
            System.out.println(defender.getName() + " has " + defender.getHp() + " hp remaining.");
        }

        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    /**
     * Default case for damage; multiplier of 1
     * @param attacker Unit dealing damage
     * @param defender Unit taking damage
     */
    private static void damage(Unit attacker, Unit defender) {
        damage(attacker, defender, 1, true, true);
    }

    private static void damage(Unit attacker, Unit defender, double multiplier) {
        damage(attacker, defender, multiplier, true, true);
    }

    /**
     * Deals the damage to units
     * @param attacker Unit dealing damage
     * @param defender Unit taking damage
     * @param multiplier For anytime nonstandard damage is dealt (e.g. a backstab has a 2x multiplier)
     */
    private static void damage(Unit attacker, Unit defender, double multiplier, boolean xpGain, boolean durabilityUse) {
        Random rng = new Random();  // Initialize the rng
        int roll = rng.nextInt(100);
        int damage = 0;             // Initialize the damage number

        if (roll > (attacker.getAccuracy())-defender.getDodge()) { // Miss chance
            System.out.println(attacker.getName() + " missed!");
        } else {

            // Gets the type of attack and calculates damage
            if (attacker.getAttackType().equals("phys")) {
                if (attacker.isRole("Monk")) {
                    damage = attacker.physDamage() - (defender.getDefense()/2);
                } else {
                    damage = attacker.physDamage() - defender.getDefense();
                }
            } else if (attacker.getAttackType().equals("mag")) {
                if (attacker.isRole("Monk")) {
                    damage = attacker.magDamage() - (defender.getRes()/2);
                } else {
                    damage = attacker.magDamage() - defender.getRes();
                }
            }

            // Blademaster ability
            if (attacker.isRole("Blademaster") && (attacker.getSpd() >= defender.getSpd()+5)) {
                damage *= 2;
                System.out.print("Double strike! ");
            }

            // Roll for crit
            if (roll < attacker.getMastery()) {
                damage *= 3;
                System.out.print("Critical hit! ");
            } else if (attacker.isRole("Gladiator") && (roll < attacker.getMastery()*1.3)) {
                damage *= 3;
                System.out.print("Critical hit! ");
            }

            // Paladin ability
            if (rng.nextInt(100) < 30) {
                damage /= 2;
            }

            // Make sure damage is not less than 0
            if (damage < 0) {
                damage = 0;
            }

            // Multiply the damage by the multiplier
            damage *= multiplier;

            // Actually deal the damage
            defender.takeDamage(damage);
            System.out.print(attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + "! ");

            // Sorcerer abilities
            if (attacker.isRole("Sorcerer") && attacker.getEquipped().getName().equals("Dark Tome")) {
                attacker.heal(damage/2);
                System.out.print(attacker.getName() + " absorbed " + damage/2 + "health. ");
            } else if (attacker.isRole("Sorcerer") && attacker.getEquipped().getName().equals("Light Tome")) {
                defender.blind();
                System.out.print(defender.getName() + " was blinded! ");
            }

            // Award xp
            if (xpGain) {
                attacker.increaseXP(1);
            } else {
                System.out.println(); // Substitute for the new line in the increaseXP method
            }
            if (durabilityUse) {
                attacker.useDurability();
            }

            // If the defender dies
            if (defender.getHp() == 0) {
                System.out.print(defender.getName() + " has been killed! ");
                attacker.increaseXP(2); // Award xp
            }
        }
    }

    /**
     * Marksman ability; deals half damage to up to four targets in range
     * @param attacker Unit performing hte ability; should be a player of the Marksman class
     * @param targets  List of units getting hit; max size of 4
     */
    public static void multiShot(Unit attacker, List<Unit> targets) {
        try {
            if (!attacker.isRole("Marksman")) {
                System.out.println("Error: Multi-Shot is a Marksman ability.");
                return;
            } else if (targets.size() > 4) {
                System.out.println("Error: Multi-Shot can hit at most four targets.");
                return;
            }
            for (Unit target : targets) {
                damage(attacker, target, 0.5, false, false);
            }
            attacker.useDurability();
            attacker.increaseXP(1);
        }

        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Marksman ability; deals full damage to two targets in a line
     * @param attacker Unit attacking; should be a player with the Marksman class
     * @param targets  Units getting hit
     */
    public static void pierce(Unit attacker, List<Unit> targets) {
        try {
            if (!attacker.isRole("Marksman")) {
                System.out.println("Error: Pierce is a Marksman ability.");
                return;
            } else if (targets.size() > 2) {
                System.out.println("Error: Pierce can hit at most four targets.");
                return;
            }
            for (Unit target : targets) {
                damage(attacker, target, 1, false, false);
            }
            attacker.useDurability();
            attacker.increaseXP(1);
        }

        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Strategist ability; increases stats based on surrounding allies and then attacks
     * For each adjacent ally, the Strategist gains a temporary boost of 5 points in that ally's highest stat
     * TODO This can eventually be worked straight into the combat method
     * @param attacker Unit attacking; should be a Player of the Strategist class
     * @param defender Unit defending
     * @param distance Distance between attacker and defender
     * @param adjacent Any adjacent allies
     */
    public static void adaptability(Unit attacker, Unit defender, int distance, List<Unit> adjacent) {
        try {
            if (attacker.getAttackType().equals("heal")) { // Check for healing item
                System.out.println("Error: Healing item equipped");
                return;
            } else if (attacker.getHp() <= 0) { // Make sure the attacker is alive
                System.out.println(attacker.getName() + " is dead!");
                return;
            } else if (defender.getHp() <= 0) { // Make sure the defender is alive
                System.out.println(defender.getName() + " is dead!");
                return;
            } else if (!attacker.inRange(distance)) {
                System.out.println("Out of range!");
                return;
            } else if (!attacker.isRole("Strategist")) {
                System.out.println("Error: Adaptability is a Strategist ability.");
                return;
            }
            ArrayList<String> maxStats = new ArrayList<>();
            for (Unit ally : adjacent) {
                maxStats.add(ally.getHighestStat());
            }
            for (String stat : maxStats) {
                switch (stat) {
                    case "str":
                        attacker.setStr(attacker.getStr() + 5);
                        break;
                    case "mag":
                        attacker.setMag(attacker.getMag() + 5);
                        break;
                    case "skill":
                        attacker.setSkill(attacker.getSkill() + 5);
                        break;
                    case "spd":
                        attacker.setSpd(attacker.getSpd() + 5);
                        break;
                    case "defense":
                        attacker.setDefense(attacker.getDefense() + 5);
                        break;
                    case "res":
                        attacker.setRes(attacker.getRes() + 5);
                        break;
                    case "mastery":
                        attacker.setMastery(attacker.getMastery() + 5);
                        break;
                }
            }
            combat(attacker, defender, distance);
            for (String stat : maxStats) {
                switch (stat) {
                    case "str":
                        attacker.setStr(attacker.getStr() - 5);
                        break;
                    case "mag":
                        attacker.setMag(attacker.getMag() - 5);
                        break;
                    case "skill":
                        attacker.setSkill(attacker.getSkill() - 5);
                        break;
                    case "spd":
                        attacker.setSpd(attacker.getSpd() - 5);
                        break;
                    case "defense":
                        attacker.setDefense(attacker.getDefense() - 5);
                        break;
                    case "res":
                        attacker.setRes(attacker.getRes() - 5);
                        break;
                    case "mastery":
                        attacker.setMastery(attacker.getMastery() - 5);
                        break;
                }
            }
        }

        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Assassin ability; deals double damage from behind (no current way to check for position)
     * @param attacker Unit attacking; should be a player of the assassin class
     * @param defender Unit being hit
     * @param distance Distance between units
     */
    public static void backstab(Unit attacker, Unit defender, int distance) {
        try {
            if (attacker.getAttackType().equals("heal")) { // Check for healing item
                System.out.println("Error: Healing item equipped");
                return;
            } else if (attacker.getHp() <= 0) { // Make sure the attacker is alive
                System.out.println(attacker.getName() + " is dead!");
                return;
            } else if (defender.getHp() <= 0) { // Make sure the defender is alive
                System.out.println(defender.getName() + " is dead!");
                return;
            } else if (!attacker.inRange(distance)) {
                System.out.println("Out of range!");
                return;
            } else if (!attacker.isRole("Assassin")) {
                System.out.println("Error: Backstab is an Assassin ability");
                return;
            }

            damage(attacker, defender, 2);

            if (defender.getAttackType().equals("heal") || defender.getHp() <= 0 || !defender.inRange(distance)) {
                System.out.println("No retaliation from " + defender.getName() + "!");
            } else {
                damage(defender, attacker);
            }

            System.out.println(attacker.getName() + " has " + attacker.getHp() + " hp remaining.");
            System.out.println(defender.getName() + " has " + defender.getHp() + " hp remaining.");
        }

        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void supernova(Unit attacker, List<Unit> targets) {
        try {
            if (!attacker.isRole("Sorcerer")) {
                System.out.println("Error: Supernova is a Sorcerer ability.");
                return;
            } else if (!attacker.getEquipped().getName().equals("Fire Tome")) {
                System.out.println("Error: Fire Tome not equipped.");
                return;
            } else if (attacker.getHp() <= 0) { // Make sure the attacker is alive
                System.out.println(attacker.getName() + " is dead!");
                return;
            } else if (targets.size() > 9) {
                System.out.println("Too many targets.");
                return;
            }
            for (Unit target : targets) {
                damage(attacker, target, 0.5, false, false);
            }
            attacker.useDurability(3);
            attacker.increaseXP(1);
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
