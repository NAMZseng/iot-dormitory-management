package nuc.edu.dormitorydemo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import nuc.edu.dormitorydemo.R;

public class ShowStudentDialog extends Dialog {
    Context context;
    public TextView dialog_tv_name;

    public TextView dialog_tv_tectel;

    public TextView dialog_tv_num;

    public TextView dialog_tv_buildingnum;

    public TextView dialog_tv_school;

    public TextView dialog_tv_major;

    public TextView dialog_tv_teaname;

    public TextView dialog_tv_roomnum;

    public ShowStudentDialog(Context context) {
        super(context);
        this.context = context;
    }

    public ShowStudentDialog(Context context, View.OnClickListener clickListener) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.student_dialog);

        dialog_tv_name = (TextView) findViewById(R.id.dialog_tv_name);
        dialog_tv_tectel = (TextView) findViewById(R.id.dialog_tv_tectel);
        dialog_tv_num = (TextView) findViewById(R.id.dialog_tv_num);
        dialog_tv_buildingnum = (TextView) findViewById(R.id.dialog_tv_buildingnum);
        dialog_tv_school = (TextView) findViewById(R.id.dialog_tv_school);
        dialog_tv_major = (TextView) findViewById(R.id.dialog_tv_major);
        dialog_tv_teaname = (TextView) findViewById(R.id.dialog_tv_teaname);
        dialog_tv_roomnum = (TextView) findViewById(R.id.dialog_tv_roomnum);

        Window dialogWindow = this.getWindow();

        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
    }
}
