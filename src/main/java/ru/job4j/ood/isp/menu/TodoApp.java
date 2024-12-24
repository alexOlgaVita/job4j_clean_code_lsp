package ru.job4j.ood.isp.menu;

import java.util.Optional;
import java.util.Scanner;

/**
 * 6. Создайте простенький класс TodoApp. Этот класс будет представлять собой консольное приложение, которое позволяет:
 * Добавить элемент в корень меню;
 * Добавить элемент к родительскому элементу;
 * Вызвать действие, привязанное к пункту меню (действие можно сделать константой,
 * например, ActionDelete DEFAULT_ACTION = () -> System.out.println("Some action") и указывать при добавлении элемента в меню);
 * Вывести меню в консоль.
 */
public class TodoApp {
    public static final ActionDelegate DEFAULT_ACTION = () -> System.out.println("Some action");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        String childName = "";
        Menu menu = new SimpleMenu();
        Printer printer = new Printer();
        while (running) {
            System.out.println("1. Добавить элемент в корень меню");
            System.out.println("2. Добавить элемент к родительскому элементу");
            System.out.println("3. Вызвать действие, привязанное к пункту меню");
            System.out.println("4. Вывести меню в консоль");
            System.out.println("5. Выход");
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Введите название пункта меню: ");
                    childName = scanner.nextLine();
                    if (!menu.add(Menu.ROOT, childName, DEFAULT_ACTION)) {
                        System.out.println("Пункт меню добавлен не был: "
                                + "скорее всего такой уже существует.");
                    }
                    break;
                case 2:
                    System.out.print("Введите название родительского пункта меню: ");
                    String rootName = scanner.nextLine();
                    System.out.print("Введите название добавляемого пункта меню: ");
                    childName = scanner.nextLine();
                    if (!menu.add(rootName, childName, DEFAULT_ACTION)) {
                        System.out.println("Пункт меню добавлен не был: "
                                + "либо не существует родительского элемента, либо такой пункт в нем уже существует.");
                    }
                    break;
                case 3:
                    System.out.print("Введите название пункта меню, действие которого хотите вызвать: ");
                    String menuItem = scanner.nextLine();
                    Optional<Menu.MenuItemInfo> item = menu.select(menuItem);
                    if (item.isPresent()) {
                        item.get().getActionDelegate().delegate();
                    } else {
                        System.out.println("Невозможно выполнить действие, т.к. указанный пункт меню отсутствует.");
                    }
                    break;
                case 4:
                    System.out.println("Меню:");
                    printer.print(menu);
                    System.out.println();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }
}
