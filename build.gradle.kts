var loaderName: String = "fabric"

var javaVersion: String = property("java_version").toString()
var minecraftVersion: String = property("minecraft_version").toString()
var loaderVersion: String = property("${loaderName}_version").toString()

var modId: String = property("mod_id").toString()
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
	id("fabric-loom") version "1.6.11"
	id("com.modrinth.minotaur") version "2.+"
	id("idea")
}

base {
	archivesName = "${modId}-${loaderName}"
	version = "${modVersion}+mc${minecraftVersion}"
}

dependencies {
	minecraft("com.mojang:minecraft:${minecraftVersion}")
	mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:${loaderVersion}")
}

java {
	withSourcesJar()
	sourceCompatibility = JavaVersion.valueOf("VERSION_${javaVersion}")
	targetCompatibility = JavaVersion.valueOf("VERSION_${javaVersion}")
}

modrinth {
	token = System.getenv("MODRINTH_TOKEN") ?: properties["modrinth_token"]?.toString()
	projectId = modId
	versionName = "${loaderName}@${version}"
	versionType = getVersionType(modVersion)
	versionNumber = modVersion
	uploadFile = tasks.remapJar as Any
	additionalFiles = listOf(tasks.remapSourcesJar)
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
	}

	jar {
		from("LICENSE") {
			rename { "${it}_${modId}" }
		}
	}
}
