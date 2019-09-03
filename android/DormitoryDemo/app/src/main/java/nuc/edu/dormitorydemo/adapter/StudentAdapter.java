package nuc.edu.dormitorydemo.adapter;

/**
 * 学生信息的适配器
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nuc.edu.dormitorydemo.R;
import nuc.edu.dormitorydemo.domain.StudentInfo;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private List<StudentInfo> studentInfos = null;

    public StudentAdapter(List<StudentInfo> studentInfos){
        this.studentInfos = studentInfos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.student_item, viewGroup, false);

        final MyViewHolder myViewHolder =new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        StudentInfo student = studentInfos.get(i);
        myViewHolder.num.setText("学号：" + student.getNum()+"");
        myViewHolder.name.setText(student.getName());
        myViewHolder.school.setText("学院：" + student.getSchool());
        myViewHolder.major.setText("专业：" + student.getMajor());
        myViewHolder.teacherName.setText("班主任：" + student.getTeacherName());
        myViewHolder.teacherTel.setText("班主任电话：" + student.getTeacherTel());
        myViewHolder.buildingNum.setText("所在宿舍楼：" + student.getBuildingNum()+"");
        myViewHolder.roomNum.setText("宿舍号：" + student.getRoomNum()+"");
    }

    @Override
    public int getItemCount() {
        if(studentInfos != null)
            return studentInfos.size();
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        View studentView;

        private TextView name;
        private TextView num;
        private TextView school;
        private TextView major;
        private TextView teacherName;
        private TextView teacherTel;
        private TextView buildingNum;
        private TextView roomNum;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            studentView = itemView;
            name = (TextView) itemView.findViewById(R.id.name);
            num = (TextView) itemView.findViewById(R.id.num);
            school = (TextView) itemView.findViewById(R.id.school);
            major = (TextView) itemView.findViewById(R.id.major);
            teacherName = (TextView) itemView.findViewById(R.id.teacherName);
            teacherTel = (TextView) itemView.findViewById(R.id.teacherTel);
            buildingNum = (TextView) itemView.findViewById(R.id.buildingNum);
            roomNum = (TextView) itemView.findViewById(R.id.roomNum);

        }

    }

}
