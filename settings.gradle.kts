pluginManagement {
	repositories {
		mavenLocal()
		gradlePluginPortal()
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		mavenCentral()
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "SkinTypeFix"
