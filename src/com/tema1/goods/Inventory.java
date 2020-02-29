package com.tema1.goods;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory<T> implements Iterable<Inventory.Entry<T>> {
    private final List<Entry<T>> inventory;
    private int totalItemCount;

    public Inventory() {
        totalItemCount = 0;
        inventory = new LinkedList<Entry<T>>();
    }

    public Inventory(final Inventory<T> other) {
        this();
        inventory.addAll(other.inventory);
        totalItemCount = other.getTotalItemCount();
    }

    public Inventory(final List<Entry<T>> entriesList) {
        this();

        for (Entry<T> entry : entriesList) {
            setAmount(entry.getItem(), entry.getQuantity());
        }
    }

    public final void setAmount(final T item, final int amount) {
        if (amount <= 0) {
            inventory.removeIf((x) -> {
                boolean isSame = x.getItem().equals(item);
                if (isSame) {
                    totalItemCount -= x.getQuantity();
                }
                return isSame;
            });
        } else {
            inventory.stream()
                    .filter(x -> x.getItem().equals(item))
                    .findFirst()
                    .ifPresentOrElse((x) -> {
                        totalItemCount -= x.getQuantity();
                        x.setQuantity(amount);
                    }, () -> inventory.add(new Entry<T>(item, amount)));

            totalItemCount += amount;
        }
    }

    public final int getAmount(final T item) {
        Entry entry = inventory.stream()
                .filter(x -> x.getItem().equals(item))
                .findFirst()
                .orElse(null);

        if (entry != null) {
            return entry.getQuantity();
        }
        return 0;
    }

    public final void increaseItemAmount(final T item) {
        increaseItemAmount(item, 1);
    }
    public final void decreaseItemAmount(final T item) {
        decreaseItemAmount(item, 1);
    }
    public final void increaseItemAmount(final T item, final int amount) {
        setAmount(item, getAmount(item) + amount);
    }
    public final void decreaseItemAmount(final T item, final int amount) {
        setAmount(item, getAmount(item) - amount);
    }

    /**
     * sterge continutul inventarului si reseteaza starea interna.
     */
    public void clear() {
        inventory.clear();
        totalItemCount = 0;
    }

    public final int size() {
        return inventory.size();
    }

    public final int getTotalItemCount() {
        return totalItemCount;
    }

    public final Entry<T> get(final int i) {
        return inventory.get(i);
    }

    @Override
    public final Iterator<Entry<T>> iterator() {
        return inventory.iterator();
    }

    public final Inventory<T> sort(final java.util.Comparator<Entry<T>> comp) {
        return new Inventory<T>(inventory.stream().sorted(comp).collect(Collectors.toList()));
    }

    public static final class Entry<T> {
        private T item;
        private int quantity = 0;

        public Entry(final T item, final int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public T getItem() {
            return item;
        }

        public int getQuantity() {
            return quantity;
        }
        public void setQuantity(final int quantity) {
            this.quantity = quantity;
        }
    }
}
