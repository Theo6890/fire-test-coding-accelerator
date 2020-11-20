package anagram;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Anagram {
    public static void main(String[] args) {
        String wordToMix = args[0];
        String dictionary = args[1];
        String workingDir = new File("src/main/scala/anagram").getAbsolutePath();

        System.out.println(workingDir);

        searchAnagram(wordToMix, dictionary, workingDir);
    }

    private static void searchAnagram(String wordToMix, String dictionary, String workingDir) {
        List<String> words = new ArrayList<String>();

        BufferedReader dictionaryReader = null;
        try {
            dictionaryReader = new BufferedReader(new FileReader(new File(workingDir + "/" + dictionary)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String dictionaryWord;
            while ((dictionaryWord = dictionaryReader.readLine()) != null) {
                if (dictionaryWord.length() == wordToMix.length()) {
                    if (containsAllLetters(dictionaryWord, wordToMix)) {
                        words.add(dictionaryWord);
                        System.out.println("Yay! That's it baby! xD --> " + dictionaryWord);
                    } else
                        System.out.println("Shame! \t\t" + dictionaryWord + "\t\tis not an anagram :/ Try again mate!");
                }
            }
            System.out.println("Anagrams: " + words.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dictionaryReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean containsAllLetters(String toCompare, String baseWord) {
        for (Character c : toCompare.toCharArray()) {
            if (!baseWord.contains(c.toString())) return false;
        }
        return true;
    }
}
