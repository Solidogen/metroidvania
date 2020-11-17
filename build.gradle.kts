import com.soywiz.korge.gradle.*

buildscript {
	repositories {
		mavenLocal()
		maven { url = uri("https://dl.bintray.com/korlibs/korlibs") }
		maven { url = uri("https://plugins.gradle.org/m2/") }
		mavenCentral()
		google()
	}
	dependencies {
		classpath("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:2.0.0.2-rc2")
	}
}

apply<KorgeGradlePlugin>()

korge {
	id = "com.spyrdonapps.metroidvania"
	bundle("https://github.com/korlibs/korge-bundles.git::korge-box2d::4ac7fcee689e1b541849cedd1e017016128624b9##d15f5b28abbc2f58ba375d7204a6db2e90342978d02e675cd29249d0cade8f9f")
}
