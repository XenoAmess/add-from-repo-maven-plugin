package com.xenoamess;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class AddUtil {
    public static void execute(
            MavenProject project,
            String repoGitUri,
            String repoGitBranch,
            String relativeDirectory,
            File outputDirectory,
            String[] relativeDirectories,
            File[] outputDirectories
    ) throws MojoExecutionException {
        final int length1 = ArrayUtils.getLength(relativeDirectories);
        final int length2 = ArrayUtils.getLength(outputDirectories);
        if (length1 != length2) {
            throw new MojoExecutionException(
                    "relativeDirectories and outputDirectories have different param "
                            + "lengths. "
                            + length1
                            + " and "
                            + length2 + "."
            );
        }
        if (length1 == 0) {
            execute(
                    project,
                    repoGitUri,
                    repoGitBranch,
                    relativeDirectories,
                    outputDirectories
            );
        } else {
            execute(
                    project,
                    repoGitUri,
                    repoGitBranch,
                    new String[]{relativeDirectory},
                    new File[]{outputDirectory}
            );
        }
    }

    public static void execute(
            MavenProject project,
            String repoGitUri,
            String repoGitBranch,
            String[] relativeDirectories,
            File[] outputDirectories
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
                    .setBranch(repoGitBranch)
                    .setDirectory(tempFolder)
                    .call();
        } catch (GitAPIException e) {
            throw new MojoExecutionException("Cannot clone git repo", e);
        }

        try {
            for (int i = 0; i < relativeDirectories.length; i++) {
                FileUtils.copyDirectory(
                        new File(tempFolder.getAbsolutePath() + relativeDirectories[i]),
                        outputDirectories[i]
                );
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot copy dirs", e);
        }

        try {
            FileUtils.forceDelete(tempFolder);
        } catch (IOException ignored) {
        }
    }
}
