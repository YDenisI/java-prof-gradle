dependencies {
    implementation ("ch.qos.logback:logback-classic")

    testImplementation ("org.openjdk.jmh:jmh-core")
    testAnnotationProcessor ("org.openjdk.jmh:jmh-generator-annprocess")
    testImplementation ("org.junit.jupiter:junit-jupiter-engine")
    testImplementation ("org.assertj:assertj-core")
    testImplementation ("org.mockito:mockito-core")
    testImplementation ("org.mockito:mockito-junit-jupiter")
}