.DEFAULT_GOAL := help

GRADLE := ./gradlew

.PHONY: help setup check test coverage format format-check build clean client server data check-generated gametest gametest-coverage stop

help: ## Show the available commands
	@grep -E '^[a-z-]+:.*## ' $(MAKEFILE_LIST) | awk -F ':.*## ' '{printf "  %-18s %s\n", $$1, $$2}'

setup: ## Install the development tools and enable the git hooks
	scripts/development/setup-dev.sh

check: ## Run every gate: build, tests, architecture, coverage, formatting and static analysis
	$(GRADLE) check

test: ## Run the unit and architecture tests
	$(GRADLE) test

coverage: ## Check the domain coverage and show where the report is
	$(GRADLE) jacocoTestCoverageVerification
	@echo "Report: build/reports/jacoco/test/html/index.html"

format: ## Format the Java code
	$(GRADLE) spotlessApply

format-check: ## Check the Java formatting without changing files
	$(GRADLE) spotlessCheck

build: ## Run every gate and generate the jar in build/libs
	$(GRADLE) build

clean: ## Remove the build output
	$(GRADLE) clean

client: ## Open the game with the mod
	$(GRADLE) runClient

server: ## Start a dedicated server with the mod
	$(GRADLE) runServer

data: ## Run the data generation
	$(GRADLE) runData

check-generated: ## Fail when the data generation changes src/generated
	scripts/development/check-generated.sh

gametest: ## Run the GameTests on a headless server and confirm that they passed
	scripts/development/run-gametests.sh

gametest-coverage: ## Run the GameTests and report what they cover outside the domain, without failing on it
	$(GRADLE) -PgameTestCoverage gameTestCoverageReport
	@echo "Report: build/reports/jacoco/gametest/index.html"

stop: ## Stop the Gradle daemons
	$(GRADLE) --stop
