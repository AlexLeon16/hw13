package ru.netology.stats;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AviaSoulsTest {

    @Test
    public void testTicketCompareTo() {
        Ticket cheaper = new Ticket("DME", "LED", 5000, 10, 12);
        Ticket moreExpensive = new Ticket("DME", "LED", 8000, 14, 16);

        assertTrue(cheaper.compareTo(moreExpensive) < 0, "Более дешевый билет должен быть меньше");
        assertTrue(moreExpensive.compareTo(cheaper) > 0, "Более дорогой билет должен быть больше");

        Ticket samePrice = new Ticket("DME", "LED", 5000, 15, 17);
        assertEquals(0, cheaper.compareTo(samePrice), "Билеты с одинаковой ценой должны быть равны");
    }

    @Test
    public void testTicketTimeComparator() {
        Ticket shorterFlight = new Ticket("DME", "LED", 5000, 10, 12); // 2 часа
        Ticket longerFlight = new Ticket("DME", "LED", 5000, 10, 15); // 5 часов

        TicketTimeComparator comparator = new TicketTimeComparator();

        assertTrue(comparator.compare(shorterFlight, longerFlight) < 0, "Более короткий полет должен быть меньше");
        assertTrue(comparator.compare(longerFlight, shorterFlight) > 0, "Более длинный полет должен быть больше");

        Ticket sameDuration = new Ticket("DME", "LED", 6000, 20, 22); // 2 часа
        assertEquals(0, comparator.compare(shorterFlight, sameDuration), "Билеты с одинаковой длительностью должны быть равны");
    }

    @Test
    public void testSearchSortedByPrice() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 8000, 10, 12);
        Ticket ticket2 = new Ticket("DME", "LED", 5000, 14, 16);
        Ticket ticket3 = new Ticket("DME", "LED", 12000, 8, 10);
        Ticket ticket4 = new Ticket("SVO", "LED", 7000, 18, 20); // другой маршрут

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);
        manager.add(ticket4);

        Ticket[] result = manager.search("DME", "LED");

        assertEquals(3, result.length, "Должно найти 3 билета по маршруту DME-LED");

        // Ожидаемый порядок: ticket2 (5000), ticket1 (8000), ticket3 (12000)
        Ticket[] expected = {ticket2, ticket1, ticket3};
        assertArrayEquals(expected, result, "Билеты должны быть отсортированы по цене");
    }

    @Test
    public void testSearchAndSortByTime() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 10, 15); // 5 часов
        Ticket ticket2 = new Ticket("DME", "LED", 5000, 14, 16); // 2 часа
        Ticket ticket3 = new Ticket("DME", "LED", 5000, 8, 12); // 4 часа
        Ticket ticket4 = new Ticket("SVO", "LED", 5000, 18, 20); // другой маршрут

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);
        manager.add(ticket4);

        TicketTimeComparator timeComparator = new TicketTimeComparator();
        Ticket[] result = manager.searchAndSortBy("DME", "LED", timeComparator);

        assertEquals(3, result.length, "Должно найти 3 билета по маршруту DME-LED");

        // Ожидаемый порядок: ticket2 (2 часа), ticket3 (4 часа), ticket1 (5 часов)
        Ticket[] expected = {ticket2, ticket3, ticket1};
        assertArrayEquals(expected, result, "Билеты должны быть отсортированы по времени полета");
    }

    @Test
    public void testSearchNoResults() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 10, 12);
        Ticket ticket2 = new Ticket("SVO", "AER", 7000, 14, 18);

        manager.add(ticket1);
        manager.add(ticket2);

        Ticket[] result = manager.search("VKO", "LED");
        Ticket[] expected = new Ticket[0];
        assertArrayEquals(expected, result, "Не должно быть результатов для несуществующего маршрута");

        Ticket[] resultSorted = manager.searchAndSortBy("VKO", "LED", new TicketTimeComparator());
        assertArrayEquals(expected, resultSorted, "Не должно быть результатов для несуществующего маршрута (с сортировкой)");
    }

    @Test
    public void testFlightTimeCalculation() {
        Ticket ticket = new Ticket("DME", "LED", 5000, 10, 15);
        assertEquals(5, ticket.getFlightTime(), "Время полета должно быть 5 часов");

        // Для ночных перелетов время может быть отрицательным, если не учитывать смену суток
        Ticket overnight = new Ticket("DME", "LED", 5000, 22, 2);
        assertEquals(-20, overnight.getFlightTime(), "Ночной перелет: время вылета больше времени прилета");
    }

    @Test
    public void testSearchSingleTicket() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket = new Ticket("DME", "LED", 5000, 10, 12);
        manager.add(ticket);

        Ticket[] result = manager.search("DME", "LED");
        Ticket[] expected = {ticket};
        assertArrayEquals(expected, result, "Должен найти единственный подходящий билет");
    }

    @Test
    public void testSearchDifferentRoutes() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 10, 12);
        Ticket ticket2 = new Ticket("SVO", "LED", 6000, 14, 16);
        Ticket ticket3 = new Ticket("DME", "AER", 7000, 18, 20);
        Ticket ticket4 = new Ticket("DME", "LED", 8000, 22, 24);

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);
        manager.add(ticket4);

        Ticket[] result = manager.search("DME", "LED");
        Ticket[] expected = {ticket1, ticket4}; // по возрастанию цены
        assertArrayEquals(expected, result, "Должны найти только билеты DME-LED, отсортированные по цене");
    }

    @Test
    public void testSearchEmptyManager() {
        AviaSouls manager = new AviaSouls();

        Ticket[] result = manager.search("DME", "LED");
        Ticket[] expected = new Ticket[0];
        assertArrayEquals(expected, result, "Для пустого менеджера должен вернуть пустой массив");
    }
}