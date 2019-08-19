package com.example.jihye.musicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

// audio/video 파일 재생을 제어하는데 사용하는 클래스
public class MusicService extends Service {

    File musicList; // 파일
    String[] playlist; // 음악 리스트
    MediaPlayer mediaPlayer; // MediaPlayer 객체
    int currentPos = 0; // 현재 실행할 음악의 position값

    IBinder mBinder = new MyBinder();

    NotificationManager notificationManager;
    NotificationChannel mChannel;
    Notification noti;
    Notification.Builder notiBuilder;
    private int REQUEST_CODE = 0; //Pendingintent를 위한 REQUEST_CODE

    @Override
    public IBinder onBind(Intent intent) {
        //액티비티에서 bindService()를 실행하면 호출됨
        //리턴한 IBinder 객체는 서비스와 클라이언트 사이의 인터페이스 정의한다.
        return mBinder;// 서비스 객체를 리턴
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicList = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC); //외부저장소에서 음악 파일을 받아온다.
        playlist = musicList.list(); // 음악 파일 목록을 리스트에 저장
    }

    public void makeNotification() { //notification 생성 메소드
        //***************************************
        // Service를 Foreground로 실행하기 위한 과정
        // Android version이 8.0 Oreo 이상이면
        if (Build.VERSION.SDK_INT >= 26) {
            Log.d("MusicService", "Android version Oreo!");

            // NotificationChannel(String id, CharSequence name, int importance);
            mChannel = new NotificationChannel("music_service_channel_id",
                    "music_service_channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            // Notification.VISIBILITY_PUBLIC
            // Notification.VISIBILITY_SECRET

            // 노티피케이션 매니저 초기화
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 노티피케이션 채널 생성
            notificationManager.createNotificationChannel(mChannel);

            // 노티피케이션 빌더 객체 생성
            notiBuilder = new Notification.Builder(this, mChannel.getId());

        } else {
            // 노티피케이션 빌더 객체 생성
            notiBuilder = new Notification.Builder(this);
        }

        // Intent 객체 생성 - MusicPlayer 클래스를 실행하기 위한 Intent 객체
        Intent intent = new Intent(this, MusicPlayer.class);
        // Intent 객체를 이용하여 PendingIntent 객체를 생성 - Activity를 실행하기 위한 PendingIntent
        PendingIntent pIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Notification 객체 생성
        noti = notiBuilder.setContentTitle("Now playing")
                .setContentText(playlist[currentPos])
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pIntent)
                .setOnlyAlertOnce(true)
                .build();

        // foregound service 설정 - startForeground() 메소드 호출, 위에서 생성한 nofication 객체 넘겨줌
        startForeground(123, noti);

        //****************************************

    }

    public int onStartCommand(Intent intent, int flags, int startId) { //앱이 종료되도 음악을 실행시키기 위해 재생은 onStartCommand 이용(Started Service)

        currentPos = intent.getIntExtra("posNo", 0); // 현재 음악 위치 번호를 받아온다.
        mediaPlayer = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/" + playlist[currentPos]));

        makeNotification(); // notification 생성

        mediaPlayer.setLooping(false); // 반복재생 여부 설정
        mediaPlayer.start();
        return START_REDELIVER_INTENT;//intent값을 받아야 해당 위치의 음악이 재생되므로 redeliver intent 선택
    }

    @Override
    public void onDestroy() { // 음악을 완전히 종료시키기 위해 종료는 onDestroy 이용(Started Service)
        mediaPlayer.stop(); // 실행중이던 음악을 멈춘다
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }

    public void onClickPause() {
        mediaPlayer.pause();
    } //음악 일시정지 (Bound Service)

    public void onClickRestart() { //일시정지된 음악 재시작(Bound Service)
        mediaPlayer.setLooping(false); // 반복재생 여부 설정
        mediaPlayer.start();
    }

    public void Rewind() { //이전곡 재생 (Bound Service)
        mediaPlayer.stop();
        mediaPlayer.release();

        if (currentPos != 0)
            mediaPlayer = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/" + playlist[currentPos - 1]));
        else
            mediaPlayer = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/" + playlist[playlist.length - 1]));
        if (currentPos != 0)
            currentPos--;
        else
            currentPos = playlist.length - 1;

        notiBuilder.setContentText(playlist[currentPos]);
        notificationManager.notify(123, notiBuilder.build()); //notification 데이터 갱신


        mediaPlayer.setLooping(false); // 반복재생 여부 설정
        mediaPlayer.start();
    }

    public void Foward() { //다음곡 재생 (Bound Service)
        mediaPlayer.stop(); // 실행중이던 음악을 멈춘다
        mediaPlayer.release();

        if (currentPos != playlist.length - 1)
            mediaPlayer = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/" + playlist[currentPos + 1]));
        else
            mediaPlayer = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/" + playlist[0]));
        if (currentPos != playlist.length - 1)
            currentPos++;
        else
            currentPos = 0;

        notiBuilder.setContentText(playlist[currentPos]);
        notificationManager.notify(123, notiBuilder.build()); //notification 데이터 갱신

        mediaPlayer.setLooping(false); // 반복재생 여부 설정
        mediaPlayer.start();
    }


    class MyBinder extends Binder {
        MusicService getService() { // 서비스 객체를 리턴
            return MusicService.this;
        }
    }

}