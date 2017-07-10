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
 * 检测所有ExpandableListView 子类，如果子类未继承DDExpandableListView,提示错误
 */

public class AutoPointExpandableListViewDetector extends Detector implements Detector.JavaPsiScanner {
    private static final String CLASS_EXPANDABLE_LIST_VIEW = "android.widget.ExpandableListView";

    private static final String CLASS_AUTO_POINT_EXPANDABLE_LIST_VIEW = "com.luojilab.ddlibrary.widget.expandablelistview.DDExpandableListView";

    public static final Issue ISSUE_EXPANDABLE_LIST_VIEW = Issue.create(
            "ExpandableListViewAutoPoint",
            "ExpandableListView subclass must extends DDExpandableListView",

            "ExpandableListView subclass must extends DDExpandableListView,so it can support auto point",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointExpandableListViewDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> applicableSuperClasses() {
        return Collections.singletonList(CLASS_EXPANDABLE_LIST_VIEW);
    }

    @Override
    public void checkClass(JavaContext context, PsiClass node) {
        super.checkClass(context, node);

        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator.isAbstract(node)) {
            return;
        }

        boolean supportAutoPoint = evaluator.extendsClass(node, CLASS_AUTO_POINT_EXPANDABLE_LIST_VIEW, false);

        if (!supportAutoPoint) {
            context.report(ISSUE_EXPANDABLE_LIST_VIEW, node, context.getLocation(node),
                    String.format("%s do not support auto point,should extends DDExpandableListView", node.toString()));
        }
    }

}
