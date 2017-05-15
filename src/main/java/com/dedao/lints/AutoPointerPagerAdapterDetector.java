package com.dedao.lints;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * user liushuo
 * date 2017/4/24
 */

public class AutoPointerPagerAdapterDetector extends Detector implements Detector.JavaPsiScanner {
    private static final String CLASS_PAGER_ADAPTER = "android.support.v4.view.PagerAdapter";

    private static final String CLASS_AUTOPOINTER_PAGER_ADAPTER = "com.luojilab.autopoint.view.AutoPointPagerAdapter";

    public static final Issue ISSUE_PAGER_ADAPTER = Issue.create(
            "ViewPagerAutoPoint",
            "the PagerAdapter do not support auto point",

            "the PagerAdapter do not support auto point",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointerPagerAdapterDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> applicableSuperClasses() {
        return Collections.singletonList(CLASS_PAGER_ADAPTER);
    }

    @Override
    public void checkClass(JavaContext context, PsiClass node) {
        super.checkClass(context, node);

        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator.isAbstract(node)) {
            return;
        }

        boolean supportAutoPoint = evaluator.extendsClass(node, CLASS_AUTOPOINTER_PAGER_ADAPTER, false);

        if (!supportAutoPoint) {
            context.report(ISSUE_PAGER_ADAPTER, node, context.getLocation(node),
                    String.format("unsupport auto point ViewPager,%s,contact shuoliu", node.toString()));
        }
    }

}
