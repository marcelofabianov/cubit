package io.github.marcelofabianov.cubit.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
    packages = DomainBoundaryTest.ROOT_PACKAGE,
    importOptions = ImportOption.DoNotIncludeTests.class)
class DomainBoundaryTest {
  static final String ROOT_PACKAGE = "io.github.marcelofabianov.cubit";
  private static final String DOMAIN = "..cubit.domain..";
  private static final String JAVA = "java..";
  private static final String MOD_CLIENT = "..cubit.client..";
  private static final String GAME_CLIENT = "net.minecraft.client..";
  private static final String BLOCK = "..cubit.block..";
  private static final String ENTITY = "..cubit.entity..";
  private static final String GETTER = "get[A-Z].*";
  private static final String JADE_PLUGIN = "..cubit.compat.jade..";
  private static final String JADE = "snownee.jade..";

  /** AGENTS.md, rule 12: this keeps out the game and also the other packages of the mod. */
  @ArchTest
  static final ArchRule theDomainOnlyDependsOnItselfAndJava =
      classes()
          .that()
          .resideInAPackage(DOMAIN)
          .should()
          .onlyDependOnClassesThat()
          .resideInAnyPackage(DOMAIN, JAVA);

  /**
   * AGENTS.md, rule 23. The GameTests only find a client class that the server happens to load, and
   * this finds any of them.
   */
  @ArchTest
  static final ArchRule commonCodeDoesNotUseTheClient =
      noClasses()
          .that()
          .resideOutsideOfPackage(MOD_CLIENT)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(MOD_CLIENT, GAME_CLIENT);

  /** AGENTS.md, rule 42: the dock knows the robot by its interface, never by a concrete class. */
  @ArchTest
  static final ArchRule theDockDoesNotKnowTheEntity =
      noClasses()
          .that()
          .resideInAPackage(BLOCK)
          .should()
          .dependOnClassesThat()
          .resideInAPackage(ENTITY);

  /**
   * AGENTS.md, rule 24, and answer 667: only the plugin imports Jade, which Jade finds by itself,
   * so the mod never loads a class of Jade and opens without it.
   */
  @ArchTest
  static final ArchRule onlyThePluginUsesJade =
      noClasses()
          .that()
          .resideOutsideOfPackage(JADE_PLUGIN)
          .should()
          .dependOnClassesThat()
          .resideInAPackage(JADE);

  /** AGENTS.md, rule 14: {@code capacity()}, not {@code getCapacity()}. */
  @ArchTest
  static final ArchRule theDomainHasNoGetters =
      noMethods()
          .that()
          .areDeclaredInClassesThat()
          .resideInAPackage(DOMAIN)
          .should()
          .haveNameMatching(GETTER);
}
