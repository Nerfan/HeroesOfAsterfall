package Units;

/**
 * Created by jeremy on 2/15/16.
 */
public class Player extends Unit {
    String role;
    int level;
    int xp;
    int gold;
    Weapon equipped;

    public Player(String name, String role, int level, int xp, int maxhp, int hp, int move, int str, int mag, int skill, int spd, int defense, int res, int mastery, int gold, Weapon equipped) {
        super(name, maxhp, hp, move, str, mag, skill, spd, defense, res, mastery);
        this.role = role;
        this.level = level;
        this.xp = xp;
        this.gold = gold;
        this.equipped = equipped;
    }

    @Override
    public int physDamage() {
        return (this.str+this.equipped.str);
    }

    @Override
    public int magDamage() {
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

    public Weapon getEquipped() {
        return equipped;
    }

    public String getAttackType() {
        return this.equipped.getType();
    }

    @Override
    public String toString() {
        return "Player{" +
                "role='" + role + '\'' +
                ", level=" + level +
                ", xp=" + xp +
                ", gold=" + gold +
                ", equipped=" + equipped +
                '}';
    }
}
