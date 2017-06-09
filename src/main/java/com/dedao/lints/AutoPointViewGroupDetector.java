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
 * 检测所有ViewGroup子类，如果子类未继承DataAdapter,提示错误
 */

public class AutoPointViewGroupDetector extends Detector implements Detector.JavaPsiScanner {
    private static final String CLASS_VIEW_GROUP = "android.view.ViewGroup";

    private static final String CLASS_AUTO_POINT_VIEW_GROUP = "com.luojilab.autopoint.view.DataAdapter";

    public static final Issue ISSUE_VIEW_GROUP = Issue.create(
            "ViewGroupAutoPoint",
            "ViewGroup subclass must extends DataAdapter",

            "ViewGroup subclass must extends DataAdapter,so it can support auto point",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointViewGroupDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> applicableSuperClasses() {
        return Collections.singletonList(CLASS_VIEW_GROUP);
    }

    @Override
    public void checkClass(JavaContext context, PsiClass node) {
        super.checkClass(context, node);

        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator.isAbstract(node)) {
            return;
        }

        boolean autopoint_viewgroup = evaluator.extendsClass(node, CLASS_AUTO_POINT_VIEW_GROUP, false);

        if (!autopoint_viewgroup) {
            context.report(ISSUE_VIEW_GROUP, node, context.getLocation(node),
                    String.format("%s do not support auto point,should extends DataAdapter", node.toString()));
        }
    }

}
