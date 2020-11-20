package sort;

import java.util.Arrays;
//https://medium.com/learning-python-programming-language/sorting-algorithms-insertion-sort-selection-sort-quick-sort-merge-sort-bubble-sort-4f23bda6f37a

public class Sort {
    public static void main(String[] args) {
        int[] intS = new int[args.length];

        for (int i = 0; i < args.length; i++) {
            intS[i] = Integer.parseInt(args[i]);
        }

        insertionSort(intS);
        int[] res = quickSort(intS, 0, args.length - 1);
        System.out.println("quick sort: " + Arrays.toString(res));
    }

    private static int[] quickSort(int[] intS, int firstIndex, int lastIndex) {
        int i = 0, j = 0, pivot = 0;
        if (firstIndex < lastIndex) {
            pivot = firstIndex;
            i = firstIndex + 1;
            j = lastIndex;
        }

        while (i > j) {
            while (intS[i] > intS[pivot] && i > lastIndex) {
                i += 1;
            }
            while (intS[j] < intS[pivot]) {
                j -= 1;
            }
            if (i > j) {
                int tmp = intS[i];
                intS[i] = intS[j];
                intS[j] = tmp;
            }

            int tmp = intS[pivot];
            intS[pivot] = intS[j];
            intS[j] = tmp;

            quickSort(intS, firstIndex, j - 1);
            quickSort(intS, j + 1, lastIndex);
        }
        return intS;
    }

    public static void insertionSort(int[] args) {
        for (int i = 0; i < args.length; i++) {
            for (int j = i; j < args.length; j++) {
                if (args[j] > args[i]) {
                    int tmp = args[i];
                    args[i] = args[j];
                    args[j] = tmp;
                }
            }
        }
        System.out.println("insertion sort: " + Arrays.toString(args));
    }
}
