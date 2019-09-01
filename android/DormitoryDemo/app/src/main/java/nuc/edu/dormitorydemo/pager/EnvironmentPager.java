package nuc.edu.dormitorydemo.pager;

/**
 * 环境页面
 */

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import nuc.edu.dormitorydemo.R;
import nuc.edu.dormitorydemo.base.BasePager;
import nuc.edu.dormitorydemo.domain.HumitureInfo;

import nuc.edu.dormitorydemo.utils.CacheUtils;
import nuc.edu.dormitorydemo.utils.Constants;
import nuc.edu.dormitorydemo.utils.DynamicLineChartManager;
import nuc.edu.dormitorydemo.utils.LogUtil;

public class EnvironmentPager extends BasePager {
    public EnvironmentPager(Context context) {
        super(context);
    }

    @ViewInject(R.id.environment_reflash)
    private Button environment_reflash;
    private DynamicLineChartManager dynamicLineChartManager;
    private List<HumitureInfo> list; //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合

    @ViewInject(R.id.dynamic_chart1)
    LineChart lineChart;

    /**
     * 初始化视图
     * @return
     */
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.environment_pager, null);
        x.view().inject(this, view);
        environment_reflash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromNet();
//                lineChart.notifyDataSetChanged();
            }
        });
        return view;
    }


    /**
     * 初始化数据
     */
    @Override
    public void InitData() {
        super.InitData();
        names.add("温度");
        names.add("湿度");
        colour.add(Color.CYAN);
        colour.add(Color.GREEN);
        dynamicLineChartManager = new DynamicLineChartManager(lineChart, names, colour);
        dynamicLineChartManager.setYAxis(100, 0, 10);
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NET_URL + "humiture/getTodayData");
        params.addQueryStringParameter("buildingNum", CacheUtils.getUser(context).getBuildingNum() + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result = " + result);
                parseJson(result);
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


    private void parseJson(String json) {
        list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            if(array != null && array.length() > 0){
                for(int i = 0;i < array.length();i++){
                    JSONObject jsonObject = (JSONObject) array.get(i);
                    if(jsonObject != null){
                        HumitureInfo humitureInfo = new HumitureInfo();

                        String macAddress = jsonObject.optString("macAddress");
                        humitureInfo.setMacAddress(macAddress);

                        int buildingNum = jsonObject.optInt("buildingNum");
                        humitureInfo.setBuildingNum(buildingNum);

                        String location = jsonObject.optString("location");
                        humitureInfo.setLocation(location);

                        long collectTime = jsonObject.optLong("collectTime");
                        humitureInfo.setCollectTime(collectTime);

                        Double temperature = jsonObject.getDouble("temperature");
                        humitureInfo.setTemperature(temperature);

                        Double humidity = jsonObject.getDouble("humidity");
                        humitureInfo.setHumidity(humidity);

                        list.add(humitureInfo);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(list);

        for(HumitureInfo humitureInfo : list){
            dynamicLineChartManager.addEntry(humitureInfo);
        }
    }
}
