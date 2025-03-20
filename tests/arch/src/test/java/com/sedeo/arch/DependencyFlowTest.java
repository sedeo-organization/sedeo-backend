package com.sedeo.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.junit.LocationProvider;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.runner.RunWith;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "com.sedeo",
        importOptions = ImportOption.DoNotIncludeTests.class,
        locations = DependencyFlowTest.CustomLocationProvider.class)
public class DependencyFlowTest {

    @ArchTest
    public static final ArchRule domainShouldNotDependOnRepositoryOrController =
            ArchRuleDefinition.noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("..repository..", "..controllers..");

    @ArchTest
    public static final ArchRule repositoryShouldDependOnDomain =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..repository..")
                    .and().haveSimpleNameEndingWith("JdbcRepository")
                    .should().dependOnClassesThat()
                    .resideInAPackage("..domain..");

    @ArchTest
    public static final ArchRule controllersShouldDependOnDomain =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..controllers..")
                    .and().areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                    .should().dependOnClassesThat()
                    .resideInAPackage("..domain..");

    @ArchTest
    public static final ArchRule controllersShouldNotDependOnRepositories =
            ArchRuleDefinition.noClasses()
                    .that().resideInAPackage("..controllers..")
                    .should().dependOnClassesThat()
                    .resideInAPackage("..repository..");

    public static class CustomLocationProvider implements LocationProvider {

        public CustomLocationProvider() {
        }

        @Override
        public Set<Location> get(Class<?> testClass) {
            return Collections.singleton(Location.of(Paths.get("../..")));
        }
    }
}
