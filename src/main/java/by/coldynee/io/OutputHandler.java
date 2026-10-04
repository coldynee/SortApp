package by.coldynee.io;

import by.coldynee.model.Bus;

import java.util.List;

/**
 * Обработчик вывода данных.
 */
public interface OutputHandler {
    /**
     * Выводит список автобусов.
     *
     * @param buses список для вывода
     */
    void display(List<Bus> buses);

    /**
     * Сохраняет список автобусов в файл.
     *
     * @param buses список для сохранения
     * @param filePath путь к файлу
     * @param append true — дозапись, false — перезапись
     */
    void saveToFile(List<Bus> buses, String filePath, boolean append);
}
