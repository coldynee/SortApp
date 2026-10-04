package by.coldynee.sort;

import by.coldynee.model.Bus;

import java.util.Comparator;

/**
 * Набор компараторов для сортировки автобусов по разным полям.
 */
public class BusComparators {
    public static final Comparator<Bus> BY_ROUTE_NUMBER =
            Comparator.comparingInt(Bus::getRouteNumber);

    public static final Comparator<Bus> BY_MODEL_NAME =
            Comparator.comparing(Bus::getModelName, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<Bus> BY_KILOMETRAGE =
            Comparator.comparingInt(Bus::getKilometrage);
}
