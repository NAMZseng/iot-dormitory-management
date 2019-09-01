package nuc.edu.dormitorydemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import nuc.edu.dormitorydemo.R;
import nuc.edu.dormitorydemo.domain.Staff;
import nuc.edu.dormitorydemo.utils.CacheUtils;
import nuc.edu.dormitorydemo.utils.Constants;
import nuc.edu.dormitorydemo.utils.LogUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText user_pwd;
    private EditText user_name;
    private Button login;
    private CheckBox cb_remember;
    public static final String url = Constants.NET_URL + "staff/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_pwd = (EditText) findViewById(R.id.user_pwd);
        user_name = (EditText) findViewById(R.id.user_name);

        cb_remember = (CheckBox) findViewById(R.id.cb_remember);
        login = (Button) findViewById(R.id.login);
//        if(CacheUtils.getRememeber(LoginActivity.this, "rem")){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkMsg(user_name.getText().toString().trim(), user_pwd.getText().toString().trim())){
                    sendRequest();
                }

            }
        });

    }

    private boolean checkMsg(String name, String pwd) {
        if(name.length() < 1 || pwd.length() < 1){
            Toast.makeText(this, "请输入正确的用户名和密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void sendRequest() {
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("tel", user_name.getText().toString());
        params.addQueryStringParameter("password", user_pwd.getText().toString());

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if(result.length() > 0){
                    Toast.makeText(x.app(), "登陆成功", Toast.LENGTH_SHORT).show();
                    Staff staff = parseJson(result);
                    CacheUtils.putUser(LoginActivity.this, staff);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "用户名或密码不正确", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(LoginActivity.this, cex.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 解析从服务器发回的json对象，其中存放有当前登录的用户信息
     * @param json
     * @return
     */
    private Staff parseJson(String json) {
        Staff staff = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            staff = new Staff();
            int num = jsonObject.optInt("num");
            staff.setNum(num);

            String name = jsonObject.optString("name");
            staff.setName(name);

            String tel = jsonObject.optString("tel");
            staff.setTel(tel);

            int buildingNum = jsonObject.optInt("buildingNum");
            staff.setBuildingNum(buildingNum);

            String title = jsonObject.optString("title");
            staff.setTitle(title);

            String password = jsonObject.optString("password");
            staff.setPassward(password);

            LogUtil.e(staff.getName()+" " + staff.getBuildingNum());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return staff;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}