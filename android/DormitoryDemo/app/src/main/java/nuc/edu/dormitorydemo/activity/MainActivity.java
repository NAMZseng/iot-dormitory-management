package nuc.edu.dormitorydemo.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import nuc.edu.dormitorydemo.R;
import nuc.edu.dormitorydemo.base.BasePager;
import nuc.edu.dormitorydemo.fragment.ReplaceFragment;
import nuc.edu.dormitorydemo.pager.EnvironmentPager;
import nuc.edu.dormitorydemo.pager.IOPager;
import nuc.edu.dormitorydemo.pager.MinePager;
import nuc.edu.dormitorydemo.pager.StudentPager;

public class MainActivity extends FragmentActivity {

    private RadioGroup rg_bottom_tag;
    private ViewPager vp_main_content;
    private ArrayList<BasePager> basePagers;
    private List<Fragment> fragments;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        vp_main_content = (ViewPager) findViewById(R.id.vp_main_content);
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);

        basePagers = new ArrayList<>();
        basePagers.add(new StudentPager(this));//添加学生信息界面 0
        basePagers.add(new IOPager(this));//添加出入界面 1
        basePagers.add(new EnvironmentPager(this));//添加环境界面 2
        basePagers.add(new MinePager(this));//添加我的界面 3

        fragments = new ArrayList<>();
        fragments.add(new ReplaceFragment(getBasePager(0)));
        fragments.add(new ReplaceFragment(getBasePager(1)));
        fragments.add(new ReplaceFragment(getBasePager(2)));
        fragments.add(new ReplaceFragment(getBasePager(3)));

        vp_main_content.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        //设置RadioGroup的监听
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置viewpager的监听
        vp_main_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            public void onPageSelected(int position) {

                //根据当前展示的ViewPager页面，使RadioGroup对应的按钮被选中
                switch (position) {
                    case 0:
                        rg_bottom_tag.check(R.id.rb_video);
                        break;
                    case 1:
                        rg_bottom_tag.check(R.id.rb_audio);
                        break;
                    case 2:
                        rg_bottom_tag.check(R.id.rb_download);
                        break;
                    case 3:
                        rg_bottom_tag.check(R.id.rb_mine);
                        break;
                    default:
                        break;

                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        rg_bottom_tag.check(R.id.rb_video);//默认选中学生信息
        vp_main_content.setCurrentItem(0, true);

    }

    /**
     * RadioGroup的监听
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                default:
                    position = 0;
                    break;
                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_download:
                    position = 2;
                    break;
                case R.id.rb_mine:
                    position = 3;
                    break;
            }
            vp_main_content.setCurrentItem(position);
        }
    }

    /**
     * viewpager适配器填充
     */
    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //返回每个position对应的Fragment对象
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        //返回fragments的长度，也就是Fragment对象的个数
        @Override
        public int getCount() {
            return fragments.size();
        }



    }

    /**
     * 根据位置得到对应的界面
     * @return
     */
    private BasePager getBasePager(int position) {

        BasePager basePager = basePagers.get(position);

        if(basePager != null && !basePager.isInitData){
            basePager.InitData();//联网请求或者绑定数据
            basePager.isInitData = true;
        }

        return basePager;
    }

    @Override
    public void finish() {
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        viewGroup.removeAllViews();
        super.finish();
    }
}
