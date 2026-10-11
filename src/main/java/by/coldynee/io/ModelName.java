package by.coldynee.io;

/**
 * Перечисление доступных марок автобусов.
 * <p>
 * Содержит 50 популярных марок автобусов со всего мира.
 * </p>
 */
public enum ModelName {
    // Европейские марки
    MERCEDES_BENZ("Mercedes-Benz"),
    MAN("MAN"),
    VOLVO("Volvo"),
    IVECO("Iveco"),
    SCANIA("Scania"),
    NEOPLAN("Neoplan"),
    SETRA("Setra"),
    VDL("VDL"),
    VAN_HOOL("Van Hool"),
    IRISBUS("Irisbus"),
    BOVA("Bova"),
    TEMSA("Temsa"),
    OTOKAR("Otokar"),
    RENAULT("Renault"),
    HEULIEZ("Heuliez"),
    IRIZAR("Irizar"),
    JONCKHEERE("Jonckheere"),
    DIETRICH("Dietrich"),
    PVI("PVI"),
    GRUAU("Gruau"),

    // Японские марки
    ISUZU("Isuzu"),
    HINO("Hino"),
    MITSUBISHI_FUSO("Mitsubishi Fuso"),

    // Китайские марки
    YUTONG("Yutong"),
    KING_LONG("King Long"),
    HIGER("Higer"),
    ZHONGTONG("Zhongtong"),
    ANKAI("Ankai"),
    FOTON("Foton"),

    // Индийские марки
    TATA("Tata"),
    ASHOK_LEYLAND("Ashok Leyland"),

    // Бразильские марки
    MARCOPOL("Marcopol"),
    BUSSCAR("Busscar"),
    CAIO_INDUSCAR("Caio Induscar"),

    // Испанские марки
    CASTROSUA("Castrosua"),
    INDCAR("Indcar"),
    AYATS("Ayats"),
    BEULAS("Beulas"),
    NOGE("Noge"),
    UNVI("UNVI"),
    FERQUI("Ferqui"),
    SUNSUNDEGUI("Sunsundegui"),

    // Российские марки
    PAZ("ПАЗ"),
    LIAZ("ЛиАЗ"),
    MAZ("МАЗ"),
    KAVZ("КАвЗ"),
    NEFAZ("НефАЗ"),
    GOLAZ("ГолАЗ");

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
}