package Mechanics;

import Units.Unit;

/**
 * Gives functionality for healing units
 * Very basic for now, but probably doesn't need to be too complex
 * @author Jeremy Lefurge
 */
public class Heal {

    /**
     * A healer heals a recipient for an amount depending on their magic and currently equipped weapon
     * @param healer    Unit doing the healing
     * @param recipient Unit being healed
     */
    public static void heal(Unit healer, Unit recipient) {
        try {
            if (!healer.getAttackType().equals("heal")) {
                System.out.println("Error: Healing item not equipped.");
                return;
            } else if (healer.getHp() <= 0) {
                System.out.println("Error: Healer is dead.");
                return;
            } else if (recipient.getHp() <= 0) {
                System.out.println("Error: Recipient is dead.");
                return;
            } else if (!healer.hasDurability()) {
                System.out.println("Error: No durability remaining.");
                return;
            } else if (recipient.getHp() == recipient.getMaxhp()) {
                System.out.println("Error: Recipient already has full HP.");
                return;
            }
            int heal = healer.getMag();
            switch(healer.getEquipped().getName()) {
                case("Oak Staff"):
                    heal += 10;
                    break;
                case("Trimord Staff"):
                    heal += 20;
                    break;
            }
            if (heal > (recipient.getMaxhp() - recipient.getHp())) {
                heal = (recipient.getMaxhp() - recipient.getHp());
            }
            recipient.setHp(recipient.getHp() + heal);
            System.out.print(recipient.getName() + " was healed for " + heal + " hp by " + healer.getName() + "!");
            System.out.print(" " + recipient.getName() + " now has " + recipient.getHp() + "/" + recipient.getMaxhp() + " hp. ");
            healer.increaseXP(1);
        }

        catch(Exception ex) {
            System.out.println("Error: " + ex);
        }
    }
}
