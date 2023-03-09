package custom_jfx_plugin.property;

import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ushiosan.jvm_utilities.lang.Obj;

public interface Property<T> {
	
	/**
	 * Returns the property values
	 *
	 * @return the property value or {@code null} if value is not declared
	 */
	@Nullable T get();
	
	/**
	 * Change the property value
	 *
	 * @param newValue the new property value
	 */
	void set(@Nullable T newValue);
	
	/**
	 * Change the property value
	 *
	 * @param provider the object provider
	 */
	default void set(@NotNull Provider<T> provider) {
		set(provider.get());
	}
	
	/**
	 * Check if the property contains a valid not null value
	 *
	 * @return {@code true} if the value is valid or {@code false} otherwise
	 */
	default boolean isPresent() {
		return Obj.isNotNull(get());
	}
	
	/**
	 * Check if the property contains a valid not null value
	 *
	 * @return {@code true} if the value is null or is not defined or {@code false} otherwise
	 */
	default boolean isNotPresent() {
		return !isPresent();
	}
	
	/**
	 * Returns the property value, or it's replaced with a default valid content
	 *
	 * @param defaultValue the valid value if the content value is not valid
	 * @return the valid value
	 */
	default @NotNull T getOrElse(@NotNull T defaultValue) {
		T content = get();
		return Obj.isNull(content) ? defaultValue : content;
	}
	
	/**
	 * Returns the property value, or it's replaced with a default valid content
	 *
	 * @param provider the valid value if the content value is not valid
	 * @return the valid value
	 */
	default @NotNull T getOrElse(@NotNull Provider<T> provider) {
		return getOrElse(provider.get());
	}
	
	/**
	 * Returns the property value, or throws an error if is not defined
	 *
	 * @return the valid value
	 */
	@SuppressWarnings("DataFlowIssue")
	default @NotNull T getOrElseThrow() {
		if (isNotPresent()) throw new RuntimeException("Value is not present");
		return get();
	}
	
	/**
	 * Returns the property value, or throws an error if is not defined
	 *
	 * @param name property name
	 * @return the valid value
	 */
	default @NotNull T getOrElseThrow(@NotNull String name) {
		if (isNotPresent()) throw new RuntimeException(String.format("Property \"%s\" value is not present", name));
		return get();
	}
	
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
	static <T> @NotNull Property<T> create() {
		return new SimpleProperty<>();
	}
	
	/**
	 * Generate new instance of property with initial value
	 *
	 * @param <T> property type value
	 * @return property instance
	 */
	static <T> @NotNull Property<T> create(@Nullable T initial) {
		return new SimpleProperty<>(initial);
	}
	
}
