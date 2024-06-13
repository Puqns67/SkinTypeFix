pluginManagement {
	repositories {
		mavenLocal()
		maven {
			name = "Neoforged"
			url = uri("https://maven.neoforged.net/releases")
		}
		gradlePluginPortal()
		mavenCentral()
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "SkinTypeFix"
