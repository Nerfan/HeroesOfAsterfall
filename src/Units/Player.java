package Units;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeremy on 2/15/16.
 */
public class Player extends Unit {
    String role;
    int level;
    int xp;
    int gold;
    HashMap<String, Weapon> inventory;
    Weapon equipped;

    public Player(String name, String role,
                  int level, int xp, int maxhp, int hp, int move, int str, int mag, int skill, int spd,
                  int defense, int res, int mastery, int gold, HashMap inventory, Weapon equipped) {
        super(name, maxhp, hp, move, str, mag, skill, spd, defense, res, mastery);
        this.role = role;
        this.level = level;
        this.xp = xp;
        this.gold = gold;
        this.inventory = inventory;
        this.equipped = equipped;
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

    public Weapon getEquipped() {
        return equipped;
    }

    public String getAttackType() {
        return this.equipped.getType();
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

    @Override
    public String toString() {
        String printInv = "";
        for (Map.Entry weapon : inventory.entrySet()) {
            printInv += "\n\t\t" + weapon.getValue();
        }
        return this.name +
                "(" + this.hp + "/" + this.maxhp + " hp): Level " + this.level + " " + this.role + " (" + this.xp +  " xp)" +
                "\n\tstr: " + this.str + ", mag: " + this.mag + ", defense: " + this.defense + ", res: " + this. res +
                "\n\tEquipped:\n\t\t" + this.equipped.name + "(" + this.equipped.durability + ")" +
                "\n\tInventory:" + printInv;
    }


}
