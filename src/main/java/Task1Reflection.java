import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Task1Reflection {
    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());

        String literalStr = "Початковий літерал";
        System.out.println("=== Спосіб 1: Строковий літерал ===");
        System.out.println("ДО зміни: " + literalStr);
        modifyString(literalStr, "Змінений літерал!");
        System.out.println("ПІСЛЯ зміни: " + literalStr);

        System.out.println("\n=== Спосіб 2: Введення з клавіатури ===");
        System.out.print("Введіть початковий рядок: ");
        String inputStr = scanner.nextLine();
        System.out.print("Введіть нове значення для заміни: ");
        String replacement = scanner.nextLine();

        System.out.println("ДО зміни: " + inputStr);
        modifyString(inputStr, replacement);
        System.out.println("ПІСЛЯ зміни: " + inputStr);
    }

    private static void modifyString(String target, String newValue) {
        try {
            Field valueField = String.class.getDeclaredField("value");
            valueField.setAccessible(true);
            valueField.set(target, valueField.get(newValue));

            try {
                Field coderField = String.class.getDeclaredField("coder");
                coderField.setAccessible(true);
                coderField.set(target, coderField.get(newValue));
            } catch (NoSuchFieldException ignored) {
                
            }
        } catch (Exception e) {
            System.out.println("Помилка рефлексії: " + e.getMessage());
        }
    }
}