package by.coldynee.sorting;

import by.coldynee.model.Bus;

import java.util.Comparator;

/**
 * Фабрика стандартных компараторов для сортировки автобусов.
 */
public class BusComparators {
    public static final Comparator<Bus> BY_STATE_BUS_NUMBER =
            Comparator.comparing(Bus::getStateBusNumber);

    public static final Comparator<Bus> BY_MODEL_NAME =
            Comparator.comparing(Bus::getModelName, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<Bus> BY_KILOMETRAGE =
            Comparator.comparingInt(Bus::getKilometrage);
}
