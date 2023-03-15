# Custom JavaFX Plugin

Gradle plugin in charge of managing and configuring the project with the necessary
dependencies to use JavaFX.

Unlike the [official JavaFX plugin](https://github.com/openjfx/javafx-gradle-plugin), it offers the ability to easily compile
and distribute
programs within the same platform.

## How to use

To use this plugin it is necessary to have the following requirements:

- Any Java 11+ distribution (JDK, JRE, OpenJDK, Temurin)
- Gradle 7+

In your project in the file ```build.gradle``` or ```build.gradle.kts``` you must use the following code:

#### Groovy DSL

```groovy
import custom_jfx_plugin.configuration.JfxModule

plugins {
	id "io.github.ushiosan23.custom_jfx_plugin" version "0.1.0"
}


customJfx {
	// required property for correct dependency inclusion
	modules.set([JfxModule.FXML, JfxModule.CONTROLS])
}
```

#### Kotlin DSL

```kotlin
import custom_jfx_plugin.configuration.JfxModule

plugins {
	id("io.github.ushiosan23.custom_jfx_plugin") version "0.1.0"
}


customJfx {
	// required property for correct dependency inclusion
	modules.set(listOf(JfxModule.FXML, JfxModule.CONTROLS))
}
```

With this the configuration would be ready for the platform on which the application is running.

### Change of platform and architecture

In this plugin it is possible to change the target platform of the application and the architecture of the processor (Currently
only ```x86_64``` and ```ARM``` are supported but only the supported platforms).
To do this in the plugin configuration area you can make the following changes:

#### Groovy DSL

```groovy
import ushiosan.jvm_utilities.system.Platform
import ushiosan.jvm_utilities.system.Arch

customJfx {
	platform.set Platform.MACOS
	arch.set Arch.ARM
	//... Other configurations
}
```

#### Kotlin DSL

```kotlin

import ushiosan.jvm_utilities.system.Arch
import ushiosan.jvm_utilities.system.Platform

customJfx {
	platform.set(Platform.MACOS)
	arch.set(Arch.ARM)
	//... Other configurations
}
```

By default, if these properties are not specified, the properties of the system in which the application is running are taken.

### Change JavaFX version

Like the official plugin, you can make use of the version of JavaFX you want, but unlike that it is possible to use special
settings in case you want to perform specific tasks.
Currently, there are only 2 special configurations, and they are limited to the type of version that will be obtained from Maven
Central:

- ```#latest#```
	- Returns the latest stable version of JavaFX and includes it in the project for use.
- ```#early#```
	- Returns the latest version of JavaFx named early, beta, or something similar.

It should be noted that if a new version of JavaFx comes out, it will be updated automatically without the intervention of the
programmer, that is why these configurations must be handled with caution and be aware of the problems that can cause.

#### Gradle DSL

```groovy
customJfx {
	// Using a static JavaFx Version
	version.set "17"
	// Using a special configuration
	version.set "#early#"
}
```

#### Kotlin DSL

```kotlin
customJfx {
	// Using a static JavaFx Version
	version.set("17")
	// Using a special configuration
	version.set("#early#")
}
```

By default, and for practical purposes the version is defined as ```#latest#```.

### Custom build configuration

Like the official plugin it is possible to change the configuration of the dependencies. This in order to use the dependencies
according to the needs of the project. It is not the same to create an application and that it contains the necessary
dependencies than a library that should not contain all the dependencies.
It is for that reason that it can be configured as follows:

#### Groovy DSL

```groovy
customJfx {
	// Default value
	dependencyConfiguration.set "implementation"
	// Other configurations
	dependencyConfiguration.set "compileOnly"
	dependencyConfiguration.set "api"
}
```

#### Kotlin DSL

```kotlin
customJfx {
	// Default value
	dependencyConfiguration.set("implementation")
	// Other configurations
	dependencyConfiguration.set("compileOnly")
	dependencyConfiguration.set("api")
}
```

By default, the setting is ```implementation```.

### What's Next

The next thing to implement is the possibility of creating executables configured for each platform automatically depending on
the configuration and the platform for which it is intended. In addition to adding the configuration for the application to run,
without the need for the programmer or user to insert any command.