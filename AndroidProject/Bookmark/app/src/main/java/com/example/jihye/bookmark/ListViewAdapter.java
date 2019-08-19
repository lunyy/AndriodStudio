package com.example.jihye.bookmark;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ListViewAdapter extends BaseAdapter { //custom ListView를 위해 따로 만든 ListViewAdapter
    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> b_arraylist = new ArrayList<ListViewItem>();
    private ArrayList<String> name = new ArrayList<>();
    int cnt, check = 0; // 아이템 카운터 변수, 아이템 중복 체크 변수


    // ListViewAdapter의 생성자
    public ListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return b_arraylist.size();
    }

    //poistion에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        //"listview_item" Layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        //화면에 표시될 View(Layout이 inflate된)으로 부터 위젯에 대한 참조 획득
        TextView nameTextView = (TextView) convertView.findViewById(R.id.Name);

        //Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = b_arraylist.get(position);

        //아이템 내 각 위젯에 데이터 반영
        nameTextView.setText(listViewItem.getName());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    //지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return b_arraylist.get(position);
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(String name, String url) {
        ListViewItem item = new ListViewItem();

        item.setName(name);
        item.setUrl(url);
        if (cnt == 0) {//ArrayList에 저장된 데이터가 0개일 경우 우선 데이터를 추가한다.
            b_arraylist.add(item);
            cnt++;
        }
        for (int i = 0; i < b_arraylist.size(); i++) { //데이터 중복 체크
            String name1 = item.getName();
            String name2 = b_arraylist.get(i).getName();
            if (name1.equals(name2)) { //ArrayList에 저장된 이름들과 새로 추가할 아이템의 이름들을 비교하여 중복이 있을 경우 check증가
                check++;
            }
        }
        if (check == 0) // 중복이 없을 경우
            b_arraylist.add(item); // ArrayList에 데이터를 추가한다.

        check = 0;//중복 체크 변수 초기화
    }

    //아이템 데이터 삭제를 위한 함수
    public void deleteItem(int position) {
        b_arraylist.remove(position);
    }
}
