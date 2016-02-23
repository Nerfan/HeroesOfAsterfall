package Units;

/**
 * Created by jeremy on 2/15/16.
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

    public Unit(String name, int maxhp, int hp, int move, int str, int mag, int skill, int spd, int defense, int res, int mastery) {
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
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
    }

    public abstract int physDamage();

    public abstract int magDamage();

    public abstract int getAccuracy();

    public abstract int getDodge();

    public abstract String getAttackType();

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
}
