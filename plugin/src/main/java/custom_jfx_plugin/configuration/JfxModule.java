package custom_jfx_plugin.configuration;

import org.jetbrains.annotations.NotNull;

public enum JfxModule {
	/**
	 * Only the base of content of JavaFX
	 */
	BASE,
	/**
	 * Module with all graphics functionality
	 */
	GRAPHICS(BASE),
	/**
	 * Module with all GUI and functionality
	 */
	CONTROLS(BASE, GRAPHICS),
	/**
	 * Module with all functionality to use MVC in the project
	 */
	FXML(BASE, GRAPHICS),
	/**
	 * Module with all image, videos, audio functionality
	 */
	MEDIA(BASE, GRAPHICS),
	/**
	 * Module with functionality to work with JavaSwing and JavaFX
	 */
	SWING(BASE, GRAPHICS),
	/**
	 * Module with all web functionality
	 */
	WEB(BASE, CONTROLS, MEDIA),
	/**
	 * All modules
	 */
	ALL(BASE, GRAPHICS, CONTROLS, FXML, MEDIA, SWING, WEB);
	
	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Module dependencies
	 */
	private final JfxModule[] dependencies;
	
	/* -----------------------------------------------------------------------
	 * Constructor
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Default constructor
	 *
	 * @param dependencies module dependencies
	 */
	JfxModule(JfxModule @NotNull ... dependencies) {
		this.dependencies = dependencies;
	}
	
	/* -----------------------------------------------------------------------
	 * Methods
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Returns all module dependencies
	 *
	 * @return module dependencies
	 */
	public JfxModule @NotNull [] dependencies() {
		return dependencies;
	}
	
}
