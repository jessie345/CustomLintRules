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

public class AutoPointDialogDetector extends Detector implements Detector.JavaPsiScanner {
    private static final String CLASS_ALERT_DIALOG = "android.app.AlertDialog";
    private static final String CLASS_ALERT_V7_DIALOG = "android.support.v7.app.AlertDialog";
    private static final String CLASS_DIALOG = "android.app.Dialog";

    private static final String CLASS_AUTO_POINT_DIALOG = "com.luojilab.netsupport.autopoint.widget.dialog.DDDialog";

    public static final Issue ISSUE_DIALOG = Issue.create(
            "DialogAutoPoint",
            "Dialog subclass must extends DDDialog",

            "Dialog subclass must extends DDDialog,so it can support auto point",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointDialogDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> applicableSuperClasses() {
        return Collections.singletonList(CLASS_DIALOG);
    }

    @Override
    public void checkClass(JavaContext context, PsiClass node) {
        super.checkClass(context, node);

        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator.isAbstract(node)) {
            return;
        }

        boolean autopoint_dialog = evaluator.extendsClass(node, CLASS_AUTO_POINT_DIALOG, false);
        boolean alert_dialog = evaluator.extendsClass(node, CLASS_ALERT_DIALOG, false);
        boolean alert_v7_dialog = evaluator.extendsClass(node, CLASS_ALERT_V7_DIALOG, false);

        if (!alert_v7_dialog && !alert_dialog && !autopoint_dialog) {
            context.report(ISSUE_DIALOG, node, context.getLocation(node),
                    String.format("%s do not support auto point,should extends DDDialog", node.toString()));
        }
    }

}
