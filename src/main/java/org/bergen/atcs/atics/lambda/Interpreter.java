package org.bergen.atcs.atics.lambda;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bergen.atcs.atics.lambda.Parser.makeTree;
import static org.bergen.atcs.atics.lambda.Parser.parse;

/**
 * Provides a REPL environment for evaluating lambda expressions.
 */
public class Interpreter {
    public static void main(String[] args) {
        HashMap<String, Expression> variables = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
			System.out.print("> ");
            String in = scanner.nextLine()
                    .replaceAll("λ", "\\\\")
                    .replaceAll("\uFEFF", "");

            in = in.split(";")[0];
            if (in.matches("\\s*exit\\s*")) break;
            if (in.matches("\\s*")) continue;

            // This searches for the word `populate`, followed by two numbers
            Pattern popPattern = Pattern.compile("^\\s*populate\\s+(\\d+)\\s+(\\d+)\\s*$");
            Matcher popMatch = popPattern.matcher(in);
            if (popMatch.find()) {
                int low = Integer.parseInt(popMatch.group(1));
                int high = Integer.parseInt(popMatch.group(2));

                Lambda succ = (Lambda) makeTree(parse("\\n.\\f.\\x.f (n f x)")).convert();
                Expression num = makeTree(parse("\\f.\\x.x")).convert();

                for (int i = 0; i <= high; i++) {
                    if (i >= low) {
                        if (variables.containsKey(String.valueOf(i))) {
                            System.out.printf("%d is already defined\n", i);
                        } else {
                            variables.put(String.valueOf(i), num);
                        }
                    }
                    num = Expression.run(new Application(succ, num));
                }

                System.out.printf("Populated numbers %d to %d\n", low, high);
                continue;
            }

            // This checks if the input is the name of an existing variable
            // In this case, we want to print the stored value and skip further processing
            if (variables.containsKey(in.trim())) {
                System.out.println(variables.get(in.trim()).expToString());
                continue;
            }

            String assignTo = null;
            boolean shouldRun = false;

            // This searches for a `=` and a variable name to store the exp at
            Pattern assignPattern = Pattern.compile("^\\s*(\\S+)\\s*=(.+)$");
            Matcher assignMatch = assignPattern.matcher(in);
            if (assignMatch.find()) {
                assignTo = assignMatch.group(1);
                if (variables.containsKey(assignTo)) {
                    System.out.printf("%s is already defined\n", assignTo);
                    continue;
                }
                in = assignMatch.group(2);
            }

            // This checks if the user wants to run the expression
            Pattern runPattern = Pattern.compile("^\\s*run\\s+(.+)$");
            Matcher runMatch = runPattern.matcher(in);
            if (runMatch.find()) {
                shouldRun = true;
                in = runMatch.group(1);
            }

            Expression exp = makeTree(parse(in)).convert(new HashMap<>(), variables);
            if (shouldRun) exp = Expression.run(exp);

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
