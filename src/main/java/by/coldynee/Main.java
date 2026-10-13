package by.coldynee;

import by.coldynee.collections.CustomArrayList;
import by.coldynee.io.InputSource;
import by.coldynee.io.ManualInput;
import by.coldynee.io.RandomInput;
import by.coldynee.io.loaders.CsvFileInput;
import by.coldynee.io.loaders.JsonFileInput;
import by.coldynee.io.loaders.TxtFileInput;
import by.coldynee.io.output.CsvFileOutputStrategy;
import by.coldynee.io.output.JsonOutputStrategy;
import by.coldynee.io.output.OutputStrategy;
import by.coldynee.io.output.TxtFileOutputStrategy;
import by.coldynee.model.Bus;
import by.coldynee.sorting.*;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Главное консольное приложение SortApp.
 * <p>
 * Реализует интерактивное меню с циклом выполнения, позволяющее:
 * - Загружать данные из различных источников (вручную, случайно, из файлов)
 * - Сортировать данные различными алгоритмами
 * - Выполнять многопоточный поиск вхождений
 * - Сохранять данные в файлы различных форматов
 * </p>
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Bus> buses = new CustomArrayList<>();
        boolean isRunning = true;

        printHeader();

        while (isRunning) {
            System.out.println("\nГлавное меню");
            System.out.println("1. Загрузить данные");
            System.out.println("2. Показать текущие данные");
            System.out.println("3. Сортировка данных");
            System.out.println("4. Многопоточный поиск вхождений");
            System.out.println("5. Сохранение данных в файл");
            System.out.println("6. Информация о программе");
            System.out.println("0. Выход из программы");
            System.out.println("Выберите действие (0-6): ");

            String menuChoice = scanner.nextLine().trim();

            switch (menuChoice) {
                case "1" -> buses = handleDataInput(scanner, buses);
                case "2" -> handleDisplayData(buses);
                case "3" -> handleSorting(scanner, buses);
                case "4" -> handleMultithreadedSearch(scanner, buses);
                case "5" -> handleDataOutput(scanner, buses);
                case "6" -> printInfo();
                case "0" -> {
                    isRunning = handleExit(scanner);
                }
                default -> System.out.println("Введенные данные некорректны, ожидается число (0-6), повторите попытку");
            }
        }
        scanner.close();
    }

    //Методы обработки действий
    private static List<Bus> handleDataInput(Scanner scanner, List<Bus> currentBuses) {
        while (true) {
            System.out.println("\nИсточники данных");
            System.out.println("1. Ручной ввод");
            System.out.println("2. Генератор случайных автобусов");
            System.out.println("3. Загрузка из '.txt' файла");
            System.out.println("4. Загрузка из '.csv' файла ");
            System.out.println("5. Загрузка из '.json' файла");
            System.out.println("0. Назад в главное меню");

            int inputChoice = readIntInRange(scanner, "Выберите действие (0-5): ", 0, 5);

            if (inputChoice == 0) {
                System.out.println("Возвращение в главное меню");
                return currentBuses;
            }

            int count = readPositiveInt(scanner, "Выберите количество автобусов для загрузки");

            InputSource inputSource = null ;
            switch (inputChoice) {
                case 1 -> inputSource = new ManualInput(scanner);
                case 2 -> inputSource = new RandomInput();
                case 3 -> {
                    System.out.println("Внимание: поддерживается формат ввода абсолютного пути, а также только имени файла");
                    System.out.println("При вводе имени файла будет произведена поиск внутри папки приложения");
                    System.out.println("Введите имя '.txt' файла или полный путь: ");
                    inputSource = new TxtFileInput(scanner.nextLine().trim());
                }
                case 4 -> {
                    System.out.println("Внимание: поддерживается формат ввода абсолютного пути, а также только имени файла");
                    System.out.println("При вводе имени файла будет произведена поиск внутри папки приложения");
                    System.out.println("Введите имя '.csv' файла: ");
                    inputSource = new CsvFileInput(scanner.nextLine().trim());
                }
                case 5 -> {
                    System.out.println("Внимание: поддерживается формат ввода абсолютного пути, а также только имени файла");
                    System.out.println("При вводе имени файла будет произведена поиск внутри папки приложения");
                    System.out.println("Введите имя '.json' файла: ");
                    inputSource = new JsonFileInput(scanner.nextLine().trim());
                }
                default -> {
                    System.out.println("Введенные данные некорректны, ожидается число (1-5): ");
                    continue;
                }
            }
            boolean isAppend = false;
            if (!currentBuses.isEmpty()) {
                System.out.println("Режимы загрузки");
                System.out.println("1. Дозаписать к существующим данным");
                System.out.println("2. Перезаписать текущие данные");
                int loadMode = readIntInRange(scanner, "Выберите режим (1-2)", 1, 2);
                isAppend = (loadMode == 1);
            }

            System.out.println("Начат процесс загрузки данных");
            List<Bus> loadedBuses = new CustomArrayList<>();


            if(!isAppend) {
                loadedBuses.addAll(inputSource.load(count));
            } else {
                loadedBuses.addAll(currentBuses);
                loadedBuses.addAll(inputSource.load(count));
            }
            if (loadedBuses.isEmpty()) {
                System.out.println("Внимание: данные не были загружены (файл не найден, пуст или отменен ввод).");
                System.out.println("Список автобусов не изменен");
                return currentBuses;
            } else {
                System.out.println("Загрузка данных завершена, всего автобусов: " + loadedBuses.size());
            }
            return loadedBuses;
        }
    }

    private static void handleDisplayData(List<Bus> buses) {
        if (buses.isEmpty()) {
            System.out.println("Список автобусов пуст, необходимо заполнить");
        } else {
            int limit = Math.min(buses.size(), 30);
            for (int i = 0; i < limit; i++) {
                System.out.printf("%3d. %s%n", (i + 1), buses.get(i));
            }
            if (buses.size() > 30) {
                System.out.println("Показаны первые 30 записанных (осталось " + (buses.size() - 30) + ")");
            }
        }
    }

    private static void handleSorting(Scanner scanner, List<Bus> buses) {
        if (buses.isEmpty()) {
            System.out.println("Список автобусов пуст, необходимо заполнить");
            return;
        }
        while (true) {
            System.out.println("\nАлгоритмы сортировки");
            System.out.println("1. Сортировка пузырьком (Bubble sort)");
            System.out.println("2. Сортировка вставками (Insertion sort)");
            System.out.println("3. Сортировка выбором (Selection sort)");
            System.out.println("0. Назад в главное меню");
            int sortingChoice = readIntInRange(scanner, "Выберите алгоритм (1-3) или '0' для выхода: ", 0, 3);
            if (sortingChoice == 0) {
                System.out.println("Возвращение в главное меню");
                return;
            }

            SortingStrategy strategy = null;
            switch (sortingChoice) {
                case 1 -> strategy = new BubbleSortStrategy();
                case 2 -> strategy = new InsertionSortStrategy();
                case 3 -> strategy = new SelectionSortStrategy();
            }

            while (true) {
                System.out.println("\nКритерии сортировки");
                System.out.println("1. По государственному номеру");
                System.out.println("2. По названию модели");
                System.out.println("3. По пробегу");
                System.out.println("4. Четные по пробегу (нечетные остаются на местах)");
                System.out.println("0. Назад");
                int criterionChoice = readIntInRange(scanner, "Выберите критерий (1-4) или '0' для возврата: ", 0, 4);
                if (criterionChoice == 0) break;

                System.out.println("Начался процесс сортировки");
                long startTime = System.currentTimeMillis();
                if (criterionChoice == 4) {
                    strategy.sortByEvenKilometrage(buses);
                } else {
                    Comparator<Bus> comparator = null;
                    switch (criterionChoice) {
                        case 1 -> comparator = BusComparators.BY_STATE_BUS_NUMBER;
                        case 2 -> comparator = BusComparators.BY_MODEL_NAME;
                        case 3 -> comparator = BusComparators.BY_KILOMETRAGE;
                    }
                    strategy.sort(buses, comparator);
                }
                long endTime = System.currentTimeMillis();
                System.out.printf("Сортировка %s завершена за %d мс %n", strategy.getName(), (endTime - startTime));
                break;
            }
        }
    }

    private static void handleMultithreadedSearch(Scanner scanner, List<Bus> buses) {
        if (buses.isEmpty()) {
            System.out.println("Список автобусов пуст, необходимо заполнить");
            return;
        }

        System.out.println("Поддерживаемые параметры поиска");
        System.out.println("1. Государственный номер");
        System.out.println("2. Название модели");
        System.out.println("3. Пробег");
        System.out.println("4. По всем параметрам (универсальный поиск)");
        System.out.println("0. Назад в главное меню");
        int choiceInput = readIntInRange(scanner, "Выберите параметр (1-4) или '0' для возврата", 0, 4);
        if (choiceInput == 0) {
            System.out.println("Возвращение в главное меню");
            return;
        }
        System.out.println("Введите параметр");
        String paramInput = scanner.nextLine().trim();

        System.out.println("Начат процесс многопоточного поиска");
        long startTime = System.currentTimeMillis();

        // TODO:
        // Реализовать вызов многопоточного метода
        // int count = MultithreadedCounter.countOccurrences(buses, bus -> {
        //     return switch (choiceInput) {
        //         case 1 -> bus.getStateBusNumber().equalsIgnoreCase(paramInput);
        //         case 2 -> bus.getModelName().equalsIgnoreCase(paramInput);
        //         case 3 -> String.valueOf(bus.getKilometrage()).equals(paramInput);
        //         case 4 -> bus.getStateBusNumber().equalsIgnoreCase(paramInput) ||
        //                  bus.getModelName().equalsIgnoreCase(paramInput) ||
        //                  String.valueOf(bus.getKilometrage()).equals(paramInput);
        //         default -> false;
        //     };
        // });


        int count = 0;

        long endTime = System.currentTimeMillis();
        System.out.println("Найдено " + count + " вхождений");
        System.out.println("Многопоточный поиск завершен за " + (endTime-startTime) + " мс");

    }

    private static void handleDataOutput(Scanner scanner, List<Bus> buses) {
        if (buses.isEmpty()) {
            System.out.println("Список автобусов пуст, необходимо заполнить");
            return;
        }

        while (true) {
            System.out.println("Поддерживаемые форматы файлов");
            System.out.println("1. '.txt' - объекты через ';'");
            System.out.println("2. '.csv' - объекты через ','");
            System.out.println("3. '.json' - массив объектов");
            System.out.println("0. Назад в главное меню");
            int formatChoice = readIntInRange(scanner, "Выберите формат (1-3) или '0' для выхода: ", 0, 3);
            if (formatChoice == 0) {
                System.out.println("Возвращение в главное меню");
                return;
            }

            while (true) {
                System.out.println("Режимы записи");
                System.out.println("1. Перезаписать файл");
                System.out.println("2. Режим добавления данных в конец файла");
                System.out.println("0. Назад");
                int modeChoice = readIntInRange(scanner, "Выберите режим (1-2) или '0' для возврата: ", 0, 2);
                if (modeChoice == 0) break;

                boolean isAppend = (modeChoice == 2);

                System.out.println("Внимание: поддерживается формат ввода абсолютного пути, а также только имени файла");
                System.out.println("При вводе имени файла будет произведена запись в файл внутри папки приложения");
                System.out.println("Введите имя или полный путь для сохраняемого файла: ");
                String fileNameInput = scanner.nextLine().trim();

                OutputStrategy strategy = null;
                switch (formatChoice) {
                    case 1 -> strategy = new TxtFileOutputStrategy();
                    case 2 -> strategy = new CsvFileOutputStrategy();
                    case 3 -> strategy = new JsonOutputStrategy();
                }
                System.out.println("Начался процесс сохранения данных");
                strategy.save(buses, fileNameInput, isAppend);
                System.out.println("Сохранение данных завершено");
                return;
            }
        }
    }

    //Вспомогательные методы для проверки значений
    private static int readPositiveInt(Scanner scanner, String message) {
        while (true) {
            System.out.print(message + " ");
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) return value;
                System.out.println("Ожидается число больше нуля");
            } catch (NumberFormatException e) {
                System.out.println("Введенные данные некорректны, ожидается целое число");
            }
        }
    }

    private static int readIntInRange(Scanner scanner, String message, int min, int max) {
        while (true) {
            System.out.print(message + " ");
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
                System.out.printf("Ожидается число в диапазоне от %d до %d %n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Введенные данные некорректны, ожидается целое число");
            }
        }
    }

    //Вспомогательные методы для вывода информации
    private static void printInfo() {
        System.out.println("\nПриложение SortApp служит для работы с объектами автобусов");
        System.out.println("\nПоддерживаемые функции");
        System.out.println("Чтение автобусов из файла (txt, csv, json),");
        System.out.println("Заполнение автобусов вручную или случайным образом,");
        System.out.println("Запись автобусов в файлы (txt, csv, json) с поддержкой режима добавления данных,");
        System.out.println("Сортировка автобусов по их параметрам, включая кастомную сортировку");
        System.out.println("\nИспользуемые библиотеки: Jackson (для работы с файлами формата '.json'), JUnit 5 (для тестирования)");
        System.out.println("\nПриложение разработано на языке программирования Java 21 версии");
        System.out.println("Список разработчиков");
        System.out.println("Морозов Никита - Технический лидер");
        System.out.println("Осипов Павел");
        System.out.println("Дамаскин Максим");
        System.out.println("Алиев Евгений");
    }

    private static void printHeader() {
        System.out.println("\nSortApp");
    }

    private static boolean handleExit(Scanner scanner) {
        while (true) {
            System.out.println("Завершить работу программы?");
            System.out.println("1. Нет, вернуться в меню");
            System.out.println("0. Да, выйти");
            String inputChoice = scanner.nextLine().trim();
            if ("0".equals(inputChoice)) {
                System.out.println("Завершение работы, спасибо что выбираете нас!");
                return false;
            } else if ("1".equals(inputChoice)){
                return true;
            } else {
                System.out.println("Введенный ответ некорректен, ожидается целое число");
            }
        }
    }
}