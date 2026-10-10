package by.coldynee.io;

/**
 * Перечисление доступных марок автобусов.
 * <p>
 * Содержит полный список марок, используемых в приложении,
 * с отображаемыми названиями на кириллице.
 * </p>
 */
public enum ModelName {
    // Иностранные марки
    Mercedes_Benz("Mercedes-Benz"),
    MAN("MAN"),
    Volvo("Volvo"),
    Iveco("Iveco"),
    Solaris("Solaris"),
    BYD("BYD"),

    // Российские марки
    ПАЗ("ПАЗ"),
    ЛиАЗ("ЛиАЗ"),
    МАЗ("МАЗ");

    private final String modelName;

    /**
     * Конструктор enum с названием.
     *
     * @param modelName отображаемое название марки автобуса
     */
    ModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * Возвращает отображаемое название марки автобуса.
     *
     * @return название марки (например: "Mercedes-Benz")
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Возвращает общее количество доступных марок.
     *
     * @return количество элементов в enum (9)
     */
    public int getNumberOfModels() {
        return values().length;
    }
}