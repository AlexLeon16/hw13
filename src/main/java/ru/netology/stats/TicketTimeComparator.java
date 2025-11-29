package ru.netology.stats;

import java.util.Comparator;

public class TicketTimeComparator implements Comparator<Ticket> {

    @Override
    public int compare(Ticket t1, Ticket t2) {
        int flightTime1 = t1.getFlightTime();
        int flightTime2 = t2.getFlightTime();
        return Integer.compare(flightTime1, flightTime2);
    }
}