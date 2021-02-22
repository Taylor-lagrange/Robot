package com.blueprint.robot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.blueprint.robot.data.ViewModel.ScenicSpotViewModel;
import com.blueprint.robot.data.entity.ScenicSpot;
import com.blueprint.robot.ui.carousel.CarouselFragment;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity<datetime> extends AppCompatActivity {
//    private static final int msgKey1 = 1;
//    private TextView mTime;

//    public static JniEAIBotUtil jniEAIBotUtil;
    private static final String TAG = "hefeng now";
    private static final int TIME_VALUE = 1000 * 60;//无操作跳转时间，单位毫秒
//    private static final int msgKey1 = 1;
//    private TextView datetime;
    private static final int CAROUSEL_MODE = 0x102;
    private boolean isCarousel = false;

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(CAROUSEL_MODE, TIME_VALUE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScenicSpotViewModel scenicSpotViewModel = new ViewModelProvider(this).get(ScenicSpotViewModel.class);//通过ScenicSpotViewModel对象获取所有景点信息
        List<ScenicSpot> scenicSpotList = scenicSpotViewModel.getScenicSpotList();
        if (scenicSpotList.isEmpty()) {
            //background.setImageBitmap(scenicSpotList.get(scenicSpotList.size()-1).getLocalBitmap(0));
            scenicSpotViewModel.insertScenicSpot(new ScenicSpot("神泉潮井", "每周一到周五上午10点，周六周天上午12点", "每周一到周五晚上10点，周六周天晚上10点", 3f, "核心景观潮井：涨潮时，水从泉眼汩汩而出，\n如银花怒放，似珍珠滚出，间有小鱼舞动，妙趣横生。\n一涨一落持续5—10分钟，最大流量100升/秒以上。\n一年四季每日不定时涨落，多时每日涨潮七、八次，少时二、三次，\n时间不定，无规律可循，周而复始，终年不息。", 10));
            scenicSpotViewModel.insertScenicSpot(new ScenicSpot("神泉驿站", "每周一到周五上午10点，周六周天上午12点", "每周一到周五晚上10点，周六周天晚上10点", 3f, "神泉驿站主要由潮井湿地公园、葫芦湖、潮井神泉、瀑布等景观组成，\n有亭台水榭、品茗茶室、特色商铺、餐饮等配套设施，集吃喝玩乐于一体。", 10));
            scenicSpotViewModel.insertScenicSpot(new ScenicSpot("粉黛草观赏区", "每周一到周五上午10点，周六周天上午12点", "每周一到周五晚上10点，周六周天晚上10点", 3f, "每年的8月下旬到11月中旬，是粉黛乱子草盛开的季节，\n花穗呈云雾状，远看如红色云雾，十分梦幻，简直是少女心爆棚的地方。", 10));
        }
        setContentView(R.layout.activity_main2);
        //初始化讯飞SDK，并且请求对应的语音操作权限
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + getString(R.string.app_id));
        requestPermissions();

//        //线程
//        mTime = (TextView) findViewById(R.id.currentTime);
//        new MainActivity.TimeThread().start();


       // datetime = findViewById(R.id.datetime);


        /*//天气
        HeConfig.init("HE2009292026121248", "fdc88faac80a4946b9a1b55ba6b4b56f");
        HeConfig.switchToDevService();

        new TimeThread().start();
        QWeather.getWeatherNow(MainActivity.this, "CN101010100", Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getWeather onError: " + e);
            }

            @Override
            public void onSuccess(WeatherNowBean weatherBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK.getCode().equalsIgnoreCase(weatherBean.getCode())) {
                    WeatherNowBean.NowBaseBean now = weatherBean.getNow();
                    TextView weather;
                    String weatherString = now.getTemp();
                    String temp = now.getTemp();
                    //weather = (TextView) findViewById(R.id.weather);
                    //weather.setText(weather + " " + temp + "%c");

                } else {
                    //在此查看返回数据失败的原因
                    String status = weatherBean.getCode();
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });

        //初始化 jniEAIBotUtil 对象
        jniEAIBotUtil = new JniEAIBotUtil(MainActivity.this);

        //初始化SDK，该函数为异步执行，在handle返回前所有对jniEAIBotUtil的操作都会导致程序崩溃
        jniEAIBotUtil.initEAISDKROS("192.168.31.200", 500, 0xFFC1E5FF, Color.WHITE, Color.BLACK, Color.YELLOW, handler, HandlerCode.INIT_EAISDK);
        */
    }
    /*private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle;
            int extend;
            switch (msg.what) {
                case HandlerCode.EAI_LOGIN:
                    bundle = msg.getData();
                    String name = bundle.getString("name");
                    String password = bundle.getString("password");
                    toastLast("登陆成功");
                    break;
                case HandlerCode.INIT_EAISDK:
                    toastLast("初始化SDK");
                    jniEAIBotUtil.eaiLogin("admin", "admin", handler, HandlerCode.EAI_LOGIN);
                    break;
                case HandlerCode.CLOSE_EAISDK:
                    extend = msg.getData().getInt("extend");
                    if (extend == 1)
                        jniEAIBotUtil.initEAISDKROS("192.168.31.200", 500, 0xFF9DC4DF, Color.WHITE, Color.BLACK, Color.YELLOW, handler, HandlerCode.INIT_EAISDK);
                    else if (extend == 0)
                        MainActivity.this.finish();
                    break;
                default:
                    break;
            }
            return false;
        }
    });*/

    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class MainHandler extends Handler {
        private final WeakReference<MainActivity> mainActivityWeakReference;

        private MainHandler(MainActivity mainActivity) {
            super(Looper.getMainLooper());
            mainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null) {
                switch (msg.what) {
                    case CAROUSEL_MODE://跳转至巡航轮播
                        mainActivity.setCarousel(true);
//                        Log.d(TAG, "handleMessage: commit");

                        NavController navController = Navigation
                                .findNavController(mainActivity, R.id.fragment);
                        navController.navigate(R.id.carouselFragment);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private MainHandler mHandler = new MainHandler(this);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent: click" + isCarousel);
        if (!isCarousel) {//当前不在巡航轮播界面时
            if (mHandler.hasMessages(CAROUSEL_MODE)) {
                mHandler.removeMessages(CAROUSEL_MODE);
            }
            Log.d(TAG, "dispatchTouchEvent: send");
            mHandler.sendMessageDelayed(mHandler.obtainMessage(CAROUSEL_MODE), TIME_VALUE);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler.hasMessages(CAROUSEL_MODE)) {
            mHandler.removeMessages(CAROUSEL_MODE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    public void setCarousel(boolean carousel) {
        isCarousel = carousel;
    }

    public void reInitTime() {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(CAROUSEL_MODE), TIME_VALUE);
    }
}