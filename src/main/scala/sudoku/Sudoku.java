package sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sudoku {
    static FileInputStream fis;
    static Map<Integer, ArrayList<String>> squares, lines, columns;

    public static void main(String[] args) {
        init(args);

        populateLines();
//        populateSquares();
        resolveSudoku();
    }

    public static void init(String[] args) {
        String sudoku = args[0];
        String workingDir = new File("src/main/scala/sudoku").getAbsolutePath();
        fis = openFile(workingDir, sudoku);

        assert fis != null;

        squares = new HashMap<Integer, ArrayList<String>>();
        lines = new HashMap<Integer, ArrayList<String>>();
        columns = new HashMap<Integer, ArrayList<String>>();
    }

    public static FileInputStream openFile(String workingDir, String sudoku) {
        File file = new File(workingDir + "/" + sudoku);
        if (!file.exists()) {
            System.out.println(workingDir + "/" + sudoku + " does not exist.");
            return null;
        }
        if (!(file.isFile() && file.canRead())) {
            System.out.println(file.getName() + " cannot be read from.");
            return null;
        }
        try {
            fis = new FileInputStream(file);
            return fis;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void populateLines() {
        try {
            char current;
            String s, previous = "";
            ArrayList<String> lineValues = new ArrayList<String>(9);
            int lineCount = 0;

            while (fis.available() > 0) {
                current = (char) fis.read();
                s = String.valueOf(current);

                if (!s.equals("|") && !s.equals("\n") && !s.equals("+") && !s.equals("-")) {
                    lineValues.add(s);
                }

                //Increment nbr of line red & register values in corresponding Map
                if (s.equals("\n") && !previous.equals("-")) {
                    lines.put(lineCount, lineValues);
                    System.out.println(lines.get(lineCount).toString());
                    lineValues.clear();
                    lineCount++;
                }
                previous = s;
            }

            System.out.println(lines.entrySet().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void resolveSudoku() {
//        try {
//            int i = Integer.parseInt("_");
//        } catch (NumberFormatException e) {
//            System.out.println("Ceci n'est pas un nombre");
//        }
    }

    /*private static void populateSquares() {
        try {
            char c;
            String current, previous = "";
            ArrayList<String> squareValues = new ArrayList<String>(9);
            int squareCount = 0, i = 1;

            c = (char) fis.read();
            current = String.valueOf(c);

            while (fis.available() > 0) {
                previous = current;
                c = (char) fis.read();
                current = String.valueOf(c);

                if (i == 23) {
                    System.out.println("i == 23");
                    break;
                }

                if (previous.equals("|") || previous.equals("\n")) {
                    if (squares.get(squareCount) == null) {
                        ArrayList<String> tmp = squareValues;
                        squares.put(squareCount, tmp);
                        squareValues.clear();
                    }
                    if (previous.equals("\n")) {
                        squareCount -= 2;
                    } else squareCount++; // previous is a |
                } else {
                    if (squares.get(squareCount) != null) {
                        squares.get(squareCount).add(previous);
                    } else squareValues.add(previous);
                }
                i++;


//                if (previous.equals("\n") && (current instanceof Integer)) {
//                    if (squares.get(squareCount) == null) {
//                        squares.put(squareCount, squareValues);
//                        squareValues.clear();
//                    }
//                    squareCount -= 2;
//                } else squareCount += 3;

            }
            System.out.println("squares: " + squares.toString());
            System.out.println("squares: " + squareCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public boolean isInSquare(int squareIndex, int searchingNumber) {
        ArrayList<String> values = squares.get(squareIndex);
        for (String s : values) {
            int i = Integer.parseInt(s);
            if (i == searchingNumber) return true;
        }
        return false;
    }

    public boolean isInLine(int lineIndex, int searchingNumber) {
        ArrayList<String> values = lines.get(lineIndex);
        for (String s : values) {
            int i = Integer.parseInt(s);
            if (i == searchingNumber) return true;
        }
        return false;
    }

    public boolean isInColumn(int columnIndex, int searchingNumber) {
        ArrayList<String> values = columns.get(columnIndex);
        for (String s : values) {
            int i = Integer.parseInt(s);
            if (i == searchingNumber) return true;
        }
        return false;
    }

    enum SHAPE {SQUARE, LINE, COLUMN}
}
