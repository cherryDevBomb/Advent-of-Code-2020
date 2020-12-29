package day_16;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.IntRange;
import util.InputReader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        System.out.println("Ticket scanning error rate: " + errorRate);
    }

    /**
     * Given information about tickets, calculate the sum of those values in nearby tickets that are not valid for any ticket field.
     *
     * @param ticketInfo contains a list of ticket fields describing valid value ranges, values for my ticket and for nearby tickets
     * @return ticket scanning error rate, representing the sum of those values in nearby tickets that are not valid for any ticket field
     */
    private long getErrorRate(TicketInfo ticketInfo) {
        return ticketInfo.getNearbyTickets().stream()
                .map(ticket -> ticket.stream().filter(val -> ticketInfo.getFields().stream()
                        .allMatch(field -> !field.getFirstRange().containsInteger(val) && !field.getSecondRange().containsInteger(val)))
                        .collect(Collectors.toList()))
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(List::stream)
                .mapToInt(Integer::new)
                .sum();
    }
}
