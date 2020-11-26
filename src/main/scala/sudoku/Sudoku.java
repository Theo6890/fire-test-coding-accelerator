package sudoku;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Sudoku {
    static Map<Integer, ArrayList<String>> lines, columns, squares;
    static FileInputStream inputStream;
    static FileOutputStream fileOutputStream;
    static boolean incomplete = true;
    static ArrayList<String> resultsSortedPerLine;
    static String workingDir;

    public static void main(String[] args) {
        init(args);

        populateMaps();
        printPopulatedMaps();

        resolveSudoku();
        saveResultsInFile();
    }

    public static void init(String[] args) {
        String sudoku = args[0];
        workingDir = new File("src/main/scala/sudoku").getAbsolutePath();
        openFile(sudoku);

        assert inputStream != null;

        lines = new HashMap<>();
        columns = new HashMap<>();
        squares = new HashMap<>();

        resultsSortedPerLine = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            lines.putIfAbsent(i, new ArrayList<>());
            columns.putIfAbsent(i, new ArrayList<>());
            squares.putIfAbsent(i, new ArrayList<>());
        }
    }

    public static void openFile(String inputFileName) {
        File file = new File(workingDir + "/" + inputFileName);
        if (!file.exists()) System.out.println(workingDir + "/" + inputFileName + " does not exist.");
        if (!(file.isFile() && file.canRead())) System.out.println(file.getName() + " cannot be read from.");

        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveResultsInFile() {
        try {
            fileOutputStream = new FileOutputStream(workingDir + "/" + "sol.txt");

            for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                for (int columnIndex = 0; columnIndex < lines.size(); columnIndex++) {
                    if (lineIndex != 0 && lineIndex % 3 == 0 && columnIndex == 0)
                        fileOutputStream.write("---+---+---\n".getBytes());
                    if (columnIndex != 0 && columnIndex % 3 == 0) fileOutputStream.write("|".getBytes());
                    fileOutputStream.write(lines.get(lineIndex).get(columnIndex).getBytes());
                    if (columnIndex != 0 && columnIndex % 8 == 0) fileOutputStream.write("\n".getBytes());
                }
            }
            fileOutputStream.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private static void populateMaps() {
        try {
            char c;
            String current, previous = "";
            int lineCount = 0, columnCount = -1;

            while (inputStream.available() > 0) {
                c = (char) inputStream.read();
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
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private static boolean resolveSingleMissingNumberIn(Map<Integer, ArrayList<String>> shape, int shapeIndex) {
        if (missingNumbersCount(shape, shapeIndex) != 1) return false;

        shape.get(shapeIndex).set(
                getIndexMissingStringIn(shape.get(shapeIndex)),
                getMissingStringIn(shape.get(shapeIndex))
        );
        resultsSortedPerLine.add(getMissingStringIn(shape.get(shapeIndex)));
        return true;
    }

    private static int missingNumbersCount(Map<Integer, ArrayList<String>> shape, int shapeIndex) {
        return Collections.frequency(shape.get(shapeIndex), "_");
    }

    public static int getIndexMissingStringIn(ArrayList<String> values) {
        for (int i = 0; i < values.size(); i++)
            if (values.get(i).equals("_")) return i;

        return -1;
    }

    public static String getMissingStringIn(ArrayList<String> values) {
        for (int i = 0; i < values.size(); i++)
            if (!values.contains(String.valueOf(i + 1))) return String.valueOf(i + 1);

        return "";
    }
}
