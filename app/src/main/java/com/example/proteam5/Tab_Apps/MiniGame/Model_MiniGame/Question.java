package com.example.proteam5.Tab_Apps.MiniGame.Model_MiniGame;

import java.util.List;

public class Question {
    int stt;
    String content;
    List<Answer> answerList;

    public Question(int stt, String content, List<Answer> answerList) {
        this.stt = stt;
        this.content = content;
        this.answerList = answerList;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }
}
