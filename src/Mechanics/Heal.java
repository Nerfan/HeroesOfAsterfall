package Mechanics;

import Units.Player;
import Units.Unit;

/**
 * Gives functionality for healing units
 * Very basic for now, but probably doesn't need to be too complex
 */
public class Heal {
    public static void heal(Unit healer, Unit recipient) {
        try {
            if (!healer.getAttackType().equals("heal")) {
                System.out.println("Error: Healing item not equipped.");
                return;
            }
            int heal = healer.getMag();
            recipient.setHp(recipient.getHp() + heal);
            if (recipient.getHp() > recipient.getMaxhp()) {
                recipient.setHp(recipient.getMaxhp());
            }
            System.out.print(recipient.getName() + " was healed for " + heal + " hp by " + healer.getName() + "!");
            System.out.print("\t" + recipient.getName() + " now has " + recipient.getHp() + "/" + recipient.getMaxhp() + " hp");
            healer.increaseXP(1);
        }

        catch(Exception ex) {
            System.out.println("Error: " + ex);
        }
    }
}
