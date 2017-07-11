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
 * 检测所有ListView 子类，如果子类未继承DDListView,提示错误
 */

public class AutoPointPopWindowDetector extends Detector implements Detector.JavaPsiScanner {
    private static final String CLASS_POP_WINDOW = "android.widget.PopupWindow";

    private static final String CLASS_AUTO_POINT_POP_WINDOW = "com.luojilab.netsupport.autopoint.widget.popup.DDPopupWindow";

    public static final Issue ISSUE_POP_WINDOW = Issue.create(
            "AlertDialogAutoPoint",
            "PopupWindow subclass must extends DDPopupWindow",

            "PopupWindow subclass must extends DDPopupWindow,so it can support auto point",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointPopWindowDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> applicableSuperClasses() {
        return Collections.singletonList(CLASS_POP_WINDOW);
    }

    @Override
    public void checkClass(JavaContext context, PsiClass node) {
        super.checkClass(context, node);

        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator.isAbstract(node)) {
            return;
        }

        boolean supportAutoPoint = evaluator.extendsClass(node, CLASS_AUTO_POINT_POP_WINDOW, false);

        if (!supportAutoPoint) {
            context.report(ISSUE_POP_WINDOW, node, context.getLocation(node),
                    String.format("%s do not support auto point,should extends DDPopupWindow", node.toString()));
        }
    }

}
