import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        var fileContent = new ArrayList<String>();

        try (var fileReader = new Scanner(new File("input.txt"))) {
            while (fileReader.hasNextLine()) {
                fileContent.add(fileReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // PART 1

        // Rows
        int rowTotal = 0;
        for (String row : fileContent) {
            for (int colIdx = 0; colIdx < row.length() - 3; colIdx++) {
                String window = row.substring(colIdx, colIdx + 4);
                if (window.equals("XMAS") || window.equals("SAMX")) {
                    rowTotal++;
                }
            }
        }


        // Cols
        int colTotal = 0;
        for (int colIdx = 0; colIdx < fileContent.getFirst().length(); colIdx++) {
            for (int rowIdx = 0; rowIdx < fileContent.size() - 3; rowIdx++) {
                String window = new StringBuilder()
                        .append(fileContent.get(rowIdx).charAt(colIdx))
                        .append(fileContent.get(rowIdx + 1).charAt(colIdx))
                        .append(fileContent.get(rowIdx + 2).charAt(colIdx))
                        .append(fileContent.get(rowIdx + 3).charAt(colIdx))
                        .toString();
                if (window.equals("XMAS") || window.equals("SAMX")) {
                    colTotal++;
                }
            }
        }

        // Diag 1
        int diagTotal = 0;
        for (int rowIdx = 0; rowIdx < fileContent.size() - 3; rowIdx++) {
            for (int colIdx = 0; colIdx < fileContent.get(rowIdx).length() - 3; colIdx++) {
                String window = new StringBuilder()
                        .append(fileContent.get(rowIdx).charAt(colIdx))
                        .append(fileContent.get(rowIdx + 1).charAt(colIdx + 1))
                        .append(fileContent.get(rowIdx + 2).charAt(colIdx + 2))
                        .append(fileContent.get(rowIdx + 3).charAt(colIdx + 3))
                        .toString();
//                System.out.printf("row %d col %d: %s\n", rowIdx, colIdx, window);
                if (window.equals("XMAS") || window.equals("SAMX")) {
                    diagTotal++;
                }
            }
        }

        // Diag 2
        for (int rowIdx = 0; rowIdx < fileContent.size() - 3; rowIdx++) {
            for (int colIdx = 3; colIdx < fileContent.get(rowIdx).length(); colIdx++) {
                String window = new StringBuilder()
                        .append(fileContent.get(rowIdx).charAt(colIdx))
                        .append(fileContent.get(rowIdx + 1).charAt(colIdx - 1))
                        .append(fileContent.get(rowIdx + 2).charAt(colIdx - 2))
                        .append(fileContent.get(rowIdx + 3).charAt(colIdx - 3))
                        .toString();
//                System.out.printf("row %d col %d: %s\n", rowIdx, colIdx, window);
                if (window.equals("XMAS") || window.equals("SAMX")) {
                    diagTotal++;
                }
            }
        }

        System.out.printf("Row total: %d\nColumn total: %d\nDiag total: %d\n", rowTotal, colTotal, diagTotal);
        System.out.printf("Total: %d\n", rowTotal + colTotal + diagTotal);

        // PART 2
        int x_masCount = 0;
        for (int rowIdx = 1; rowIdx < fileContent.size() - 1; rowIdx++) {
            for (int colIdx = 1; colIdx < fileContent.get(rowIdx).length() - 1; colIdx++) {
                if (fileContent.get(rowIdx).charAt(colIdx) != 'A') {
                    continue;
                }

                Pattern pattern = Pattern.compile("^(MAS|SAM)(MAS|SAM)$");
                String diag = new StringBuilder()
                        .append(fileContent.get(rowIdx-1).charAt(colIdx-1))
                        .append(fileContent.get(rowIdx).charAt(colIdx))
                        .append(fileContent.get(rowIdx+1).charAt(colIdx+1))
                        .append(fileContent.get(rowIdx-1).charAt(colIdx+1))
                        .append(fileContent.get(rowIdx).charAt(colIdx))
                        .append(fileContent.get(rowIdx+1).charAt(colIdx-1))
                        .toString();

                var matcher = pattern.matcher(diag);
                if (matcher.find()) {
                    x_masCount++;
                }
            }
        }

        System.out.printf("X-MAS count: %d\n", x_masCount);
    }

}