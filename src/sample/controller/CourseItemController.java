package sample.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sample.entity.Constant;
import sample.entity.Course;
import sample.util.AlertUtil;
import sample.util.HttpUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

public class CourseItemController {
    @FXML
    private Label title;
    @FXML
    private Label startTime;
    @FXML
    private Label endTime;
    private Course course;
    private String viewId;
    private String testId;
    private List<String> answerList;

    public void setCourse(Course course) throws IOException {
        title.setText(course.getTitle());
        startTime.setText(course.getStartTime());
        endTime.setText(course.getEndTime());
        this.course = course;
        genderViewId();
    }

    public void seeAnswer() {
        AlertController alertController = AlertUtil.getAlert(title.getText(), "答案正在加载中....");
        Thread t = new Thread(() -> {
            try {
                if (viewId == null) {
                    Platform.runLater(() -> {
                        alertController.getTitle().setText("错误");
                        alertController.getBody().setText("获取答案失败");
                    });
                    return;
                }
                Document document = Jsoup.parse(EntityUtils.toString(HttpUtil.doGet(Constant.VIEW_RES_URL + viewId, null, null)));
                Elements elements = document.getElementsByClass("Fimg");
                List<String> list = new ArrayList<>();
                for (Element element : elements) {
                    String text = element.text();
                    if (text.contains("[试题解析]")) continue;
                    String s = text.replace("[参考答案]", "");
                    if (StringUtils.isBlank(s)) {
                        s = element.getElementsByTag("input").val();
                    }
                    list.add(s);
                }
                answerList = list;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < answerList.size(); i++) {
                    stringBuilder.append(i + 1).append(answerList.get(i)).append("\n");
                }
                Platform.runLater(() -> {
                    alertController.getTitle().setText(title.getText());
                    alertController.getBody().setText(stringBuilder.toString());
                    alertController.getStage().setAlwaysOnTop(true);
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
    }

    public void automaticAnswer() {
        AlertController alertController = AlertUtil.getAlert(title.getText(), "正在准备开始考试");
        if (answerList == null || answerList.isEmpty()) {
            if (viewId != null) {
                alertController.getBody().appendText("请先尝试查看答案,生成答案完毕后再进行自动答题");
                return;
            }
            alertController.getBody().appendText("未检测到有答题存在开始自动进行首次答题(获取答案)");
            answerList = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                answerList.add("A");
            }
        }
        new Thread(() -> {
            try {
                TextArea body = alertController.getBody();
                HttpUtil.doGet(Constant.TEST_MAIN_URL + testId, null, null);
                Document document = Jsoup.parse(EntityUtils.toString(HttpUtil.doGet(Constant.TEST_MAIN_URL + testId, null, null)));
                Elements elements = document.getElementsByClass("left");
                if (elements.size() > 5)
                    body.appendText("\n试卷生成成功\n开始考试后请不要关闭窗口或者使用浏览器访问考试地址否则会发生访问频繁异常");
                else {
                    body.appendText("\n试卷生成失败");
                    return;
                }
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                int index = 0;
                for (Element element : elements) {
                    String tId = element.select("a[href]").attr("onclick");
                    if (StringUtils.isNotBlank(tId) && tId.contains("jumpto")) {
                        int topic = Integer.parseInt(tId.substring("jumpto)".length(), tId.length() - 1));
                        String answer = answerList.get(index++).trim();
                        Map<String, Object> requestData = new HashMap<>();
                        requestData.put("currentSubmitQuestionid", String.valueOf(topic));
                        requestData.put("actionType", "saveAnswer");
                        requestData.put("testId", testId);
                        requestData.put("eqId", String.valueOf(topic));
                        requestData.put("nextquestion", "named");
                        body.appendText("\n正在准备获取第" + index + "道题目");
                        //防止请求频繁
                        Thread.sleep(2000);
                        Document documentQue = Jsoup.parse(EntityUtils.toString(HttpUtil.doPost(Constant.QUESTION_URL + testId, header, requestData)));
                        Elements elementsQue = documentQue.getElementsByTag("label");
                        body.appendText("\n获取第" + index + "道题目完成");
                        body.appendText("\n正在比对" + index + "道题目答案\n");
                        if (elementsQue == null || elementsQue.isEmpty()) {
                            requestData.put("answer", URLEncoder.encode(answer, "utf-8"));
                            queSubmit(body, index, header, requestData);
                            continue;
                        }
                        LinkedList<String> multipleChoiceList = new LinkedList<>();
                        for (int i = 0; i < elementsQue.size(); i++) {
                            body.appendText(elementsQue.get(i).text().trim() + "::" + answer + ",");
                            requestData.put("actionType", "saveAnswer");
                            requestData.put("currentSubmitQuestionid", String.valueOf(topic));
                            requestData.put("testId", testId);
                            if ("正确".equals(answer)) {
                                requestData.put("answer", "T");
                                queSubmit(body, index, header, requestData);
                                break;
                            } else if ("错误".equals(answer)) {
                                requestData.put("answer", "F");
                                queSubmit(body, index, header, requestData);
                                break;
                            } else if (answer.equals(elementsQue.get(i).text().trim())) {
                                requestData.put("answer", String.valueOf(topic + i + 1));
                                queSubmit(body, index, header, requestData);
                                break;
                            } else if (answer.trim().contains(elementsQue.get(i).text().trim())) {
                                multipleChoiceList.addLast(String.valueOf(topic + i + 1));
                            }
                            if (i == elementsQue.size() - 1) {
                                if (multipleChoiceList.isEmpty()) {
                                    body.appendText("\n没有查找到正确结果");
                                    requestData.put("answer", String.valueOf(topic + 1));
                                    queSubmit(body, index, header, requestData);
                                    break;
                                }
                                requestData.put("answer", multipleChoiceList);
                                queSubmit(body, index, header, requestData);
                                break;
                            }
                        }
                    }
                }
                HttpUtil.doGet(Constant.SUBMIT_URL + testId, null, null);
                body.appendText("\n交卷完成,请在浏览器中查看提交结果");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    private void queSubmit(TextArea body, int index, Map<String, String> header, Map<String, ?> requestData) throws InterruptedException, IOException {
        //防止请求频繁
        Thread.sleep(2000);
        String res = EntityUtils.toString(HttpUtil.doPost(Constant.QUESTION_URL + testId, header, requestData));
        if (res.contains("保存答案")) {
            body.appendText("\n提交" + index + "道题目完成");
        } else {
            body.appendText("\n提交" + index + "道题目失败");
        }
    }

    private void genderViewId() throws IOException {
        if (StringUtils.isNotBlank(course.getTestId())) {
            this.testId = course.getTestId();
            Document document = Jsoup.parse(EntityUtils.toString(HttpUtil.doGet(Constant.STU_MORE_RESULT + course.getTestId(), null, null)));
            Elements elements = document.getElementsByClass("align_c");
            if (elements.isEmpty()) return;
            int index = 0;
            int maxS = 0;
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                if (i % 2 == 0) {
                    try {
                        int source = Integer.parseInt(element.text().split("\\.")[0].trim());
                        if (source > maxS) {
                            maxS = source;
                            index = i + 1;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
            String viewId = elements.get(index).select("a[href]").attr("href");
            if (StringUtils.isNotBlank(viewId)) this.viewId = viewId;
        }
    }
}
