package com.example.jihye.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1; //runtime permission시 사용할 resquest code
    boolean isPermitted = false;
    private File folder; // 파일
    private String folderPath;
    private int currentPosition; // 리스트뷰 아이템의 현재 위치
    private ListView m_ListView; //음악 목록 리스트뷰
    private ArrayAdapter<String> m_Adapter; // 음악 목록 리스트뷰 어댑터


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Runtime permission 요청

        requestRuntimePermission();

        // 외부 공용 디렉토리 중 Music 디렉토리에 대한 File 객체 얻음
        folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        // 절대 경로 String 값을 얻음
        folderPath = folder.getAbsolutePath();
        // 로그 파일 절대 경로 생성 (String 값)

        String[] playlist = folder.list(); // 음악 리스트

        //Anroid에서 제공하는 String 문자열 하나를 출력하는 layout으로 어댑터 생성
        m_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playlist);
        //Xml에서 추가한 ListView의 객체
        m_ListView = (ListView) findViewById(R.id.playlist);
        //ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);
        //ListView 아이템 터치 시 이벤트를 처리할 리스너 설정
        m_ListView.setOnItemClickListener(onClickListItem);
        // Here, thisActivity is the current activity

    }
    // 아이템 터치 이벤트 리스너 구현
    AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // 리스트뷰 아이템
            currentPosition = position;
            //이벤트 발생 시 해당 아이템 위치 를 텍스트 로 출력
            Intent playMusic = new Intent(MainActivity.this, MusicPlayer.class);
            playMusic.putExtra("musicNo", currentPosition);
            startActivityForResult(playMusic, 119);
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
// MainActivity 에서 요청할 때 보낸 요청 코드 (119)
                case 119:
                    break;
            }
        }
    }


    private void requestRuntimePermission() {
        //*******************************************************************
        // Runtime permission check
        //*******************************************************************
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            // WRITE_EXTERNAL_STORAGE 권한이 있는 것
            isPermitted = true;
        }
        //*********************************************************************
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // read_external_storage-related task you need to do.
                    Log.v("ok", "accepted");
                    // WRITE_EXTERNAL_STORAGE 권한을 얻음
                    isPermitted = true;

                } else {
                    Toast.makeText(getApplicationContext(), "접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    // 권한을 얻지 못 하였으므로 location 요청 작업을 수행할 수 없다
                    // 적절히 대처한다
                    isPermitted = false;

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}







