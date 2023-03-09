rootProject.name = "custom-jfx-plugin"

pluginManagement {
	repositories {
		// Use Maven Central for resolving dependencies.
		mavenCentral()
		mavenLocal()
		maven {
			setUrl("https://plugins.gradle.org/m2/")
		}
	}
}

include("plugin", "project-example")