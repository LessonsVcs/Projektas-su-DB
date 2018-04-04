package extras;

import java.util.Scanner;

public class ScannerUntils {
    public static final Scanner scanner = new Scanner(System.in);

    public static String scanString(String question) {
        System.out.println(question);
        return scanner.nextLine();
    }
}
