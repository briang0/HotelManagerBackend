package frontend;

import java.util.Scanner;

abstract public class SystemConsole {
    protected Scanner scanner;

    abstract String getSystemName();
    protected abstract boolean executeCommand(String command);
    protected abstract void displayHelp();

    public SystemConsole(Scanner scanner) {
        this.scanner = scanner;
    }

    void run() {
        while (true) {
            System.out.print(getSystemName() + " > ");
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                break;
            } else if (command.equals("help")) {
                displayHelp();
                continue;
            }
            // maybe add help() check here..

            if (!executeCommand(command)) {
                System.out.println("Unrecognized command.");
            }
        }
    }
}
