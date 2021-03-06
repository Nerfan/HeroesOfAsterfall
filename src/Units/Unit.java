package Units;

import java.util.HashMap;

/**
 * Gives the framework for player and enemy units. Acts as an abstract class for them to extend
 * @author Jeremy Lefurge
 */
public abstract class Unit {
    protected String name;  // Name of unit
    protected int maxhp;    // Max hp
    protected int hp;       // Current HP
    protected int move;     // Number of spaces the unit can move
    protected int str;      // Physical damage
    protected int mag;      // Magic damage
    protected int skill;    // Increases hit chance by 1:1
    protected int spd;      // Increases dodge chance by 1:2
    protected int defense;  // Decreases physical damage 1:1
    protected int res;      // Decreases magic damage 1:1
    protected int mastery;  // Percent chance to crit
    protected Weapon equipped;
    protected boolean blinded; // For sorcerer light tome ability
    protected boolean actionable;

    /**
     * Constructor, all parameters are explained in subclasses
     */
    public Unit(String name, int maxhp, int hp, int move, int str, int mag, int skill, int spd, int defense, int res, int mastery, Weapon equipped) {
        this.name = name;
        this.maxhp = maxhp;
        this.hp = hp;
        this.move = move;
        this.str = str;
        this.mag = mag;
        this.skill = skill;
        this.spd = spd;
        this.defense = defense;
        this.res = res;
        this.mastery = mastery;
        this.equipped = equipped;
        this.blinded = false;
        this.actionable = false;
    }

    /**
     * Decreases the unit's health by an amount, never below 0
     * @param damage Amount to decrease health by
     */
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    /**
     * Increases the unit's health by a set amount, never over max
     * @param heal Amount to increase health by
     */
    public void heal(int heal) {
        this.hp += heal;
        if (this.hp > this.maxhp) {
            this.hp = this.maxhp;
        }
    }

    public void blind() {
        this.blinded = true;
    }

    /**
     * To be used whenever a unit takes an action; marks them as unable to go again on this turn
     */
    public void takeTurn() {
        this.actionable = false;
    }

    /**
     * To be used when a new turn starts; marks a unit as able to take another action
     */
    public void newTurn() {
        this.actionable = true;
    }

    /**
     * Tells whether or not the unit still has an action left for this turn
     * @return True if actionable, false otherwise
     */
    public boolean hasTurn() {
        return this.actionable;
    }

    /**
     * Gets the amount of physical damage dealt by the unit
     * @return  Integer representing the unit's physical damage
     */
    public abstract int physDamage();

    /**
     * Gets the amount of magical damage dealt by the unit
     * @return  Integer representing the unit's magical damage
     */
    public abstract int magDamage();

    /**
     * Gets the accuracy of the unit
     * @return  Integer representing the percent chance for the unit to hit
     */
    public abstract int getAccuracy();

    /**
     * Gets the dodge chance of the unit
     * @return  Integer representing the percent chance for the unit to dodge an attack
     */
    public abstract int getDodge();

    /**
     * Increases the unit's xp. Does nothing for enemy units
     * @param xp    Amount to raise xp by
     */
    public abstract void increaseXP(int xp);

    /**
     * Returns type of weapon equipped
     * @return  String: phys, mag, or heal
     */
    public String getAttackType() {
        return this.equipped.getType();
    }

    /**
     * Checks if a unit's weapon has enough durability to attack
     * @return  True if can attack, false if not
     */
    public abstract boolean hasDurability();

    /**
     * Uses durability of the currently equipped weapon if the unit is a player. Does nothing for enemies.
     * @param uses Number of uses to subtract from the durability
     */
    public abstract void useDurability(int uses);

    /**
     * Default use case for durability use is 1
     */
    public void useDurability() {
        this.useDurability(1);
    }

    /**
     * Checks if a unit is in range to attack with their currently equipped weapon
     * @param distance  Distance of attack
     * @return  True if they can attack, false if they cannot
     */
    public boolean inRange(int distance) {
        return this.equipped.inRange(distance);
    }

    /**
     * Checks a unit to see if it fits a specific role/class
     * @param role Name of the role to check (e.g. Marksman, Adept)
     * @return true if the unit is a player and has that role, false otherwise
     */
    public boolean isRole(String role) {
        if (this instanceof Player) {
            if (((Player) this).role.equals(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the string representing the unit's highest stat
     * Used for adaptability and not much else
     * @return Short-form name of the highest stat (e.g. str, mag, res)
     */
    public String getHighestStat() {
        // Default to strength; then take whatever is higher (only replaces if higher; ties leave the first one)
        String highest = "str";
        if (this.mag > this.str) {
            highest  ="mag";
        }
        if (this.skill > this.mag) {
            highest  ="skill";
        }
        if (this.spd > this.skill) {
            highest  ="spd";
        }
        if (this.defense > this.spd) {
            highest  ="defense";
        }
        if (this.res > this.defense) {
            highest  ="res";
        }
        if (this.mastery > this.res) {
            highest  ="mastery";
        }
        return highest;
    }

    // Simple getters and setters

    public String getName() {
        return name;
    }

    public int getMaxhp() {
        return maxhp;
    }

    public int getHp() {
        return hp;
    }

    public int getMove() {
        return move;
    }

    public int getStr() {
        return str;
    }

    public int getMag() {
        return mag;
    }

    public int getSkill() {
        return skill;
    }

    public int getSpd() {
        return spd;
    }

    public int getDefense() {
        return defense;
    }

    public int getRes() {
        return res;
    }

    public int getMastery() {
        return mastery;
    }

    public Weapon getEquipped() {
        return equipped;
    }

    public abstract HashMap<String, Weapon> getInventory();

    public void setStr(int str) {
        this.str = str;
    }

    public void setMag(int mag) {
        this.mag = mag;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public void setMastery(int mastery) {
        this.mastery = mastery;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "name='" + name + '\'' +
                ", maxhp=" + maxhp +
                ", hp=" + hp +
                ", move=" + move +
                ", str=" + str +
                ", mag=" + mag +
                ", skill=" + skill +
                ", spd=" + spd +
                ", defense=" + defense +
                ", res=" + res +
                ", mastery=" + mastery +
                ", equipped=" + equipped +
                '}';
    }

    public String allStats() {
        return "maxhp=" + maxhp +
                ", hp=" + hp +
                ", move=" + move +
                ", str=" + str +
                ", mag=" + mag +
                ", skill=" + skill +
                ", spd=" + spd +
                ", defense=" + defense +
                ", res=" + res +
                ", mastery=" + mastery;
    }
}
