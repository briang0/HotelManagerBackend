package frontend;

import domain.Employee;
import domain.Mail;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MailManagementConsole extends SystemConsole {
    private int employeeID;

    public MailManagementConsole(Scanner scanner) {
        super(scanner);
    }

    @Override
    String getSystemName() {
        return "Mail";
    }

    @Override
    protected boolean executeCommand(String command) {
        switch (command) {
            case "inbox":
                return inbox();
        }
        return false;
    }

    @Override
    protected void displayHelp() {

    }

    @Override
    protected void init() {
        System.out.print("Enter employee ID: ");
        employeeID = Integer.parseInt(scanner.nextLine());
    }

    private String truncateMessage(String message) {
        if (message.length() > 20) {
            return message.substring(0, 18) + "..";
        }
        return message;
    }

    private boolean inbox() {
        LinkedList<Mail> inbox = Mail.getInbox(employeeID);
        //TableDisplay table = new TableDisplay(inbox.size(), 4);
        TableDisplay table = TableDisplay.builder(inbox.size(), 5)
                .build();
        /*
        for (Mail mail : inbox) {
            System.out.printf("%s%d %d %d %s\n", mail.isRead() ? "" : "* ", mail.getMessageID(), mail.getTimestamp(),
                    mail.getSenderID(), truncateMessage(mail.getMessage()));
        }
         */
        for (int i = 0; i < inbox.size(); i++) {
            Mail mail = inbox.get(i);
            String employeeName;

            try {
                Employee employee = Employee.get(mail.getSenderID());
                employeeName = employee.getFirstName() + " " + employee.getLastName();
            } catch (NoSuchElementException e) {
                employeeName = "Unknown";
            }

            table.setRow(i, List.of(
                    (mail.isRead() ? "" : "* ") + mail.getMessageID(),
                    LocalDateTime.ofEpochSecond(mail.getTimestamp(), 0,
                            ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now())).format(DateTimeFormatter.ofPattern("MM/dd kk:mm")),
                    String.format("'%s @%d'", employeeName, employeeID),
                    //truncaseMessage("") + ""[]",
                    "[]",
                    truncateMessage(mail.getMessage())
            ));
        }

        table.display();

        return true;
    }
}
