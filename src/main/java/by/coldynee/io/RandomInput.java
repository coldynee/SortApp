package by.coldynee.io;

import by.coldynee.model.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Реализация {@link InputSource} для загрузки автобусов с рандомными данными.
 * <p>
 * Генерирует случайные значения для:
 * <ul>
 *     <li>государственного номера автобуса</li>
 *     <li>модели/марки автобуса</li>
 *     <li>пробега в километрах</li>
 * </ul>
 * </p>
 */
public class RandomInput implements InputSource {

    private Random random = new Random();
    private ModelName[] allModelNames = ModelName.values();
    private GenerateRandomStateNumber generator = new GenerateRandomStateNumber();

    @Override
    public List<Bus> load(int count) {
        List<Bus> buses = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(allModelNames.length);
            String randomStateNumber = generator.randomStateNumber();
            ModelName randomModelName = allModelNames[randomIndex];
            int randomKilometrage = random.nextInt(Bus.MAX_KILOMETRAGE) + 1;
            Bus bus = new Bus.BusBuilder()
                    .setStateBusNumber(randomStateNumber)
                    .setModelName(randomModelName.getModelName())
                    .setKilometrage(randomKilometrage)
                    .build();
            buses.add(bus);
        }
        return buses;
    }
}