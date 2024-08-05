pluginManagement {
	repositories {
		mavenLocal()
		gradlePluginPortal()
		mavenCentral()
		maven {
			name = "Neoforged"
			url = uri("https://maven.neoforged.net/releases")
			content {
				includeGroup("net.neoforged")
			}
		}
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "SkinTypeFix"
