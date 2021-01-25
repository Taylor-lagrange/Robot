package com.blueprint.robot.ui.carouselactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueprint.robot.R;
import com.blueprint.robot.data.entity.ScenicSpot;
import com.google.android.material.tabs.TabLayout;

import java.lang.ref.WeakReference;
import java.util.List;

public class CarouselActivity extends AppCompatActivity {
    private static final String TAG = "CarouselActivity";
    public static final int SCROLL = 0x101;
    public static final int LOAD_IMAGE = 0x103;
    private static final int INIT_TERM = 50;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private View clickView;
    private TextView textView;
    private CarouselThread carouselThread = null;
    private CarouselHandler handler = new CarouselHandler(this);
    private CarouselViewModel carouselViewModel;
    private int picNum = 0;
    private ImageView savePicture;

    private static class CarouselThread extends Thread {
        private static final String TAG = "CarouselThread";
        private static final int FIRST_SLEEP = 5000;
        private static final int COMMON_SLEEP = 2500;

        private Handler handler;
        private boolean isRunning = true;
        public CarouselThread(Handler handler) {
            this.handler = handler;
        }

        public void stopRunning() {
            isRunning = false;
        }

        @Override
        public void run() {
            super.run();
            try {
                sleep(FIRST_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (isRunning) {
                handler.sendEmptyMessage(CarouselActivity.SCROLL);
                try {
                    sleep(COMMON_SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ImageLoadThread extends Thread {
        private Handler handler;
        private CarouselViewModel carouselViewModel;
        private int position;
        public ImageLoadThread(Handler handler, CarouselViewModel carouselViewModel, int position) {
            this.handler = handler;
            this.carouselViewModel = carouselViewModel;
            this.position = position;
        }

        @Override
        public void run() {
            super.run();
            Bitmap bitmap = carouselViewModel.getPictureScenicSpotList().get(position).getLocalBitmap(0);
            Message message = Message.obtain();
            message.obj = bitmap;
            message.what = LOAD_IMAGE;
            handler.sendMessage(message);
        }
    }

    private static class CarouselHandler extends Handler {
        private WeakReference<CarouselActivity> carouselActivityWeakReference;

        public CarouselHandler(CarouselActivity carouselActivity) {
            carouselActivityWeakReference = new WeakReference<CarouselActivity>(carouselActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            CarouselActivity carouselActivity = carouselActivityWeakReference.get();
            if(carouselActivity != null) {
                if(msg.what == SCROLL && carouselActivity.getPicNum() != 0) {
                    carouselActivity.scrollViewPager();
                } else if(msg.what == LOAD_IMAGE) {
                    carouselActivity.getSavePicture().setImageBitmap((Bitmap)msg.obj);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();
        if(viewPager2 != null) {
            viewPager2.setCurrentItem(
                    carouselViewModel.getPictureNum() * carouselViewModel.getTerm() + carouselViewModel.getPosition(),
                    false);
            _startThread();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        carouselViewModel = new ViewModelProvider(this).get(CarouselViewModel.class);
        List<ScenicSpot> scenicSpotList = carouselViewModel.getPictureScenicSpotList();
        picNum = carouselViewModel.getPictureNum();

        viewPager2 = findViewById(R.id.ViewPager2_Carousel);
        tabLayout = findViewById(R.id.TabLayout_Carousel);
        clickView = findViewById(R.id.clickLayout_Carousel);
        textView = findViewById(R.id.textView_warn_Carousel);

        if(picNum == 0) {
            viewPager2.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            return;
        }

        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        viewPager2.setAdapter(new CarouselAdapter() {
            @Override
            public void endActivity() {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            @Override
            public void loadImage(ImageView imageView, int position) {
                savePicture = imageView;
                new ImageLoadThread(handler, carouselViewModel, position % picNum);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                Log.d(TAG, "onTabSelected: " + tab.getPosition());
                viewPager2.setCurrentItem(picNum * carouselViewModel.getTerm()
                        + tab.getPosition());
                _endThread();
                _startThread();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                Log.d(TAG, "onTabReselected: " + tab.getPosition());
                viewPager2.setCurrentItem(picNum * carouselViewModel.getTerm()
                        + tab.getPosition());
                _endThread();
                _startThread();
            }
        });

        for (int i = 0; i < picNum; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(scenicSpotList.get(i).getName()));
        }

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                carouselViewModel.setTerm(position / picNum);
                carouselViewModel.setPosition(position % picNum);
                tabLayout.setScrollPosition(position % picNum, 0, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //super.onPageScrollStateChanged(state);
                if(state == ViewPager2.SCROLL_STATE_IDLE) {
                    _startThread();
                } else if(state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    _endThread();
                }
            }
        });

        viewPager2.setCurrentItem(picNum * INIT_TERM, false);

        _startThread();

    }

    @Override
    protected void onPause() {
        super.onPause();
        _endThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _endThread();
        handler.removeCallbacksAndMessages(null);
    }

    public void scrollViewPager() {
        viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
    }

    public int getPicNum() {
        return picNum;
    }

    public ImageView getSavePicture() {
        return savePicture;
    }

    private void _startThread() {
        if(carouselThread == null) {
            carouselThread = new CarouselThread(handler);
            carouselThread.start();
        }
    }

    private void _endThread() {
        if(carouselThread != null) {
            carouselThread.stopRunning();
            carouselThread = null;
        }
        if(handler.hasMessages(SCROLL)) {
            handler.removeMessages(SCROLL);
        }
    }
}