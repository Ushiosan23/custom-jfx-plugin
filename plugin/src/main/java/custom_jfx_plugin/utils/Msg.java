package custom_jfx_plugin.utils;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ushiosan.jvm_utilities.error.Errors;
import ushiosan.jvm_utilities.lang.Obj;

public final class Msg {
	
	/**
	 * Output message prefix
	 */
	private static final String MSG_PREFIX = "custom-jfx-plugin%s: %s";
	
	/**
	 * This class cannot be instantiated
	 */
	private Msg() {}
	
	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */
	
	/**
	 * Generate message string
	 *
	 * @param project target project
	 * @param format  message format
	 * @param args    arguments
	 */
	public static void info(@Nullable Project project, @NotNull String format, Object @Nullable ... args) {
		System.out.println(formatMsg(project, format, args));
	}
	
	/**
	 * Generate message string
	 *
	 * @param format message format
	 * @param args   arguments
	 */
	public static void info(@NotNull String format, Object @Nullable ... args) {
		info(null, format, args);
	}
	
	/**
	 * Generate message string
	 *
	 * @param project target project
	 * @param format  message format
	 * @param args    arguments
	 */
	public static void error(@Nullable Project project, @NotNull String format, Object @Nullable ... args) {
		System.err.println(formatMsg(project, format, args));
	}
	
	/**
	 * Generate message string
	 *
	 * @param format message format
	 * @param args   arguments
	 */
	public static void error(@NotNull String format, Object @Nullable ... args) {
		error(null, format, args);
	}
	
	/**
	 * Generate message string
	 *
	 * @param project target project
	 * @param error   the error to show
	 */
	public static void error(@Nullable Project project, @NotNull Throwable error) {
		error(project, Errors.toString(error, false));
	}
	
	/* -----------------------------------------------------
	 * Internal methods
	 * ----------------------------------------------------- */
	
	/**
	 * Generate message string
	 *
	 * @param project target project
	 * @param format  message format
	 * @param args    arguments
	 * @return formatted message
	 */
	private static String formatMsg(@Nullable Project project, @NotNull String format, Object @Nullable ... args) {
		String projectPrefix = Obj.applyNotNull(project, it -> " \":" + it.getName() + "\"")
			.orElse("");
		String prefix = String.format(MSG_PREFIX, projectPrefix, format);
		// Generate result
		return String.format(prefix, args);
	}
	
}
