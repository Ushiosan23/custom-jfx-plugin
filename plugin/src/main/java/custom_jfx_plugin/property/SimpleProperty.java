package custom_jfx_plugin.property;

import org.jetbrains.annotations.Nullable;

class SimpleProperty<T> implements Property<T> {
	
	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/
	
	/**
	 * The property value
	 */
	private T value;
	
	/* -----------------------------------------------------------------------
	 * Constructors
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Default constructor without initial value
	 */
	public SimpleProperty() {
		set((T) null);
	}
	
	/**
	 * Default constructor
	 *
	 * @param initial initial value
	 */
	public SimpleProperty(T initial) {
		set(initial);
	}
	
	/* -----------------------------------------------------------------------
	 * Methods
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Returns the property values
	 *
	 * @return the property value or {@code null} if value is not declared
	 */
	@Override
	public @Nullable T get() {
		return value;
	}
	
	/**
	 * Change the property value
	 *
	 * @param newValue the new property value
	 */
	@Override
	public void set(@Nullable T newValue) {
		value = newValue;
	}
	
}
