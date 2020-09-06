package com.xenoamess;

import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "addTestsFromRepo", defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES)
public class AddTestsFromRepo extends AbstractMojo {

    @Parameter(property = "repoGitUri", required = true)
    private String repoGitUri;

    @Parameter(
            defaultValue = "master",
            property = "repoGitBranch",
            required = true
    )
    private String repoGitBranch;

    @Parameter(
            defaultValue = "/src/test",
            property = "relativeDir",
            required = true
    )
    private String relativeDirectory;

    @Parameter(
            defaultValue = "${project.build.directory}/generated-test-sources/src/test",
            property = "outputDir",
            required = true
    )
    private File outputDirectory;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        project.addTestCompileSourceRoot(outputDirectory.getAbsolutePath());

        AddUtil.execute(project, repoGitUri, repoGitBranch, relativeDirectory, outputDirectory);
    }
}
