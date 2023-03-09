package custom_jfx_plugin.property.listener;

import org.jetbrains.annotations.Nullable;

import java.util.EventListener;

public interface ValueListener<T> extends EventListener {
	
	/**
	 * Called when the property value was changed
	 *
	 * @param oldValue the last value of the property
	 * @param newValue the new current value
	 */
	void onValueChange(@Nullable T oldValue, @Nullable T newValue);
	
}
