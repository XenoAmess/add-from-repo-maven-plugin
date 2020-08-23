package com.xenoamess;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

@Mojo(name = "addTestsFromRepo", defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES)
public class AddTestsFromRepo extends AbstractMojo {

    @Parameter(property = "repoGitUri", required = true)
    private String repoGitUri;

    @Parameter(
            defaultValue = "/src/test",
            property = "relativeDir",
            required = true
    )
    private String relativeDirectory;

    @Parameter(
            defaultValue = "${project.build.directory}/generated-test-sources/test",
            property = "outputDir",
            required = true
    )
    private File outputDirectory;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        project.addTestCompileSourceRoot(outputDirectory.getAbsolutePath());

        File tempFolder = new File(project.getBasedir().getAbsolutePath() + "/target/addTestsFromRepoTmp/");
        tempFolder.mkdirs();
        while (!tempFolder.delete()) {
        }
        while (!tempFolder.mkdirs()) {
        }
        try {
            Git.cloneRepository()
                    .setURI(repoGitUri)
                    .setDirectory(tempFolder)
                    .call();
        } catch (GitAPIException e) {
            throw new MojoExecutionException("Cannot clone git repo", e);
        }

        try {
            FileUtils.copyDirectory(
                    new File(tempFolder.getAbsolutePath() + relativeDirectory),
                    outputDirectory
            );
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot copy dirs", e);
        }

        while (!tempFolder.delete()) {
        }
    }
}
