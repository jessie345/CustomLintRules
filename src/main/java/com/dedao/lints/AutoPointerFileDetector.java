package com.dedao.lints;

import com.android.SdkConstants;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiClass;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * user liushuo
 * date 2017/4/27
 */

public class AutoPointerFileDetector extends Detector implements Detector.JavaPsiScanner {
    private static final String CLASS_RECYCLERVIEW = "android.support.v7.widget.RecyclerView";
    private static final String CLASS_LISTVIEW = "android.widget.ListView";
    private static final String CLASS_GRIDVIEW = "android.widget.GridView";
    private static final String CLASS_VIEWPAGER = "android.support.v4.view.ViewPager";

    public static final Issue ISSUE_NO_FILE = Issue.create(
            "NoFile",
            "no file",

            "no file",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointerFileDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    public static final Issue ISSUE_UN_REGISTER_VIEW = Issue.create(
            "UnRegisterView",
            "unregister view",

            "unregister view",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(
                    AutoPointerFileDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    private Set<String> mSet = new HashSet<>();

    @Override
    public List<String> applicableSuperClasses() {
        return Arrays.asList(CLASS_RECYCLERVIEW
                , CLASS_LISTVIEW
                , CLASS_GRIDVIEW
                , CLASS_VIEWPAGER);
    }

    @Override
    public void checkClass(JavaContext context, PsiClass node) {
        super.checkClass(context, node);

        String name = node.getName();
        mSet.add(name);

    }

    @Override
    public void afterCheckProject(Context context) {
        super.afterCheckProject(context);

        File dir = context.getMainProject().getDir();
        File parentDir = dir.getParentFile();
        boolean gradleExists = new File(parentDir, SdkConstants.FN_BUILD_GRADLE).exists();
        if (!gradleExists) {
            return;
        }

        File file = new File(parentDir, "file.properties");
        if (!file.exists()) {
            context.report(ISSUE_NO_FILE, Location.create(context.file),
                    "no file in project reference dir '" + parentDir.getAbsolutePath() + "'");
            return;
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fis);

            Iterator<String> itr = mSet.iterator();
            while (itr.hasNext()) {
                String key = itr.next();

                String value = properties.getProperty(key);
                if (value == null || value.isEmpty()) {
                    context.report(ISSUE_UN_REGISTER_VIEW, Location.create(context.file),
                            "unregister view(" + key + ") at project " + context.getProject().getName());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
