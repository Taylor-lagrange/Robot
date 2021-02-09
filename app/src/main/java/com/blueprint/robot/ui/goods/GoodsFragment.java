package com.blueprint.robot.ui.goods;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.blueprint.robot.R;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsFragment extends Fragment {

    public static final int SCROLL_GOODS = 0x105;
    private static final int INIT_TERM = 50;
    public static final int PIC_NUM = 3;
    private ViewPager2 viewPager2;
    private ImageButton buttonLeft;
    private ImageButton buttonRight;
    private GoodsFragment.CarouselThread carouselThread = null;
    private GoodsFragment.CarouselHandler handler = new CarouselHandler(this);
    private int picNum = PIC_NUM;
    private ImageView savePicture;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GoodsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goods, container, false);
    }

    private static class CarouselThread extends Thread {
        private static final String TAG = "CarouselThread";
        private static final int GOODS_SLEEP = 2500;

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
                sleep(GOODS_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (isRunning) {
                handler.sendEmptyMessage(GoodsFragment.SCROLL_GOODS);
                try {
                    sleep(GOODS_SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class CarouselHandler extends Handler {
        private WeakReference<GoodsFragment> goodsFragmentWeakReference;

        public CarouselHandler(GoodsFragment goodsFragment) {
            goodsFragmentWeakReference = new WeakReference<GoodsFragment>(goodsFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            GoodsFragment goodsFragment = goodsFragmentWeakReference.get();
            if(goodsFragment != null) {
                if(msg.what == SCROLL_GOODS && goodsFragment.getPicNum() != 0) {
                    goodsFragment.scrollViewPager();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewPager2 != null) {
            viewPager2.setCurrentItem(INIT_TERM * PIC_NUM,false);
            _startThread();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager2 = view.findViewById(R.id.ViewPager2_goods);
        buttonLeft = view.findViewById(R.id.imageButton_goods);
        buttonRight = view.findViewById(R.id.imageButton2_goods);

        if(picNum == 0) {
            viewPager2.setVisibility(View.GONE);
            buttonLeft.setVisibility(View.GONE);
            buttonRight.setVisibility(View.GONE);
            return;
        }

        viewPager2.setAdapter(new GoodsAdapter());

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1, true);
                _endThread();
                _startThread();
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);
                _endThread();
                _startThread();
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

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
    public void onPause() {
        super.onPause();
        _endThread();
    }

    @Override
    public void onDestroy() {
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

    private void _startThread() {
        if(carouselThread == null) {
            carouselThread = new GoodsFragment.CarouselThread(handler);
            carouselThread.start();
        }
    }

    private void _endThread() {
        if(carouselThread != null) {
            carouselThread.stopRunning();
            carouselThread = null;
        }
        if(handler.hasMessages(SCROLL_GOODS)) {
            handler.removeMessages(SCROLL_GOODS);
        }
    }
}