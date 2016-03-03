package Mechanics;

import Units.Player;
import Units.Unit;

/**
 * Gives functionality for healing units
 * Very basic for now, but probably doesn't need to be too complex
 */
public class Heal {
    public static void heal(Unit healer, Unit recipient) {
        if (!healer.getAttackType().equals("heal")) {
            System.out.println("Error: healing item not equipped.");
            return;
        }
        int heal = healer.getMag();
        recipient.setHp(recipient.getHp() + heal);
        System.out.println(recipient.getName() + " was healed for " + heal + " by " + healer.getName());
        if (healer instanceof Player) {
            ((Player) healer).setXp(((Player) healer).getXp() + 1);
            System.out.println(healer.getName() + " gained 1 xp.");
        }
    }
}
