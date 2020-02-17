package org.bergen.atcs.atics.lambda;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static org.bergen.atcs.atics.lambda.Parser.makeTree;
import static org.bergen.atcs.atics.lambda.Parser.parse;

public class Interpreter {
    public static void main(String[] args) {
        HashMap<String, Expression> variables = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        Pattern assignPattern = Pattern.compile("\\s*(\\S+)\\s*=(.*)");
        Pattern runPattern = Pattern.compile("\\s*run(.*)");
        Pattern popPattern = Pattern.compile("\\s*populate\\s*(\\d*)\\s*(\\d*)\\s*");

        while (true) {
            String in = scanner.nextLine()
                    .replaceAll("λ", "\\\\")
                    .replaceAll("\uFEFF", "");

            in = in.split(";")[0];
            if (in.matches("\\s*exit\\s*")) break;
            if (in.matches("\\s*")) continue;

            Matcher popMatch = popPattern.matcher(in);
            if (popMatch.find()) {
                int low = Integer.parseInt(popMatch.group(1));
                int high = Integer.parseInt(popMatch.group(2));

                for (int i = low; i <= high; i++) {
                    if (variables.containsKey(String.valueOf(i))) continue;

                    StringBuilder calls = new StringBuilder();
                    StringBuilder closes = new StringBuilder();
                    for (int j = 0; j < i; j++) {
                        calls.append("(f ");
                        closes.append(")");
                    }
                    String number = "\\f.\\x." + calls + "x" + closes;
                    variables.put(String.valueOf(i), makeTree(parse(number)).convert());
                }

                System.out.printf("Populated numbers %d to %d\n", low, high);
                continue;
            }

            String assignTo = null;
            boolean shouldRun = false;

            Matcher assignMatch = assignPattern.matcher(in);
            if (assignMatch.find()) {
                assignTo = assignMatch.group(1);
                if (variables.containsKey(assignTo)) {
                    System.out.printf("%s is already defined\n", assignTo);
                    continue;
                }
                in = assignMatch.group(2);
            }

            Matcher runMatch = runPattern.matcher(in);
            if (runMatch.find()) {
                shouldRun = true;
                in = runMatch.group(1);
            }

            Expression exp = makeTree(parse(in)).convert(new HashMap<>(), variables);
            if (shouldRun) exp = exp.run();

            if (assignTo == null) {
                Expression finalExp = exp;
                String[] matches = variables.entrySet().stream()
                        .filter(x -> finalExp.equalsExp(x.getValue()))
                        .map(Map.Entry::getKey)
                        .toArray(String[]::new);

                if (matches.length == 0) {
                    System.out.println(exp.expToString());
                } else if (matches.length == 1) {
                    System.out.println(matches[0]);
                } else {
                    System.out.println(Arrays.toString(matches));
                }
            } else {
                variables.put(assignTo, exp);
                System.out.printf("Added %s as %s\n", exp.expToString(), assignTo);
            }
        }

        System.out.println("Goodbye!");
    }
}