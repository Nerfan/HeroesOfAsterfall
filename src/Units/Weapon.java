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

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        if (this.type.equals("tome")) {
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

    @Override
    public String toString() {
        return "Weapon{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", str=" + str +
                ", mag=" + mag +
                ", hit=" + hit +
                ", durability=" + durability +
                ", cost=" + cost +
                '}';
    }
}
