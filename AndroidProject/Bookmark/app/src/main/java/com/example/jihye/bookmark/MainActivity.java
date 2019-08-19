package com.example.jihye.bookmark;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity { // 기본 리스트뷰 화면

    ActionMode bActionMode; // 컨텍스트액션모드
    private ListView b_ListView; //즐겨찾기 리스트뷰
    private ListViewAdapter b_Adapter; // 즐겨찾기 리스트뷰 어댑터
    private ArrayList<ListViewItem> b_arraylist = new ArrayList<ListViewItem>();
    static final int GET_DATA = 1;
    String FILENAME = "bookmark.txt";
    int pos; //LongClick한 ListViewItem의 위치

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //즐겨찾기 리스트 화면

        b_Adapter = new ListViewAdapter();

        b_ListView = (ListView) findViewById(R.id.list);

        // ListView에 어댑터 연결
        b_ListView.setAdapter(b_Adapter);

        // ListView 아이템 터치 시 이벤트를 처리할 리스너 설정
        b_ListView.setOnItemClickListener(onClickListItem);

        // ListView에서 발생한 long click 이벤트를 처리할 이벤트 리스너 객체 등록
        b_ListView.setOnItemLongClickListener(onLongCickListItem);
    }

    // 아이템 터치 이벤트 리스너
    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
            String site_url = item.getUrl();
            Intent homepage = new Intent(Intent.ACTION_VIEW, Uri.parse(site_url)); // 홈페이지 화면을 띄우는 암시적 인텐트
            startActivity(homepage);
        }
    };
    // 아이템 롱 클릭 이벤트 리스너
    private AdapterView.OnItemLongClickListener onLongCickListItem = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            pos = position; // 롱 클릭한 리스트뷰 아이템 위치
            if (bActionMode != null)
                return false;
            bActionMode = startActionMode(mActionCallback); // 컨텍스트 액션모드를 시작한다.
            view.setSelected(true);
            return true;
        }
    };
    ActionMode.Callback mActionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //MenuInflater 객체를 이용하여 컨텍스트 메뉴를 생성
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.activity_context_menu, menu);
            return true;
        }

        //onCreateActionMode가 호출된 후에 호출
        //액션 메뉴를 refresh하는 목적으로 여러 번 호출될 수 있음
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;//아무것도 하지 않을 때 false 반환, 액션 메뉴가 업데이트 되면 true 반환
        }

        //사용자가 액션 메뉴 항목을 클릭했을 때 호출
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.b_context_menu:
                    b_Adapter.deleteItem(pos);//아이템 삭제 함수 호출
                    b_ListView.clearChoices();//ListView 선택 초기화
                    b_Adapter.notifyDataSetChanged();//ListView 갱신
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // 사용자가 컨텍스트 액션 모드를 빠져나갈 때 호출
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            bActionMode = null;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_addbookmark_menu, menu); // 액션바에 즐겨찾기를 추가하는 메뉴를 추가
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//add버튼 선택
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent Add = new Intent(this, BookmarkInfo.class); // Add 인텐트 : 즐겨찾기정보를 추가하는 화면으로 이동
                startActivityForResult(Add, GET_DATA); //Add 인텐트 실행
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_DATA) {
            if (resultCode == RESULT_OK) { // 인텐트가 정상적으로 작동하였다.

                File info = new File(getFilesDir(), FILENAME);
                FileReader fr = null;
                BufferedReader bufrd = null;
                String s_name, s_url;

                if (info.exists()) {
                    try {
                        fr = new FileReader(info);
                        bufrd = new BufferedReader(fr);
                        s_name = bufrd.readLine(); // 파일을 1줄씩 읽어온다
                        s_url = bufrd.readLine(); // 첫째줄은 이름, 둘째줄은 url
                        bufrd.close();
                        fr.close();
                        b_Adapter.addItem("" + s_name, "" + s_url); // ArrayList에 아이템을 추가한다
                        b_Adapter.notifyDataSetChanged(); // 데이터가 변경되었음을 알려서 ListView에 출력하게 한다.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) { // 인텐트가 정상적으로 작동하지 못하였다.
        }
    }

}
