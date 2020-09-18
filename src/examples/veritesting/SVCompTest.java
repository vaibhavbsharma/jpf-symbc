package veritesting;


public class SVCompTest {

    int sideEffect = 0;

    public static void main(String[] args) {
        StaticCharMethods02("abc");
    }


    public static void StaticCharMethods02(String arg) {
        if (arg.length() < 1)
            return;
        char c = arg.charAt(0);
//        System.out.println("printing output");
        char upperC = Character.toUpperCase(c);
        char lowerC = Character.toLowerCase(c);
        if(upperC == lowerC)
            System.out.println("Failing.");
    }

}
