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

public class AutoPointAlertDialogDetector extends Detector implements Detector.JavaPsiScanner {
    private static final String CLASS_ALERT_DIALOG = "android.app.AlertDialog";

    private static final String CLASS_AUTO_POINT_ALERT_DIALOG = "com.luojilab.netsupport.autopoint.widget.dialog.DDAlertDialog";

    public static final Issue ISSUE_ALERT_DIALOG = Issue.create(
            "AlertDialogAutoPoint",
            "AlertDialog subclass must extends DDAlertDialog",

            "AlertDialog subclass must extends DDAlertDialog,so it can support auto point",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointAlertDialogDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> applicableSuperClasses() {
        return Collections.singletonList(CLASS_ALERT_DIALOG);
    }

    @Override
    public void checkClass(JavaContext context, PsiClass node) {
        super.checkClass(context, node);

        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator.isAbstract(node)) {
            return;
        }

        boolean supportAutoPoint = evaluator.extendsClass(node, CLASS_AUTO_POINT_ALERT_DIALOG, false);

        if (!supportAutoPoint) {
            context.report(ISSUE_ALERT_DIALOG, node, context.getLocation(node),
                    String.format("%s do not support auto point,should extends DDAlertDialog", node.toString()));
        }
    }

}
