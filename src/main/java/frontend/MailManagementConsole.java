package frontend;

import domain.Employee;
import domain.Mail;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MailManagementConsole extends SystemConsole {
    private int employeeID;
    private static final HelpDisplay help = HelpDisplay.builder()
            .add("send", "send an email to employee(s)")
            .add("inbox", "display inbox contents")
            .add("read", "read contents of an email")
            .add("reply", "Reply to an email")
            .build();

    private static InformationPrompt sendMessagePrompt = InformationPrompt.builder()
            .add("Subject", "subject")
            .add("Recipients (Employee ID, space-separated)", "recipients")
            .build();

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
            case "read":
                return read();
            case "reply":
                return reply();
            case "send":
                return send();
        }
        return false;
    }

    @Override
    protected void displayHelp() {
        help.display();
    }

    @Override
    protected void init() {
        System.out.print("Enter employee ID: ");
        employeeID = Integer.parseInt(scanner.nextLine());
    }

    private boolean reply() {
        System.out.print("Enter a message ID to reply to: ");
        int messageID = Integer.parseInt(scanner.nextLine());

        LinkedList<Mail> inbox = Mail.getInbox(employeeID);
        try {
            Mail mail = inbox.stream()
                    .filter(msg -> msg.getMessageID() == messageID)
                    .findFirst()
                    .get();

            int recipient = mail.getSenderID();
            System.out.print("Subject: ");
            String subject = scanner.nextLine();
            String message = inputMessage();


            return Mail.send(employeeID, subject, message, List.of(recipient));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private String truncateMessage(String message) {
        message = message.replace("\n", " ");
        if (message.length() > 20) {
            return message.substring(0, 18) + "..";
        }
        return message;
    }

    private String truncateSubject(String subject) {
        subject = subject.replace("\n", " ");
        if (subject.length() > 20) {
            return subject.substring(0, 18);
        }
        return subject;
    }

    private boolean read() {
        System.out.println("Enter message ID to read: ");
        int messageID = Integer.parseInt(scanner.nextLine());
        LinkedList<Mail> inbox = Mail.getInbox(employeeID);

        try {
            Mail message = inbox.stream()
                    .filter(msg -> msg.getMessageID() == messageID)
                    .findFirst()
                    .get();

            System.out.println(message.getMessage());
            Mail.markReadStatus(employeeID, messageID, Mail.ReadStatus.READ);
            return true;
            // Need to also update read status
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean inbox() {
        LinkedList<Mail> inbox = Mail.getInbox(employeeID);
        if (inbox.size() == 0) {
            System.out.println("No mail!");
            return true;
        }

        TableDisplay table = TableDisplay.builder(inbox.size(), 5)
                .build();

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
                    String.format("[%s]", truncateSubject(mail.getSubject())),
                    truncateMessage(mail.getMessage())
            ));
        }

        table.display();

        return true;
    }

    private String inputMessage() {
        System.out.println("Type '!' on its own line and hit enter to send.");
        StringBuilder message = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("!")) {
            message.append(line + "\n");
        }

        return message.toString();
    }

    private boolean send() {
        //System.out.print("Subject: ");
        //String subject = scanner.nextLine();
        HashMap<String, String> answers = sendMessagePrompt.prompt(scanner);

        /*
        StringBuilder message = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("!")) {
            message.append(line + "\n");
        }
         */
        String message = inputMessage();

        List<Integer> recipients = Arrays.stream(answers.get("recipients").split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Mail.send(
                employeeID,
                answers.get("subject"),
                message.toString(),
                recipients
        );
        return true;
    }
}
