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

public class AutoPointCustomViewGroupDetector extends Detector implements Detector.JavaPsiScanner {

    private static final String CLASS_VIEW_GROUP = "android.view.ViewGroup";

    private static final String CLASS_DATA_ADAPTER = "com.luojilab.netsupport.autopoint.view.DataAdapter";

    public static final Issue ISSUE_VIEW_GROUP = Issue.create(
            "ViewGroupAutoPoint",
            "Custom ViewGroup that need data must extend DataAdapter",

            "Custom ViewGroup that need data must extend DataAdapter",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointCustomViewGroupDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    private List<PsiMethod> mMethods = new ArrayList<>();


    @Override
    public List<String> applicableSuperClasses() {
        return Lists.newArrayList(CLASS_VIEW_GROUP);
    }

    @Override
    public void checkClass(JavaContext context, PsiClass node) {
        super.checkClass(context, node);
        PsiMethod[] methods = node.getAllMethods();

        mMethods.clear();

        for (PsiMethod method : methods) {
            visitMethod(context, method);
        }

        afterCheckClass(context);

    }

    public void visitMethod(JavaContext context, PsiMethod method) {

        boolean isPublicMethod = method.hasModifierProperty(PsiModifier.PUBLIC);
        if (!isPublicMethod) return;

        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator == null) return;

        PsiParameterList parameterList = method.getParameterList();
        PsiParameter[] psiParameters = parameterList.getParameters();

        boolean hasBeanParameter = false;
        for (PsiParameter p : psiParameters) {
            PsiType type = p.getType();

            if (!(type instanceof PsiClassType)) continue;

            PsiClassType classType = (PsiClassType) type;
            PsiClass psiCls = classType.resolve();
            if (psiCls == null) continue;

            hasBeanParameter = Utils.isCustomBean(context, psiCls);
            if (hasBeanParameter) break;
        }

        if (hasBeanParameter) {
            mMethods.add(method);
        }
    }

    public void afterCheckClass(Context context) {

        if (mMethods.size() == 0) return;

        JavaContext javaContext = (JavaContext) context;
        JavaEvaluator evaluator = javaContext.getEvaluator();
        if (evaluator == null) return;

        StringBuilder sb = new StringBuilder();

        boolean methodInDataAdapter = false;
        for (PsiMethod method : mMethods) {
            PsiClass pc = method.getContainingClass();
            boolean isSubclass = evaluator.implementsInterface(pc, CLASS_DATA_ADAPTER, false);

            methodInDataAdapter = methodInDataAdapter || isSubclass;

            if (!isSubclass) {
                sb.append(method.getName())
                        .append(",");
            }
        }

        if (!methodInDataAdapter) {
            context.report(ISSUE_VIEW_GROUP, Location.create(context.file), "the Custom ViewGroup with data bind should extend DataAdapter,method names:" + sb.toString());
        }
    }
}
