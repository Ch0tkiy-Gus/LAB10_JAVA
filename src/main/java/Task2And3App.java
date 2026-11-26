import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Task2And3App {
    private static final Logger logger = Logger.getLogger(Task2And3App.class.getName());
    private static ResourceBundle bundle;

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupLogger();
        setLocale(new Locale("uk"));

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
        logger.info("Програму запущено.");

        boolean running = true;
        while (running) {
            System.out.println(bundle.getString("menu.title"));
            System.out.println(bundle.getString("menu.encrypt"));
            System.out.println(bundle.getString("menu.decrypt"));
            System.out.println(bundle.getString("menu.lang"));
            System.out.println(bundle.getString("menu.exit"));
            System.out.print(bundle.getString("menu.choose"));

            String choice = scanner.nextLine().trim();
            logger.fine("Користувач обрав пункт меню: " + choice);

            switch (choice) {
                case "1":
                    processText(scanner, true);
                    break;
                case "2":
                    processText(scanner, false);
                    break;
                case "3":
                    changeLanguage(scanner);
                    break;
                case "0":
                    System.out.println(bundle.getString("exit.msg"));
                    logger.info("Користувач завершив роботу програми.");
                    running = false;
                    break;
                default:
                    System.out.println(bundle.getString("error.invalid_choice"));
                    logger.warning("Введено невірний пункт меню: " + choice);
            }
        }
    }

    private static void processText(Scanner scanner, boolean encrypt) {
        System.out.print(bundle.getString("prompt.text"));
        String text = scanner.nextLine();
        System.out.print(bundle.getString("prompt.key"));
        String keyStr = scanner.nextLine().trim();

        try {
            int key = Integer.parseInt(keyStr);
            int shift = encrypt ? key : -key;
            StringBuilder result = new StringBuilder();
            for (char c : text.toCharArray()) {
                result.append((char) (c + shift));
            }
            if (encrypt) {
                System.out.println(bundle.getString("result.encrypted") + result);
                logger.info("Виконано шифрування тексту (ключ=" + key + ").");
            } else {
                System.out.println(bundle.getString("result.decrypted") + result);
                logger.info("Виконано розшифрування тексту (ключ=" + key + ").");
            }
        } catch (NumberFormatException e) {
            System.out.println(bundle.getString("error.invalid_number"));
            logger.severe("Помилка парсингу ключа '" + keyStr + "': " + e.getMessage());
        }
    }

    private static void changeLanguage(Scanner scanner) {
        System.out.print(bundle.getString("lang.prompt"));
        String langChoice = scanner.nextLine().trim();
        if ("2".equals(langChoice)) {
            setLocale(new Locale("en"));
            logger.info("Інтерфейс перемкнуто на англійську мову (EN).");
        } else {
            setLocale(new Locale("uk"));
            logger.info("Інтерфейс перемкнуто на українську мову (UK).");
        }
        System.out.println(bundle.getString("lang.changed"));
    }

    private static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("location.messages", locale);
    }

    private static void setupLogger() {
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.WARNING);
        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setLevel(Level.FINE);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.out.println("Не вдалося створити файл логів: " + e.getMessage());
        }
    }
}