package day_16;

import org.apache.commons.lang.math.IntRange;
import util.InputReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketTranslation {

    private static final String INPUT_FILE = "input_16.txt";

    public static void main(String[] args) {
        new TicketTranslation().analyzeTickets();
    }

    private void analyzeTickets() {
        List<List<String>> ticketInfoInput = InputReader.readInputFileLineGroups(INPUT_FILE);

        List<TicketField> fields = ticketInfoInput.get(0).stream()
                .map(line -> line.split(": "))
                .map(line -> {
                    String name = line[0];
                    String[] ranges = line[1].split(" or ");
                    String[] range1 = ranges[0].split("-");
                    String[] range2 = ranges[1].split("-");
                    return new TicketField(name, new IntRange(Integer.parseInt(range1[0]), Integer.parseInt(range1[1])),
                            new IntRange(Integer.parseInt(range2[0]), Integer.parseInt(range2[1])));
                })
                .collect(Collectors.toList());

        List<Integer> myTicket = Arrays.stream(ticketInfoInput.get(1).get(1).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<List<Integer>> nearbyTickets = ticketInfoInput.get(2).subList(1, ticketInfoInput.get(2).size()).stream()
                .map(ticket -> Arrays.stream(ticket.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        TicketInfo ticketInfo = new TicketInfo(fields, myTicket, nearbyTickets);
        long errorRate = getErrorRate(ticketInfo);

        ticketInfo.setNearbyTickets(getValidTickets(ticketInfo));
        long departureProduct = calculateDepartureProduct(ticketInfo);

        System.out.println("Ticket scanning error rate: " + errorRate);
        System.out.println("Product of departure field values from my ticket: " + departureProduct);
    }

    /**
     * Given information about tickets, calculate the sum of those values in nearby tickets that are not valid for any ticket field.
     *
     * @param ticketInfo contains a list of ticket fields describing valid value ranges, values for my ticket and for nearby tickets
     * @return ticket scanning error rate, representing the sum of those values in nearby tickets that are not valid for any ticket field
     */
    private long getErrorRate(TicketInfo ticketInfo) {
        return ticketInfo.getNearbyTickets().stream()
                .map(ticket -> getInvalidFields(ticketInfo).apply(ticket))
                .flatMap(List::stream)
                .mapToInt(Integer::new)
                .sum();
    }

    /**
     * Given information about tickets, filter only those tickets that contain only valid values.
     *
     * @param ticketInfo contains a list of ticket fields describing valid value ranges, values for my ticket and for nearby tickets
     * @return list of valid tickets
     */
    private List<List<Integer>> getValidTickets(TicketInfo ticketInfo) {
        return ticketInfo.getNearbyTickets().stream()
                .filter(ticket -> getInvalidFields(ticketInfo).apply(ticket).size() == 0)
                .collect(Collectors.toList());
    }

    /**
     * Given a list of ticket field values, filter only those that are not valid for any ticket field.
     *
     * @param ticketInfo contains a list of ticket fields describing valid value ranges, values for my ticket and for nearby tickets
     * @return list of invalid fields
     */
    private Function<List<Integer>, List<Integer>> getInvalidFields(TicketInfo ticketInfo) {
        return ticket -> ticket.stream().filter(val -> ticketInfo.getFields().stream()
                .noneMatch(field -> field.isValidValue(val)))
                .collect(Collectors.toList());
    }

    /**
     * Look for the six fields on your ticket that start with the word departure.
     * What do you get if you multiply those six values together?
     *
     * @param ticketInfo contains a list of ticket fields describing valid value ranges, values for my ticket and for nearby tickets
     * @return product of all values fro ticket fields that start with "departure"
     */
    private long calculateDepartureProduct(TicketInfo ticketInfo) {
        Map<Integer, TicketField> fieldMapping = getFieldMapping(ticketInfo);

        List<Integer> departureFields = fieldMapping.entrySet().stream()
                .filter(entry -> entry.getValue().getName().startsWith("departure"))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return departureFields.stream()
                .mapToLong(position -> ticketInfo.getMyTicket().get(position))
                .boxed()
                .reduce(1L, (a, b) -> a * b);
    }

    /**
     * Using the valid ranges for each field, determine what order the fields appear on the tickets
     *
     * @param ticketInfo contains a list of ticket fields describing valid value ranges, values for my ticket and for nearby tickets
     * @return map that shows correspondence between position in the ticket and field
     */
    private Map<Integer, TicketField> getFieldMapping(TicketInfo ticketInfo) {
        //list of all values in nearby tickets, grouped by position
        List<List<Integer>> positionValues = IntStream.range(0, ticketInfo.getFields().size()).boxed()
                .map(pos -> ticketInfo.getNearbyTickets().stream().map(ticket -> ticket.get(pos)).collect(Collectors.toList()))
                .collect(Collectors.toList());

        // possible field values for each position
        Map<Integer, List<TicketField>> possibleFieldMapping = positionValues.stream()
                .collect(Collectors.toMap(positionValues::indexOf, posValues ->
                        ticketInfo.getFields().stream().filter(field -> posValues.stream().allMatch(field::isValidValue)).collect(Collectors.toList())
                ));

        //exact field values for each position
        Map<Integer, TicketField> fieldMapping = new HashMap<>();
        while (fieldMapping.size() < possibleFieldMapping.size()) {
            possibleFieldMapping.forEach((key, values) -> {
                if (!fieldMapping.containsKey(key)) {
                    if (values.size() > 1) {
                        values.removeIf(fieldMapping::containsValue);
                    }
                    if (values.size() == 1) {
                        fieldMapping.put(key, values.get(0));
                    }
                }
            });
        }

        return fieldMapping;
    }
}
