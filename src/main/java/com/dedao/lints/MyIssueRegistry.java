package com.dedao.lints;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;

public class MyIssueRegistry extends IssueRegistry {
    public MyIssueRegistry() {
    }

    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(
                /*检测layout.xml中是否有id重复*/
                DuplicateIdInFileDetector.DEDAO_WITHIN_LAYOUT,

                /*检测layout.xml 对应的ViewTree中，id是否有重复(排除include 形同的item layout)
                * 带来的id重复*/
                DuplicateIdInFileDetector.DEDAO_CROSS_LAYOUT,

                /*检测那些没有继承AutoPointRecyclerAdapter的子类*/
                AutoPointerAdapterDetector.ISSUE_RECYCLER_ADAPTER,

                /*检测每个module下是否存在file.properties文件，并且
                * 是否存在没有在文件中注册的自定义View*/
                AutoPointerFileDetector.ISSUE_NO_FILE,
                AutoPointerFileDetector.ISSUE_UN_REGISTER_VIEW,

                /*检测系统中对LayoutInflater.from 等相关方法的调用，
                * 提示替换成LayoutInflaterWrapper对应的方法*/
                AutoPointerLayoutInflaterDetector.ISSUE_LAYOUTINFLATER
        );
    }

}
