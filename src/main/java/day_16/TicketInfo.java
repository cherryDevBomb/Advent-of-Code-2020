package day_16;

import java.util.List;

public class TicketInfo {

    private List<TicketField> fields;
    private List<Integer> myTicket;
    private List<List<Integer>> nearbyTickets;

    public TicketInfo(List<TicketField> fields, List<Integer> myTicket, List<List<Integer>> nearbyTickets) {
        this.fields = fields;
        this.myTicket = myTicket;
        this.nearbyTickets = nearbyTickets;
    }

    public List<TicketField> getFields() {
        return fields;
    }

    public List<Integer> getMyTicket() {
        return myTicket;
    }

    public List<List<Integer>> getNearbyTickets() {
        return nearbyTickets;
    }

    public void setNearbyTickets(List<List<Integer>> nearbyTickets) {
        this.nearbyTickets = nearbyTickets;
    }
}
