package com.dedao.lints;
/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;

import java.util.Arrays;
import java.util.List;

import static com.android.SdkConstants.CLASS_LAYOUT_INFLATER;
import static com.android.SdkConstants.CLASS_VIEW;

public class AutoPointLayoutInflaterDetector extends Detector implements Detector.JavaPsiScanner {
    public static final Issue ISSUE_LAYOUTINFLATER = Issue.create(
            "AutoPointerLayoutInflater",
            "Using Wrong LayoutInflater Method",
            "Using Wrong LayoutInflater Method,you should use LayoutInflaterWrapper",
            Category.CORRECTNESS, 10, Severity.FATAL,
            new Implementation(
                    AutoPointLayoutInflaterDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    public static final Issue ISSUE_VIEW_INFLATE = Issue.create(
            "AutoPointerViewInflate",
            "Using Wrong View.inflate() Method",
            "Using Wrong View.inflate() Method,you should use LayoutInflaterWrapper.inflate()",
            Category.CORRECTNESS, 10, Severity.FATAL,
            new Implementation(
                    AutoPointLayoutInflaterDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    private static final String LAYOUTINFLATER_FROM = "from";
    private static final String VIEW_INFLATE = "inflate";

    public AutoPointLayoutInflaterDetector() {
    }

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList(
                VIEW_INFLATE,
                LAYOUTINFLATER_FROM);
    }

    @Override
    public void visitMethod(@NonNull JavaContext context, @Nullable JavaElementVisitor visitor,
                            @NonNull PsiMethodCallExpression node, @NonNull PsiMethod method) {

        boolean isLayoutInflaterCall = isLayoutInflaterCall(context, node, method);
        boolean isViewInflateCall = isInViewCall(context, node, method);

        String name = method.getName();
        boolean fromMethod = LAYOUTINFLATER_FROM.equals(name);
        boolean viewInflateMethod = VIEW_INFLATE.equals(name);

        if (isLayoutInflaterCall && fromMethod) {

            context.report(ISSUE_LAYOUTINFLATER, node, context.getLocation(node),
                    "error use system LayoutInflater,should use LayoutInflaterWrapper.");
            return;
        }

        if (viewInflateMethod && isViewInflateCall) {

            context.report(ISSUE_VIEW_INFLATE, node, context.getLocation(node),
                    "error use View.inflate(),should use LayoutInflaterWrapper.inflate.");
            return;
        }

    }

    private static boolean isLayoutInflaterCall(@NonNull JavaContext context,
                                                @NonNull PsiMethodCallExpression node, @NonNull PsiMethod method) {
        JavaEvaluator evaluator = context.getEvaluator();
        return evaluator.isMemberInClass(method, CLASS_LAYOUT_INFLATER);
    }

    private static boolean isInViewCall(@NonNull JavaContext context,
                                        @NonNull PsiMethodCallExpression node, @NonNull PsiMethod method) {
        JavaEvaluator evaluator = context.getEvaluator();
        return evaluator.isMemberInClass(method, CLASS_VIEW);
    }
}

