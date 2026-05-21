package com.ragapi.config;

import com.ragapi.service.FormTemplateParserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormTemplateInitializer
        implements CommandLineRunner {

    private final FormTemplateParserService
            formTemplateParserService;

    @Override
    public void run(String... args) {

        log.info(
                "Starting form template initialization..."
        );

        try {

            /**
             * Load templates from resources/forms
             */
            formTemplateParserService
                    .loadTemplatesFromResources();

            log.info(
                    "Form templates initialized successfully"
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to initialize templates",
                    ex
            );
        }
    }
}

