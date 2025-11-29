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
        assertEquals(ticket2, result[0], "Первый билет должен быть самым дешевым");
        assertEquals(ticket1, result[1], "Второй билет должен быть по цене между");
        assertEquals(ticket3, result[2], "Третий билет должен быть самым дорогим");

        // Проверка порядка цен
        assertTrue(result[0].getPrice() <= result[1].getPrice(), "Цены должны быть отсортированы по возрастанию");
        assertTrue(result[1].getPrice() <= result[2].getPrice(), "Цены должны быть отсортированы по возрастанию");
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
        assertEquals(ticket2, result[0], "Первый билет должен быть с самым коротким временем полета (2 часа)");
        assertEquals(ticket3, result[1], "Второй билет должен быть со средним временем полета (4 часа)");
        assertEquals(ticket1, result[2], "Третий билет должен быть с самым длинным временем полета (5 часов)");

        // Проверка порядка времени полета
        assertTrue(result[0].getFlightTime() <= result[1].getFlightTime(), "Время полета должно быть отсортировано по возрастанию");
        assertTrue(result[1].getFlightTime() <= result[2].getFlightTime(), "Время полета должно быть отсортировано по возрастанию");
    }

    @Test
    public void testSearchNoResults() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 10, 12);
        Ticket ticket2 = new Ticket("SVO", "AER", 7000, 14, 18);

        manager.add(ticket1);
        manager.add(ticket2);

        Ticket[] result = manager.search("VKO", "LED");
        assertEquals(0, result.length, "Не должно быть результатов для несуществующего маршрута");

        Ticket[] resultSorted = manager.searchAndSortBy("VKO", "LED", new TicketTimeComparator());
        assertEquals(0, resultSorted.length, "Не должно быть результатов для несуществующего маршрута (с сортировкой)");
    }

    @Test
    public void testFlightTimeCalculation() {
        Ticket ticket = new Ticket("DME", "LED", 5000, 10, 15);
        assertEquals(5, ticket.getFlightTime(), "Время полета должно быть 5 часов");

        Ticket overnight = new Ticket("DME", "LED", 5000, 22, 2);
        assertEquals(-20, overnight.getFlightTime(), "Для ночных перелетов время может быть отрицательным (нужно учесть в логике)");
    }
}
