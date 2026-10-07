package by.coldynee.io;

import by.coldynee.model.Bus;

import java.util.List;

/**
 * Источник данных для создания списка автобусов.
 * Реализации: ручной ввод, чтение из файла, рандомная генерация.
 */
public interface InputSource {
    /**
     * Загружает список автобусов из источника.
     * @param count количество автобусов
     * @return список валидных автобусов
     */
    List<Bus> load(int count);
}
