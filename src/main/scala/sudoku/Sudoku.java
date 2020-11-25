package sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sudoku {
    static Map<Integer, ArrayList<String>> lines, columns, squares;
    static FileInputStream fis;

    public static void main(String[] args) {
        init(args);

        populateMaps();

        System.out.println("LINES:");
        printData(lines);
        System.out.println("\nCOLUMNS:");
        printData(columns);
        System.out.println("\nSQUARES:");
        printData(squares);

        resolveSudoku();
    }

    public static void init(String[] args) {
        String sudoku = args[0];
        String workingDir = new File("src/main/scala/sudoku").getAbsolutePath();
        fis = openFile(workingDir, sudoku);

        assert fis != null;

        lines = new HashMap<>();
        columns = new HashMap<>();
        squares = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            lines.putIfAbsent(i, new ArrayList<>());
            columns.putIfAbsent(i, new ArrayList<>());
            squares.putIfAbsent(i, new ArrayList<>());
        }
    }

    private static void populateMaps() {
        try {
            char c;
            String current, previous = "";
            int lineCount = 0, columnCount = -1;

            while (fis.available() > 0) {
                c = (char) fis.read();
                current = String.valueOf(c);

                if (current.equals("\n")) columnCount = -1;
                if (!current.equals("|") && !current.equals("\n") && !current.equals("+") && !current.equals("-")) {
                    columnCount++;
//                    lines[lineCount][columnCount] = current;
                    lines.get(lineCount).add(current);
//                    columns[columnCount][lineCount] = current;
                    columns.get(columnCount).add(current);
                    populateSquares(lineCount, columnCount, current);
                }

                //Increment nbr of line red & register values in corresponding Map
                if (current.equals("\n") && !previous.equals("-")) {
                    lineCount++;
                }
                previous = current;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static void populateSquares(int lineCount, int columnCount, String value) {
        if (lineCount < 3) { // Fill in 3 first SQUARES
            if (columnCount < 3) squares.get(0).add(value);
            else if (columnCount < 6) squares.get(1).add(value);
            else squares.get(2).add(value);
        } else if (lineCount < 6) {
            if (columnCount < 3) squares.get(3).add(value);
            else if (columnCount < 6) squares.get(4).add(value);
            else squares.get(5).add(value);
        } else {
            if (columnCount < 3) squares.get(6).add(value);
            else if (columnCount < 6) squares.get(7).add(value);
            else squares.get(8).add(value);
        }
    }

    private static void printData(Map<Integer, ArrayList<String>> dataMapped) {
        for (int i = 0; i < 9; i++) {
            System.out.println(dataMapped.get(i).toString());
            if (i == 2 || i == 5) System.out.println("---------------------------");
        }
    }

    enum SHAPE {SQUARE, LINE, COLUMN}

    private static void resolveSudoku() {
//        try {
//            int i = Integer.parseInt("_");
//        } catch (NumberFormatException e) {
//            System.out.println("Ceci n'est pas un nombre");
//        }
    }

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
}
