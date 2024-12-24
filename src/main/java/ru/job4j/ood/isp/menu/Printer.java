package ru.job4j.ood.isp.menu;

import java.util.Iterator;

import static ru.job4j.ood.isp.menu.Menu.ROOT;

public class Printer implements MenuPrinter {

    private static final Character SEPARATOR = '.';
    private static final String FILL_SYMBOL = "-";

    @Override
    public void print(Menu menu) {
        Iterator<Menu.MenuItemInfo> it = menu.iterator();
        String filledIndent = "";
        String filledPrev = "";
        long separatorCount = 0;
        long separatorCountPrev = 0;
        while (it.hasNext()) {
            Menu.MenuItemInfo menuItemInfo = it.next();
            separatorCount = menuItemInfo.getNumber().chars().filter(ch -> ch == SEPARATOR).count();
            if (separatorCount == 1) {
                filledIndent = "";
                filledPrev = "";
            } else if (separatorCount != separatorCountPrev) {
                filledIndent = filledIndent.concat(FILL_SYMBOL.repeat(menuItemInfo.getNumber().length() - 1));
                filledPrev = FILL_SYMBOL.repeat(menuItemInfo.getNumber().length() + 1);
            }
            System.out.printf("%s%s %s %s", filledIndent, menuItemInfo.getNumber(), menuItemInfo.getName(), System.lineSeparator());
            separatorCountPrev = separatorCount;
            filledIndent.concat(filledPrev);
        }
    }

    public static void main(String[] args) {
        final ActionDelegate STUB_ACTION = System.out::println;
        Menu menu = new SimpleMenu();
        menu.add(ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(ROOT, "Покормить собаку", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);
        Printer printer = new Printer();
        printer.print(menu);
    }
}
