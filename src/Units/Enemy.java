package Units;

/**
 * Class for objects to represent enemies
 */
public class Enemy extends Unit {

    public Enemy(String name,
                 int maxhp, int hp, int move, int str, int mag, int skill, int spd, int defense, int res, int mastery,
                 Weapon equipped) {
        super(name, maxhp, hp, move, str, mag, skill, spd, defense, res, mastery, equipped);
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
    public void increaseXP(int xp) {}

    @Override
    public boolean hasDurability() {
        return true;
    }
}
