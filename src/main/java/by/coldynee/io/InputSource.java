package by.coldynee.io;

import by.coldynee.model.Bus;

import java.util.List;

/**
 * Источник данных для создания списка автобусов.
 * Реализации: ручной ввод, чтение из файла, рандомная генерация.
 */
public interface InputSource {
    /**
     * Загружает автобусы из источника.
     * @return список валидных автобусов
     */
    List<Bus> load();

    /**
     * Загружает ограниченное количество автобусов.
     * @param count максимальное количество
     * @return список валидных автобусов (максимум count)
     */
    default List<Bus> load(int count) {
        return load().stream().limit(count).toList();
    }
}
