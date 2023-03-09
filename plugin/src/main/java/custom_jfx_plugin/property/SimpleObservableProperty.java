package custom_jfx_plugin.property;

import custom_jfx_plugin.property.listener.ValueListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.EventListenerList;

class SimpleObservableProperty<T> extends SimpleProperty<T> implements ObservableProperty<T> {
	
	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Event listener container
	 */
	private final EventListenerList listenerList = new EventListenerList();
	
	/* -----------------------------------------------------------------------
	 * Constructor
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Default constructor without initial value
	 */
	public SimpleObservableProperty() {
		super();
	}
	
	/**
	 * Default constructor
	 *
	 * @param initial initial value
	 */
	public SimpleObservableProperty(T initial) {
		super(initial);
	}
	
	/* -----------------------------------------------------------------------
	 * Methods
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Bind changes to passed listener
	 *
	 * @param listener the listener object
	 */
	@Override
	public void addChangeListener(@NotNull ValueListener<T> listener) {
		listenerList.add(ValueListener.class, listener);
	}
	
	/**
	 * Remove the listener from instance
	 *
	 * @param listener the listener to remove
	 */
	@Override
	public void removeListener(@NotNull ValueListener<T> listener) {
		listenerList.remove(ValueListener.class, listener);
	}
	
	/**
	 * Change the property value
	 *
	 * @param newValue the new property value
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void set(@Nullable T newValue) {
		T oldValue = get();
		super.set(newValue);
		
		// Send signal to other elements
		try {
			ValueListener<T>[] listeners = listenerList.getListeners(ValueListener.class);
			for (var listener : listeners) {
				listener.onValueChange(oldValue, get());
			}
		} catch (Exception ignore) {
		}
	}
	
}
