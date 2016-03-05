package Units;

/**
 * Class for objects to represent enemies
 * @author Jeremy Lefurge
 */
public class Enemy extends Unit {

    /** Constructor
     * @param name      Name of unit (title case)
     * @param maxhp     Max hp of unit
     * @param hp        Current HP of unit (while constructing, will usually be equal to maxhp)
     * @param move      Number of spaces the unit can move
     * @param str       Strength (physical damage) of unit
     * @param mag       Magic (magic damage + healing) of unit
     * @param skill     Skill (hit chance) of unit (added to weapon hit chance)
     * @param spd       Speed is 1:2 with dodge chance of unit
     * @param defense   Defense of unit; each point reduces incoming physical damage by 1
     * @param res       Resistance of unit; each point reduces incoming magic damage by 1
     * @param mastery   Mastery is 1:1 with percent chance to crit
     * @param equipped  Currently equipped weapon
     */
    public Enemy(String name,
                 int maxhp, int hp, int move, int str, int mag, int skill, int spd, int defense, int res, int mastery,
                 Weapon equipped) {
        super(name, maxhp, hp, move, str, mag, skill, spd, defense, res, mastery, equipped);
    }

    @Override
    public void increaseXP(int xp) {
        System.out.println();
    }

    @Override
    public boolean hasDurability() {
        return true;
    }

    @Override
    public int physDamage() {
        return str;
    }

    @Override
    public int magDamage() {
        return mag;
    }

    @Override
    public int getAccuracy() {
        return skill;
    }

    @Override
    public int getDodge() {
        return (2*spd);
    }

    @Override
    public String toString() {
        return this.name +
                "(" + this.hp + "/" + this.maxhp + " hp)" +
                "\n\tstr: " + this.str + ", mag: " + this.mag + ", defense: " + this.defense + ", res: " + this. res +
                ", skill: " + this.skill + ", speed: " + this.spd + ", mastery: " + this.mastery +
                "\n\tEquipped: " + this.equipped.name;
    }
}
