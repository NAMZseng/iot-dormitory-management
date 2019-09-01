package nuc.edu.dormitorydemo.adapter;

/**
 * 非法进入人员Recycle view的适配器
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import nuc.edu.dormitorydemo.R;
import nuc.edu.dormitorydemo.domain.BlockInfo;

public class IOAdapter extends RecyclerView.Adapter{

    private List<BlockInfo> list;

    public IOAdapter(List<BlockInfo> list){
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.io_item, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        BlockInfo blockInfo = list.get(i);
        MyViewHolder vh = (MyViewHolder)viewHolder;
        vh.tv_name.setText(blockInfo.getStudentName());
        vh.tv_num.setText(blockInfo.getNum()+"");
        vh.tv_teacherTel.setText(blockInfo.getTeacherTel());
        vh.tv_time.setText(blockInfo.getAccessTime());
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private TextView tv_num;
        private TextView tv_teacherTel;
        private TextView tv_time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_teacherTel = (TextView) itemView.findViewById(R.id.tv_teacherTel);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }



    }
}
