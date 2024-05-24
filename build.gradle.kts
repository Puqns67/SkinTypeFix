plugins {
	id("java")
	id("net.neoforged.gradle.userdev") version "7.0.134"
	id("idea")
}

base {
	archivesName = "${properties["mod_id"]}-neoforge"
	version = "${properties["mod_version"]}+mc${properties["minecraft_version"]}"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(properties["java_version"] as String)
	}

	withSourcesJar()
	sourceCompatibility = JavaVersion.valueOf("VERSION_${properties["java_version"]}")
	targetCompatibility = JavaVersion.valueOf("VERSION_${properties["java_version"]}")
}

dependencies {
	implementation("net.neoforged:neoforge:${properties["neoforge_version"]}")
}

tasks {
	processResources {
		filesMatching("META-INF/neoforge.mods.toml") {
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
