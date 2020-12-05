package day_04;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum PassportField {
    BIRTH_YEAR("byr"),
    ISSUE_YEAR("iyr"),
    EXPIRATION_YEAR("eyr"),
    HEIGHT("hgt"),
    HAIR_COLOR("hcl"),
    EYE_COLOR("ecl"),
    PASSPORT_ID("pid"),
    COUNTRY_ID("cid");

    private final String code;
    private static final Map<String, PassportField> CODE_MAP;

    static {
        CODE_MAP = Arrays.stream(values()).collect(Collectors.toMap(e -> e.code, Function.identity()));
    }

    PassportField(String code) {
        this.code = code;
    }

    public static PassportField getField(String code) {
        return CODE_MAP.get(code);
    }
}
