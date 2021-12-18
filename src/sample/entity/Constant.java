package sample.entity;

import javafx.scene.image.Image;

import java.io.File;

public class Constant {
    /**
     * 提交单个题目url
     */
    public static final String QUESTION_URL = "http://jiaoxue.cugbonline.cn/meol/common/question/test/student/stu_qtest_question.jsp?testId=";
    /**
     * 提交试卷url
     */
    public static final String SUBMIT_URL = "http://jiaoxue.cugbonline.cn/meol/common/question/test/student/stu_qtest_submit.jsp?testId=";
    /**
     * 获取试卷url
     */
    public static final String TEST_MAIN_URL = "http://jiaoxue.cugbonline.cn/meol/common/question/test/student/stu_qtest_main.jsp?testId=";
    /**
     * 获取考试结果url
     */
    public static final String VIEW_RES_URL = "http://jiaoxue.cugbonline.cn/meol/common/question/test/student/";
    /**
     * 获取考试结果主界面url
     */
    public static final String STU_MORE_RESULT = "http://jiaoxue.cugbonline.cn/meol/common/question/test/student/stu_qtest_more_result.jsp?testId=";
    /**
     * 资源列表url
     */
    public static final String COURSE_LIST_URL = "http://jiaoxue.cugbonline.cn/meol/common/question/test/student/list.jsp?tagbug=client&status=1&strStyle=new06&cateId=";
    /**
     * 图标
     */
    public static final Image DEFAULT_IMAGE = new Image("file:///" + new File(Constant.class.getResource("../res/81f82bff0ca7600a.png").getFile()).getAbsolutePath());


}
