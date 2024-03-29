package custom_jfx_plugin.configuration;

import custom_jfx_plugin.dependency.maven.MavenResolver;
import custom_jfx_plugin.property.ObservableProperty;
import custom_jfx_plugin.utils.ModuleUtils;
import custom_jfx_plugin.utils.Msg;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ushiosan.jvm_utilities.lang.Obj;
import ushiosan.jvm_utilities.lang.collection.elements.Pair;
import ushiosan.jvm_utilities.system.Arch;
import ushiosan.jvm_utilities.system.Platform;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ushiosan.jvm_utilities.lang.Obj.toObjString;

public abstract class JfxOptions {
	
	/**
	 * Default JavaFX version
	 */
	private static final String DEFAULT_VERSION = "#latest#";
	
	/**
	 * Default JavaFX dependency configuration
	 */
	private static final String DEFAULT_DEPENDENCY_CONFIGURATION = "implementation";
	
	/**
	 * Default JavaFX dependency configuration
	 */
	private static final String DEFAULT_TEST_DEPENDENCY_CONFIGURATION = "testImplementation";
	
	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Project instance
	 */
	private final Project project;
	
	/**
	 * JavaFX version
	 */
	public final ObservableProperty<String> version;
	
	/**
	 * JavaFX target architecture
	 */
	public final ObservableProperty<Arch> arch;
	
	/**
	 * JavaFX target platform
	 */
	public final ObservableProperty<Platform> platform;
	
	/**
	 * JavaFX required modules
	 */
	public final ObservableProperty<List<JfxModule>> modules;
	
	/**
	 * Dependency configuration name
	 */
	public final ObservableProperty<String> dependencyConfiguration;
	
	/* -----------------------------------------------------------------------
	 * Constructors
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Default constructor
	 *
	 * @param project Current project
	 */
	public JfxOptions(@NotNull Project project) {
		this.project = project;
		// Initialize options properties
		version = ObservableProperty.create(DEFAULT_VERSION);
		arch = ObservableProperty.create(Arch.getRunningArch());
		platform = ObservableProperty.create(Platform.getRunningPlatform());
		modules = ObservableProperty.create();
		dependencyConfiguration = ObservableProperty.create(DEFAULT_DEPENDENCY_CONFIGURATION);
		
		updateProjectConfiguration(null, true);
		
		// Add listeners
		version.addChangeListener(this::updateVersion);
		arch.addChangeListener(this::updateArch);
		platform.addChangeListener(this::updatePlatform);
		modules.addChangeListener(this::updateModules);
		dependencyConfiguration.addChangeListener((o, n) -> updateDependencyConfiguration(false, o, n));
	}
	
	/* -----------------------------------------------------------------------
	 * Internal methods
	 * -----------------------------------------------------------------------*/
	
	private void updateArch(@Nullable Arch old, @Nullable Arch newArch) {
		if (old != newArch) {
			Msg.error(project, "Arch update -> %s to %s", toObjString(old), toObjString(newArch));
			updateProjectConfiguration(null, false);
		}
	}
	
	private void updateDependencyConfiguration(boolean isTest, @Nullable String old, @Nullable String newConfiguration) {
		if (!Objects.equals(old, newConfiguration)) {
			String kind = isTest ? "(Test)" : "(Common)";
			Msg.error(project, "Configuration update %s -> %s to %s", kind,
					  toObjString(old),
					  toObjString(newConfiguration));
			updateProjectConfiguration(old, false);
		}
	}
	
	private void updateModules(@Nullable List<JfxModule> old, @Nullable List<JfxModule> newModules) {
		Msg.error(project, "Modules update -> %s to %s", toObjString(old), toObjString(newModules));
		updateProjectConfiguration(null, false);
	}
	
	private void updatePlatform(@Nullable Platform old, @Nullable Platform newPlatform) {
		if (old != newPlatform) {
			Msg.error(project, "Platform update -> %s to %s", toObjString(old), toObjString(newPlatform));
			updateProjectConfiguration(null, false);
		}
	}
	
	private void updateVersion(@Nullable String old, @Nullable String newVersion) {
		if (!Objects.equals(old, newVersion)) {
			Msg.error(project, "Version update -> %s to %s", toObjString(old), toObjString(newVersion));
			updateProjectConfiguration(null, false);
		}
	}
	
	/**
	 * Update project dependency configuration
	 */
	@SuppressWarnings("DataFlowIssue")
	private void updateProjectConfiguration(@Nullable String oldConfiguration, boolean silent) {
		// Check if dependencies exists
		if (modules.isNotPresent()) return;
		// Get all project dependencies
		List<JfxModule> moduleList = modules.getOrElseThrow("modules");
		Set<String> moduleArtifacts = ModuleUtils.resolveArtifacts(version.getOrElse(DEFAULT_VERSION), moduleList);
		Pair<String, String> artifactConfiguration = ModuleUtils.getPlatformArtifactConfig(
			platform.getOrElse(Platform.getRunningPlatform()),
			arch.getOrElse(Arch.getRunningArch()));
		
		// Remove old dependencies
		removeOldDependencies(oldConfiguration);
		
		// Resolve dependencies
		for (String item : moduleArtifacts) {
			String artifactId = MavenResolver.getInstance()
									.resolveDependency(item, silent) + ":" + artifactConfiguration.first;
			
			// Attach dependency
			project.getDependencies()
				.add(dependencyConfiguration.getOrElse(DEFAULT_DEPENDENCY_CONFIGURATION), artifactId);
			project.getDependencies()
				.add(DEFAULT_TEST_DEPENDENCY_CONFIGURATION, artifactId);
		}
	}
	
	/**
	 * Remove old project dependencies
	 *
	 * @param configuration dependency configuration
	 */
	private void removeOldDependencies(@Nullable String configuration) {
		// Resolve project configuration
		String realConfiguration = Obj.isNull(configuration) ? DEFAULT_DEPENDENCY_CONFIGURATION :
								   configuration;
		Configuration container = project.getConfigurations()
			.findByName(realConfiguration);
		
		// Ignore invalid configurations
		if (Obj.isNull(container)) return;
		
		// Remove dependencies
		container.getDependencies()
			.removeIf(ModuleUtils::checkJavaFxDependency);
	}
	
}
