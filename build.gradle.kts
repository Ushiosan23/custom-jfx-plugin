allprojects {
	repositories {
		// Use Maven Central for resolving dependencies.
		mavenCentral()
		mavenLocal()
		maven {
			setUrl("https://plugins.gradle.org/m2/")
		}
	}
}