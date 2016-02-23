package Units;

/**
 * Created by jeremy on 2/15/16.
 */
public class Enemy extends Unit {
    protected String attackType;

    public Enemy(String name, int maxhp, int hp, int move, int str, int mag, int skill, int spd, int defense, int res, int mastery, String attackType) {
        super(name, maxhp, hp, move, str, mag, skill, spd, defense, res, mastery);
        this.attackType = attackType;
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
    public String getAttackType() {
        return attackType;
    }
}
