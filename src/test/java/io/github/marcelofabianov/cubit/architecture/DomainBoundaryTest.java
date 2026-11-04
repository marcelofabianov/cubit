package io.github.marcelofabianov.cubit.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
    packages = DomainBoundaryTest.ROOT_PACKAGE,
    importOptions = ImportOption.DoNotIncludeTests.class)
class DomainBoundaryTest {
  static final String ROOT_PACKAGE = "io.github.marcelofabianov.cubit";
  private static final String DOMAIN = "..domain..";
  private static final String MINECRAFT = "net.minecraft..";
  private static final String NEOFORGE = "net.neoforged..";
  private static final String MOJANG = "com.mojang..";

  @ArchTest
  static final ArchRule domainDoesNotDependOnTheGame =
      noClasses()
          .that()
          .resideInAPackage(DOMAIN)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(MINECRAFT, NEOFORGE, MOJANG);
}
