package net.romvoid95.curseforge.core;

import java.util.function.Consumer;

import javax.annotation.Nullable;

public interface Action<T> {

	default void queue() {
		queue(null);
	}

	default void queue(@Nullable Consumer<? super T> success) {
		queue(success, null);
	}

	void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure);
}
