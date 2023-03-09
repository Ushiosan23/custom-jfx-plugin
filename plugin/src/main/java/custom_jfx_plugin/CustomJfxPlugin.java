package custom_jfx_plugin;

import custom_jfx_plugin.configuration.JfxOptions;
import custom_jfx_plugin.task.JfxRunConfigTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.javamodularity.moduleplugin.ModuleSystemPlugin;
import org.jetbrains.annotations.NotNull;

public class CustomJfxPlugin implements Plugin<Project> {
	
	/**
	 * Apply this plugin to the given target object.
	 *
	 * @param project The target object
	 */
	@Override
	public void apply(@NotNull Project project) {
		// Register some plugins
		project.getPlugins().apply(ModuleSystemPlugin.class);
		
		// Register plugin configuration
		project.getExtensions()
			.create("customJfx", JfxOptions.class, project);
		
		// Register tasks
		project.getTasks()
			.create("customJfxRunConfig", JfxRunConfigTask.class, project);
	}
	
}
