package com.example.jihye.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;

public class MusicPlayer extends AppCompatActivity {

    MusicService ms; // 서비스 객체
    boolean isService = false; // 서비스 연결 여부

    private File musicList; // 파일
    String[] songs; // 음악 리스트
    private int currentPosition = 0; // 현재 실행할 음악의 position값
    int flag = 0; //start와 restart를 구분하는 flag

    TextView musicTitle; // 음악 제목 텍스트뷰
    ToggleButton playBt; // 재생 & 일시정지 토글버튼

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) { // 서비스와 연결되었을 때 호출되는 메서드, 서비스 객체를 전역변수로 저장
            MusicService.MyBinder mb = (MusicService.MyBinder) service;
            ms = mb.getService(); // 서비스가 제공하는 메소드 호출하여 서비스쪽 객체를 전달받을 수 있음

            isService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isService = false; // 서비스와 연결이 끊겼을 때 호출되는 메서드
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplay);

        playBt = (ToggleButton) findViewById(R.id.playBt);
        musicTitle = (TextView) findViewById(R.id.music_title);

        musicList = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC); // 외부저장소에서 음악 파일들을 받아온다.
        songs = musicList.list(); // 음악 파일 목록을 리스트에 저장

        //현재 눌러진 음악 위치를 인텐트로 값을 받아온다.
        Intent intent = new Intent(this.getIntent());
        currentPosition = intent.getIntExtra("musicNo", 0);
        musicTitle.setText(songs[currentPosition]); // 받아온 위치 값으로 제목을 설정한다.

        playBt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { // 토글버튼 클릭 이벤트 리스너(Play & Pause)
                if (isChecked == true) { // 시작 버튼
                    if (flag == 0) { // 만약 곡이 처음 시작이라면
                        Intent startMusic = new Intent(MusicPlayer.this, MusicService.class);
                        startMusic.putExtra("posNo", currentPosition);
                        startService(startMusic); // onStartCommand(Started Service)
                        flag = 1;
                    } else { // 일시정지된 곡이었다면
                        if (isService) { // onClickRestart(Bound Service)
                            ms.onClickRestart();
                        }
                    }
                } else { // 일시 정지 버튼
                    if (isService) {
                        ms.onClickPause();
                    }
                }
            }
        });
    }

    public void onClick(View view) { //뮤직 플레이어 버튼 이벤트 처리
        switch (view.getId()) {
            case R.id.stopBt: // 정지 버튼
                Intent stopMusic = new Intent(MusicPlayer.this, MusicService.class);
                stopService(stopMusic);
                flag = 0;
                setResult(RESULT_OK); // MusicPlayer.class 액티비티를 종료하고 메인 화면으로 돌아간다.
                finish();
                break;
            case R.id.rewindBt: // 이전곡 재생 버튼
                if (isService) {
                    playBt.setChecked(true);
                    ms.Rewind();
                    if (currentPosition != 0)
                        musicTitle.setText(songs[currentPosition - 1]);
                    else
                        musicTitle.setText(songs[songs.length - 1]);
                    if (currentPosition != 0)
                        currentPosition--;
                    else
                        currentPosition = songs.length - 1;
                }
                break;
            case R.id.fowardBt: // 다음곡 재생 버튼
                if (isService) {
                    playBt.setChecked(true);
                    ms.Foward();
                    if (currentPosition != songs.length - 1)
                        musicTitle.setText(songs[currentPosition + 1]);
                    else
                        musicTitle.setText(songs[0]);
                    if (currentPosition != songs.length - 1)
                        currentPosition++;
                    else
                        currentPosition = 0;
                }
                break;
        }
    }

    @Override
    public void onStart() { //액티비티가 시작되면 서비스에 연결
        super.onStart();
        Intent Musicplayer = new Intent(MusicPlayer.this, MusicService.class);
        bindService(Musicplayer, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() { //액티비티가 종료되면 서비스 연결을 해제
        super.onStop();
        if (isService) {
            unbindService(conn);
            isService = false;
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}



