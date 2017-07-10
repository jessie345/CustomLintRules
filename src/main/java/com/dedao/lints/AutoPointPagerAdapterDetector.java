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

import java.util.Collections;
import java.util.List;

/**
 * user liushuo
 * date 2017/4/24
 */

public class AutoPointPagerAdapterDetector extends Detector implements Detector.JavaPsiScanner {
    private static final String CLASS_PAGER_ADAPTER = "android.support.v4.view.PagerAdapter";
    private static final String CLASS_FRAGMENT_PAGER_ADAPTER = "android.support.v4.app.FragmentPagerAdapter";
    private static final String CLASS_FRAGMENT_STATE_PAGER_ADAPTER = "android.support.v4.app.FragmentStatePagerAdapter";

    private static final String CLASS_AUTOPOINT_PAGER_ADAPTER = "com.luojilab.ddlibrary.widget.adapter.DDPagerAdapter";

    public static final Issue ISSUE_PAGER_ADAPTER = Issue.create(
            "ViewPagerAutoPoint",
            "the PagerAdapter do not support auto point",

            "the PagerAdapter do not support auto point",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointPagerAdapterDetector.class,
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

        boolean isFragmentPagerAdapter = evaluator.extendsClass(node, CLASS_FRAGMENT_PAGER_ADAPTER, false);
        if (isFragmentPagerAdapter) return;

        boolean isFragmentStatePagerAdapter = evaluator.extendsClass(node, CLASS_FRAGMENT_STATE_PAGER_ADAPTER, false);
        if (isFragmentStatePagerAdapter) return;

        boolean supportAutoPoint = evaluator.extendsClass(node, CLASS_AUTOPOINT_PAGER_ADAPTER, false);

        if (!supportAutoPoint) {
            context.report(ISSUE_PAGER_ADAPTER, node, context.getLocation(node),
                    "Pager Adapter 必须实现DDPagerAdapter,否则不支持自动打点 class:" + node.getName());
        }
    }

}
