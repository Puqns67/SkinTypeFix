plugins {
	id("java")
	id("fabric-loom") version "1.6.11"
	id("idea")
}

base {
	archivesName = "${properties["mod_id"]}-fabric"
	version = "${properties["mod_version"]}+mc${properties["minecraft_version"]}"
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

tasks {
	processResources {
		filesMatching("fabric.mod.json") {
			expand(project.properties)
		}
	}

	compileJava {
		options.encoding = "UTF-8"
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
