var loaderName: String = "neoforge"

var javaVersion: String = property("java_version").toString()
var minecraftVersion: String = property("minecraft_version").toString()
var loaderVersion: String = property("${loaderName}_version").toString()

var modId: String = property("mod_id").toString()
var modName: String = property("mod_name").toString()
var modVersion: String = property("mod_version").toString()

fun getVersionType(version: String): String {
	return when {
		version.contains("alpha") -> "alpha"
		version.contains("beta") -> "beta"
		else -> "release"
	}
}

// Need to be set for neoforge
java.toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)

plugins {
	id("java")
	id("net.neoforged.gradle.userdev") version "7.0.142"
	id("com.modrinth.minotaur") version "2.+"
	id("idea")
}

base {
	archivesName = "${modId}-${loaderName}"
	version = "${modVersion}+mc${minecraftVersion}"
}

dependencies {
	implementation("net.neoforged:neoforge:${loaderVersion}")
}

java {
	withSourcesJar()
	sourceCompatibility = JavaVersion.valueOf("VERSION_${javaVersion}")
	targetCompatibility = JavaVersion.valueOf("VERSION_${javaVersion}")
}

modrinth {
	token = System.getenv("MODRINTH_TOKEN") ?: properties["modrinth_token"]?.toString()
	projectId = modId
	versionName = "[${loaderName.uppercase()}][${minecraftVersion}] $modName $modVersion"
	versionType = getVersionType(modVersion)
	versionNumber = modVersion
	uploadFile = tasks.jar as Any
	// Minotaur cannot detect Minecraft version in Neoforge environment
	// https://github.com/modrinth/minotaur/issues/59
	gameVersions = listOf(minecraftVersion)
}

idea {
	module {
		isDownloadSources = true
		isDownloadJavadoc = true
	}
}

tasks {
	compileJava {
		options.encoding = "UTF-8"
	}

	processResources {
		filesMatching("*.json") {
			expand(project.properties)
		}

		filesMatching("META-INF/neoforge.mods.toml") {
			expand(project.properties)
		}
	}

	jar {
		from("LICENSE") {
			rename { "${it}_${modId}" }
		}
	}
}
