plugins {
    id("dev.frozenmilk.android-library") version "11.1.0-1.1.1"
    id("dev.frozenmilk.publish") version "0.0.5"
    id("dev.frozenmilk.doc") version "0.0.5"
}

android.namespace = "com.erwinherrera.froglib.pedroPathing"

dairyPublishing {
    gitDir = file("..")
}

ftc {
    kotlin()

    sdk {
        compileOnly(RobotCore)
        compileOnly(FtcCommon)
        compileOnly(Hardware)

        testImplementation(RobotCore)
    }

    solvers {
        api(core(dairyPublishing.version))
    }
}

dependencies {
    implementation("org.ejml:ejml-simple:0.39") {
        exclude(group = "org.ejml", module = "ejml-all")
    }
    implementation("com.pedropathing:ftc:2.0.6")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.erwinherrera.froglib"
            artifactId = "pedroPathing"
            // note that version was previously 2.1.1

            artifact(dairyDoc.dokkaHtmlJar)
            artifact(dairyDoc.dokkaJavadocJar)

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}