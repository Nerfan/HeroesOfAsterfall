package Mechanics;

import Units.Unit;

import java.util.List;
import java.util.Random;

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
            if (healer.isRole("Saint")) {
                Random rng = new Random();
                if (rng.nextInt(100) < healer.getMastery()) {
                    heal *= 3;
                    System.out.print("Critical heal! ");
                }
            }
            if (heal > (recipient.getMaxhp() - recipient.getHp())) {
                heal = (recipient.getMaxhp() - recipient.getHp());
            }
            recipient.heal(heal);
            System.out.print(recipient.getName() + " was healed for " + heal + " hp by " + healer.getName() + "!");
            System.out.print(" " + recipient.getName() + " now has " + recipient.getHp() + "/" + recipient.getMaxhp() + " hp. ");

            if (healer.isRole("Sorcerer") && healer.getEquipped().getName().equals("Water Tome")) {
                healer.heal(heal/2);
                System.out.println(healer.getName()  +" also healed for " + heal/2 + ". ");
            }

            healer.increaseXP(1);
            healer.useDurability();
            healer.takeTurn();
        }

        catch(Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * Saint ability: heal two people for half as much as normal
     * @param healer     Unit that is healing the others; should be of the Saint class
     * @param recipients Units to be healed; there should be exactly two
     */
    public static void linkHeal(Unit healer, List<Unit> recipients) {
        try {
            if (!healer.getAttackType().equals("heal")) {
                System.out.println("Error: Healing item not equipped.");
                return;
            } else if (!healer.isRole("Saint")) {
                System.out.println("Error: Link Heal is a Saint ability.");
                return;
            } else if (healer.getHp() <= 0) {
                System.out.println("Error: Healer is dead.");
                return;
            } else if (!healer.hasDurability()) {
                System.out.println("Error: No durability remaining.");
                return;
            } else if (recipients.size() != 2) {
                System.out.println("Error: Link Heal requires two targets.");
                return;
            }
            // Check this ahead of time so that we don't get a situation where we heal one but not the other
            for (Unit recipient : recipients) {
                if (recipient.getHp() == 0) {
                    System.out.println("Error: " + recipient.getName() + " is dead.");
                    return;
                } else if (recipient.getHp() == recipient.getMaxhp()) {
                    System.out.println("Error: " + recipient.getName() + " already has full HP.");
                    return;
                }
            }
            for (Unit recipient : recipients) {
                int heal = healer.getMag();
                switch(healer.getEquipped().getName()) {
                    case("Oak Staff"):
                        heal += 10;
                        break;
                    case("Trimord Staff"):
                        heal += 20;
                        break;
                }
                heal /= 2; // Link heal, so only half the amount
                Random rng = new Random();
                if (rng.nextInt(100) < healer.getMastery()) { // Don't need to check for Saint class again
                    heal *= 3;
                    System.out.print("Critical heal! ");
                }
                if (heal > (recipient.getMaxhp() - recipient.getHp())) {
                    heal = (recipient.getMaxhp() - recipient.getHp());
                }
                recipient.heal(heal);
                System.out.print(recipient.getName() + " was healed for " + heal + " hp by " + healer.getName() + "!");
                System.out.println(" " + recipient.getName() + " now has " + recipient.getHp() + "/" + recipient.getMaxhp() + " hp. ");
            }
            healer.increaseXP(1);
            healer.useDurability();
            healer.takeTurn();
        }

        catch(Exception ex) {
            System.out.println("Error: " + ex);
        }
    }
}
