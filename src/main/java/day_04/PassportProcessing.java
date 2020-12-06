package day_04;

import org.apache.commons.lang3.StringUtils;
import util.InputReader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PassportProcessing {

    private static final String INPUT_FILE = "input_04.txt";

    private final PassportField OPTIONAL_FIELD = PassportField.COUNTRY_ID;

    public static void main(String[] args) {
        new PassportProcessing().countValidPassports();
    }

    private void countValidPassports() {
        List<Map<PassportField, String>> parsedPassports = readPassportData();

        List<Map<PassportField, String>> passportsWithAllRequiredFields = getValidPassports(parsedPassports);
        List<Map<PassportField, String>> passportsWithValidData = getPassportsWithValidData(passportsWithAllRequiredFields);

        System.out.println("Number of valid passports: " + passportsWithAllRequiredFields.size());
        System.out.println("Number of passports containing only valid data: " + passportsWithValidData.size());
    }

    /**
     * Given a list of passports, filter only those that contain all required fields
     *
     * @param passports initial list of passports
     * @return passports hat contain all required fields
     */
    private List<Map<PassportField, String>> getValidPassports(List<Map<PassportField, String>> passports) {
        return passports.stream()
                .filter(passport -> Arrays.stream(PassportField.values())
                        .filter(field -> field != OPTIONAL_FIELD)
                        .allMatch(passport::containsKey))
                .collect(Collectors.toList());
    }

    /**
     * Given a list of passports, filter only those that contain valid data for all fields
     *
     * @param passports initial list of passports
     * @return passports hat contain valid data for all fields
     */
    private List<Map<PassportField, String>> getPassportsWithValidData(List<Map<PassportField, String>> passports) {
        return passports.stream()
                .filter(passport -> passport.entrySet()
                        .stream()
                        .allMatch(PassportFieldValidator::isValidField))
                .collect(Collectors.toList());
    }

    /**
     * Convert each string of passport data into a Map
     *
     * @return a List of passports represented as Map<PassportField, String>
     */
    private List<Map<PassportField, String>> readPassportData() {
        return InputReader.readInputFileChunks(INPUT_FILE)
                .stream()
                .map(passport -> passport.split(" "))
                .map(fieldList -> Arrays.stream(fieldList)
                        .filter(StringUtils::isNotBlank)
                        .map(pair -> pair.split(":"))
                        .collect(Collectors.toMap(p -> PassportField.getField(p[0]), p -> p[1])))
                .collect(Collectors.toList());
    }
}
