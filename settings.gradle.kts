pluginManagement {
	repositories {
		mavenLocal()
		gradlePluginPortal()
		maven {
			name = "Neoforged"
			url = uri("https://maven.neoforged.net/releases")
		}
		mavenCentral()
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "SkinTypeFix"
