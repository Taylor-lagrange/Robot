package com.blueprint.robot.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint.robot.CH34xUARTDriverHelper;
import com.blueprint.robot.R;
import com.blueprint.robot.util.HttpUtil;
import com.blueprint.robot.util.JsonParser;
import com.blueprint.robot.util.MyRecognizerListener;
import com.blueprint.robot.util.MySynthesizerListener;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE;
import com.iflytek.cloud.util.ResourceUtil;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SidebarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SidebarFragment extends Fragment {

    private static final int msgKey1 = 1;
    private TextView mTime;

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 识别结果内容
    private String recoString;
    //用于显示对话信息
    private TextView communicationInfo;
    //用于在屏幕上提示用户程序状态
    private Toast toast;
    //本地发声人
    public static String voicerXtts = "xiaoyan";

    public Handler handler = null;
    // 在用户和机器对话的时候需要屏蔽唤醒信号，避免打乱用户语音交互时的状态机状态
    public boolean useWakeup = true;
    // 用于 destroy 的时候终止线程执行
    public boolean threadTerminate = false;
    public Thread thread;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SidebarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SidebarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SidebarFragment newInstance(String param1, String param2) {
        SidebarFragment fragment = new SidebarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        initWakeUpDevice();
        setVoiceInteraction();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mTime = (TextView) getView().findViewById(R.id.currentTime_Sidebar);
        communicationInfo = getView().findViewById(R.id.communicationInfo);

        //线程
        //new SidebarFragment.TimeThread().start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sidebar, container, false);
    }

    //添加线程实时显示时间
//    public class TimeThread extends Thread {
//        @Override
//        public void run() {
//            do {
//                try {
//                    Thread.sleep(1000);
//                    Message msg = new Message();
//                    msg.what = msgKey1;
//                    mHandler.sendMessage(msg);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } while (true);
//        }
//    }

//    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case msgKey1:
//                    mTime.setText(getTime());
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    @SuppressLint("HandlerLeak")
    public void initWakeUpDevice() {
        threadTerminate = false;
        handler = new Handler() {
            public void handleMessage(Message msg) {
                mTts.startSpeaking("您好，请问有什么能够帮您的吗？", mTtsListener);
                communicationInfo.setText("您好");
            }
        };
        thread = new listenThread();
        thread.start();//开启读线程读取串口接收的数据
    }

    private class listenThread extends Thread {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {

            byte[] buffer = new byte[4096];
            //  清除之前 buffer 里的东西
            while (CH34xUARTDriverHelper.driver.ReadData(buffer, 4096) != 0) ;
            // threadTerminate 在 destroy 的时候会终止循环促使 线程终止
            while (!threadTerminate && CH34xUARTDriverHelper.isOpen) {
                // 禁用 useWakeup 会停止处理所有唤醒
                if (!useWakeup) {
                    // 处理掉buffer里的东西，防止 useWakeup 后之前累积的东西误触唤醒
                    while (CH34xUARTDriverHelper.driver.ReadData(buffer, 4096) != 0) ;
                    continue;
                }
                Message msg = Message.obtain();
                // 如果在 buffer 接收了消息
                if (CH34xUARTDriverHelper.driver.ReadData(buffer, 4096) != 0) {
                    StringBuilder wakeInfo = new StringBuilder(new String(buffer, StandardCharsets.US_ASCII));
                    // 串口数据分段传输，需要用一个字符串进行拼接
                    while (CH34xUARTDriverHelper.driver.ReadData(buffer, 4096) != 0)
                        wakeInfo.append(new String(buffer, StandardCharsets.US_ASCII));
                    useWakeup = false;//暂时屏蔽唤醒信号
                    msg.obj = wakeInfo.toString();
                    handler.sendMessage(msg);
                }
            }
        }
    }


    //获得当前年月日时分秒星期
//    public String getTime() {
//        final Calendar c = Calendar.getInstance();
//        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
//        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
//        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
//        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
//        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
//        String mMinute = String.valueOf(c.get(Calendar.MINUTE));//分
//        String mSecond = String.valueOf(c.get(Calendar.SECOND));//秒
//
//        if ("1".equals(mWay)) {
//            mWay = "天";
//        } else if ("2".equals(mWay)) {
//            mWay = "一";
//        } else if ("3".equals(mWay)) {
//            mWay = "二";
//        } else if ("4".equals(mWay)) {
//            mWay = "三";
//        } else if ("5".equals(mWay)) {
//            mWay = "四";
//        } else if ("6".equals(mWay)) {
//            mWay = "五";
//        } else if ("7".equals(mWay)) {
//            mWay = "六";
//        }
//        return mYear + "年" + mMonth + "月" + mDay + "日" + "  " + "星期" + mWay + "  " + mHour + ":" + mMinute + ":" + mSecond;
//    }

    private String getResource() {
        final String resPath = ResourceUtil.generateResourcePath(SidebarFragment.this.getActivity(), RESOURCE_TYPE.assets, "ivw/" + getString(R.string.app_id) + ".jet");
        Log.d("XUNFEI", "resPath: " + resPath);
        return resPath;
    }

    //获取发音人资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        String type = "xtts";
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this.getActivity(), RESOURCE_TYPE.assets, type + "/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this.getActivity(), RESOURCE_TYPE.assets, type + "/" + SidebarFragment.voicerXtts + ".jet"));
        return tempBuffer.toString();
    }

    private InitListener initListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("XUNFEI", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS)
                Log.d("XUNFEI", "初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }
    };

    //机器人语音合成回调
    private MySynthesizerListener mTtsListener = new MySynthesizerListener() {
        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                mIat.startListening(mRecognizerListener);
            } else
                toastLast(error.getPlainDescription(true));
        }
    };

    private String question = "";//下面这个类一次性不一定返回一个完整的问题需要用这个变量进行拼接

    private Boolean exitConfirm = false;//用户长时间没有输入提示输入，还是不输入就结束

    //机器人语音听写回调
    private MyRecognizerListener mRecognizerListener = new MyRecognizerListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            super.onResult(recognizerResult, b);
            question += JsonParser.parseIatResult(recognizerResult.getResultString());
            if (!b)//b为true是结尾
                return;
            mIat.stopListening();
            //用户如果说了话的话向服务器发送用户说的话并返回应答
            if (!question.equals("")) {
                HttpUtil httpUtil = new HttpUtil() {
                    @Override
                    public void onSuccess(final String result) {
                        super.onSuccess(result);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                communicationInfo.setText(result);
                                mTts.startSpeaking(result, mTtsListener);
                            }
                        });
                    }
                };
                httpUtil.send(question);
                question = "";
                exitConfirm = false;
            } else//否则返回待唤醒状态
            {
                if (!exitConfirm) {
                    exitConfirm = true;
                    mTts.startSpeaking("请问还有什么能够帮您的吗？", mTtsListener);
                    communicationInfo.setText("请问还有什么能够帮您的吗？");
                } else {
                    exitConfirm = false;
                    useWakeup = true;//恢复唤醒信号的使用
                    communicationInfo.setText("对话框");
                }
            }
        }
    };

    //设置机器人的唤醒、语音合成、语音识别能力
    private void setVoiceInteraction() {

        // 设置机器人的语音合成（说话）功能
        mTts = SpeechSynthesizer.createSynthesizer(this.getActivity(), initListener);
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_XTTS);
        //设置发音人资源路径
        mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");

        // 设置机器人的语音听写（语音->文字）功能
        mIat = SpeechRecognizer.createRecognizer(this.getActivity(), initListener);
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    //用于在屏幕上显示提示信息，有多条信息要显示只显示最后一条
    private void toastLast(String message) {
        if (message != null) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 切页面的时候不会调用 onDestroy 会调用 onPause 但再切进来的时候会调用onCreate，
        // 防止多次调用 onCreate 创建多个线程
        threadTerminate = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("XUNFEI", "onDestroy OneShotDemo");
        threadTerminate = true;
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }
}