package day_02;

import util.InputReader;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PasswordPhilosophy {

    private static final String INPUT_FILE = "day_02/input.txt";

    public static void main(String[] args) {
        new PasswordPhilosophy().countValidPasswords();
    }

    private void countValidPasswords() {
        List<Password> passwords = readInput();
        System.out.println("Number of valid passwords by first policy: " + countValidByPolicy(passwords, Password::isFirstPolicyValid));
        System.out.println("Number of valid passwords by second policy: " + countValidByPolicy(passwords, Password::isSecondPolicyValid));
    }

    private long countValidByPolicy(List<Password> passwords, Predicate<Password> policy) {
        return passwords.stream().filter(policy).count();
    }

    private List<Password> readInput() {
        return InputReader.readInputFile(INPUT_FILE)
                .stream()
                .map(line -> line.replace("-", " "))
                .map(line -> line.replace(":", ""))
                .map(line -> line.split(" "))
                .map(elems -> new Password(Integer.parseInt(elems[0]), Integer.parseInt(elems[1]), elems[2].charAt(0), elems[3]))
                .collect(Collectors.toList());
    }
}
