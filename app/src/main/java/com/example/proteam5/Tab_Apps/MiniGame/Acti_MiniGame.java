package com.example.proteam5.Tab_Apps.MiniGame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteam5.DAO.User_DAO;
import com.example.proteam5.Model.User;
import com.example.proteam5.R;
import com.example.proteam5.Tab_Apps.MiniGame.Model_MiniGame.Answer;
import com.example.proteam5.Tab_Apps.MiniGame.Model_MiniGame.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Acti_MiniGame extends AppCompatActivity implements View.OnClickListener {
    TextView tvQuestion,tvContentQuestion,tvCoin,tvDapAnA,tvDapAnB,tvDapAnC;
    List<Question> mlistQuetion;
    int currentQuetion = 0;
    Question mQuestion;
    int Coin =0;
    FirebaseUser USER;
    User_DAO user_dao;
    private FirebaseFirestore firebaseFirestore;
    private List<User> userList;
    private User user;
    String mail,age,userCoin,avt,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_mini_game);
        anhXa();
        ReadFireBase_User();
        mlistQuetion =getListQuestion();
        if (mlistQuetion.isEmpty()){
            return;
        }
        setDataQuestion(mlistQuetion.get(currentQuetion));
    }

    private void setDataQuestion(Question question) {
        if (question==null){
            return;
        }
        mQuestion = question;
        String titleQuestion = "Câu Hỏi "+question.getStt();
        tvQuestion.setText(titleQuestion);
        tvContentQuestion.setText(question.getContent());
        tvCoin.setText(String.valueOf(Coin));
        tvDapAnA.setText(question.getAnswerList().get(0).getContent());
        tvDapAnB.setText(question.getAnswerList().get(1).getContent());
        tvDapAnC.setText(question.getAnswerList().get(2).getContent());
        tvDapAnA.setBackgroundResource(R.drawable.boder_cauhoi);
        tvDapAnB.setBackgroundResource(R.drawable.boder_cauhoi);
        tvDapAnC.setBackgroundResource(R.drawable.boder_cauhoi);
        tvDapAnA.setOnClickListener(Acti_MiniGame.this);
        tvDapAnB.setOnClickListener(Acti_MiniGame.this);
        tvDapAnC.setOnClickListener(Acti_MiniGame.this);
    }


    public void anhXa(){
        tvQuestion = findViewById(R.id.tv_soThuTu);
        tvContentQuestion = findViewById(R.id.tv_cauHoi);
        tvCoin = findViewById(R.id.tv_dialog_coinALTP);
        tvDapAnA = findViewById(R.id.tv_dapAn_A);
        tvDapAnB = findViewById(R.id.tv_dapAn_B);
        tvDapAnC = findViewById(R.id.tv_dapAn_C);
        USER = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();
        user= new User();
        user_dao = new User_DAO(this);
    }
    private List<Question> getListQuestion(){
        List<Question> list = new ArrayList<>();

        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer("java",false));
        answerList.add(new Answer("class",true));
        answerList.add(new Answer("jav",false));
        list.add(new Question(1,"File chứa mã nguồn java sau khi được biên dịch có đuôi là gì?",answerList));

        List<Answer> answerList1 = new ArrayList<>();
        answerList1.add(new Answer("3",false));
        answerList1.add(new Answer("2",true));
        answerList1.add(new Answer("1",false));
        list.add(new Question(2,"Java platform gồm mấy thành phần?",answerList1));

        List<Answer> answerList2 = new ArrayList<>();
        answerList2.add(new Answer("public đứng trước static",false));
        answerList2.add(new Answer("static đứng trước public",false));
        answerList2.add(new Answer("Thứ tự bất kỳ nhưng thông thường public đứng trước",true));
        list.add(new Question(3,"Thứ tự các từ khóa public và static khi khai bao như thế nào?",answerList2));

        List<Answer> answerList3 = new ArrayList<>();
        answerList3.add(new Answer("1",true));
        answerList3.add(new Answer("2",false));
        answerList3.add(new Answer("3",false));
        list.add(new Question(4," Một lớp trong Java có thể có bao nhiêu lớp cha?",answerList3));

        List<Answer> answerList4 = new ArrayList<>();
        answerList4.add(new Answer("Vô số",true));
        answerList4.add(new Answer("1",false));
        answerList4.add(new Answer("2",false));
        list.add(new Question(5," Một lớp trong Java có bao nhiêu lớp con?",answerList4));

        List<Answer> answerList5 = new ArrayList<>();
        answerList5.add(new Answer("class A extend B {}",false));
        answerList5.add(new Answer("public classs A extend B {}",false));
        answerList5.add(new Answer("class A extends B {}",true));
        list.add(new Question(6,"Để khai báo lớp A kế thừa lớp B phải làm như thế nào?",answerList5));

        List<Answer> answerList6 = new ArrayList<>();
        answerList6.add(new Answer("float f;",true));
        answerList6.add(new Answer("public static f;",false));
        answerList6.add(new Answer("Không có giá trị đúng",false));
        list.add(new Question(7,"Biến f nào sau đây là biến đại diện?",answerList6));

        List<Answer> answerList7 = new ArrayList<>();
        answerList7.add(new Answer("final;",true));
        answerList7.add(new Answer("dem",false));
        answerList7.add(new Answer("$final",false));
        list.add(new Question(8,"Cách đặt tên nào sau đây là không chính xác?",answerList7));

        List<Answer> answerList8 = new ArrayList<>();
        answerList8.add(new Answer("6",false));
        answerList8.add(new Answer("7",false));
        answerList8.add(new Answer("8",true));
        list.add(new Question(9,"Có bao nhiêu kiểu dữ liệu cơ sở trong Java?",answerList8));

        List<Answer> answerList9 = new ArrayList<>();
        answerList9.add(new Answer("Software design",false));
        answerList9.add(new Answer("Software requirement specification",true));
        answerList9.add(new Answer("Testing.",false));
        list.add(new Question(10,"IEEE 830-1993 là một khuyến nghị tiêu chuẩn cho?",answerList9));

        List<Answer> answerList10 = new ArrayList<>();
        answerList10.add(new Answer("Con người",true));
        answerList10.add(new Answer("Quy trình",false));
        answerList10.add(new Answer("Sản phầm",false));
        list.add(new Question(11,"Trong phát triển phần mềm, yếu tố nào quan trọng nhất?",answerList10));



        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_dapAn_A:
                tvDapAnA.setBackgroundResource(R.drawable.boder_dapan_select);
                CheckAnswer(tvDapAnA,mQuestion,mQuestion.getAnswerList().get(0));
                break;

            case R.id.tv_dapAn_B:
                tvDapAnB.setBackgroundResource(R.drawable.boder_dapan_select);
                CheckAnswer(tvDapAnB,mQuestion,mQuestion.getAnswerList().get(1));
                break;

            case R.id.tv_dapAn_C:
                tvDapAnC.setBackgroundResource(R.drawable.boder_dapan_select);
                CheckAnswer(tvDapAnC,mQuestion,mQuestion.getAnswerList().get(2));
                break;
        }
    }
    private void CheckAnswer(TextView textView,Question question,Answer answer){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (answer.isCorrect()){
                    textView.setBackgroundResource(R.drawable.boder_dapan_true);
                    nextQuestion();
                }else{
                    textView.setBackgroundResource(R.drawable.boder_dapan_false);
                    ShowAnswerCorrect(question);
                    GameOver();
                }
            }
        },1000);
    }

    private void GameOver() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ShowDialog("Game Over");
            }
        },1000);
    }

    private void ShowAnswerCorrect(Question question) {
        if (question==null||question.getAnswerList()==null ||question.getAnswerList().isEmpty()){
            return;
        }
        if (question.getAnswerList().get(0).isCorrect()){
            tvDapAnA.setBackgroundResource(R.drawable.boder_dapan_true);
        }else if (question.getAnswerList().get(1).isCorrect()){
            tvDapAnB.setBackgroundResource(R.drawable.boder_dapan_true);
        }else if (question.getAnswerList().get(2).isCorrect()){
            tvDapAnC.setBackgroundResource(R.drawable.boder_dapan_true);
        }
    }

    private void nextQuestion() {
        if (currentQuetion==mlistQuetion.size()-1){
            ShowDialog("YOU WIN");
        }else{
            currentQuetion++;
            Coin=Coin+1000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setDataQuestion(mlistQuetion.get(currentQuetion));
                }
            },1000);
        }
    }

    private void ShowDialog(String massage) {

        Dialog dialog1 = new Dialog(Acti_MiniGame.this);
        dialog1.setContentView(R.layout.dialog_ailatrieuphu);
        dialog1.setCancelable(false);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView title = dialog1.findViewById(R.id.tv_dialog_title_ALTP);
        TextView content = dialog1.findViewById(R.id.tv_dialog_content_ALTP);
        title.setText(massage);
        content.setText(String.valueOf(Coin));
        TextView Yes = dialog1.findViewById(R.id.btn_dialog_yes_ALTP);
        TextView No = dialog1.findViewById(R.id.btn_dialog_no_ALTP);
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuetion =0;
                Coin=0;
                setDataQuestion(mlistQuetion.get(currentQuetion));
                dialog1.dismiss();
            }
        });

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tien = Integer.parseInt(userCoin);
                int _age = Integer.parseInt(age);
                if (Coin == 0){
                    return;
                }
                int tongCoin = Coin+tien;//(String avatar, String userName, String mail, int age, int coin)
                user = new User(avt,userName,USER.getEmail(),_age,tongCoin);
                user_dao.Update_User(user);
                currentQuetion =0;
                Coin=0;
                setDataQuestion(mlistQuetion.get(currentQuetion));
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }


    public void ReadFireBase_User(){
        firebaseFirestore.collection("User")
                .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User us = document.toObject(User.class);
                            userList.add(us);
                        }
                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i).getMail().equals(USER.getEmail())){
                                mail=userList.get(i).getMail();
                                age=String.valueOf(userList.get(i).getAge());
                                userCoin=String.valueOf(userList.get(i).getCoin());
                                avt=userList.get(i).getAvatar();
                                userName = userList.get(i).getUserName();
                            }
                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.getException());
                    }
                }
            });
    }

}