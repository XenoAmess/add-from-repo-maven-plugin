# add-from-repo-maven-plugin

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.xenoamess/jcpp-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.xenoamess/jcpp-maven-plugin)

add-from-repo-maven-plugin, A plugin for adding other git repo's sources/tests into this repo while building.

## Goal:

addTestsFromRepo for adding tests from repo.
addSourcesFromRepo for adding sources from repo.

## Usage:

```
            <plugin>
                <groupId>com.xenoamess</groupId>
                <artifactId>add-from-repo-maven-plugin</artifactId>
                <version>0.0.1-SNAPSHOT</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>addTestsFromRepo</goal>
                        </goals>
                        <configuration>
                            <repoGitUri>https://github.com/apache/commons-crypto</repoGitUri>
                            <relativeDirectory>/src/test</relativeDirectory>
                            <outputDirectory>${project.build.directory}/generated-test-sources/test</outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```

## Params:

I will use addTestsFromRepo as example.
addSourcesFromRepo is similar actually, so I don't think I need to repeat twice.

All params and their default value are listed here.
```
   @Parameter(property = "repoGitUri", required = true)
   private String repoGitUri;

   @Parameter(
           defaultValue = "/src/test",
           property = "relativeDirectory",
           required = true
   )
   private String relativeDirectory;

   @Parameter(
           defaultValue = "${project.build.directory}/generated-test-sources/test",
           property = "outputDirectory",
           required = true
   )
   private File outputDirectory;

   @Parameter(defaultValue = "${project}")
   private MavenProject project;
```

**repoGitUri** means the git repo you want to add 's uri.

**relativeDirectory** means the folder which you want to add.

**outputDirectory** means the output dir.

**project** means your project. I don't think it shall be changed but if you insisted, then you are free to do what you want.

## Examples:

Projects using this:
https://github.com/XenoAmess/commonx
