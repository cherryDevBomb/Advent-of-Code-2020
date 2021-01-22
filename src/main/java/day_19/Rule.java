package day_19;

import java.util.Arrays;
import java.util.List;

public class Rule {

    private List<String> lexeme;
    private List<List<String>> composedRules;
    private boolean isFinal;

    public Rule(String lexeme) {
        this.lexeme = Arrays.asList(lexeme);
        this.isFinal = true;
    }

    public Rule(List<List<String>> composedRules) {
        this.composedRules = composedRules;
        this.isFinal = false;
    }

    public List<String> getLexeme() {
        return lexeme;
    }

    public List<List<String>> getComposedRules() {
        return composedRules;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setLexeme(List<String> lexeme) {
        this.lexeme = lexeme;
    }

    public void setComposedRules(List<List<String>> composedRules) {
        this.composedRules = composedRules;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }
}
