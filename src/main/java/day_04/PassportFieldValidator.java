package day_04;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

public class PassportFieldValidator {

    private static final String HEX_COLOR_PATTERN = "#[0-9a-f]{6}";
    private static final List<String> EYE_COLORS = Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    /**
     * Validate the correctness of a field value based on the following rules:
     * byr (Birth Year) - four digits; at least 1920 and at most 2002.
     * iyr (Issue Year) - four digits; at least 2010 and at most 2020.
     * eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
     * hgt (Height) - a number followed by either cm or in:
     * If cm, the number must be at least 150 and at most 193.
     * If in, the number must be at least 59 and at most 76.
     * hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
     * ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
     * pid (Passport ID) - a nine-digit number, including leading zeroes.
     * cid (Country ID) - ignored, missing or not.
     *
     * @param field key-value dictionary field pair
     * @return true if field is valid, false otherwise
     */
    public static boolean isValidField(Entry<PassportField, String> field) {
        switch (field.getKey()) {
            case BIRTH_YEAR:
                return field.getValue().length() == 4 && isNumberBetween(field.getValue(), 1920, 2002);
            case ISSUE_YEAR:
                return field.getValue().length() == 4 && isNumberBetween(field.getValue(), 2010, 2020);
            case EXPIRATION_YEAR:
                return field.getValue().length() == 4 && isNumberBetween(field.getValue(), 2020, 2030);
            case HEIGHT:
                return field.getValue().length() > 2 && isValidHeight(field.getValue());
            case HAIR_COLOR:
                return field.getValue().matches(HEX_COLOR_PATTERN);
            case EYE_COLOR:
                return EYE_COLORS.contains(field.getValue());
            case PASSPORT_ID:
                return StringUtils.isNumeric(field.getValue()) && field.getValue().length() == 9;
            default:
                return true;
        }
    }

    public static boolean isNumberBetween(String value, int min, int max) {
        return StringUtils.isNumeric(value) && Integer.parseInt(value) >= min && Integer.parseInt(value) <= max;
    }

    public static boolean isValidHeight(String height) {
        String value = height.substring(0, height.length() - 2);
        String unit = height.substring(height.length() - 2);

        return ("cm".equals(unit) && isNumberBetween(value, 150, 193)) ||
                ("in".equals(unit) && isNumberBetween(value, 59, 76));
    }
}
