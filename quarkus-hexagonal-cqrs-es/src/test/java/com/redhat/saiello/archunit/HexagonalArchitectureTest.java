package com.redhat.saiello.archunit;

import com.redhat.saiello.es.AggregateRoot;
import com.redhat.saiello.es.EventPayload;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.apache.commons.lang3.arch.Processor;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;


@AnalyzeClasses(packages = {
        "com.redhat.saiello.bank.account"
})
public class HexagonalArchitectureTest {


    @ArchTest
    static final ArchRule onion_architecture_is_respected = onionArchitecture()
            .adapter("rest", "..adapter.rest")
            .adapter("messaging", "..adapter.messaging")
            .domainModels("..domain.model..")
            .domainServices("..domain.model..")
            .applicationServices("..application..");

    @ArchTest
    static final ArchRule rest_resources_should_reside_in_adapter_rest = classes().that()
                .resideInAPackage("..adapter.rest")
                .should().haveSimpleNameEndingWith("RestResource");

    @ArchTest
    static final ArchRule commands_should_be_record = classes().that()
            .resideInAPackage("..domain.model.commands").should().beRecords();

    @ArchTest
    static final ArchRule events_should_implement_event_payload = classes().that()
            .resideInAPackage("..domain.model.events").should().implement(EventPayload.class);


}
