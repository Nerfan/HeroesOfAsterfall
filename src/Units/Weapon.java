package Units;

/**
 * Created by jeremy on 2/16/16.
 */
public class Weapon {
    protected String type;
    protected String name;
    protected int str;
    protected int mag;
    protected int hit;
    protected int durability;
    protected int cost;

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
                ", str=" + str +
                ", mag=" + mag +
                ", hit=" + hit +
                ", durability=" + durability +
                ", cost=" + cost;
    }
}
