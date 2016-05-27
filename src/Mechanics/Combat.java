package Mechanics;

import Units.Unit;

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
     * Deals the damage to units
     * @param attacker  The unit dealing damage
     * @param defender  The unit taking damage
     */
    private static void damage(Unit attacker, Unit defender) {
        Random rng = new Random();  // Initialize the rng
        int roll = rng.nextInt(100) + 1;
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

            // Make sure damage is not less than 0 or more than the remaining health of the defender
            if (damage < 0) {
                damage = 0;
            } else if (defender.getHp() < damage) {
                damage = defender.getHp();
            }

            // Actually deal the damage
            defender.takeDamage(damage);
            System.out.print(attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + "! ");
            attacker.increaseXP(1); // Award xp
            attacker.useDurability();

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
            int damage = attacker.physDamage() / 2; // TODO is armor applied here or after the halving
            for (Unit target : targets) {
                // Make sure the damage is within a valid range
                int temp = damage - target.getDefense();
                if (damage < 0) {
                    temp = 0;
                } else if (target.getHp() < damage) {
                    temp = target.getHp();
                }
                target.takeDamage(temp);
                System.out.println(target.getName() + " took " + temp + " damage!");
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
            int damage = attacker.physDamage();
            for (Unit target : targets) {
                // Make sure the damage is within a valid range
                int temp = damage - target.getDefense();
                if (damage < 0) {
                    temp = 0;
                } else if (target.getHp() < damage) {
                    temp = target.getHp();
                }
                target.takeDamage(temp);
                System.out.println(target.getName() + " took " + temp + " damage!");
            }
            attacker.useDurability();
            attacker.increaseXP(1);
        }

        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
