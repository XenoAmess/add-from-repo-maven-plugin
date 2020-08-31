package com.xenoamess;

import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "addFromRepo", defaultPhase = LifecyclePhase.INITIALIZE)
public class AddFromRepo extends AbstractMojo {

    @Parameter(property = "repoGitUri", required = true)
    private String repoGitUri;

    @Parameter(
            defaultValue = "",
            property = "relativeDirectory",
            required = true
    )
    private String relativeDirectory;

    @Parameter(
            defaultValue = "${project.build.directory}",
            property = "outputDirectory",
            required = true
    )
    private File outputDirectory;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        AddUtil.execute(project, repoGitUri, relativeDirectory, outputDirectory);
    }
}
