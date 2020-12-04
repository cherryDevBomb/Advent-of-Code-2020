package day_02;

public class Password {
    private int minOccurrences;
    private int maxOccurrences;
    private char referenceChar;
    private String password;

    public Password(int minOccurrences, int maxOccurrences, char policyChar, String password) {
        this.minOccurrences = minOccurrences;
        this.maxOccurrences = maxOccurrences;
        this.referenceChar = policyChar;
        this.password = password;
    }

    /**
     * Check if the password has at least minOccurrences and at most maxOccurrences of referenceChar.
     *
     * @return true if the password meets the requirement, false otherwise
     */
    public boolean isFirstPolicyValid() {
        long occurrences = password.chars().filter(c -> c == referenceChar).count();
        return (occurrences >= minOccurrences) && (occurrences <= maxOccurrences);
    }

    /**
     * Check if exactly one of the positions minOccurrences or maxOccurrences contain referenceChar.
     * Positions are indexed from 1.
     *
     * @return true if the password meets the requirement, false otherwise
     */
    public boolean isSecondPolicyValid() {
        return password.charAt(minOccurrences - 1) == referenceChar ^ password.charAt(maxOccurrences - 1) == referenceChar;
    }

    public int getMinOccurrences() {
        return minOccurrences;
    }

    public void setMinOccurrences(int minOccurrences) {
        this.minOccurrences = minOccurrences;
    }

    public int getMaxOccurrences() {
        return maxOccurrences;
    }

    public void setMaxOccurrences(int maxOccurrences) {
        this.maxOccurrences = maxOccurrences;
    }

    public char getReferenceChar() {
        return referenceChar;
    }

    public void setReferenceChar(char referenceChar) {
        this.referenceChar = referenceChar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
