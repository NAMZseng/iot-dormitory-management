package nuc.edu.dormitorydemo.pager;

/**
 * 个人信息界面
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import nuc.edu.dormitorydemo.R;
import nuc.edu.dormitorydemo.activity.LoginActivity;
import nuc.edu.dormitorydemo.base.BasePager;
import nuc.edu.dormitorydemo.domain.Staff;
import nuc.edu.dormitorydemo.utils.CacheUtils;
import nuc.edu.dormitorydemo.utils.Constants;
import nuc.edu.dormitorydemo.view.CreateUserDialog;

public class MinePager extends BasePager {

    private Staff staff = null;

    @ViewInject(R.id.mine_btn_tel)
    private Button tel;

    @ViewInject(R.id.mine_tv_name)
    private TextView mine_tv_name;



    @ViewInject(R.id.mine_tv_tel)
    private TextView mine_tv_tel;

    @ViewInject(R.id.mine_tv_buildingNum)
    private TextView mine_tv_buildingNum;

    @ViewInject(R.id.mine_btn_exit)
    private Button mine_btn_exit;

    @ViewInject(R.id.mine_tv_title)
    private TextView mine_tv_title;

    @ViewInject(R.id.mine_btn_pwd)
    private Button pwd;

    private CreateUserDialog createUserDialog;

    private boolean flag = false; //true表示修改密码，false表示修改手机号

    private String Modify_Url = Constants.NET_URL + "staff/";
    public MinePager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.mine_pager, null);
        x.view().inject(this, view);

        tel.setOnClickListener(new MyOnClickListener());
        pwd.setOnClickListener(new MyOnClickListener());
        mine_btn_exit.setOnClickListener(new MyOnClickListener());
        return view;
    }

    @Override
    public void InitData() {
        super.InitData();
        mine_tv_name.setText(CacheUtils.getUser(context).getName());
        mine_tv_tel.setText(CacheUtils.getUser(context).getTel());
        mine_tv_title.setText(CacheUtils.getUser(context).getTitle());
        mine_tv_buildingNum.setText(CacheUtils.getUser(context).getBuildingNum()+"号楼");
    }

    public void showEditDialog(View view) {
        createUserDialog = new CreateUserDialog(context, new MyOnClickListener());
        createUserDialog.show();
    }

    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.mine_btn_pwd:
                    flag = true;
                    showEditDialog(v);
                    createUserDialog.mine_modify_title.setText("密码修改");
                    createUserDialog.text_tel.setHint("请输入原密码");
                    createUserDialog.text_tel.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    createUserDialog.text_pwd.setHint("请输入新密码");
                    createUserDialog.text_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    createUserDialog.text_again.setHint("再次输入新密码");
                    createUserDialog.text_again.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    createUserDialog.text_again.setVisibility(View.VISIBLE);
                    break;
                case R.id.mine_btn_tel:
                    flag = false;
                    showEditDialog(v);
                    createUserDialog.mine_modify_title.setText("手机号修改");
                    createUserDialog.text_tel.setHint("请输入密码");
                    createUserDialog.text_tel.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    createUserDialog.text_pwd.setHint("请输入新手机号");
                    createUserDialog.text_pwd.setInputType(InputType.TYPE_CLASS_TEXT);
                    createUserDialog.text_again.setVisibility(View.GONE);
                    break;
                case R.id.btn_save_pop:
                    if(!flag){
                        if(createUserDialog.text_tel.getText().toString().length() < 1||createUserDialog.text_pwd.getText().toString().length() < 1
                                ){
                            Toast.makeText(context,"输入不可为空！", Toast.LENGTH_SHORT).show();
                        }else{
                            String oldTel = CacheUtils.getUser(context).getTel();
                            String newTel = createUserDialog.text_pwd.getText().toString();
                            String password = createUserDialog.text_tel.getText().toString();
                            RequestParams params = new RequestParams(Modify_Url + "updateTel");
                            params.addQueryStringParameter("oldTel",oldTel);
                            params.addQueryStringParameter("password",password);
                            params.addQueryStringParameter("newTel",newTel);
                            x.http().post(params, new Callback.CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    Toast.makeText(x.app(), result, Toast.LENGTH_SHORT).show();
                                    if(result.equals("0")){
                                        Toast.makeText(context, "要修改的手机号已存在！", Toast.LENGTH_SHORT).show();
                                    }else if(result.equals("1")){
                                        Toast.makeText(context, "修改成功，请重新登录！", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                        this.onFinished();
                                    }else{
                                        Toast.makeText(context, "手机号或密码错误！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onError(Throwable ex, boolean isOnCallback) {
                                    Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(CancelledException cex) {
                                    Toast.makeText(context, cex.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFinished() {

                                }
                            });
                        }

                    }else{
                        String tel = CacheUtils.getUser(context).getTel();
                        String oldPassword = createUserDialog.text_tel.getText().toString();
                        String newPassword = createUserDialog.text_pwd.getText().toString();
                        String newPasswordAgain = createUserDialog.text_again.getText().toString();
                        if(oldPassword.length() < 1||newPassword.length() < 1 || newPasswordAgain.length() < 1){
                            Toast.makeText(context, "输入不可为空", Toast.LENGTH_SHORT).show();
                        }
                        else if(!newPasswordAgain.equals(newPassword)){
                            Toast.makeText(context, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                        }else{
                            RequestParams params = new RequestParams(Modify_Url + "updatePassword");
                            params.addQueryStringParameter("tel",tel);
                            params.addQueryStringParameter("oldPassword",oldPassword);
                            params.addQueryStringParameter("newPassword",newPassword);
                            x.http().post(params, new Callback.CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    if(result.equals("1")){
                                        Toast.makeText(x.app(), "修改成功，请重新登录！", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                        this.onFinished();
                                    }else if(result.equals("-1")){
                                        Toast.makeText(x.app(), "密码不正确！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onError(Throwable ex, boolean isOnCallback) {
                                    Toast.makeText(x.app(), ex.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(CancelledException cex) {
                                    Toast.makeText(x.app(), cex.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFinished() {

                                }
                            });
                        }

                    }
                    break;
                case R.id.mine_btn_exit:
                    ((Activity)context).finish();
	    break;
                    default:
                        break;
            }
        }
    }

}
