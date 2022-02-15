val ktlint by configurations.creating

dependencies {
  ktlint("com.pinterest:ktlint:0.43.2") {
    attributes {
      attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
    }
  }
  // additional 3rd party ruleset(s) can be specified here
  // just add them to the classpath (ktlint 'groupId:artifactId:version') and
  // ktlint will pick them up
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))
val outputFile = "${outputDir}ktlint-results.xml"

val ktlintCheck by tasks.creating(JavaExec::class) {
  inputs.files(inputFiles)
  outputs.dir(outputDir)
  isIgnoreExitValue = true
  description = "Check Kotlin code style."
  classpath = ktlint
  main = "com.pinterest.ktlint.Main"
  args = listOf("src/**/*.kt", "--reporter=checkstyle,output=${outputFile}", "--android")
}

val ktlintPreCommit by tasks.creating(JavaExec::class) {
  inputs.files(inputFiles)
  outputs.dir(outputDir)
  isIgnoreExitValue = false
  description = "Check Kotlin code style."
  main = "com.pinterest.ktlint.Main"
  classpath = ktlint
  args = listOf("src/**/*.kt", "--android")
}

val ktlintFormat by tasks.creating(JavaExec::class) {
  inputs.files(inputFiles)
  outputs.dir(outputDir)

  description = "Fix Kotlin code style deviations."
  classpath = ktlint
  main = "com.pinterest.ktlint.Main"
  args = listOf("-F", "src/**/*.kt", "--android")
}
