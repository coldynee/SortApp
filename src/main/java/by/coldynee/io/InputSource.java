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
     *
     * @param count максимальное количество автобусов для загрузки.
     * Если count <= 0, загружаются все доступные записи.
     * @return список успешно созданных валидных автобусов
     */
    List<Bus> load(int count);
}
