package com.xenoamess;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class AddUtil {
    public static void execute(
            MavenProject project,
            String repoGitUri,
            String relativeDirectory,
            File outputDirectory
    ) throws MojoExecutionException {
        File tempFolder =
                new File(project.getBasedir().getAbsolutePath() + "/target/addSourcesFromRepoTmp/" + UUID.randomUUID().toString());
        tempFolder.mkdirs();
        try {
            FileUtils.forceDelete(tempFolder);
        } catch (IOException ignored) {
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

        try {
            FileUtils.forceDelete(tempFolder);
        } catch (IOException ignored) {
        }
    }
}
