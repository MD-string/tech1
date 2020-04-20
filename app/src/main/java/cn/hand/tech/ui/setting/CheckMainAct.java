package cn.hand.tech.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.BaseActivity;
import cn.hand.tech.ui.weight.adapter.WFragmentAdapter;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.weiget.NavigationConfig;

/*
 *通道选择
 */
@NavigationConfig(isShow = false, titleId = R.string.main_title, showLeft = false)
public class CheckMainAct extends BaseActivity {
    private Context context;
    private LinearLayout ll_tab_1,ll_tab_2;
    private TextView tv_tab_1,tv_tab_2;
    private TextView mTabLineIv;
    private ViewPager mPageVp;
    private int tabCount = 2;
    private int screenWidth,leftStartMargin;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private WFragmentAdapter mFragmentAdapter;
    private int currentIndex;
    private FragmentManager mFragmentManager;
    private LinearLayout ll_back;
    private String mtag;
    private TextView tv_next_check;


    public static void start(Context context) {
        Intent intent = new Intent(context, CheckMainAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_weight_trend);
        mtag=getIntent().getStringExtra("start_tag");
        initView();
        initTabLineWidth();
        init();
    }
    private void initView() {
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_next_check=(TextView)findViewById(R.id.tv_next_check);//重新检测
        tv_next_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mIndex=mPageVp.getCurrentItem();
                Intent i=new Intent(BleConstant.ACTION_RESTART_AUTO_CHECK);
                i.putExtra("page_tag",mIndex+"");
                sendBroadcast(i);

            }
        });

        ll_tab_1=(LinearLayout)findViewById(R.id.ll_tab_1);//通道选择
        tv_tab_1=(TextView)findViewById(R.id.tv_tab_1);
        tv_tab_1.setText("主机检测");
        ll_tab_2=(LinearLayout)findViewById(R.id.ll_tab_2);//通道阀值
        tv_tab_2=(TextView)findViewById(R.id.tv_tab_2);
        tv_tab_2.setText("进厂检测");
        mTabLineIv=(TextView)findViewById(R.id.id_tab_line_iv);//引导线

        mPageVp=(ViewPager)findViewById(R.id.id_page_vp);
        ll_tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageVp.setCurrentItem(0,true);
            }
        });
        ll_tab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageVp.setCurrentItem(1,true);
            }
        });
    }


    private void init() {
        BaseCheckFragment baseCheck=new BaseCheckFragment();

        EnterFrgoryCheckFragment enterFragory=new EnterFrgoryCheckFragment();
        mFragmentList.add(baseCheck);
        mFragmentList.add(enterFragory);

        mFragmentManager =getSupportFragmentManager();

        mFragmentAdapter = new WFragmentAdapter(
                mFragmentManager, mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();

                DLog.d("offset1:", offset+ "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记x个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */

                int tabWidth = (int)((screenWidth *1.0 -2* CommonUtils.dip2px(context, 20))/ tabCount);
                //
                int leftMarginToLeft = (int) (offset * tabWidth + currentIndex * tabWidth + ((tabWidth - leftStartMargin+CommonUtils.dip2px(context, 42)) / 2));
                int leftMarginToRight = (int) (-(1 - offset) * tabWidth + currentIndex * tabWidth + ((tabWidth - leftStartMargin+CommonUtils.dip2px(context, 42)) / 2));
                //				int leftMarginToLeft  =(int) (offset * (screenWidth * 1.0 / 3) + currentIndex  * (screenWidth / 3)+CommonUtils.dip2px(mactivity, 60)+(tabWidth-leftStartMargin)/2);
                //				int leftMarginToRight= (int) (-(1 - offset)  * (screenWidth * 1.0 /3) + currentIndex   * (screenWidth / 3)+(tabWidth-leftStartMargin)/2+CommonUtils.dip2px(mactivity, 60));

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = leftMarginToLeft;

                } else if (currentIndex == 1 && position == 0) // 1->0
                {

                    lp.leftMargin = leftMarginToRight;

                }
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                switchBorderTab(getClass(),position);
                resetTextView() ;
                switch (position) {
                    case 0:
                        tv_tab_1.setTextColor(getResources().getColor(R.color.hand_blue));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.hand_txt));
                        break;
                    case 1:
                        tv_tab_1.setTextColor(getResources().getColor(R.color.hand_txt));
                        tv_tab_2.setTextColor(getResources().getColor(R.color.hand_blue));
                        break;

                }
                currentIndex = position;
            }
        });

        mPageVp.setCurrentItem(0);
    }

    /**
     * 设置滑动条的宽度为屏幕的1/x(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        leftStartMargin = (int)((screenWidth *1.0 -2* CommonUtils.dip2px(context, 24))/ tabCount);
        lp.width = leftStartMargin;
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        tv_tab_1.setTextColor(getResources().getColor(R.color.hand_txt));
        tv_tab_2.setTextColor(getResources().getColor(R.color.hand_txt));
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }

}
