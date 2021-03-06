package com.kalessil.phpStorm.phpInspectionsEA.api.strings;

import com.intellij.codeInsight.intention.IntentionAction;
import com.kalessil.phpStorm.phpInspectionsEA.PhpCodeInsightFixtureTestCase;
import com.kalessil.phpStorm.phpInspectionsEA.inspectors.apiUsage.strings.SubStrShortHandUsageInspector;

final public class SubStrShortHandUsageInspectorTest extends PhpCodeInsightFixtureTestCase {
    public void testIfFindsAllPatterns() {
        myFixture.enableInspections(new SubStrShortHandUsageInspector());
        myFixture.configureByFile("fixtures/api/strings/substr-short-hand.php");
        myFixture.testHighlighting(true, false, true);

        for (final IntentionAction fix : myFixture.getAllQuickFixes()) {
            myFixture.launchAction(fix);
        }
        myFixture.setTestDataPath(".");
        myFixture.checkResultByFile("fixtures/api/strings/substr-short-hand.fixed.php");
    }
}