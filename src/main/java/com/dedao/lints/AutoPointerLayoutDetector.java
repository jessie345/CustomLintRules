package com.dedao.lints;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Collection;

@Deprecated
public class AutoPointerLayoutDetector extends LayoutDetector {

    public static final Issue ISSUE_INCLUDE = Issue.create(
            "IncludeMissingId",
            "the include tag must have id attribute",

            "the include tag must have id attribute,or more views may have same id,"
                    + "it will break auto point logic!Contact shuoLiu if have questions.",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointerLayoutDetector.class,
                    Scope.RESOURCE_FILE_SCOPE));

    public static final Issue ISSUE_MERGE = Issue.create(
            "MergeUse",
            "do not support merge tag",

            "do not support merge tag",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointerLayoutDetector.class,
                    Scope.RESOURCE_FILE_SCOPE));

    public static final Issue ISSUE_VIEWSTUB = Issue.create(
            "ViewStubUse",
            "do not support ViewStub tag",

            "do not support ViewStub tag",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointerLayoutDetector.class,
                    Scope.RESOURCE_FILE_SCOPE));

    @NonNull
    @Override
    public Speed getSpeed() {
        return Speed.FAST;
    }

    @Override
    public Collection<String> getApplicableElements() {
        return Arrays.asList(
                SdkConstants.VIEW_INCLUDE,
                SdkConstants.VIEW_MERGE,
                SdkConstants.VIEW_STUB);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        String tagName = element.getTagName();
        switch (tagName) {
//            case SdkConstants.VIEW_INCLUDE:
//                handleVisitIncludeTag(context, element);
//                break;
            case SdkConstants.VIEW_MERGE:
                handleVisitMergeTag(context, element);
                break;
            case SdkConstants.VIEW_STUB:
                handleVisitViewStubTag(context, element);
                break;
        }
    }

    private void handleVisitIncludeTag(XmlContext context, Element element) {
        boolean hasAttr = element.hasAttributeNS(SdkConstants.ANDROID_URI,
                SdkConstants.ATTR_ID);

        if (!hasAttr) {
            context.report(ISSUE_INCLUDE, element, context.getLocation(element),
                    "Missing required attribute 'id'");
        }
    }

    private void handleVisitMergeTag(XmlContext context, Element element) {
        context.report(ISSUE_MERGE, element, context.getLocation(element),
                "unsupport tag 'merge',contact shuoliu");
    }

    private void handleVisitViewStubTag(XmlContext context, Element element) {
        context.report(ISSUE_VIEWSTUB, element, context.getLocation(element),
                "unsupport tag 'ViewStub',contact shuoliu");
    }
}
