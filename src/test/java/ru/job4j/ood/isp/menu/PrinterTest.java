package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.job4j.ood.isp.menu.Utils.STUB_ACTION;

class PrinterTest {
    private final PrintStream standartOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standartOut);
    }

    @Test
    public void whenNotRoot() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        Printer printer = new Printer();
        printer.print(menu);
        StringBuilder menuStr = new StringBuilder();
        menuStr.append("1. Сходить в магазин");
        assertEquals(menuStr.toString(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void when1RootOnly() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        Printer printer = new Printer();
        printer.print(menu);
        StringBuilder menuStr = new StringBuilder();
        menuStr.append("1. Сходить в магазин");
        assertEquals(menuStr.toString(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void when2RootsOnly() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(Menu.ROOT, "Сходить погулять", STUB_ACTION);
        Printer printer = new Printer();
        printer.print(menu);
        StringBuilder menuStr = new StringBuilder();
        menuStr.append("1. Сходить в магазин ").append(System.lineSeparator());
        menuStr.append("2. Сходить погулять");
        assertEquals(menuStr.toString(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void when1Root1Child() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить конфет", STUB_ACTION);
        Printer printer = new Printer();
        printer.print(menu);
        StringBuilder menuStr = new StringBuilder();
        menuStr.append("1. Сходить в магазин ").append(System.lineSeparator());
        menuStr.append("---1.1. Купить конфет");
        assertEquals(menuStr.toString(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void when1Root2ChildAtSameRoot() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить конфет", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить чай", STUB_ACTION);
        Printer printer = new Printer();
        printer.print(menu);
        StringBuilder menuStr = new StringBuilder();
        menuStr.append("1. Сходить в магазин ").append(System.lineSeparator());
        menuStr.append("---1.1. Купить конфет ").append(System.lineSeparator());
        menuStr.append("---1.2. Купить чай");
        assertEquals(menuStr.toString(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void when2Roots2ChildAtDiffRoots() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(Menu.ROOT, "Сходить погулять", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить конфет", STUB_ACTION);
        menu.add("Сходить погулять", "Поглазеть", STUB_ACTION);
        Printer printer = new Printer();
        printer.print(menu);
        StringBuilder menuStr = new StringBuilder();
        menuStr.append("1. Сходить в магазин ").append(System.lineSeparator());
        menuStr.append("---1.1. Купить конфет ").append(System.lineSeparator());
        menuStr.append("2. Сходить погулять ").append(System.lineSeparator());
        menuStr.append("---2.1. Поглазеть");
        assertEquals(menuStr.toString(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void when1Root1Child1Child() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить чай", STUB_ACTION);
        menu.add("Купить чай", "Выбрать покрепче", STUB_ACTION);
        Printer printer = new Printer();
        printer.print(menu);
        StringBuilder menuStr = new StringBuilder();
        menuStr.append("1. Сходить в магазин ").append(System.lineSeparator());
        menuStr.append("---1.1. Купить чай ").append(System.lineSeparator());
        menuStr.append("--------1.1.1. Выбрать покрепче");
        assertEquals(menuStr.toString(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void whenAddSameChildToDiffParent() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(Menu.ROOT, "Сходить погулять", STUB_ACTION);
        menu.add("Сходить погулять", "Поглазеть", STUB_ACTION);
        menu.add("Сходить погулять", "Выгулять собаку", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить конфет", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить чай", STUB_ACTION);
        menu.add("Купить чай", "Выбрать покрепче", STUB_ACTION);
        Printer printer = new Printer();
        printer.print(menu);
        StringBuilder menuStr = new StringBuilder();
        menuStr.append("1. Сходить в магазин ").append(System.lineSeparator());
        menuStr.append("---1.1. Купить конфет ").append(System.lineSeparator());
        menuStr.append("---1.2. Купить чай ").append(System.lineSeparator());
        menuStr.append("--------1.2.1. Выбрать покрепче ").append(System.lineSeparator());
        menuStr.append("2. Сходить погулять ").append(System.lineSeparator());
        menuStr.append("---2.1. Поглазеть ").append(System.lineSeparator());
        menuStr.append("---2.2. Выгулять собаку");
        assertEquals(menuStr.toString(), outputStreamCaptor.toString().trim());
    }

}