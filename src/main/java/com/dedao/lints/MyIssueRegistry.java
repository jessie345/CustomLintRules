package com.dedao.lints;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyIssueRegistry extends IssueRegistry {
    private static final boolean DISABLE_LINT = false;

    public MyIssueRegistry() {
    }

    @Override
    public List<Issue> getIssues() {
        if (DISABLE_LINT) return Collections.emptyList();

        return Arrays.asList(
                /*检测layout.xml中是否有id重复*/
                AutoPointIdInFileDetector.DEDAO_WITHIN_LAYOUT,

                /*检测layout.xml 对应的ViewTree中，id是否有重复(排除include 形同的item layout)
                * 带来的id重复*/
                AutoPointIdInFileDetector.DEDAO_CROSS_LAYOUT,

                /*如果id的文本表示和布局文件的名字相同，则认为该id无效*/
                AutoPointIdInFileDetector.DEDAO_INVALID_ID,

                /*检测那些没有继承DDXXXXAdapter的子类*/
                AutoPointRecyclerAdapterDetector.ISSUE_RECYCLER_ADAPTER,
                AutoPointPagerAdapterDetector.ISSUE_PAGER_ADAPTER,

                /*检测每个module下是否存在file.properties文件，并且
                * 是否存在没有在文件中注册的自定义View*/
                AutoPointRegisteredCustomViewDetector.ISSUE_NO_FILE,
                AutoPointRegisteredCustomViewDetector.ISSUE_UN_REGISTER_VIEW,

                /*检测系统中对LayoutInflater.from 等相关方法的调用，
                * 提示替换成LayoutInflaterWrapper对应的方法*/
                AutoPointLayoutInflaterDetector.ISSUE_LAYOUTINFLATER,
                AutoPointLayoutInflaterDetector.ISSUE_VIEW_INFLATE,

                /*检测ListView/GridView/ExpandableListView子类是否继承DDListView(支持自动打点)*/
                AutoPointListViewDetector.ISSUE_LIST_VIEW,
                AutoPointGridViewDetector.ISSUE_GRID_VIEW,
                AutoPointExpandableListViewDetector.ISSUE_EXPANDABLE_LIST_VIEW,

                /*检测哪些设置了OnClick 属性，但是没有指定id的view*/
                AutoPointLayoutOnclickDetector.ISSUE_MISSING_ID,

                /*检测不支持自动打点的Dialog,PopUpWindow*/
                AutoPointAlertDialogDetector.ISSUE_ALERT_DIALOG,
                AutoPointV7AlertDialogDetector.ISSUE_ALERT_DIALOG,
                AutoPointDialogDetector.ISSUE_DIALOG,
                AutoPointPopWindowDetector.ISSUE_POP_WINDOW,
//
//                /*检测自定义ViewGroup是否实现指定接口*/
                AutoPointCustomViewGroupDetector.ISSUE_VIEW_GROUP,

                /*检测动态创建的view*/
                DynamicNewViewDetector.ISSUE

        );
    }

}
