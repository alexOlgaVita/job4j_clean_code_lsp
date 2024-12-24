package ru.job4j.ood.isp.menu;

import java.util.*;

public class SimpleMenu implements Menu {

    private final List<MenuItem> rootElements = new ArrayList<>();

    /**
     * Метод добавления пункта меню. Реализован так, чтобы можно быдо добавлять одинаковые пункты, но
     * к разным родительским элементам.
     *
     * @param parentName Название родительского элемента
     * @param childName Название дочернего элемента
     * @param actionDelegate Метод-делегат
     * @return возвращает true в случае успешного добавления, иначе false
     */
    @Override
    public boolean add(String parentName, String childName, ActionDelegate actionDelegate) {
        boolean result = false;
        Optional<ItemInfo> fChild = findItem(childName);
        if (parentName == ROOT && fChild.isEmpty()) {
            rootElements.add(new SimpleMenuItem(childName, actionDelegate));
            result = true;
        } else if (parentName != ROOT
                && findItem(parentName).isPresent()
                && (fChild.isEmpty()
                || !findItem(parentName).get().menuItem.getChildren().stream()
                .map(e -> e.getName()).toList().contains(childName))) {
            SimpleMenuItem par = (SimpleMenuItem) findItem(parentName).get().menuItem;
            SimpleMenuItem ch = new SimpleMenuItem(childName, actionDelegate);
            par.children.add(ch);
            result = true;
        }
        return result;
    }

    @Override
    public Optional<MenuItemInfo> select(String itemName) {
        MenuItemInfo result = null;
        if (findItem(itemName).isPresent()) {
            result = new MenuItemInfo(findItem(itemName).get().menuItem, findItem(itemName).get().number);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Iterator<MenuItemInfo> iterator() {
        List<MenuItemInfo> list = new ArrayList<>();
        DFSIterator dfsIterator = new DFSIterator();
        while (dfsIterator.hasNext()) {
            list.add(select(dfsIterator.next().menuItem.getName()).get());
        }
        Iterator<MenuItemInfo> it = list.iterator();
        return it;
    }

    private Optional<ItemInfo> findItem(String name) {
        ItemInfo result = null;
        DFSIterator it = new DFSIterator();
        while (it.hasNext()) {
            ItemInfo i = it.next();
            if (name.equals(i.menuItem.getName())) {
                result = new ItemInfo(i.menuItem, i.number);
                break;
            }
        }
        return Optional.ofNullable(result);
    }

    private static class SimpleMenuItem implements MenuItem {

        private String name;
        private List<MenuItem> children = new ArrayList<>();
        private ActionDelegate actionDelegate;

        public SimpleMenuItem(String name, ActionDelegate actionDelegate) {
            this.name = name;
            this.actionDelegate = actionDelegate;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<MenuItem> getChildren() {
            return children;
        }

        @Override
        public ActionDelegate getActionDelegate() {
            return actionDelegate;
        }
    }

    private class DFSIterator implements Iterator<ItemInfo> {

        private Deque<MenuItem> stack = new LinkedList<>();

        private Deque<String> numbers = new LinkedList<>();

        DFSIterator() {
            int number = 1;
            for (MenuItem item : rootElements) {
                stack.addLast(item);
                numbers.addLast(String.valueOf(number++).concat("."));
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public ItemInfo next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            MenuItem current = stack.removeFirst();
            String lastNumber = numbers.removeFirst();
            List<MenuItem> children = current.getChildren();
            int currentNumber = children.size();
            for (var i = children.listIterator(children.size()); i.hasPrevious();) {
                stack.addFirst(i.previous());
                numbers.addFirst(lastNumber.concat(String.valueOf(currentNumber--)).concat("."));
            }
            return new ItemInfo(current, lastNumber);
        }
    }

    private class ItemInfo {

        private MenuItem menuItem;
        private String number;

        public ItemInfo(MenuItem menuItem, String number) {
            this.menuItem = menuItem;
            this.number = number;
        }
    }
}