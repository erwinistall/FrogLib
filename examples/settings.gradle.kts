pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
		maven("https://repo.dairy.foundation/releases/")
	}
}

includeBuild("../core") {
	dependencySubstitution {
		substitute(module("com.erwinherrera.froglib:core")).using(project(":"))
	}
}

includeBuild("../pedroPathing") {
	dependencySubstitution {
		substitute(module("com.erwinherrera.froglib:pedroPathing")).using(project(":"))
	}
}

includeBuild("../photon") {
    dependencySubstitution {
        substitute(module("com.erwinherrera.froglib:photon")).using(project(":"))
    }
}
