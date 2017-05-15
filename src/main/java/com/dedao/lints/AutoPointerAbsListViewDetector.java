package com.dedao.lints;

import com.android.SdkConstants;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Collection;

public class AutoPointerAbsListViewDetector extends LayoutDetector {

    public static final Issue ISSUE_ABSLISTVIEW = Issue.create(
            "UnSupportAutoPointAbsList",
            "the AbsList do not support AutoPoint",

            "the AbsList do not support AutoPoint",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointerAbsListViewDetector.class,
                    Scope.RESOURCE_FILE_SCOPE));


    @Override
    public Collection<String> getApplicableElements() {
        return Arrays.asList(
                SdkConstants.LIST_VIEW,
                SdkConstants.GRID_VIEW);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        handleUnSupportTag(context, element);
    }

    private void handleUnSupportTag(XmlContext context, Element element) {
        context.report(ISSUE_ABSLISTVIEW, element, context.getLocation(element),
                "unsupport auto point AbsListView,'" + element.getTagName() + "'");
    }
}
