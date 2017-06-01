package com.dedao.lints;

import com.android.SdkConstants;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import com.dedao.utils.Utils;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

public class AutoPointLayoutOnclickDetector extends LayoutDetector {

    public static final Issue ISSUE_MISSING_ID = Issue.create(
            "OnClickMissingId",
            "the view with onClick attribute set should has id attribute",

            "the view with onClick attribute set should has id attribute",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointLayoutOnclickDetector.class,
                    Scope.RESOURCE_FILE_SCOPE));

    @Override
    public Collection<String> getApplicableAttributes() {
        return Collections.singletonList(SdkConstants.ATTR_ON_CLICK);
    }

    @Override
    public void visitAttribute(XmlContext context, Attr attribute) {
        super.visitAttribute(context, attribute);

        String value = attribute.getValue();
        if (Utils.stringIsEmpty(value)) return;

        //onClick 属性被赋值
        Element element = attribute.getOwnerElement();
        boolean has = element.hasAttributeNS(SdkConstants.ANDROID_URI, SdkConstants.ATTR_ID);
        if (!has) {

            Location location = context.getLocation(attribute);
            context.report(ISSUE_MISSING_ID, location, "the view with onClick attribute set should has id attribute");
        }
    }
}
