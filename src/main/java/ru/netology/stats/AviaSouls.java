package ru.netology.stats;

import java.util.Arrays;
import java.util.Comparator;

public class AviaSouls {
    private Ticket[] tickets = new Ticket[0];

    /**
     * Вспомогательный метод для имитации добавления элемента в массив
     * @param current Массив, в который мы хотим добавить элемент
     * @param ticket Элемент, который мы хотим добавить
     * @return Возвращает новый массив, который выглядит как тот что мы передали,
     * но с добавлением нового элемента в конец
     */
    private Ticket[] addToArray(Ticket[] current, Ticket ticket) {
        Ticket[] tmp = new Ticket[current.length + 1];
        for (int i = 0; i < current.length; i++) {
            tmp[i] = current[i];
        }
        tmp[tmp.length - 1] = ticket;
        return tmp;
    }

    /**
     * Метод добавления билета в менеджер
     * @param ticket Добавляемый билет
     */
    public void add(Ticket ticket) {
        tickets = addToArray(tickets, ticket);
    }

    public Ticket[] findAll() {
        return tickets;
    }

    /**
     * Метод поиска билетов по маршруту с сортировкой по цене
     * @param from Откуда вылетаем
     * @param to Куда прилетаем
     * @return Массив из подходящих билетов, отсортированный по цене
     */
    public Ticket[] search(String from, String to) {
        Ticket[] result = new Ticket[0];
        for (Ticket ticket : tickets) {
            if (ticket.getFrom().equals(from) && ticket.getTo().equals(to)) {
                result = addToArray(result, ticket);
            }
        }

        // Сортировка по цене (используя compareTo)
        Arrays.sort(result);
        return result;
    }

    /**
     * Метод поиска билетов по маршруту с сортировкой по заданному компаратору
     * @param from Откуда вылетаем
     * @param to Куда прилетаем
     * @param comparator Компаратор для сортировки
     * @return Массив из подходящих билетов, отсортированный по компаратору
     */
    public Ticket[] searchAndSortBy(String from, String to, Comparator<Ticket> comparator) {
        Ticket[] result = new Ticket[0];
        for (Ticket ticket : tickets) {
            if (ticket.getFrom().equals(from) && ticket.getTo().equals(to)) {
                result = addToArray(result, ticket);
            }
        }

        // Сортировка по заданному компаратору
        Arrays.sort(result, comparator);
        return result;
    }
}
