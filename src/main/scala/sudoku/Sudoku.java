package sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Sudoku {
    static Map<Integer, ArrayList<String>> lines, columns, squares;
    static FileInputStream fis;
    static boolean incomplete = true;

    public static void main(String[] args) {
        init(args);

        populateMaps();
        printPopulatedMaps();

        resolveSudoku();
//        saveResultsInFile();
//        closeEverything();
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
                    lines.get(lineCount).add(current);
                    columns.get(columnCount).add(current);
                    updateSquares(lineCount, columnCount, current);
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

    //TODO: Simplify algo
    private static void updateSquares(int lineCount, int columnCount, String value) {
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

    private static void printPopulatedMaps() {
        System.out.println("LINES:");
        printData(lines);
        System.out.println("\nCOLUMNS:");
        printData(columns);
        System.out.println("\nSQUARES:");
        printData(squares);
    }

    private static void printData(Map<Integer, ArrayList<String>> dataMapped) {
        for (int i = 0; i < 9; i++) {
            System.out.println(dataMapped.get(i).toString());
            if (i == 2 || i == 5) System.out.println("---------------------------");
        }
    }

    private static void resolveSudoku() {
        while (incomplete) {
            for (int i = 0; i < squares.size(); i++) {
                resolveSingleBlankInLine(i);
                resolveSingleBlankInColumn(i);
//                resolveSingleBlankInSquare(i);
            }
            hasBeenCompleted();
        }

        System.out.println("\n\n\n-----SUDOKU RESOLVED-----");
        System.out.println("\nLINES:");
        printData(lines);
    }

    private static void hasBeenCompleted() {
        int i = 0;
        boolean done;
        while (i < 8 && missingNumbersCount(squares, i) == 0) i++;
        done = missingNumbersCount(squares, i) == 0;
        incomplete = !done;
    }

    private static void resolveSingleBlankInColumn(int i) {
        boolean updateNeeded = resolveSingleMissingNumberIn(columns, i);
        if (updateNeeded) updateMapsFromColumns();
    }


    private static void resolveSingleBlankInLine(int i) {
        boolean updateNeeded = resolveSingleMissingNumberIn(lines, i);
        if (updateNeeded) updateMapsFromLines();
    }

    private static void resolveSingleBlankInSquare(int i) {
        boolean updateNeeded = resolveSingleMissingNumberIn(squares, i);
        if (updateNeeded) updateMapsFromSquares();
    }

    private static void updateMapsFromColumns() {
        for (int i = 0; i < columns.size(); i++) {
            lines.get(i).clear();
            squares.get(i).clear();
        }

        for (int columnI = 0; columnI < columns.size(); columnI++) {
            for (int lineI = 0; lineI < columns.size(); lineI++) {
                String value = columns.get(columnI).get(lineI);
                lines.get(lineI).add(value);
                updateSquares(lineI, columnI, value);
            }
        }
    }

    private static void updateMapsFromLines() {
        for (int i = 0; i < lines.size(); i++) {
            columns.get(i).clear();
            squares.get(i).clear();
        }

        for (int lineI = 0; lineI < lines.size(); lineI++) {
            for (int columnI = 0; columnI < lines.size(); columnI++) {
                String value = lines.get(lineI).get(columnI);
                columns.get(columnI).add(value);
                updateSquares(lineI, columnI, value);
            }
        }
    }

    private static void updateMapsFromSquares() {
        for (int i = 0; i < squares.size(); i++) {
            lines.get(i).clear();
            columns.get(i).clear();
        }

        for (int lineI = 0; lineI < squares.size(); lineI++) {
            for (int columnI = 0; columnI < squares.size(); columnI++) {

            }
        }
    }

    private static boolean resolveSingleMissingNumberIn(Map<Integer, ArrayList<String>> shape, int shapeIndex) {
        if (missingNumbersCount(shape, shapeIndex) == 1) {
            shape.get(shapeIndex).set(
                    getIndexesMissingStringsInShape(shape.get(shapeIndex)).get(0),
                    getMissingStringsInShape(shape.get(shapeIndex)).get(0)
            );
            return true;
        }
        return false;
    }

    private static int missingNumbersCount(Map<Integer, ArrayList<String>> shape, int shapeIndex) {
        return Collections.frequency(shape.get(shapeIndex), "_");
    }

    public static ArrayList<Integer> getIndexesMissingStringsInShape(ArrayList<String> values) {
        ArrayList<Integer> indexesMissingNumbers = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).equals("_")) indexesMissingNumbers.add(i);
        }
        return indexesMissingNumbers;
    }

    public static ArrayList<String> getMissingStringsInShape(ArrayList<String> values) {
        ArrayList<String> missingNumbers = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            if (!values.contains(String.valueOf(i + 1))) missingNumbers.add(String.valueOf(i + 1));
        }
        return missingNumbers;
    }
}
