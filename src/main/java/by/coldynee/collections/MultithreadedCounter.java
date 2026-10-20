package by.coldynee.collections;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * Класс для многопоточного подсчёта вхождений элементов в коллекцию.
 * <p>
 * Реализует паттерн <b>MapReduce</b>:
 * <ol>
 *   <li>Разделяет список на чанки (по числу ядер процессора).</li>
 *   <li>Каждый чанк обрабатывается отдельным потоком из пула.</li>
 *   <li>Частичные результаты агрегируются в итоговое значение.</li>
 * </ol>
 * </p>
 * <p>
 * Метод является дженериком и принимает {@link Predicate}, что делает его
 * переиспользуемым для любых типов коллекций и любых условий поиска.
 * </p>
 */
public class MultithreadedCounter {

    /**
     * Подсчитывает количество элементов в списке, удовлетворяющих заданному условию,
     * используя многопоточную обработку.
     *
     * @param list      список элементов для поиска
     * @param condition предикат, определяющий, является ли элемент искомым
     * @param <T>       тип элементов в списке
     * @return количество элементов, удовлетворяющих условию
     */
    public static <T> long countOccurrences(List<T> list, Predicate<T> condition) {
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        int chunkSize = (int) Math.ceil((double) list.size() / threadCount);
        List<Future<Long>> futures = new java.util.ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, list.size());

            if (start >= list.size()) break;

            List<T> chunk = list.subList(start, end);
            futures.add(executor.submit(() ->
                    chunk.stream().filter(condition).count()
            ));
        }

        long totalCount = 0;
        try {
            for (Future<Long> future : futures) {
                try {
                    totalCount += future.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (ExecutionException e) {
                    System.err.println("Ошибка в потоке при подсчёте: " + e.getCause());
                }
            }
        } finally {
            executor.shutdown();
        }
        return totalCount;
    }
}
