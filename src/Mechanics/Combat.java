package Mechanics;

import Units.Enemy;
import Units.Player;
import Units.Unit;
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
    public static void combat(Unit attacker, Unit defender) {
        try {
            if (attacker.getAttackType().equals("heal")) {
                System.out.println("Error: Healing item equipped");
            } else damage(attacker, defender);

            if (!defender.getAttackType().equals("heal") && defender.getHp() > 0) {
                damage(defender, attacker);
            }
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
    public static void damage(Unit attacker, Unit defender) {
        Random rng = new Random();  // Initialize the rng
        int roll = rng.nextInt(100) + 1;
        int damage = 0;             // Initialize the damage number

        if (roll > (attacker.getAccuracy())-defender.getDodge()) { // Miss chance
            System.out.println(attacker.getName() + " missed!");
        } else {

            // Gets the type of attack
            if (attacker.getAttackType().equals("phys")) {
                damage = attacker.physDamage() - defender.getDefense();
            } else if (attacker.getAttackType().equals("mag")) {
                damage  = attacker.magDamage() - defender.getRes();
            }

            // Roll for crit
            if (roll < attacker.getMastery()) {
                damage *= 3;
                System.out.println("Critical hit!");
            }

            // Make sure damage is not less than 0 or more than the remaining health of the defender
            if (damage < 0) {
                damage = 0;
            }
            if (defender.getHp() < damage) {
                damage = defender.getHp();
            }

            // Actually deal the damage
            defender.takeDamage(damage);
            System.out.println(attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + "!");

            // Give players xp
            if (attacker instanceof Player) {
                ((Player) attacker).setXp(((Player) attacker).getXp() + 1);
                System.out.println(attacker.getName() + " gained 1 xp!");
                if (((Player) attacker).getXp() >= 10) {
                    ((Player) attacker).setXp(((Player) attacker).getXp()-10);
                    LevelUp.levelUp(((Player) attacker));
                }
            }

            // If the defender dies
            if (defender.getHp() == 0) {
                System.out.println(defender.getName() + " has been killed!");
                if (attacker instanceof Player) {
                    ((Player) attacker).setXp(((Player) attacker).getXp() + 2);
                    System.out.println(attacker.getName() + " gained an additional 2 xp!");
                    if (((Player) attacker).getXp() >= 10) {
                        ((Player) attacker).setXp(((Player) attacker).getXp()-10);
                        LevelUp.levelUp(((Player) attacker));
                    }
                }
            }
        }
    }
}
