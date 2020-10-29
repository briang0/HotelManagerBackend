package frontend;

import java.util.Scanner;

/**
 * An abstract class representing a system console, which the user interacts
 * with to execute commands.
 *
 * @author Collin
 */
abstract public class SystemConsole {
    protected Scanner scanner;

    abstract String getSystemName();

    protected abstract boolean executeCommand(String command);
    protected abstract void displayHelp();

    public SystemConsole(Scanner scanner) {
        this.scanner = scanner;
    }

    protected void init() {}

    /**
     * Load the console prompt and begin executing user commands.
     */
    void run() {
        init();
        while (true) {
            System.out.print(getSystemName() + " > ");
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                break;
            } else if (command.equals("help")) {
                System.out.println("Commands:");
                displayHelp();
                continue;
            }

            if (!executeCommand(command)) {
                System.out.println("Unrecognized command.");
            }
        }
    }
}