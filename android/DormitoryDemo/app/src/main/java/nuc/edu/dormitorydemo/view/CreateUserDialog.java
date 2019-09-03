package nuc.edu.dormitorydemo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import nuc.edu.dormitorydemo.R;

public class CreateUserDialog extends Dialog {

    /**
     * 上下文对象 *
     */
    Context context;

    private Button btn_save;

    public EditText text_tel;

    public EditText text_pwd;

    public EditText text_again;

    public TextView mine_modify_title;

    private View.OnClickListener mClickListener;

    public CreateUserDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CreateUserDialog(Context context, View.OnClickListener clickListener) {
        super(context);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.staff_dialog);

        mine_modify_title = (TextView) findViewById(R.id.mine_modify_title);
        text_tel = (EditText) findViewById(R.id.text_tel);
        text_pwd = (EditText) findViewById(R.id.text_password);
        text_again = (EditText) findViewById(R.id.text_again);

        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        // 根据id在布局中找到控件对象
        btn_save = (Button) findViewById(R.id.btn_save_pop);
        // 为按钮绑定点击事件监听器
        btn_save.setOnClickListener(mClickListener);

        this.setCancelable(true);
    }
}