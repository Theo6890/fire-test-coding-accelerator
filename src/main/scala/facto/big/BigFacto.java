package facto.big;

public class BigFacto {
    public static void main(String[] args) {
        System.out.println("!" + args[0] + " = " + facto(Integer.parseInt(args[0])));
    }

    public static int facto(int entry) {
        if (entry <= 1) return 1;
        else return entry * facto(entry - 1);
    }
}
