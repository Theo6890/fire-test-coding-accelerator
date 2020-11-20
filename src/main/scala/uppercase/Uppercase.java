package uppercase;

public class Uppercase {
    public static void main(String[] args) {
        StringBuilder concatArgs = new StringBuilder();
        char c;

        for (String arg : args) {
            for (int i = 0; i < arg.length(); i++) {
                if (i % 2 == 0) c = Character.toLowerCase(arg.charAt(i));
                else c = Character.toUpperCase(arg.charAt(i));
                concatArgs.append(c);
            }
            concatArgs.append(" ");
        }

        System.out.println(concatArgs);
    }
}
