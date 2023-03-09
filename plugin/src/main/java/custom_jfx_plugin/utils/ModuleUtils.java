package custom_jfx_plugin.utils;

import custom_jfx_plugin.configuration.JfxModule;
import org.gradle.api.artifacts.Dependency;
import org.jetbrains.annotations.NotNull;
import ushiosan.jvm_utilities.lang.collection.Collections;
import ushiosan.jvm_utilities.lang.collection.elements.Pair;
import ushiosan.jvm_utilities.system.Arch;
import ushiosan.jvm_utilities.system.Platform;

import java.io.File;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ModuleUtils {
	
	/**
	 * This class cannot be instantiated
	 */
	private ModuleUtils() {}
	
	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/
	
	/**
	 * JavaFX maven group id
	 */
	private static final String JAVAFX_GROUP_ID = "org.openjfx";
	
	/**
	 * JavaFX artifact name
	 */
	private static final String ARTIFACT_NAME_FORMAT = "javafx-%s";
	
	/**
	 * JavaFX artifact format
	 */
	private static final String ARTIFACT_PREFIX_FORMAT = String.format("%s:%s:%s", JAVAFX_GROUP_ID, "%s", "%s");
	
	/**
	 * JavaFX module format
	 */
	private static final String MODULE_PREFIX_FORMAT = "javafx.%s";
	
	/* -----------------------------------------------------------------------
	 * Methods
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Resolve all valid modules
	 *
	 * @param modules the modules to inspect
	 * @return a set with all modules
	 */
	public static @NotNull Set<JfxModule> resolveAndCleanModules(@NotNull Collection<JfxModule> modules) {
		final Set<JfxModule> moduleResult = Collections.mutableSetOf();
		
		// Iterate all elements
		for (JfxModule mod : modules) {
			// Ignore All module
			if (mod == JfxModule.ALL) {
				moduleResult.addAll(Collections.listOf(mod.dependencies()));
				continue;
			}
			// Insert all modules and dependencies
			moduleResult.add(mod);
			moduleResult.addAll(Collections.listOf(mod.dependencies()));
		}
		
		return Collections.setOf(moduleResult);
	}
	
	/**
	 * Resolve all valid modules
	 *
	 * @param modules the modules to inspect
	 * @return a set with all modules
	 */
	public static @NotNull Set<JfxModule> resolveAndCleanModules(JfxModule... modules) {
		return resolveAndCleanModules(Collections.listOf(modules));
	}
	
	/**
	 * Resolve java module name
	 *
	 * @param module the {@link JfxModule} to resolve
	 * @return the java module name
	 */
	public static @NotNull String resolveModuleName(@NotNull JfxModule module) {
		return String.format(MODULE_PREFIX_FORMAT, moduleName(module));
	}
	
	/**
	 * Resolve java module jar name
	 *
	 * @param module the {@link JfxModule} to resolve
	 * @return the java jar module name
	 */
	public static @NotNull String resolveModuleJarName(@NotNull JfxModule module) {
		return resolveModuleName(module) + ".jar";
	}
	
	/**
	 * Resolve multiple modules and dependencies
	 *
	 * @param modules the modules to resolve
	 * @return a set with all formatted artifacts
	 */
	public static @NotNull Set<String> resolveArtifacts(String version, Collection<JfxModule> modules) {
		final Set<JfxModule> moduleResult = resolveAndCleanModules(modules);
		
		// Generate result
		return moduleResult.stream()
			.map(ModuleUtils::resolveArtifact)
			.map(it -> String.format(it, version))
			.map(String::trim)
			.collect(Collectors.toUnmodifiableSet());
	}
	
	/**
	 * Resolve only the artifact name
	 *
	 * @param module the module to resolve
	 * @return the artifact name
	 */
	public static @NotNull String resolveArtifactName(@NotNull JfxModule module) {
		return String.format(ARTIFACT_NAME_FORMAT, moduleName(module));
	}
	
	/**
	 * Resolve the module artifact with dependencies
	 *
	 * @param module the module to resolve
	 * @return a formatted artifact
	 */
	public static @NotNull String resolveArtifact(@NotNull JfxModule module) {
		return String.format(ARTIFACT_PREFIX_FORMAT, resolveArtifactName(module), "%s");
	}
	
	/**
	 * Returns the artifact configuration depending on the platform and architecture
	 *
	 * @param platform the target configuration platform
	 * @param arch     the target configuration architecture
	 * @return the artifact configuration
	 */
	public static @NotNull Pair<String, String> getPlatformArtifactConfig(@NotNull Platform platform, @NotNull Arch arch) {
		switch (platform) {
			case WINDOWS:
				return getWindowsArtifactConfiguration(arch);
			case MACOS:
				return getMacosArtifactConfiguration(arch);
			case LINUX:
			case SOLARIS:
			case FREE_BSD:
				return getLinuxArtifactConfiguration(arch);
			default:
				throw new RuntimeException("Platform is not supported");
		}
	}
	
	/**
	 * Check if dependency is a javafx dependency
	 *
	 * @param dependency the dependency to check
	 * @return {@code true} if dependency is a javafx element or {@code false} otherwise
	 */
	public static boolean checkJavaFxDependency(@NotNull Dependency dependency) {
		return Objects.requireNonNull(dependency.getGroup()).contentEquals(JAVAFX_GROUP_ID);
	}
	
	/**
	 * Check if classpath file is a valid jfx jar file
	 *
	 * @param file     the file to inspect
	 * @param module   the module to check
	 * @param platform target platform
	 * @param arch     target architecture
	 * @return {@code true} if the file is a valid jfx file or {@code false} otherwise
	 */
	public static boolean compareJfxFile(@NotNull File file, @NotNull JfxModule module, @NotNull Platform platform,
		@NotNull Arch arch) {
		// Temporal variables
		String filename = file.getName();
		Pair<String, String> platConfig = getPlatformArtifactConfig(platform, arch);
		Pattern pattern = Pattern.compile(String.format("%s-.+-%s\\.jar", resolveArtifactName(module), platConfig.first));
		
		// Check result
		return pattern.matcher(filename).matches();
	}
	
	/* -----------------------------------------------------------------------
	 * Internal methods
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Returns the artifact/module name
	 *
	 * @param module the module to check
	 * @return the artifact/module name
	 */
	private static @NotNull String moduleName(@NotNull JfxModule module) {
		return module.name().toLowerCase(Locale.ROOT);
	}
	
	@SuppressWarnings("unused")
	private static @NotNull Pair<String, String> getWindowsArtifactConfiguration(@NotNull Arch arch) {
		return Pair.of("win", "windows-x86_64");
	}
	
	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	private static @NotNull Pair<String, String> getMacosArtifactConfiguration(@NotNull Arch arch) {
		switch (arch) {
			case ARM:
				return Pair.of("mac-aarch64", "osx-aarch_64");
			default:
				return Pair.of("mac", "osx-x86_64");
		}
	}
	
	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	private static @NotNull Pair<String, String> getLinuxArtifactConfiguration(@NotNull Arch arch) {
		switch (arch) {
			case ARM:
				return Pair.of("linux-aarch64", "linux-aarch_64");
			default:
				return Pair.of("linux", "linux-86_64");
		}
	}
	
}
