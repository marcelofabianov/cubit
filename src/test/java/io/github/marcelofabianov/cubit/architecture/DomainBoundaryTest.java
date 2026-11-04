package io.github.marcelofabianov.cubit.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
        packages = "io.github.marcelofabianov.cubit",
        importOptions = ImportOption.DoNotIncludeTests.class)
class DomainBoundaryTest {

    // allowEmptyShould: the domain package has no classes until the first domain issue.
    @ArchTest
    static final ArchRule domainDoesNotDependOnTheGame =
            noClasses()
                    .that()
                    .resideInAPackage("..domain..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage("net.minecraft..", "net.neoforged..", "com.mojang..")
                    .allowEmptyShould(true);
}
