var loaderName: String = "neoforge"

var javaVersion: String = property("java_version").toString()
var minecraftVersion: String = property("minecraft_version").toString()
var loaderVersion: String = property("${loaderName}_version").toString()
var parchmentVersion: String = property("parchment_version").toString()

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

plugins {
	id("java")
	id("net.neoforged.moddev") version "2.0.1-beta"
	id("com.modrinth.minotaur") version "2.8.7"
	id("idea")
}

base {
	archivesName = "${modId}-${loaderName}"
	version = "${modVersion}+mc${minecraftVersion}"
}

java {
	sourceCompatibility = JavaVersion.valueOf("VERSION_${javaVersion}")
	targetCompatibility = JavaVersion.valueOf("VERSION_${javaVersion}")
}

neoForge {
	version = loaderVersion

	mods {
		register(modId) {
			sourceSet(sourceSets["main"])
		}
	}

	runs {
		create("client") {
			client()
			logLevel = org.slf4j.event.Level.DEBUG
		}
	}

	parchment {
		minecraftVersion
		mappingsVersion = parchmentVersion
	}
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
