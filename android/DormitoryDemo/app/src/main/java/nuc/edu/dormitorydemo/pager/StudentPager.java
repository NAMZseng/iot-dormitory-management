package nuc.edu.dormitorydemo.pager;

/**
 * 学生信息界面
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


import nuc.edu.dormitorydemo.R;
import nuc.edu.dormitorydemo.adapter.StudentAdapter;
import nuc.edu.dormitorydemo.base.BasePager;
import nuc.edu.dormitorydemo.domain.StudentInfo;
import nuc.edu.dormitorydemo.utils.CacheUtils;
import nuc.edu.dormitorydemo.utils.Constants;
import nuc.edu.dormitorydemo.utils.LogUtil;

public class StudentPager extends BasePager {
    public StudentPager(Context context) {
        super(context);

    }

    private List<StudentInfo> studentInfoList = null;

    @ViewInject(R.id.student_recylerview)
    private RecyclerView recyclerView;

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.student_pager, null);
        x.view().inject(this, view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,  StaggeredGridLayoutManager.VERTICAL));

        return view;
    }

    @Override
    public void InitData() {
        super.InitData();
        initList();
    }

    private void initList(){
        RequestParams params = new RequestParams(Constants.NET_URL + "student/getStudentsInfo");
        params.addQueryStringParameter("buildingNum", CacheUtils.getUser(context).getBuildingNum()+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result = " + result);
                processData(result);
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

    private void processData(String result) {
        studentInfoList = parseJson(result);
        StudentAdapter adapter = new StudentAdapter(studentInfoList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 向服务器请求学生信息数据
     * @param json
     * @return 学生信息列表
     */
    private ArrayList<StudentInfo> parseJson(String json) {
        ArrayList<StudentInfo> studentInfos = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            if(array != null && array.length() > 0){
                for(int i = 0;i < array.length();i++){
                    JSONObject jsonObject = (JSONObject) array.get(i);
                    if(jsonObject != null){
                        StudentInfo studentInfo = new StudentInfo();

                        int num = jsonObject.optInt("num");
                        studentInfo.setNum(num);

                        String name = jsonObject.optString("name");
                        studentInfo.setName(name);

                        String school = jsonObject.optString("school");
                        studentInfo.setSchool(school);

                        String major = jsonObject.optString("major");
                        studentInfo.setMajor(major);

                        String teacherName = jsonObject.optString("teacherName");
                        studentInfo.setTeacherName(teacherName);

                        String teacherTel = jsonObject.optString("teacherTel");
                        studentInfo.setTeacherTel(teacherTel);

                        int buildingNum = jsonObject.optInt("buildingNum");
                        studentInfo.setBuildingNum(buildingNum);

                        int roomNum = jsonObject.optInt("roomNum");
                        studentInfo.setRoomNum(roomNum);

                        studentInfos.add(studentInfo);

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return studentInfos;

    }

}
