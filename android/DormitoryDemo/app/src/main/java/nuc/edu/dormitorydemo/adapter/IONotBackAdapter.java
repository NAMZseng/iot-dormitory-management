package nuc.edu.dormitorydemo.adapter;

/**
 * 未归寝人员列表适配器
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nuc.edu.dormitorydemo.R;
import nuc.edu.dormitorydemo.domain.StudentInfo;

public class IONotBackAdapter extends BaseAdapter {

    private  Context context;
    private ArrayList<StudentInfo> studentInfos;
    private ViewHolder viewHolder;

    public IONotBackAdapter(Context context, ArrayList<StudentInfo> studentInfos){
        this.context = context;
        this.studentInfos = studentInfos;
    }

    @Override
    public int getCount() {
        return studentInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return studentInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = View.inflate(context, R.layout.notback_item, null);
            viewHolder = new ViewHolder();
            viewHolder.notback_tv_name = (TextView)convertView.findViewById(R.id.notback_tv_name);
            viewHolder.notback_tv_teachername = (TextView)convertView.findViewById(R.id.notback_tv_teachername);
            viewHolder.notback_tv_teachertel = (TextView)convertView.findViewById(R.id.notback_tv_teachertel);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        StudentInfo studentInfo = studentInfos.get(position);
        viewHolder.notback_tv_name.setText(studentInfo.getName());
        viewHolder.notback_tv_teachertel.setText(studentInfo.getTeacherTel());
        viewHolder.notback_tv_teachername.setText(studentInfo.getTeacherName());

        return convertView;
    }


    static class ViewHolder{
        TextView notback_tv_name;
        TextView notback_tv_teachername;
        TextView notback_tv_teachertel;
    }

}
