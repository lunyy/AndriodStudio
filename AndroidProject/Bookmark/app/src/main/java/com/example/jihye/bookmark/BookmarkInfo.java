package com.example.jihye.bookmark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.IOException;


public class BookmarkInfo extends AppCompatActivity { //subclass, 즐겨찾기 사이트 이름과 주소 입력

    EditText b_name, b_url;
    Button AddBt;
    String FILENAME = "bookmark.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbookmark); // 즐겨찾기 추가화면

        b_name = (EditText) findViewById(R.id.bookmarkName_E); // 즐겨찾기 사이트 이름
        b_url = (EditText) findViewById(R.id.URL_E); // 즐겨찾기 사이트 주소
        AddBt = (Button) findViewById(R.id.AddBt); // 데이터를 추가하는 버튼
    }

    public void AddInfo(View view) {
        try {
            FileOutputStream bookmark = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            bookmark.write(b_name.getText().toString().getBytes());
            bookmark.write('\n');//이름과 주소를 구분하기 위해 enter
            bookmark.write(b_url.getText().toString().getBytes());// private 모드로 파일 생성후 쓰기
            bookmark.close(); // 쓰기 후 닫기
        } catch (IOException e) { // IOException처리
            e.printStackTrace();
        }
        Intent Info = new Intent();
        setResult(RESULT_OK, Info);
        finish();
    }
}

