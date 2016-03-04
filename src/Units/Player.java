package Units;

import Mechanics.LevelUp;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates a class for objects that represent player characters
 */
public class Player extends Unit {
    String role;
    int level;
    int xp;
    int gold;
    HashMap<String, Weapon> inventory;

    public Player(String name, String role,
                  int level, int xp, int maxhp, int hp, int move, int str, int mag, int skill, int spd,
                  int defense, int res, int mastery, int gold, HashMap<String, Weapon> inventory, Weapon equipped) {
        super(name, maxhp, hp, move, str, mag, skill, spd, defense, res, mastery, equipped);
        this.role = role;
        this.level = level;
        this.xp = xp;
        this.gold = gold;
        this.inventory = inventory;
    }

    @Override
    public int physDamage() {
        this.equipped.durability -= 1;
        return (this.str+this.equipped.str);
    }

    @Override
    public int magDamage() {
        this.equipped.durability -= 1;
        return (this.mag+this.equipped.mag);
    }

    @Override
    public int getAccuracy() {
        return (this.skill + this.equipped.hit);
    }

    @Override
    public int getDodge() {
        return (2*this.spd);
    }

    public String getRole() {
        return role;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public int getGold() {
        return gold;
    }

    public HashMap<String, Weapon> getInventory() {
        return inventory;
    }

    @Override
    public boolean hasDurability() {
        return (this.equipped.durability > 0);
    }

    /**
     * Increases xp by a set amount,
     * prints a string saying that xp has gone up,
     * and levels up the player if their xp goes above 10
     * @param xp    amount of xp to add
     */
    public void increaseXP(int xp) {
        this.xp += 1;
        System.out.println(this.name + " gained " + xp + " xp! ");
        if (this.xp >= 10) {
            this.xp -= 10;
            LevelUp.levelUp(this);
        }
    }

    /**
     * Switches player weapon to one in their inventory
     * @param weaponName    String, key of weapon in inventory HashMap
     */
    public void switchWeapon(String weaponName) {
        if (!this.inventory.containsKey(weaponName)) {
            System.out.println("Error: Weapon not in inventory.");
            return;
        }
        this.equipped = this.inventory.get(weaponName);
        System.out.println(this.name + " now has " + this.equipped.name + " equipped.");
    }

    @Override
    public String toString() {

        return this.name +
                "(" + this.hp + "/" + this.maxhp + " hp): Level " + this.level + " " + this.role +
                " (" + this.xp +  " xp)" +
                "\n\tstr: " + this.str + ", mag: " + this.mag + ", defense: " + this.defense + ", res: " + this. res +
                    ", skill: " + this.skill + ", speed: " + this.spd + ", mastery: " + this.mastery +
                "\n\tEquipped: " + this.equipped.name + "(" + this.equipped.durability + ")";
    }

    public String inventoryToString() {
        String printInv = "";
        for (Map.Entry weapon : inventory.entrySet()) {
            printInv += "\n\t\t" + weapon.getValue();
        }
        return ("\tInventory:" + printInv);
    }
}
