package com.next.game.rules.data;

import com.next.game.model.Item;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Data
public class Inventory {
    private List<Item> items = new ArrayList<>();

    public boolean add(Item i) {
        if (items.size() == capacity())
            return false;

        items.add(i);
        return true;
    }

    public Optional<Item> get(int index) {
        if (index >= items.size()) return Optional.empty();
        return Optional.ofNullable(items.get(index));
    }

    public Optional<Item> get(Class<?> clazz) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getClass().equals(clazz)) return Optional.of(item);
        }
        return Optional.empty();
    }

    public void pop(int index) {
        items.remove(index);
    }

    public void pop(Item item) {
        items.remove(item);
    }

    public void forEach(Consumer<Item> action) {
        items.forEach(action);
    }

    public final int capacity() {
        return 16;
    }
}
