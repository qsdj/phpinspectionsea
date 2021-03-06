package com.kalessil.phpStorm.phpInspectionsEA.codeStyle;

import com.intellij.codeInsight.intention.IntentionAction;
import com.kalessil.phpStorm.phpInspectionsEA.PhpCodeInsightFixtureTestCase;
import com.kalessil.phpStorm.phpInspectionsEA.inspectors.codeStyle.UnnecessaryFinalModifierInspector;

final public class UnnecessaryFinalModifierInspectorTest extends PhpCodeInsightFixtureTestCase {
    public void testIfFindsAllPatterns() {
        myFixture.enableInspections(new UnnecessaryFinalModifierInspector());
        myFixture.configureByFile("fixtures/codeStyle/unnecessary-final-modifier.php");
        myFixture.testHighlighting(true, false, true);

        for (final IntentionAction fix : myFixture.getAllQuickFixes()) {
            myFixture.launchAction(fix);
        }
        myFixture.setTestDataPath(".");
        myFixture.checkResultByFile("fixtures/codeStyle/unnecessary-final-modifier.fixed.php");
    }
}
