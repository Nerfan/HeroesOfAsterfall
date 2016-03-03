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

    @Override
    public boolean hasDurability() {
        return (this.equipped.durability > 0);
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void levelUp() {
        LevelUp.levelUp(this);
    }

    public void increaseXP(int xp) {
        this.xp += 1;
        System.out.println(this.name + " gained " + xp + " xp! ");
        if (this.xp >= 10) {
            this.xp -= 10;
            LevelUp.levelUp(this);
        }
    }

    public void switchWeapon(String weapon) {
        if (!this.inventory.containsKey(weapon)) {
            System.out.println("Error: Weapon not in inventory.");
            return;
        }
        this.equipped = this.inventory.get(weapon);
        System.out.println(this.name + " now has " + this.equipped.name + " equipped.");
    }

    @Override
    public String toString() {
        String printInv = "";
        for (Map.Entry weapon : inventory.entrySet()) {
            printInv += "\n\t\t" + weapon.getValue();
        }
        return this.name +
                "(" + this.hp + "/" + this.maxhp + " hp): Level " + this.level + " " + this.role +
                " (" + this.xp +  " xp)" +
                "\n\tstr: " + this.str + ", mag: " + this.mag + ", defense: " + this.defense + ", res: " + this. res +
                    ", skill: " + this.skill + ", speed: " + this.spd + ", mastery: " + this.mastery +
                "\n\tEquipped:\n\t\t" + this.equipped.name + "(" + this.equipped.durability + ")" +
                "\n\tInventory:" + printInv;
    }
}
