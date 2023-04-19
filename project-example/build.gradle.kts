import custom_jfx_plugin.configuration.JfxModule

plugins {
	java
	application
	id("io.github.ushiosan23.custom_jfx_plugin") version "0.1.3"
}

application {
	mainClass.set("example.Program")
}

customJfx {
	version.set("#latest#")
	modules.set(listOf(JfxModule.FXML, JfxModule.CONTROLS, JfxModule.MEDIA, JfxModule.WEB))
}