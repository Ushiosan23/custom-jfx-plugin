plugins {
	// Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
	`java-gradle-plugin`
	id("com.gradle.plugin-publish") version "1.1.0"
}

group = "io.github.ushiosan23"

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
	// Define the plugin
	val customJavaFXPlugin by plugins.creating {
		id = "io.github.ushiosan23.custom_jfx_plugin"
		version = "0.1.0"
		displayName = "Custom JavaFX Plugin"
		description = "Create projects with JavaFX with different configurations from the same platform"
		implementationClass = "custom_jfx_plugin.CustomJfxPlugin"
	}
}

pluginBundle {
	website = "https://github.com/Ushiosan23/custom-jfx-plugin"
	vcsUrl = "https://github.com/Ushiosan23/custom-jfx-plugin.git"
	tags = listOf("java", "javafx", "manager")
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
	testClassesDirs = functionalTestSourceSet.output.classesDirs
	classpath = functionalTestSourceSet.runtimeClasspath
	useJUnitPlatform()
}

gradlePlugin.testSourceSets(functionalTestSourceSet)

tasks.named<Task>("check") {
	// Run the functional tests as part of `check`
	dependsOn(functionalTest)
}

tasks.named<Test>("test") {
	// Use JUnit Jupiter for unit tests.
	useJUnitPlatform()
}

dependencies {
	implementation("org.javamodularity:moduleplugin:1.8.12")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("com.github.ushiosan23:jvm-utilities:0.4.3")
	
	compileOnly("org.jetbrains:annotations:24.0.0")
	// Use JUnit Jupiter for testing.
	testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}