package nuc.edu.dormitorydemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import nuc.edu.dormitorydemo.domain.Staff;


/**
 * 作用：缓存工具类
 */
public class CacheUtils {


    /**
     * 存放用户信息
     * @param context 上下文
     */
    public static void putUser(Context context, Staff staff){
        SharedPreferences sharedPreferences = context.getSharedPreferences("staff",Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("num", staff.getNum()).commit();
        sharedPreferences.edit().putString("name", staff.getName()).commit();
        sharedPreferences.edit().putString("tel", staff.getTel()).commit();
        sharedPreferences.edit().putInt("buildingNum", staff.getBuildingNum()).commit();
        sharedPreferences.edit().putString("title", staff.getTitle()).commit();
        sharedPreferences.edit().putString("password", staff.getPassward()).commit();
    }

    public static Staff getUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("staff",Context.MODE_PRIVATE);
        Staff staff = new Staff();
        staff.setNum(sharedPreferences.getInt("num", 1));
        staff.setName(sharedPreferences.getString("name", ""));
        staff.setTel(sharedPreferences.getString("tel", ""));
        staff.setBuildingNum(sharedPreferences.getInt("buildingNum", 1));
        staff.setTitle(sharedPreferences.getString("title", ""));
        staff.setPassward(sharedPreferences.getString("password", ""));
        return staff;
    }


    /**
     * 获取用户名
     * @param context
     * @param key
     * @return
     */
    public static String getUserName(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "defaultName");
    }

    public static Boolean getRememeber(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * 获取用户密码
     * @param context 上下文
     * @param key 在sharedPreferences中放入的键值
     * @return 用户密码
     */
    public static String getUserPwd(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "defaultPwd");
    }


    /**
     * 保存数据
     * @param context
     * @param key
     * @param values
     */
    public static  void putString(Context context,String key,String values){
        SharedPreferences sharedPreferences = context.getSharedPreferences("mobileplayer",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,values).commit();
    }

    /**
     * 得到缓存的数据
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("mobileplayer",Context.MODE_PRIVATE);
        return  sharedPreferences.getString(key,"");
    }
}
