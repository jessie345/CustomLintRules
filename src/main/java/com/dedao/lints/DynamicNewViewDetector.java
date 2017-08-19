package com.dedao.lints;

/**
 * Created by liushuo on 2017/7/14.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiNewExpression;

import java.util.Collections;
import java.util.List;

public class DynamicNewViewDetector extends Detector implements Detector.JavaPsiScanner {
    public static final Issue ISSUE;

    private static final String IDENTIFIER_ANNOTATION = "com.liushuo.testviewid.UniqueIndentifier";
    private static final String CLASS_VIEW = "android.view.View";

    public DynamicNewViewDetector() {
    }

    public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
        return Collections.singletonList(PsiNewExpression.class);
    }


    public JavaElementVisitor createPsiVisitor(JavaContext context) {
        return new DynamicNewViewDetector.NewVisitor(context);
    }

    static {
        ISSUE = Issue.create(
                "DynamicCreateView",
                "detect dynamic created View,should check if it has a identifier...",

                "detect dynamic created View,should check if it has a identifier...",
                Category.CORRECTNESS,
                10,
                Severity.FATAL,
                new Implementation(
                        DynamicNewViewDetector.class,
                        Scope.JAVA_FILE_SCOPE));

    }

    private static class NewVisitor extends JavaElementVisitor {
        private final JavaContext mContext;

        public NewVisitor(JavaContext context) {
            this.mContext = context;
        }

        @Override
        public void visitNewExpression(PsiNewExpression expression) {
            super.visitNewExpression(expression);

            PsiMethod constructor = expression.resolveConstructor();
            if (constructor == null) return;

            JavaEvaluator evaluator = mContext.getEvaluator();
            PsiClass cls = constructor.getContainingClass();
            boolean isView = evaluator.extendsClass(cls, CLASS_VIEW, true);
            if (!isView) return;

            Location location = this.mContext.getLocation(expression);

            PsiElement psiElement = expression.getParent();

            //创建的变量没有赋值给本地变量，无法指定注解
            if (!(psiElement instanceof PsiLocalVariable)) {
                this.mContext.report(DynamicNewViewDetector.ISSUE, expression, location, "检测到动态创建view操作,new 操作的结果需要赋值给本地变量");
                return;
            }

            PsiLocalVariable localVariable = (PsiLocalVariable) psiElement;
            PsiModifierList modifierList = localVariable.getModifierList();
            if (modifierList == null) {
                this.mContext.report(DynamicNewViewDetector.ISSUE, expression, location, "检测到动态创建view操作，确认是否为view指定了唯一标识");
                return;
            }

            PsiAnnotation[] annotations = modifierList.getAnnotations();
            for (PsiAnnotation annotation : annotations) {
                String fullName = annotation.getQualifiedName();
                if (IDENTIFIER_ANNOTATION.equals(fullName)) {
                    return;
                }
            }

            this.mContext.report(DynamicNewViewDetector.ISSUE, expression, location, "检测到动态创建view操作，确认是否为view指定了唯一标识");
        }

    }
}

