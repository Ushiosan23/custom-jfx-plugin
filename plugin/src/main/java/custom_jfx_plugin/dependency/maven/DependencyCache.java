package custom_jfx_plugin.dependency.maven;

public final class DependencyCache {
	
	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Dependency configuration
	 */
	public final String configuration;
	
	/**
	 * Dependency result
	 */
	public final String resolved;
	
	/**
	 * Special dependency configuration version
	 */
	public final String special;
	
	/* -----------------------------------------------------------------------
	 * Constructors
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Default constructor
	 *
	 * @param configuration dependency configuration
	 * @param resolved      dependency result
	 * @param special       special dependency configuration version
	 */
	public DependencyCache(String configuration, String resolved, String special) {
		this.configuration = configuration;
		this.resolved = resolved;
		this.special = special;
	}
	
}
