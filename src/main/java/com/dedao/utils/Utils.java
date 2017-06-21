package com.dedao.utils;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.dedao.lints.AutoPointCustomViewGroupDetector;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liushuo on 2017/6/1.
 */

public class Utils {
    public static boolean stringIsEmpty(String str) {
        if (str == null) return true;
        if (str.isEmpty()) return true;
        if (str.trim().isEmpty()) return true;

        return false;
    }

    private static final String[] sSystemPrefix = new String[]{"java.", "android.", "javax.", "org."};
    private static final String[] sJSON = new String[]{"org.json.JSONObject"};

    private static boolean isSystemClass(@NonNull String className) {
        for (String prefix : sSystemPrefix) {
            if (className.startsWith(prefix)) return true;
        }

        return false;
    }

    private static boolean isJSONObject(@NonNull JavaContext context, @NonNull String className) {

        for (String name : sJSON) {
            if (className.equals(name)) return true;
        }

        return false;
    }

    /**
     * 用户自定义的bean
     *
     * @param cls
     * @return
     */
    public static boolean isCustomBean(@NonNull JavaContext context, @NonNull PsiClass cls) {
        //忽略系统类
        String className = cls.getQualifiedName();
        if (className == null) return false;
        if (isJSONObject(context, className)) return true;
        if (isSystemClass(className)) return false;


        PsiField[] fields = cls.getFields();

        List<PsiField> dataFields = new ArrayList<>();

        for (PsiField field : fields) {
            if (!isDataField(field)) continue;

            dataFields.add(field);
        }

        if (dataFields.size() == 0) return false;

        int accessFieldsCount = 0;
        for (PsiField field : dataFields) {
            boolean isBool = field.getType().equals(PsiType.BOOLEAN);

            String getPrefix = isBool ? "is" : "get";
            String setPrefix = "set";
            boolean has = hasGetAndSet(getPrefix, setPrefix, cls, field);

            accessFieldsCount = has ? accessFieldsCount + 1 : accessFieldsCount;
        }

        if (accessFieldsCount == 0) return false;

        //类中50%的成员变量可以通过get/set 方法
        //访问，则认为类是个bean，宁可错杀一千，不能漏过一个
        //错杀的建议使用lint.xml过滤掉指定文件的lint检查
        float accessFieldsPercent = accessFieldsCount * 1f / dataFields.size();

        return accessFieldsPercent >= 0.5f;
    }

    public static boolean isDataField(@NonNull PsiField field) {
        boolean isStatic = field.hasModifierProperty(PsiModifier.STATIC);
        boolean isFinal = field.hasModifierProperty(PsiModifier.FINAL);

        return !isFinal && !isStatic;
    }

    private static boolean hasGetAndSet(@NonNull String getPrefix, @NonNull String setPrefix, @NonNull PsiClass cls, @NonNull PsiField field) {
        boolean isPublic = field.hasModifierProperty(PsiModifier.PUBLIC);
        if (isPublic) return true;

        String fieldName = captureName(field.getName());
        String getMethodName = getPrefix + fieldName;
        String setMethodName = setPrefix + fieldName;
        PsiMethod[] gets = cls.findMethodsByName(getMethodName, true);
        PsiMethod[] sets = cls.findMethodsByName(setMethodName, true);

        boolean hasGet = gets.length > 0;
        boolean hasSet = sets.length > 0;

        return hasGet && hasSet;

    }

    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
