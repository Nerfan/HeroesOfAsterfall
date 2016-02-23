package Mechanics;

import Units.Unit;
import java.util.Random;

/**
 * Created by jeremy on 2/16/16.
 */
public class Combat {
    public static void combat(Unit attacker, Unit defender) {
        if (attacker.getAttackType().equals("heal")) {
            System.out.println("Error: Healing item equipped");
        } else damage(attacker, defender);
        if (!defender.getAttackType().equals("heal")) {
            damage(defender, attacker);
        }
    }

    public static void damage(Unit attacker, Unit defender) {
        Random rng = new Random();
        int damage = 0;
        int roll = rng.nextInt(100) + 1;
        if (roll > attacker.getAccuracy()) {
            System.out.println(attacker.getName() + " missed!");
        } else {
            if (attacker.getAttackType().equals("phys")) {
                damage = attacker.physDamage() - defender.getDefense();
            } else if (attacker.getAttackType().equals("mag")) {
                damage  = attacker.magDamage() - defender.getRes();
            }
            if (roll < attacker.getMastery()) {
                damage *= 3;
                System.out.println("Critical hit!");
            }
            if (damage < 0) {
                damage = 0;
            }
            defender.takeDamage(damage);
            System.out.println(attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + "!");
        }
    }
}
