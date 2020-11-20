package uppercase;

public class Uppercase {
    public static void main(String[] args) {
        StringBuilder concatArgs = new StringBuilder(), modified = new StringBuilder();
        for (String arg : args) {
            concatArgs.append(arg);
            concatArgs.append(" ");
        }

        char c;

        for (int i = 0; i < concatArgs.length(); i++) {
            if (i % 2 == 0) c = Character.toUpperCase(concatArgs.charAt(i));
            else c = Character.toLowerCase(concatArgs.charAt(i));
            modified.append(c);
        }

        System.out.println(modified);
    }
}
