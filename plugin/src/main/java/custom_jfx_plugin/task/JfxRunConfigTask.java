package custom_jfx_plugin.task;

import custom_jfx_plugin.configuration.JfxModule;
import custom_jfx_plugin.configuration.JfxOptions;
import custom_jfx_plugin.utils.ModuleUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.ApplicationPlugin;
import org.gradle.api.plugins.AppliedPlugin;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskAction;
import org.javamodularity.moduleplugin.extensions.RunModuleOptions;
import org.jetbrains.annotations.NotNull;
import ushiosan.jvm_utilities.lang.Obj;
import ushiosan.jvm_utilities.lang.collection.Collections;
import ushiosan.jvm_utilities.system.Arch;
import ushiosan.jvm_utilities.system.Platform;

import javax.inject.Inject;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class JfxRunConfigTask extends DefaultTask {
	
	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Gradle project
	 */
	private final Project project;
	
	/**
	 * Java Executable task
	 */
	private JavaExec javaExecTask;
	
	/* -----------------------------------------------------------------------
	 * Constructors
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Default constructor
	 *
	 * @param project gradle project
	 */
	@Inject
	public JfxRunConfigTask(@NotNull Project project) {
		this.project = project;
		// Get application run task
		project.getPluginManager()
			.withPlugin(ApplicationPlugin.APPLICATION_PLUGIN_NAME, this::initializeApplicationPluginRunTask);
	}
	
	/* -----------------------------------------------------------------------
	 * Methods
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Task execution process
	 */
	@TaskAction
	public void taskProcess() {
		JfxOptions options = Objects.requireNonNull(project.getExtensions().findByType(JfxOptions.class));
		// Check if java executable exists
		if (Obj.isNull(javaExecTask)) return;
		// Task configuration
		Set<JfxModule> modules = ModuleUtils.resolveAndCleanModules(options.modules.getOrElseThrow());
		Platform platform = options.platform.getOrElse(Platform.getRunningPlatform());
		Arch arch = options.arch.getOrElse(Arch.getRunningArch());
		RunModuleOptions moduleOpts = javaExecTask.getExtensions()
			.findByType(RunModuleOptions.class);
		
		FileCollection classpathWithoutJfx = javaExecTask.getClasspath()
			.filter(file -> filterClasspathWithoutJfx(file, modules));
		FileCollection classpathJfxJars = javaExecTask.getClasspath()
			.filter(file -> filterClasspathJfxJars(file, modules, platform, arch));
		
		// Check if module options exists
		if (Obj.isNotNull(moduleOpts)) {
			// Attach valid project classpath
			javaExecTask.setClasspath(classpathWithoutJfx.plus(classpathJfxJars));
			for (JfxModule module : modules) {
				moduleOpts.getAddModules().add(ModuleUtils.resolveModuleName(module));
			}
		} else {
			// Attach valid project classpath
			javaExecTask.setClasspath(classpathWithoutJfx);
			
			// Generate JVM arguments
			List<String> jfxArgs = Collections.listOf(
				"--module-path",
				classpathJfxJars.getAsPath());
			List<String> jvmArgs = Collections.mutableListOf(
				"--add-modules",
				modules.stream().map(ModuleUtils::resolveModuleName).collect(Collectors.joining(",")));
			
			// Check if jvm args are defined
			List<String> realJvmArgs = javaExecTask.getJvmArgs();
			if (Obj.isNotNull(realJvmArgs)) {
				jvmArgs.addAll(realJvmArgs);
			}
			// Attach modules
			jvmArgs.addAll(jfxArgs);
			
			// Override jvm args in the run task
			javaExecTask.setJvmArgs(jvmArgs);
		}
	}
	
	/* -----------------------------------------------------------------------
	 * Internal methods
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Initialize application plugin configuration
	 *
	 * @param plugin the application plugin instance
	 */
	private void initializeApplicationPluginRunTask(@NotNull AppliedPlugin plugin) {
		// Find Run task
		javaExecTask = Obj.cast(project.getTasks().findByName(ApplicationPlugin.TASK_RUN_NAME));
		// Set task dependency
		Obj.notNull(javaExecTask, it -> it.dependsOn(this));
	}
	
	/**
	 * Remove all JavaFx modules and libraries from classpath
	 *
	 * @param file    the file to filter
	 * @param modules the javafx project modules
	 * @return filter result
	 */
	private boolean filterClasspathWithoutJfx(@NotNull File file, @NotNull Collection<JfxModule> modules) {
		return modules.stream()
			.noneMatch(mod -> file.getName().contains(ModuleUtils.resolveArtifactName(mod)));
	}
	
	/**
	 * Filter only all JavaFx modules and libraries
	 *
	 * @param file     the file to check
	 * @param modules  the javafx modules
	 * @param platform the project platform
	 * @param arch     the project architecture
	 * @return filter result
	 */
	private boolean filterClasspathJfxJars(@NotNull File file, @NotNull Collection<JfxModule> modules,
		@NotNull Platform platform,
		@NotNull Arch arch) {
		return file.isFile() && modules.stream()
			.anyMatch(mod -> ModuleUtils.compareJfxFile(file, mod, platform, arch) ||
							 ModuleUtils.resolveModuleJarName(mod).contentEquals(file.getName()));
	}
	
}
