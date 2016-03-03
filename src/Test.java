import Units.Enemy;
import Units.Player;
import Units.Weapon;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

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
