import java.util.Arrays;

/**
 * Created by jeremy on 2/19/16.
 */
public class Testing {
    public static void main(String[] args) {
        String string = "This is    a   string  to be split      into a     line";
        String[] list = string.split("\\s+");
        System.out.println(Arrays.toString(list));
    }
}
