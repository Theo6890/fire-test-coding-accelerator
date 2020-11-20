package rectangle;

import java.io.*;
import java.util.Arrays;

public class SquareDisplay {

    public static void main(String[] args) {
        String file1 = args[0];
        String file2 = args[1];
        String workingDir = new File("src/main/scala/rectangle").getAbsolutePath();

        System.out.println(workingDir);

        searchMagicSquare(file1, file2, workingDir);
    }

    public static void searchMagicSquare(String file1, String file2, String workingDir) {
        BufferedReader squareReader = null;
        try {
            squareReader = new BufferedReader(new FileReader(new File(workingDir + "/" + file1)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader rectangleReader = null;
        try {
            rectangleReader = new BufferedReader(new FileReader(new File(workingDir + "/" + file2)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String squareLine = squareReader.readLine(), rectangleLine;
            int[] iSZ = new int[2];
            int i = 0, j = 0;
            while ((rectangleLine = rectangleReader.readLine()) != null) {
                if (rectangleLine.contains(squareLine)) {
                    iSZ[0] = rectangleLine.indexOf(squareLine);
                    iSZ[1] = i++;
                    break;
                }
                i++;
            }
            System.out.println("Magic square at: " + Arrays.toString(iSZ));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                squareReader.close();
                rectangleReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
