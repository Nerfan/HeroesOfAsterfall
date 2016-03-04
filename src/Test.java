import Units.Enemy;
import Units.Weapon;

import java.util.Map;
import java.util.TreeMap;

public class Test {
    public static void main(String [] args) {
        TreeMap<String, Enemy> stuff = new TreeMap<>();
        stuff.put("butts", new Enemy("butts",
                10,10,0,0,0,0,0,0,0,0,
                new Weapon("Axe",
                        "Axe", 0, 0, 0, 0, 0)));
        TreeMap<String, Enemy> copy = new TreeMap<>();
        for (Map.Entry<String, Enemy> entry : stuff.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        stuff.get("butts").takeDamage(4);
        System.out.println(copy.get("butts"));
    }
}
