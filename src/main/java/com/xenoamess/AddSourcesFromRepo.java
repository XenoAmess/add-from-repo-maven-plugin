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

@Mojo(name = "addSourcesFromRepo", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class AddSourcesFromRepo extends AbstractMojo {

    @Parameter(property = "repoGitUri", required = true)
    private String repoGitUri;

    @Parameter(
            defaultValue = "/src/main",
            property = "relativeDirectory",
            required = true
    )
    private String relativeDirectory;

    @Parameter(
            defaultValue = "${project.build.directory}/generated-sources/main",
            property = "outputDirectory",
            required = true
    )
    private File outputDirectory;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        project.addCompileSourceRoot(outputDirectory.getAbsolutePath());

        File tempFolder = new File(project.getBasedir().getAbsolutePath() + "/target/addSourcesFromRepoTmp/");
        tempFolder.mkdirs();
        tempFolder.delete();
        tempFolder.mkdirs();

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

        tempFolder.delete();
    }
}
