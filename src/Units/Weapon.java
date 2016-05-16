package Units;

import java.util.Objects;

/**
 * Class to create objects to represent weapons
 * @author Jeremy Lefurge
 */
public class Weapon {
    protected String type;
    protected String name;
    protected int str;
    protected int mag;
    protected int hit;
    protected int durability;
    protected int cost;

    /**
     * Constructor
     * @param type          Type of weapon (e.g. Sword, Tome)
     * @param name          Name of weapon (e.g. Iron Sword, Fire Tome)
     * @param str           Physical damage of weapon
     * @param mag           Magical damage of weapon
     * @param hit           Hit chance (in percent) of weapon
     * @param durability    Durability of weapon
     * @param cost          Cost to buy a new one
     */
    public Weapon(String type, String name, int str, int mag, int hit, int durability, int cost) {
        this.type = type;
        this.name = name;
        this.str = str;
        this.mag = mag;
        this.hit = hit;
        this.durability = durability;
        this.cost = cost;
    }

    /**
     * Copy constructor
     * @param source    Weapon to make a copy of
     */
    public Weapon(Weapon source) {
        this.cost = source.cost;
        this.durability = source.durability;
        this.hit = source.hit;
        this.mag = source.mag;
        this.str = source.str;
        this.name = source.name;
        this.type = source.type;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    /**
     * Tells what type of attack the weapon uses
     * @return  phys, mag, or heal
     */
    public String getType() {
        if (this.name.equals("Water Tome")) {
            return "heal";
        } else if (this.type.equals("Tome")) {
            return "mag";
        } else if (this.name.equals("Steel Staff")) {
            return "phys";
        } else if (this.type.equals("Staff")) {
            return "heal";
        } else {
            return "phys";
        }
    }

    /**
     * Tells whether or not a weapon is in range to attack
     * Assumes the weapon is an attacking weapon
     * @param distance  Distance of attack
     * @return  true if in range, false if not
     */
    public boolean inRange(int distance) {
        if (distance == 1) {
            return (!this.type.equals("Bow"));
        } else if (distance == 2) {
            return (this.type.equals("Bow") || this.type.equals("Tome"));
        } else {
            return false;
        }
    }

    public int getStr() {
        return str;
    }

    public int getMag() {
        return mag;
    }

    public int getHit() {
        return hit;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public String toString() {
        return name +
                "(" + durability + ")" +
                ": str: " + str +
                ", mag: " + mag +
                ", hit: " + hit +
                ", cost: " + cost;
    }
}
