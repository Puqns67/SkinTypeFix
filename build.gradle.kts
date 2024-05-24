plugins {
	id("java")
	id("fabric-loom") version "1.6-SNAPSHOT"
	id("idea")
}

java {
	withSourcesJar()
	sourceCompatibility = JavaVersion.valueOf("VERSION_${properties["java_version"]}")
	targetCompatibility = JavaVersion.valueOf("VERSION_${properties["java_version"]}")
}

dependencies {
	minecraft("com.mojang:minecraft:${properties["minecraft_version"]}")
	mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:${properties["fabric_version"]}")
}

base {
	archivesName = "${properties["mod_id"]}-fabric"
	version = "v${properties["mod_version"]}+mc${properties["minecraft_version"]}"
}

tasks {
	processResources {
		filesMatching("fabric.mod.json") {
			expand(project.properties)
		}
	}

	jar {
		from("LICENSE") {
			rename { "${it}_${project.properties["mod_id"]}" }
		}
	}
}

idea {
	module {
		isDownloadSources = true
		isDownloadJavadoc = true
	}
}
