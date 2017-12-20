package com.likeit.sous_vide.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.likeit.sous_vide.R;
import com.likeit.sous_vide.base.BaseActivity;
import com.likeit.sous_vide.listenter.OnLoginInforCompleted;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnLoginInforCompleted {
    @BindView(R.id.slider)
    SliderLayout mSliderLayout;
    //    @BindView(R.id.logo_title)
//    TextView tvLogoTitle;
    @BindView(R.id.ll_setTime)
    LinearLayout ll_setTime;
    @BindView(R.id.ll_settemp)
    LinearLayout ll_settemp;
    @BindView(R.id.iv_open_close)
    ImageView iv_open_close;
    @BindView(R.id.tv_time)
    TextView tv_time;
    int type = 0;//0; 代表关 1：代表开
    private SetTimeFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTitle("Recipes");
        imageSlider();
    }

    private void imageSlider() {
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Beef", R.drawable.beef_bg);
        file_maps.put("Beefs", R.drawable.beef_bg);
        file_maps.put("Beef", R.drawable.beef_bg);
//        for (int i = 0; i < file_maps.size(); i++) {
//            DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
//            // textSliderView.description("");//设置标题
//            defaultSliderView.image(file_maps.get(i));//设置图片的网络地址
//            //添加到布局中显示
//            sliderShow.addSlider(defaultSliderView);
//        }
        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            toActivity(FoodListActivity.class);
                        }
                    });
            mSliderLayout.addSlider(textSliderView);
        }
        //其他设置
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//使用默认指示器，在底部显示
        mSliderLayout.setDuration(5000);//停留时间
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);

    }

    @OnClick({R.id.ll_setTime, R.id.ll_settemp, R.id.iv_open_close})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_setTime:
                showTime();
                break;
            case R.id.ll_settemp:
                break;
            case R.id.iv_open_close:
                if (type == 0) {
                    iv_open_close.setImageResource(R.mipmap.icon_tab_open);
                    type = 1;
                } else if (type == 1) {
                    iv_open_close.setImageResource(R.mipmap.icon_tab_close);
                    type = 0;
                }
                break;
        }
    }

    private void showTime() {
        dialogFragment = new SetTimeFragment();
        dialogFragment.show(this.getSupportFragmentManager(), "android");
        dialogFragment.setOnLoginInforCompleted(this);
//        dialogFragment.setOnDialogListener(new SetTimeFragment.OnDialogListener() {
//            @Override
//            public void onDialogClick(int person) {
//                tv_time.setText(person);
//            }
//        });
    }


    @Override
    public void inputLoginInforCompleted(String time) {
        tv_time.setText(time + " '");
    }
}
