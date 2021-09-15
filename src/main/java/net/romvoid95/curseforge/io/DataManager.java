package net.romvoid95.curseforge.io;

import java.io.Closeable;
import java.util.function.Supplier;

public interface DataManager<T> extends Supplier<T>, Closeable {
	
    void save();

    @Override
    default void close() {
        save();
    }
}
