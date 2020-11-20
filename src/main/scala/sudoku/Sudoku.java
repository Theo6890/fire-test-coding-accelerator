package sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Sudoku {
    public static void main(String[] args) {
        String sudoku = args[0];
        String workingDir = new File("src/main/scala/anagram").getAbsolutePath();

        resolveSudoku(openFile(workingDir, sudoku));
    }

    private static void resolveSudoku(FileReader sudokuFile) {

    }

    public static FileReader openFile(String workingDir, String sudoku) {
        try {
            return new FileReader(new File(workingDir + "/" + sudoku));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
