package com.example.jihye.bookmark;

public class ListViewItem {
    private String b_name; //즐겨찾기 사이트 이름
    private String b_url; // 즐겨찾기 사이트 url

    public void setName(String name){ //이름 설정
        b_name = name;
    }
    public void setUrl(String url){ //url 설정
        b_url = url;
    }

    public String getName(){ //이름 반환
        return this.b_name;
    }

    public String getUrl(){ //url 반환
        return this.b_url;
    }
}
