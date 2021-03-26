package io.github.astrapi69

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Disabled
import io.github.astrapi69.search.PathFinder
import io.github.astrapi69.gradle.migration.data.DependenciesInfo
import io.github.astrapi69.modify.ModifyFileExtensions
import io.github.astrapi69.delete.DeleteFileExtensions
import io.github.astrapi69.gradle.migration.data.GradleRunConfigurationsCopier
import io.github.astrapi69.gradle.migration.data.CopyGradleRunConfigurations
import io.github.astrapi69.io.file.filter.PrefixFileFilter
import io.github.astrapi69.throwable.RuntimeExceptionDecorator
import java.lang.Exception
import java.io.File

internal class InitialKotlinTemplateTest {
    @Test
    @Disabled
    fun testRenameToConcreteProject() {
        val projectDescription: String
        // TODO change the following description with your project description
        // and then remove the annotation Disabled and run this unit test method
        projectDescription = "!!!Chage this description with your project description!!!"
        renameToConcreteProject(projectDescription)
    }

    private fun renameToConcreteProject(projectDescription: String) {
        val concreteProjectName: String
        val templateProjectName: String
        val sourceProjectDir: File
        val settingsGradle: File
        val dotTravisYml: File
        val dotGithubDir: File
        val codeOfConduct: File
        val readme: File
        val initialTemplateClassFile: File
        //
        sourceProjectDir = PathFinder.getProjectDirectory()
        templateProjectName = KOTLIN_LIBRARY_TEMPLATE_NAME
        concreteProjectName = sourceProjectDir.name
        // adapt settings.gradle file
        settingsGradle = File(sourceProjectDir, DependenciesInfo.SETTINGS_GRADLE_FILENAME)
        ModifyFileExtensions.modifyFile(settingsGradle.toPath()) { count: Int?, input: String ->
            input.replace(templateProjectName.toRegex(), concreteProjectName) + System
                .lineSeparator()
        }
        // adapt CODE_OF_CONDUCT.md file
        dotGithubDir = File(sourceProjectDir, DependenciesInfo.DOT_GITHUB_DIRECTORY_NAME)
        codeOfConduct = File(dotGithubDir, DependenciesInfo.CODE_OF_CONDUCT_FILENAME)
        ModifyFileExtensions.modifyFile(codeOfConduct.toPath()) { count: Int?, input: String ->
            input.replace(templateProjectName.toRegex(), concreteProjectName) + System
                .lineSeparator()
        }
        // adapt .travis.yml file
        dotTravisYml = File(sourceProjectDir, DependenciesInfo.DOT_TRAVIS_FILENAME)
        ModifyFileExtensions.modifyFile(dotTravisYml.toPath()) { count: Int?, input: String ->
            input.replace(templateProjectName.toRegex(), concreteProjectName) + System
                .lineSeparator()
        }
        // delete template class
        initialTemplateClassFile = PathFinder
            .getRelativePath(
                PathFinder.getSrcMainJavaDir(), "io", "github", "astrapi69",
                "InitialTemplate.java"
            )
        DeleteFileExtensions.delete(initialTemplateClassFile)
        // change projectDescription from gradle.properties
        val gradleProperties = File(sourceProjectDir, DependenciesInfo.GRADLE_PROPERTIES_NAME)
        ModifyFileExtensions.modifyFile(gradleProperties.toPath()) { count: Int?, input: String ->
            input
                .replace(
                    "projectDescription=Template project for create kotlin library projects".toRegex(),
                    "projectDescription=$projectDescription"
                ) + System.lineSeparator()
        }

        // adapt README.md file
        readme = File(sourceProjectDir, DependenciesInfo.README_FILENAME)
        ModifyFileExtensions.modifyFile(readme.toPath()) { count: Int?, input: String ->
            input.replace(templateProjectName.toRegex(), concreteProjectName) + System
                .lineSeparator()
        }
        ModifyFileExtensions.modifyFile(readme.toPath()) { count: Int?, input: String ->
            input
                .replace(
                    "Template project for create kotlin library projects".toRegex(),
                    projectDescription
                ) + System.lineSeparator()
        }
        ModifyFileExtensions.modifyFile(readme.toPath()) { count: Int?, input: String ->
            input
                .replace(
                    "kotlinLibraryTemplateVersion".toRegex(),
                    GradleRunConfigurationsCopier.getProjectVersionKeyName(concreteProjectName)
                ) + System.lineSeparator()
        }

        // create run configurations for current project
        val sourceProjectDirNamePrefix: String
        val targetProjectDirNamePrefix: String
        val copyGradleRunConfigurationsData: CopyGradleRunConfigurations
        val sourceProjectName: String
        val targetProjectName: String
        sourceProjectName = templateProjectName
        targetProjectName = concreteProjectName
        sourceProjectDirNamePrefix = sourceProjectDir.parent + "/"
        targetProjectDirNamePrefix = sourceProjectDirNamePrefix
        copyGradleRunConfigurationsData = GradleRunConfigurationsCopier
            .newCopyGradleRunConfigurations(
                sourceProjectName, targetProjectName,
                sourceProjectDirNamePrefix, targetProjectDirNamePrefix, true, true
            )
        GradleRunConfigurationsCopier.of(copyGradleRunConfigurationsData).copy()

        // delete template run configurations
        RuntimeExceptionDecorator.decorate<Exception> {
            DeleteFileExtensions
                .deleteFilesWithFileFilter(
                    copyGradleRunConfigurationsData.ideaTargetDir,
                    PrefixFileFilter("kotlin_library_template", false)
                )
        }
    }

    companion object {
        const val KOTLIN_LIBRARY_TEMPLATE_NAME = "kotlin-library-template"
    }
}
