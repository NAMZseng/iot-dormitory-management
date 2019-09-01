package nuc.edu.dormitorydemo.base;

import android.content.Context;
import android.view.View;

public abstract class BasePager {
    public final Context context;
    public View rootView = null;
    public boolean isInitData;

    public BasePager(Context context){
        this.context = context;
        rootView = initView();
    }


    /**
     * 强制由孩子实现特定的效果
     * @return
     */
    public abstract View initView();

    /**
     *当子页面需要初始化数据，联网请求数据，或者绑定数据的时候要重新该方法；
     */
    public void InitData(){

    };
    public View getRootView(){
        return rootView;
    }
}
