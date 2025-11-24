// Простой консольный интерфейс для работы с FenwickTree.
// Есть:
//  - работа с файлом (чтение массива) + обработка IOException / NumberFormatException
//  - работа с пользовательским вводом (Scanner) + обработка InputMismatchException
//  - меню для вызова методов FenwickTree
//  - пункт 5 — справка по операциям.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int[] arr = null;

        System.out.println("Дерево Фенвика (Fenwick Tree)");
        System.out.println("================================");

        System.out.println("Выберите способ инициализации массива:");
        System.out.println("1 — считать из файла");
        System.out.println("2 — ввести вручную");
        System.out.println("3 — использовать массив по умолчанию {1, 2, 3, 4, 5}");
        System.out.print("Ваш выбор: ");

        int initChoice = 3; // по умолчанию
        try {
            initChoice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Некорректный ввод, будет использован вариант 3 (массив по умолчанию).");
            scanner.nextLine(); // очистить некорректный ввод
        }

        // Инициализация массива разными способами.
        switch (initChoice) {
            case 1:
                scanner.nextLine(); // добиваем перевод строки
                System.out.print("Введите имя файла (например, input.txt): ");
                String filename = scanner.nextLine();
                try {
                    arr = readArrayFromFile(filename);
                    System.out.println("Массив успешно прочитан из файла.");
                } catch (IOException e) {
                    System.out.println("Ошибка при работе с файлом: " + e.getMessage());
                    System.out.println("Будет использован массив по умолчанию.");
                    arr = new int[]{1, 2, 3, 4, 5};
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка формата данных в файле: " + e.getMessage());
                    System.out.println("Будет использован массив по умолчанию.");
                    arr = new int[]{1, 2, 3, 4, 5};
                }
                break;

            case 2:
                try {
                    System.out.print("Введите размер массива n: ");
                    int n = scanner.nextInt();
                    if (n < 0) {
                        System.out.println("Размер не может быть отрицательным. Будет использован массив по умолчанию.");
                        arr = new int[]{1, 2, 3, 4, 5};
                    } else {
                        arr = new int[n];
                        System.out.println("Введите " + n + " целых чисел:");
                        for (int i = 0; i < n; i++) {
                            arr[i] = scanner.nextInt();
                        }
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Ошибка ввода: ожидались целые числа. Будет использован массив по умолчанию.");
                    arr = new int[]{1, 2, 3, 4, 5};
                    scanner.nextLine(); // очистить буфер
                }
                break;

            default:
                System.out.println("Будет использован массив по умолчанию.");
                arr = new int[]{1, 2, 3, 4, 5};
                break;
        }

        // Строим дерево.
        FenwickTree ft = new FenwickTree();
        try {
            ft.build(arr);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при построении дерева: " + e.getMessage());
            System.out.println("Программа завершает работу.");
            scanner.close();
            return;
        }

        // Основное меню.
        while (true) {
            System.out.println();
            System.out.println("Меню:");
            System.out.println("1 — показать внутреннее дерево (массив tree)");
            System.out.println("2 — запрос prefixSum(index)");
            System.out.println("3 — запрос rangeSum(left, right)");
            System.out.println("4 — выполнить update(index, delta)");
            System.out.println("5 — справка по операциям");
            System.out.println("0 — выход");
            System.out.print("Ваш выбор: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Ошибка ввода: ожидалось целое число, попробуйте ещё раз.");
                scanner.nextLine(); // очистить некорректный ввод
                continue;
            }

            if (choice == 0) {
                System.out.println("Выход из программы.");
                break;
            }

            switch (choice) {
                case 1:
                    System.out.println(ft);
                    break;

                case 2: {
                    System.out.print("Введите index: ");
                    try {
                        int index = scanner.nextInt();
                        int sum = ft.prefixSum(index);
                        System.out.println("prefixSum(" + index + ") = " + sum);
                    } catch (InputMismatchException e) {
                        System.out.println("Ошибка ввода: ожидалось целое число.");
                        scanner.nextLine();
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                    break;
                }

                case 3: {
                    System.out.print("Введите left и right: ");
                    try {
                        int left = scanner.nextInt();
                        int right = scanner.nextInt();
                        int sum = ft.rangeSum(left, right);
                        System.out.println("rangeSum(" + left + ", " + right + ") = " + sum);
                    } catch (InputMismatchException e) {
                        System.out.println("Ошибка ввода: ожидались целые числа.");
                        scanner.nextLine();
                    } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                    break;
                }

                case 4: {
                    System.out.print("Введите index и delta: ");
                    try {
                        int index = scanner.nextInt();
                        int delta = scanner.nextInt();
                        ft.update(index, delta);
                        System.out.println("Обновление выполнено.");
                    } catch (InputMismatchException e) {
                        System.out.println("Ошибка ввода: ожидались целые числа.");
                        scanner.nextLine();
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                    break;
                }

                case 5: {
                    // Справка по операциям и индексации.
                    System.out.println();
                    System.out.println("СПРАВКА:");
                    System.out.println("- Дерево Фенвика хранит частичные суммы массива целых чисел.");
                    System.out.println("- Индексация элементов массива — с 0 до n - 1.");
                    System.out.println("- prefixSum(index): считает сумму элементов от 0 до index включительно.");
                    System.out.println("- rangeSum(left, right): считает сумму элементов от left до right включительно.");
                    System.out.println("- update(index, delta): прибавляет значение delta к элементу с индексом index.");
                    System.out.println();
                    System.out.println("Пример:");
                    System.out.println("  массив: {1, 2, 3, 4, 5}");
                    System.out.println("  prefixSum(2) = 1 + 2 + 3 = 6");
                    System.out.println("  rangeSum(1, 3) = 2 + 3 + 4 = 9");
                    System.out.println("  update(2, 10) делает элемент с индексом 2 равным 13 (3 + 10).");
                    System.out.println();
                    System.out.println("При некорректных индексах методы бросают исключения,");
                    System.out.println("а в этом меню они перехватываются и выводится сообщение об ошибке.");
                    break;
                }

                default:
                    System.out.println("Неизвестный пункт меню, попробуйте ещё раз.");
                    break;
            }
        }

        scanner.close();
    }

    // Чтение массива из файла.
    // Формат: одна строка с целыми числами через пробел.
    private static int[] readArrayFromFile(String filename) throws IOException, NumberFormatException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            if (line == null || line.trim().isEmpty()) {
                throw new IOException("Файл пустой или не содержит данных.");
            }

            String[] parts = line.trim().split("\\s+");
            int[] arr = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                arr[i] = Integer.parseInt(parts[i]);
            }
            return arr;
        }
    }
}
