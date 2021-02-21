package sample.controller;

import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import sample.Main;
import sample.entity.Course;
import sample.util.HttpUtil;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import static sample.entity.Constant.COURSE_LIST_URL;

public class MenuController extends BaseController {

    /**
     * 操作系统原理
     */
    public void operatingSystemPrinciple() throws IOException {
        getCourseList("10094", "操作系统原理");
    }

    /**
     * 微机接口技术
     */
    public void microcomputerInterfaceTechnology() throws IOException {
        getCourseList("11567", "微机接口技术");
    }

    /**
     * Java语言程序设计
     */
    public void javaLanguageProgramming() throws IOException {
        getCourseList("11571", "Java语言程序设计");
    }

    /**
     * 数据库系统原理
     */
    public void principlesOfDatabaseSystem() throws IOException {
        getCourseList("11608", "数据库系统原理");
    }

    private void getCourseList(String columnId, String title) throws IOException {
        Document document = Jsoup.parse(EntityUtils.toString(HttpUtil.doGet(COURSE_LIST_URL + columnId, null, null)));
        String className = "align_c classicLook";
        int index = 0;
        Elements elements;
        Deque<Course> deque = new LinkedList<>();
        int timeLength = "yyyy-MM-dd".length();
        while ((elements = document.getElementsByClass(className + index++)) != null && index < 10) {
            if (elements.isEmpty()) continue;
            Course course = new Course();
            course.setTitle(elements.get(0).text());
            course.setStartTime(elements.get(1).text().substring(0, timeLength));
            course.setEndTime(elements.get(2).text().substring(0, timeLength));
            course.setCount(elements.get(3).text());
            course.setDuration(elements.get(4).text());
            String testString = elements.get(5).select("a[href]").attr("id").replace("paperstart", "");
            if (StringUtils.isNotBlank(testString)) course.setTestId(testString);
            deque.addFirst(course);
        }
        CourseController courseController = Main.initStage(new Stage(), getClass().getResource("../res/course.fxml"));
        courseController.setCourseList(deque, title);
        courseController.show();
    }


    @Override
    public void windowCloseRequest() {
        exit();
    }

    @Override
    public void min() {
        getStage().setIconified(true);
    }

    @Override
    public void exit() {
        getStage().close();
        System.exit(0);
    }
}
