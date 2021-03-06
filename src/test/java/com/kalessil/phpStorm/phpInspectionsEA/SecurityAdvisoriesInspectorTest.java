package com.kalessil.phpStorm.phpInspectionsEA;

import com.intellij.codeInsight.intention.IntentionAction;
import com.kalessil.phpStorm.phpInspectionsEA.inspectors.security.SecurityAdvisoriesInspector;

public final class SecurityAdvisoriesInspectorTest extends PhpCodeInsightFixtureTestCase {
    // Code-coverage.
    public void testNoComposerJson() {
        myFixture.enableInspections(new SecurityAdvisoriesInspector());
        myFixture.configureByFile("fixtures/securityAdvisories/any.json");
        myFixture.testHighlighting(true, false, true);
    }

    public void testHasAdvisory() {
        myFixture.enableInspections(new SecurityAdvisoriesInspector());
        myFixture.configureByFile("fixtures/securityAdvisories/hasAdvisories/composer.json");
        myFixture.testHighlighting(true, false, true);
    }

    public void testNotNeededAdvisory() {
        myFixture.enableInspections(new SecurityAdvisoriesInspector());
        myFixture.configureByFile("fixtures/securityAdvisories/needsNoAdvisories/composer.json");
        myFixture.testHighlighting(true, false, true);
    }

    public void testMissingAdvisory() {
        final SecurityAdvisoriesInspector inspector = new SecurityAdvisoriesInspector();
        inspector.REPORT_MISPLACED_DEPENDENCIES     = true;
        inspector.REPORT_MISSING_ROAVE_ADVISORIES   = true;
        myFixture.enableInspections(inspector);
        myFixture.configureByFile("fixtures/securityAdvisories/needsAdvisories/composer.json");
        myFixture.testHighlighting(true, false, true);

        for (final IntentionAction fix : myFixture.getAllQuickFixes()) {
            myFixture.launchAction(fix);
        }
        myFixture.setTestDataPath(".");
        myFixture.checkResultByFile("fixtures/securityAdvisories/needsAdvisories/composer.fixed.json");
    }

    public void testLibraryType() {
        myFixture.enableInspections(new SecurityAdvisoriesInspector());
        myFixture.configureByFile("fixtures/securityAdvisories/libraryType/composer.json");
        myFixture.testHighlighting(true, false, true);
    }

    public void testNonMasterAdvisory() {
        myFixture.enableInspections(new SecurityAdvisoriesInspector());
        myFixture.configureByFile("fixtures/securityAdvisories/needsMasterAdvisories/composer.json");
        myFixture.testHighlighting(true, false, true);
    }

    public void testDevDependenciesInDevPackage() {
        final SecurityAdvisoriesInspector securityAdvisoriesInspector = new SecurityAdvisoriesInspector();
        securityAdvisoriesInspector.optionConfiguration.addAll(SecurityAdvisoriesInspector.optionConfigurationDefaults());
        securityAdvisoriesInspector.optionConfigurationMigrated = true;

        myFixture.enableInspections(securityAdvisoriesInspector);
        myFixture.configureByFile("fixtures/securityAdvisories/devDependenciesInDevPackage/composer.json");
        myFixture.testHighlighting(true, false, true);
    }

    public void testDevDependenciesInProdPackage() {
        final SecurityAdvisoriesInspector securityAdvisoriesInspector = new SecurityAdvisoriesInspector();
        securityAdvisoriesInspector.optionConfiguration.addAll(SecurityAdvisoriesInspector.optionConfigurationDefaults());
        securityAdvisoriesInspector.optionConfigurationMigrated = true;

        myFixture.enableInspections(securityAdvisoriesInspector);
        myFixture.configureByFile("fixtures/securityAdvisories/devDependenciesInProdPackage/composer.json");
        myFixture.testHighlighting(true, false, true);
    }

    public void testNoRequire() {
        myFixture.enableInspections(new SecurityAdvisoriesInspector());
        myFixture.configureByFile("fixtures/securityAdvisories/noRequire/composer.json");
        myFixture.testHighlighting(true, false, true);
    }

    public void testInvalidRequireList() {
        myFixture.enableInspections(new SecurityAdvisoriesInspector());
        myFixture.configureByFile("fixtures/securityAdvisories/invalidRequireList/composer.json");
        myFixture.testHighlighting(true, false, true);
    }

    public void testInvalidRequirePropertyValue() {
        myFixture.enableInspections(new SecurityAdvisoriesInspector());
        myFixture.configureByFile("fixtures/securityAdvisories/invalidRequirePropertyValue/composer.json");
        myFixture.testHighlighting(true, false, true);
    }
}
