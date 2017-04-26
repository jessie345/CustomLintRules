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

public class AutoPointerAdapterDetector extends Detector implements Detector.JavaPsiScanner {
    private static final String CLASS_RECYCLERVIEW_ADAPTER = "android.support.v7.widget.RecyclerView.Adapter";

    private static final String CLASS_AUTOPOINTER_RECYCLERVIEW_ADAPTER = "com.liushuo.testviewid.AutoPointRecyclerAdapter";

    public static final Issue ISSUE_RECYCLER_ADAPTER = Issue.create(
            "RecyclerViewAutoPoint",
            "the RecyclerView.Adapter do not support auto point",

            "the RecyclerView.Adapter do not support auto point",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointerAdapterDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> applicableSuperClasses() {
        return Collections.singletonList(CLASS_RECYCLERVIEW_ADAPTER);
    }

    @Override
    public void checkClass(JavaContext context, PsiClass node) {
        super.checkClass(context, node);

        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator.isAbstract(node)) {
            return;
        }

        boolean supportAutoPoint = evaluator.extendsClass(node, CLASS_AUTOPOINTER_RECYCLERVIEW_ADAPTER, false);

        if (!supportAutoPoint) {
            context.report(ISSUE_RECYCLER_ADAPTER, node, context.getLocation(node),
                    "unsupport auto point recyclerview,contact shuoliu");
        }
    }

}
