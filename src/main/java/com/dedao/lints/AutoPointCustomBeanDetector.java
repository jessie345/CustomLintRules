package com.dedao.lints;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.dedao.utils.Utils;
import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liushuo on 2017/6/15.
 */

public class AutoPointCustomBeanDetector extends Detector implements Detector.JavaPsiScanner {

    private static final String CLASS_CUSTOM_BEAN = "java.lang.Object";


    public static final Issue ISSUE_CUSTOM_BEAN = Issue.create(
            "CustomBean",
            "CustomBean",
            "CustomBean",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointCustomBeanDetector.class,
                    Scope.JAVA_FILE_SCOPE));


    @Override
    public List<String> applicableSuperClasses() {
        return Lists.newArrayList(CLASS_CUSTOM_BEAN);
    }

    @Override
    public void checkClass(JavaContext context, PsiClass node) {
        super.checkClass(context, node);

        boolean isBean = Utils.isCustomBean(context, node);

        if (isBean) {
            context.report(ISSUE_CUSTOM_BEAN, Location.create(context.file),
                    "custom bean:" + node.getName());
        }

    }
}
