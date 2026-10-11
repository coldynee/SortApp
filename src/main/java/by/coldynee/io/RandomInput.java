package by.coldynee.io;

import by.coldynee.model.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        if (count <= 0) return List.of();

        return IntStream.range(0, count)
                .mapToObj(i -> {
                    int randomIndex = random.nextInt(allModelNames.length);
                    return new Bus.BusBuilder()
                            .setStateBusNumber(generator.randomStateNumber())
                            .setModelName(allModelNames[randomIndex].getModelName())
                            .setKilometrage(random.nextInt(Bus.MAX_KILOMETRAGE) + 1)
                            .build();
                })
                .collect(Collectors.toList());
    }
}