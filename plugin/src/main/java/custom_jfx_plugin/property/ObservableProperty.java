package custom_jfx_plugin.property;

import custom_jfx_plugin.property.listener.ValueListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ObservableProperty<T> extends Property<T> {
	
	/**
	 * Bind changes to passed listener
	 *
	 * @param listener the listener object
	 */
	void addChangeListener(@NotNull ValueListener<T> listener);
	
	/**
	 * Remove the listener from instance
	 *
	 * @param listener the listener to remove
	 */
	void removeListener(@NotNull ValueListener<T> listener);
	
	/* -----------------------------------------------------------------------
	 * Static methods
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Generate new instance of property without initial value
	 *
	 * @param <T> property type value
	 * @return property instance
	 */
	@Contract(" -> new")
	static <T> @NotNull ObservableProperty<T> create() {
		return new SimpleObservableProperty<>();
	}
	
	/**
	 * Generate new instance of property with initial value
	 *
	 * @param <T> property type value
	 * @return property instance
	 */
	static <T> @NotNull ObservableProperty<T> create(@Nullable T initial) {
		return new SimpleObservableProperty<>(initial);
	}
	
}
