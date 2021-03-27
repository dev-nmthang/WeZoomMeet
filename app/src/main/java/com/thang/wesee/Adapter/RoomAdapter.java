package com.thang.wesee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.thang.wesee.Controller.Interface.UserListening;
import com.thang.wesee.Model.UserModel;
import com.thang.wesee.R;

import java.util.ArrayList;

public class RoomAdapter  extends BaseAdapter {
    private Context context;
    private ArrayList<UserModel> arrayList;
    private UserListening callback;

    public RoomAdapter(Context context, ArrayList<UserModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public  class  ViewHodler{
        TextView txtname,txtdate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_room,null);
            viewHodler=new ViewHodler();
            viewHodler.txtdate=convertView.findViewById(R.id.txtdate);
            viewHodler.txtname=convertView.findViewById(R.id.txtname);
            convertView.setTag(viewHodler);
        }else{
            viewHodler= (ViewHodler) convertView.getTag();
        }

        return convertView;
    }
}
