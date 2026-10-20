package by.coldynee.io.output;

import by.coldynee.model.Bus;

import java.util.List;

/**
 * Стратегия сохранения данных об автобусах.
 */
public interface OutputStrategy {
    /**
     * Сохраняет список автобусов в файл.
     *
     * @param buses  список автобусов для сохранения
     * @param filePath путь к файлу
     * @param append true — режим добавления (дозапись), false — перезапись
     */
    void save(List<Bus> buses, String filePath, boolean append);
}