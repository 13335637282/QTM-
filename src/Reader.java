import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moandjiezana.toml.Toml;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {
    /*
     * frame: JFrame
     *  |_  scrollPane:JScrollPane [paper:JPanel]
     *                                   |_ Title:JLabel [BorderLayout.NORTH]
     *                                   |_ check_panel:JPanel [BorderLayout.SOUTH]
     *                                   |               |_ show_score:JPanel [BorderLayout.CENTER]
     *                                   |               |              |_ max_score_label:JLabel
     *                                   |               |_ check:JButton [BorderLayout.NORTH]
     *                                   |_ Q:JPanel [BorderLayout.CENTER]
     *                                          |_ ... 等Reader读取
     */
    private static final JPanel Q = new JPanel();
    private static int QC = 0;
    private static int OC = 0;
    private static final int max_score = -1;
    private static int[] time_left = {0,0};
    private static final ArrayList<ObjPlane> Objs = new ArrayList<>();
    private static boolean end = false;
    private static final JLabel right_label = new JLabel("正确个数:--/--");
    private static final JLabel time_left_label = new JLabel("时间剩余:--:--");
    public Reader(String path) {
        JFrame frame = new JFrame("QTM");
        frame.setSize(1400,700);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel paper = new JPanel();
        paper.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(paper);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JLabel Title = new JLabel("Loading...");
        Title.setFont(new Font("宋体",Font.BOLD,30));
        paper.add(Title,BorderLayout.NORTH);
        
        // check_panel 部分
        JPanel check_panel = new JPanel();
        check_panel.setLayout(new BorderLayout());

        JPanel show_score = new JPanel(new GridLayout(6,1));

        right_label.setFont(new Font("宋体", Font.PLAIN,15));
        time_left_label.setFont(new Font("宋体", Font.PLAIN,15));

        show_score.add(time_left_label);
        show_score.add(right_label);
        check_panel.add(show_score,BorderLayout.CENTER);


        JButton check = new JButton("提前交卷");
        check.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {
                end = true;
                check_all();
            }

            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });

        check_panel.add(check,BorderLayout.NORTH);

        paper.add(check_panel,BorderLayout.SOUTH);

        Q.setLayout(new GridLayout(OC+1,1));
        paper.add(Q,BorderLayout.CENTER);
        frame.add(scrollPane);

        File file = new File(path);
        String string;
        //2025/6/6 停止支持 qtm 格式
        //if (path.endsWith(".qtm") || path.endsWith(".QTM")){
        //    try {
        //        int line = 0;
        //        Scanner scanner = new Scanner(new File(file.getAbsolutePath()));
        //        while (scanner.hasNextLine()) {
        //            line++;
        //            string = scanner.nextLine();
        //            switch (string.split(">")[0]) {
        //                case "<title" -> {
        //                    frame.setTitle("QTM-" + string.replaceFirst("[<A-Za-z>_]+", ""));
        //                    Title.setText(string.replaceFirst("[<A-Za-z>_]+", ""));
        //                }
        //                case "<max_score" -> {
        //                    //try {
        //                    //    max_score = Integer.parseInt(string.replaceFirst("[<A-Za-z>_]+", ""));
        //                    //} catch (NumberFormatException e) {
        //                    //    JOptionPane.showMessageDialog(null,"读取时错误:QTM文件 第"+line+"行,满分读取错误");
        //                    //}
        //                    //max_score_label.setText(string.replaceFirst("[<A-Za-z>_]+", ""));
        //                }
        //                case "<set_time" -> {
        //                    try {
        //                        time_left = new int[]{Integer.parseInt(string.replaceFirst("[<A-Za-z>_]+", "")), 0};
        //                    } catch (NumberFormatException e) {
        //                        JOptionPane.showMessageDialog(null, "读取时错误:QTM文件 第" + line + "行,时间读取错误");
        //                    }
        //                    time_left_label.setText("剩余时间:" + time_left[0] + ":" + time_left[1]);
        //                    new Thread(() -> {
        //                        while (!(time_left[0] <= 0 && time_left[1] <= 0) && !end) {
        //                            time_left[1]--;
        //                            if (time_left[1] < 0) {
        //                                time_left[1] = 59;
        //                                time_left[0]--;
        //                            }
        //                            try {
        //                                Thread.sleep(1000);
        //                            } catch (InterruptedException e) {
        //                                throw new RuntimeException(e);
        //                            }
        //                            time_left_label.setText("剩余时间:" + time_left[0] + ":" + time_left[1]);
        //                        }
        //                        if (!end) {
        //                            check_all();
        //                            new speak("考试结束 请考生停止作答");
        //                        }
        //                    }).start();
        //                }
        //                case "<plain_text" -> {
        //                    addOC();
        //                    Q.add(getJLabel(string));
        //                }
        //                case "<TrueOrFalse" -> {
        //                    QC++;
        //                    String f = string.replaceFirst("<TrueOrFalse>", "");
        //                    String[] ff = f.split("##");
        //                    TrueOrFalse trueOrFalse = null;
        //                    try {
        //                        trueOrFalse = new TrueOrFalse(ff[0], Integer.parseInt(ff[1]));
        //                    } catch (NumberFormatException e) {
        //                        JOptionPane.showMessageDialog(null, "读取时错误:QTM文件 第" + line + "行,答案读取错误");
        //                    }
        //                    try {
        //                        Q.add(trueOrFalse);
        //                        Objs.add(trueOrFalse);
        //                        addOC();
        //                    } catch (NullPointerException e) {
        //                        JOptionPane.showMessageDialog(null, "读取时错误:QTM文件 第" + line + "行,加载失败");
        //                    }
        //                }
        //                case "<SingleChoice" -> {
        //                    QC++;
        //                    String f = string.replaceFirst("<SingleChoice>", "");
        //                    String[] ff = f.split("##");
        //                    String[] fff = ff[0].split("#");
        //                    SingleChoice single_choice = null;
        //                    if (fff.length - 1 == 3) {
        //                        try {
        //                            single_choice = new SingleChoice(new String[]{fff[1], fff[2], fff[3]}, fff[0], Integer.parseInt(ff[1]));
        //                        } catch (NumberFormatException e) {
        //                            JOptionPane.showMessageDialog(null, "读取时错误:QTM文件 第" + line + "行,答案读取错误");
        //                        }
        //                    } else if (fff.length - 1 == 4) {
        //                        try {
        //                            single_choice = new SingleChoice(new String[]{fff[1], fff[2], fff[3], fff[4]}, fff[0], Integer.parseInt(ff[1]));
        //                        } catch (NumberFormatException e) {
        //                            JOptionPane.showMessageDialog(null, "读取时错误:QTM文件 第" + line + "行,答案读取错误");
        //                        }
        //                    }
        //                    try {
        //                        Q.add(single_choice);
        //                        Objs.add(single_choice);
        //                        addOC();
        //                    } catch (NullPointerException e) {
        //                        JOptionPane.showMessageDialog(null, "读取时错误:QTM文件 第" + line + "行,加载失败");
        //                    }
        //                }
        //                case "<MultipleChoice" -> {
        //                    QC++;
        //                    String f = string.replaceFirst("<MultipleChoice>", "");
        //                    String[] ff = f.split("##");
        //                    String[] fff = ff[0].split("#");
        //                    MultipleChoice multipleChoice = null;
        //                    if (fff.length - 1 == 3) {
        //                        try {
        //                            multipleChoice = new MultipleChoice(new String[]{fff[1], fff[2], fff[3]}, fff[0], Integer.parseInt(ff[1]));
        //                        } catch (NumberFormatException e) {
        //                            JOptionPane.showMessageDialog(null, "读取时错误:QTM文件 第" + line + "行,答案读取错误");
        //                        }
        //                    } else if (fff.length - 1 == 4) {
        //                        try {
        //                            multipleChoice = new MultipleChoice(new String[]{fff[1], fff[2], fff[3], fff[4]}, fff[0], Integer.parseInt(ff[1]));
        //                        } catch (NumberFormatException e) {
        //                            JOptionPane.showMessageDialog(null, "读取时错误:QTM文件 第" + line + "行,答案读取错误");
        //                        }
        //                    }
        //                    try {
        //                        Q.add(multipleChoice);
        //                        Objs.add(multipleChoice);
        //                        addOC();
        //                    } catch (NullPointerException e) {
        //                        JOptionPane.showMessageDialog(null, "读取时错误:QTM文件 第" + line + "行,加载失败");
        //                    }
        //                }
        //                case "<show" -> {
        //                    //addOC();
        //                    //Q.add(new SingleChoice(new String[]{"A","B","C","D"},"www",2));
        //                    new speak("考试开始 限时:" + (time_left[0] + 1) + "分钟");
        //                    frame.setVisible(true);
        //                }
        //            }
        //        }
        //    } catch (FileNotFoundException e) {
        //        throw new RuntimeException(e);
        //    }
        //}

            // 基本设置
            String title = null;
            Integer testTime = null;
            java.util.List<QuestionData> questions = new ArrayList<>();

        try{//Toml
            Toml toml = new Toml().read(file);
            title = toml.getString("title");
            testTime = toml.getLong("test_time").intValue();

            java.util.List<Toml> tomlQuestions = toml.getTables("main");
            if (tomlQuestions != null) {
                for (Toml question : tomlQuestions) {
                    questions.add(new QuestionData(
                            question.getString("type"),
                            question.getString("question"),
                            question.getList("options"),
                            question.getLong("answer") != null ? question.getLong("answer").intValue() : null
                    ));
                }
            }
            if (!path.endsWith(".toml")){
                JOptionPane.showMessageDialog(null,"你的内容为 Toml 但文件不后缀为 .toml");
            }
        } catch (Exception e) {
            try{// JSON parsing
                JsonObject json = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();

                if (json.has("property")) {
                    JsonObject property = json.getAsJsonObject("property");
                    title = property.get("title").getAsString();
                    testTime = property.get("test_time").getAsInt();
                }

                if (json.has("main")) {
                    JsonArray mainArray = json.getAsJsonArray("main");
                    for (int i = 0; i < mainArray.size(); i++) {
                        JsonObject question = mainArray.get(i).getAsJsonObject();
                        String type = question.get("type").getAsString();
                        String questionText = question.get("question").getAsString();

                        java.util.List<String> options = null;

                        if (question.has("options")) {
                            options = new ArrayList<>();
                            JsonArray optionsArray = question.getAsJsonArray("options");
                            for (int j = 0; j < optionsArray.size(); j++) {
                                options.add(optionsArray.get(j).getAsString());
                            }
                        }

                        Integer answer = null;
                        if (question.has("answer")) {
                            answer = question.get("answer").getAsInt();
                        }

                        questions.add(new QuestionData(type, questionText, options, answer));
                    }
                }
                if (!path.endsWith(".json")){
                    JOptionPane.showMessageDialog(null,"你的内容为 Json 但文件不后缀为 .json");
                }
            } catch (Exception ex) {
                JOptionPane.showConfirmDialog(null,"所选文件格式QTM不支持","文件不支持", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE,null);
                System.exit(-1);
            }
        }


            // Set up UI with parsed data
            if (title != null) {
                frame.setTitle("QTM v0.4" + title);
                Title.setText(title);
            }

            if (testTime != null) {
                time_left = new int[]{testTime, 0};
                time_left_label.setText("剩余时间:" + time_left[0] + ":" + time_left[1]);

                new Thread(() -> {
                    while (!(time_left[0] <= 0 && time_left[1] <= 0) && !end) {
                        time_left[1]--;
                        if (time_left[1] < 0) {
                            time_left[1] = 59;
                            time_left[0]--;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        time_left_label.setText("剩余时间:" + time_left[0] + ":" + time_left[1]);
                    }
                    if (!end) {
                        check_all();
                        new speak("考试结束 请考生停止作答");
                    }
                }).start();
            }

            // 加载组件
            for (QuestionData question : questions) {
                switch (question.type) {
                    case "PlainText" -> {
                        addOC();
                        Q.add(getJLabel(question.question));
                    }
                    case "plain_text" -> {
                        addOC();
                        Q.add(getJLabel(question.question));
                        JOptionPane.showMessageDialog(null,"发现老旧的纯文本方法");
                    }
                    case "TrueOrFalse" -> {
                        QC++;
                        TrueOrFalse trueOrFalse = new TrueOrFalse(question.question, question.answer);
                        Q.add(trueOrFalse);
                        Objs.add(trueOrFalse);
                        addOC();
                    }
                    case "SingleChoice" -> {
                        QC++;
                        SingleChoice singleChoice = new SingleChoice(
                                question.options.toArray(new String[0]),
                                question.question,
                                question.answer
                        );
                        Q.add(singleChoice);
                        Objs.add(singleChoice);
                        addOC();
                    }
                    case "MultipleChoice" -> {
                        QC++;
                        MultipleChoice multipleChoice = new MultipleChoice(
                                question.options.toArray(new String[0]),
                                question.question,
                                question.answer
                        );
                        Q.add(multipleChoice);
                        Objs.add(multipleChoice);
                        addOC();
                    }
                }
            }

            // Show the frame
            new speak("考试开始 限时:" + (time_left[0] + 1) + "分钟");
            frame.setVisible(true);
    }

    private static JLabel getJLabel(String string) {
        JLabel l = new JLabel(string.replaceAll("[<A-Za-z>_]+",""));
        l.setFont(new Font("宋体",Font.PLAIN,15));
        if (string.replaceFirst("[<A-Za-z>_]+","").endsWith("<b>")){
            l.setFont(new Font("宋体",Font.BOLD,15));
        } else if (string.replaceFirst("[<A-Za-z>_]+","").endsWith("<i>")) {
            l.setFont(new Font("宋体",Font.ITALIC,15));
        } else if (string.replaceFirst("[<A-Za-z>_]+","").endsWith("<bi>")) {
            l.setFont(new Font("宋体",Font.PLAIN,20));
        }
        return l;
    }

    private static void addOC() {
        OC++;
        Q.setLayout(new GridLayout(OC/2+1,2));
    }

    private static void check_all() {
        int right = 0;
        int wrong = 0;

        for (ObjPlane x : Objs) {
            x.show_answers();
            if (x.check()) {
                right++;
            } else {
                wrong++;
            }
        }

        right_label.setText("正确个数:"+right+"/"+(right+wrong)+"(正确率:"+((float) right) / ((float) right+wrong)*100+"%"+")"+"["+right+":"+wrong+"]");
    }

    static class QuestionData {
        String type;
        String question;
        java.util.List<String> options;
        Integer answer;

        public QuestionData(String type, String question, java.util.List<String> options, Integer answer) {
            this.type = type;
            this.question = question;
            this.options = options;
            this.answer = answer;
        }
    }
}
