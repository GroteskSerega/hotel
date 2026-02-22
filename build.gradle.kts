plugins {
	java
	id("org.springframework.boot") version "3.5.11-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.graalvm.buildtools.native") version "0.10.4"
	id("org.hibernate.orm") version "6.6.42.Final"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "hotel"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

graalvmNative {
	binaries {
		named("main") {
			metadataRepository {
				enabled.set(true)
			}

			buildArgs.add("-O2")
//			buildArgs.add("-O3")
			buildArgs.add("-H:+UnlockExperimentalVMOptions")
			buildArgs.add("--report-unsupported-elements-at-runtime")

//			if (project.hasProperty("nativeArgs")) {
//				val args = project.property("nativeArgs").toString().split(" ")
//				buildArgs.addAll(args)
//			}

//			buildArgs.addAll("--pgo-instrument")

//			buildArgs.add("--initialize-at-build-time=org.postgresql.Driver")
			buildArgs.add("--initialize-at-build-time=org.slf4j.LoggerFactory")
			buildArgs.add("--initialize-at-build-time=org.apache.kafka.clients.NetworkClient")

			buildArgs.add("--initialize-at-run-time=org.postgresql.Driver,org.postgresql.util.SharedTimer,java.sql.DriverManager")
			buildArgs.add("--initialize-at-run-time=io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess")
			buildArgs.add("--initialize-at-run-time=io.netty.util.internal.PlatformDependent")
		}
	}
}

hibernate {
	enhancement {
		enableLazyInitialization.set(true)
		enableDirtyTracking.set(true)
		enableAssociationManagement.set(true)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	runtimeOnly("org.postgresql:postgresql")

	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

	annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
