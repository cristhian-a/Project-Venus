package com.next.engine.debug;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a debug channel used for categorizing and managing debug information in an application.
 * Debug channels are registered and stored in a global registry, allowing for consistent usage
 * across different parts of the system.
 *
 * This class provides pre-defined channels such as {@code INFO}, {@code MEMORY}, {@code PHYSICS},
 * and {@code COLLISION}, and allows for custom channels to be created and registered. Each debug
 * channel is identified by a unique name, which is case-insensitive.
 *
 * Instances of this class are immutable and are managed in a thread-safe manner through the use of
 * a concurrent registry.
 *
 * Main features:
 * - Retrieve predefined or custom registered debug channels using their names.
 * - Access all registered debug channels via a collection.
 * - Provides equality and hash code operations for comparison.
 */
public final class DebugChannel {

    public static final ConcurrentMap<String, DebugChannel> REGISTRY = new ConcurrentHashMap<>();

    public static final DebugChannel INFO = register("INFO");
    public static final DebugChannel MEMORY = register("MEMORY");
    public static final DebugChannel PHYSICS = register("PHYSICS");
    public static final DebugChannel COLLISION = register("COLLISION");

    private final String name;
    private DebugChannel(String name) { this.name = name; }

    public String name() { return name; }

    public static DebugChannel register(String name) {
        String key = Objects.requireNonNull(name).toUpperCase(Locale.ROOT);
        return REGISTRY.computeIfAbsent(key, DebugChannel::new);
    }

    public static DebugChannel of(String name) {
        if (name == null) return null;
        return REGISTRY.get(name.toUpperCase(Locale.ROOT));
    }

    public static Collection<DebugChannel> values() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof DebugChannel c) && name.equals(c.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
