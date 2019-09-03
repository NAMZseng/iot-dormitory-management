package nuc.edu.dormitorydemo.pager;

/**
 * 出入管理界面
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nuc.edu.dormitorydemo.R;
import nuc.edu.dormitorydemo.adapter.IOAdapter;
import nuc.edu.dormitorydemo.adapter.IONotBackAdapter;
import nuc.edu.dormitorydemo.base.BasePager;
import nuc.edu.dormitorydemo.domain.BlockInfo;
import nuc.edu.dormitorydemo.domain.Staff;
import nuc.edu.dormitorydemo.domain.StudentInfo;
import nuc.edu.dormitorydemo.utils.CacheUtils;
import nuc.edu.dormitorydemo.utils.Constants;
import nuc.edu.dormitorydemo.utils.LogUtil;
import nuc.edu.dormitorydemo.view.ShowStudentDialog;


public class IOPager extends BasePager {

    @ViewInject(R.id.io_barchart)
    private BarChart io_barchart;

    @ViewInject(R.id.io_recycleview)
    private RecyclerView recyclerView;

    @ViewInject(R.id.io_tv_title)
    private TextView io_tv_title;

    @ViewInject(R.id.io_btn_refresh)
    private Button io_btn_refresh;

    @ViewInject(R.id.io_rg_state)
    private RadioGroup io_rg_state;

    @ViewInject(R.id.io_rb_normal)
    private RadioButton io_rb_normal;

    @ViewInject(R.id.io_rb_unnormal)
    private RadioButton io_rb_unnormal;

    @ViewInject(R.id.io_rb_notback)
    private RadioButton io_rb_notback;

    @ViewInject(R.id.io_ll_title)
    private LinearLayout io_ll_title;

    @ViewInject(R.id.notback_lv)
    private ListView notback_lv;

    private ArrayList<Integer> list;

    private ArrayList<StudentInfo> studentInfos;

    private ShowStudentDialog dialog;

    private static final String NET_URL = Constants.NET_URL + "access/";
    private IONotBackAdapter adapter;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public IOPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.io_pager, null);
        x.view().inject(this, view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        /**
         * 刷新按钮的点击事件
         */
        io_btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(io_rb_normal.isChecked()){
                    getNormalDataFromNet();
                    io_barchart.invalidate();
                }
                else if(io_rb_unnormal.isChecked()){
                    getUnnormalData();
                }else {

                }

            }
        });

        /**
         * radio group的点击事件
         */
        io_rg_state.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.io_rb_normal:
                        io_barchart.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        io_ll_title.setVisibility(View.GONE);
                        notback_lv.setVisibility(View.GONE);
                        io_btn_refresh.setVisibility(View.VISIBLE);
                        io_tv_title.setText("正常出入情况");
                        getNormalDataFromNet();
                        break;
                    case R.id.io_rb_unnormal:
                        io_ll_title.setVisibility(View.VISIBLE);
                        io_barchart.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        notback_lv.setVisibility(View.GONE);
                        io_btn_refresh.setVisibility(View.VISIBLE);
                        io_tv_title.setText("异常出入情况");
                        getUnnormalData();
                        break;
                    case R.id.io_rb_notback:
                        io_barchart.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        io_ll_title.setVisibility(View.GONE);
                        io_tv_title.setText("未归寝人员  （点击学生查看详情）");
                        notback_lv.setVisibility(View.VISIBLE);
                        io_btn_refresh.setVisibility(View.GONE);
                        getNotBackData();
                        break;
                }
            }
        });
        io_rg_state.check(R.id.io_rb_normal);
        return view;
    }

    /**
     * 获取未归寝人员数据
     */
    private void getNotBackData() {
        RequestParams params = new RequestParams(NET_URL + "getOutStudentInfo");
        params.addQueryStringParameter("buildingNum", CacheUtils.getUser(context).getBuildingNum()+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("not back result = " + result);
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


    private void showStudentDialog(){
        dialog = new ShowStudentDialog(context);
        dialog.show();
    }

    /**
     * 处理未归寝人员数据，并为列表设置点击
     * @param result
     */
    private void processData(String result) {
        studentInfos = new ArrayList<>();
        studentInfos = parseNotBackJson(result);
        adapter = new IONotBackAdapter(context, studentInfos);

        notback_lv.setAdapter(adapter);
        notback_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showStudentDialog();
                StudentInfo studentInfo = studentInfos.get(position);
                dialog.dialog_tv_name.setText(studentInfo.getName());
                dialog.dialog_tv_num.setText(studentInfo.getNum()+"");
                dialog.dialog_tv_teaname.setText(studentInfo.getTeacherName()+"");
                dialog.dialog_tv_tectel.setText(studentInfo.getTeacherTel()+"");
                dialog.dialog_tv_buildingnum.setText(studentInfo.getBuildingNum()+"");
                dialog.dialog_tv_school.setText(studentInfo.getSchool()+"");
                dialog.dialog_tv_roomnum.setText(studentInfo.getRoomNum()+"");
                dialog.dialog_tv_major.setText(studentInfo.getMajor()+"");
            }
        });

    }

    /**
     * 解析未归寝人员数据
     * @param json 服务器返回的数据
     * @return 数据解析完成后的列表
     */
    private ArrayList<StudentInfo> parseNotBackJson(String json) {
        ArrayList<StudentInfo> studentInfos = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            if(array != null && array.length() > 0)
                for(int i = 0;i < array.length();i++) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return studentInfos;
    }

    @Override
    public void InitData() {
        super.InitData();
        list = new ArrayList<>();
    }

    /**
     * 获取正常刷卡数据
     */
    private void getNormalDataFromNet() {
        RequestParams params = new RequestParams(NET_URL + "getTodayInOutSum");
        params.addQueryStringParameter("buildingNum", CacheUtils.getUser(context).getBuildingNum()+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result = " + result);
                parseJson(result);
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

    /**
     * 获取非法闯入数据
     */
    private void getUnnormalData() {
        RequestParams params1 = new RequestParams(NET_URL + "getTodayBlockInfo");
        params1.addQueryStringParameter("buildingNum", CacheUtils.getUser(context).getBuildingNum()+"");
        x.http().get(params1, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("blockResult = " + result);
                parseBlockJson(result);
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

    /**
     * 解析非法闯入数据
     * @param json
     */
    private void parseBlockJson(String json){
        ArrayList<BlockInfo> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            if(array != null && array.length() > 0){
                for(int i = 0;i < array.length();i++){
                    JSONObject jsonObject = (JSONObject) array.get(i);
                    if(jsonObject != null){
                        BlockInfo blockInfo = new BlockInfo();

                        String name = jsonObject.optString("studentName");
                        blockInfo.setStudentName(name);

                        int num = jsonObject.optInt("num");
                        blockInfo.setNum(num);

                        String teacherTel = jsonObject.optString("teacherTel");
                        blockInfo.setTeacherTel(teacherTel);

                        Long accessTime = jsonObject.optLong("accessTime");
                        Date date = new Date(accessTime);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        blockInfo.setAccessTime(sdf.format(date));

                        int buildingNum = jsonObject.optInt("buildingNum");
                        blockInfo.setBuildingNum(buildingNum);

                        list.add(blockInfo);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IOAdapter adapter = new IOAdapter(list);
        recyclerView.setAdapter(adapter); //适配器

    }

    /**
     * 解析服务器返回的数据
     * @param json
     */
    private void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int in = jsonObject.optInt("in");
            int out = jsonObject.optInt("out");
            list.add(in);
            list.add(out);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        io_barchart.setClickable(false);
        setDescription("出入情况");
        setLegend();
        setYAxis();
        setXAxis();
        setChartData();

    }

    /**
     * 设置描述
     * @param descriptionStr 描述的内容
     */
    private void setDescription(String descriptionStr) {
        Description description = new Description();
        description.setText(descriptionStr);
        description.setTextSize(18f);
        description.setTextAlign(Paint.Align.CENTER); // 文本居中对齐
        // 计算描述位置
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        Paint paint = new Paint();
        paint.setTextSize(18f);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        float x = outMetrics.widthPixels / 2;
        float y =  Utils.calcTextHeight(paint, descriptionStr) + Utils.convertDpToPixel(24);
        description.setPosition(x, y);
        io_barchart.setDescription(description);

    }

    /**
     * 图例
     */
    private void setLegend() {
        Legend legend = io_barchart.getLegend();
        legend.setTextSize(14f);
        legend.setXOffset(24f);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // 图例在水平线上向右对齐
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // 图例在垂直线上向上对齐
        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // 图例条目垂直方向排列
        legend.setDrawInside(true); // 绘制在图表内部
    }

    private void setYAxis() {
        // 左侧Y轴
        YAxis axisLeft = io_barchart.getAxisLeft();
        axisLeft.setAxisMinimum(0); // 最小值为0
        int max = 0;
        for(int i:list){
            if(i > max){
                max = i;
            }
        }
        axisLeft.setAxisMaximum(max + 10); // 最大值为100
        axisLeft.setValueFormatter(new IAxisValueFormatter() { // 自定义值的格式
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)value + "";
            }
        });
        // 右侧Y轴
        io_barchart.getAxisRight().setEnabled(false); // 不启用
    }

    private void setXAxis() {
        // X轴
        XAxis xAxis = io_barchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 位于底部
        xAxis.setDrawGridLines(false); // 不绘制X轴网格线
        xAxis.setAxisMinimum(-0.3f); // 最小值-0.3f，为了使左侧留出点空间
        xAxis.setGranularity(1f); // 间隔尺寸1
        xAxis.setTextSize(14f); // 文本大小14


        xAxis.setValueFormatter(new IAxisValueFormatter() { // 自定义值的格式
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value == 0f){
                    return "入";
                }else{
                    return "出";
                }
            }
        });
    }

    /**
     * 设置柱状图数据
     */
    private void setChartData() {
        if(list != null && list.size() > 0){
            final List<BarEntry> yVals1 = new ArrayList<>();
            yVals1.add(new BarEntry(0f, list.get(0)));
            yVals1.add(new BarEntry(1f, list.get(1)));

            BarDataSet barDataSet1 = new BarDataSet(yVals1, "出入情况");
            barDataSet1.setValueTextColor(Color.RED);
            barDataSet1.setColor(Color.GREEN);
            barDataSet1.setValueTextSize(14f);
            barDataSet1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return entry.getY()+"";
                }
            });


            BarData bardata = new BarData(barDataSet1);
            bardata.setBarWidth(0.4f);

            io_barchart.setData(bardata);
        }

    }

//     TimePickerDialog timePickerDialog =  new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener() {
//
//         Calendar calendar=Calendar.getInstance();
//         int hour=calendar.get(Calendar.HOUR_OF_DAY);
//         int minute=calendar.get(Calendar.MINUTE);
//
//        @Override
//        public void onTimeSet(TimePicker view, int hourOfDay, int m) {
//            hour = hourOfDay;
//            minute = m;
//            if (minute < 10){
//                endTimeTv.setText(hour+":"+"0"+minute);
//            }else {
//                endTimeTv.setText(hour+":"+minute);
//            }
//        }
//    }, 0, 0, true).show();

}
