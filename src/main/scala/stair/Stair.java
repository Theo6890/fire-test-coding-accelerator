package stair;

public class Stair {
    public static void main(String[] args) {
        int arg = Integer.parseInt(args[0]);
        System.out.println("arg = " + arg);

        for (int i = 0; i < arg; i++) {
            for (int j = 0; j < i + 1; j++) {
                System.out.print("#");
            }
            System.out.println("");
        }
    }
}
