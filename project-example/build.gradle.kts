import ushiosan.custom.jfx.configuration.JfxModule

plugins {
	java
	application
	id("io.github.ushiosan.custom-jfx-plugin") version "0.1.0"
}

application {
	mainClass.set("example.Program")
}

customJfx {
	version.set("17")
	modules.set(listOf(JfxModule.CONTROLS, JfxModule.FXML))
}