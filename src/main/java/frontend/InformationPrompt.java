package frontend;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class InformationPrompt {
    private LinkedHashMap<String, String> prompts;

    public static class InformationPromptBuilder {
        private LinkedHashMap<String, String> prompts = new LinkedHashMap<>();

        private InformationPromptBuilder() {}

        public InformationPromptBuilder add(String prompt, String key) {
            prompts.put(key, prompt);
            return this;
        }

        public InformationPrompt build() {
            return new InformationPrompt(prompts);
        }
    }

    private InformationPrompt(LinkedHashMap<String, String> prompts) {
        this.prompts = prompts;
    }

    public static InformationPromptBuilder builder() {
        return new InformationPromptBuilder();
    }

    public HashMap<String, String> prompt(Scanner scanner) {
        HashMap<String, String> answers = new HashMap<>();
        // Uniform padding here too? Maybe pad prompts to the right..? (So left pad)
        int padding = prompts.values().stream()
                .reduce((acc, item) -> acc.length() > item.length() ? acc : item)
                .get()
                .length();

        System.out.println("Please enter the following information:");
        for (String key : prompts.keySet()) {
            System.out.printf("%" + padding + "s: ", prompts.get(key));
            answers.put(key, scanner.nextLine());
        }

        return answers;
    }
}