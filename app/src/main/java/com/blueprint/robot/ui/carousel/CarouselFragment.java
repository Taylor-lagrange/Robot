package com.blueprint.robot.ui.carousel;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueprint.robot.MainActivity;
import com.blueprint.robot.R;
import com.blueprint.robot.data.ViewModel.ScenicSpotViewModel;
import com.blueprint.robot.data.entity.ScenicSpot;
import com.google.android.material.tabs.TabLayout;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarouselFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarouselFragment extends Fragment {
    private static final String TAG = "CarouselFragment";
    public static final int SCROLL = 0x101;
    public static final int LOAD_IMAGE = 0x103;
    private static final int INIT_TERM = 50;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private View clickView;
    private TextView textView;
    private CarouselThread carouselThread = null;
    private CarouselHandler handler = new CarouselHandler(this);
    private ScenicSpotViewModel carouselViewModel;
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
                handler.sendEmptyMessage(SCROLL);
                try {
                    sleep(COMMON_SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ImageLoadThread extends Thread {
        private Handler handler;
        private ScenicSpotViewModel carouselViewModel;
        private int position;
        public ImageLoadThread(Handler handler, ScenicSpotViewModel carouselViewModel, int position) {
            this.handler = handler;
            this.carouselViewModel = carouselViewModel;
            this.position = position;
        }

        @Override
        public void run() {
            super.run();
            Bitmap bitmap = carouselViewModel.getPictureScenicSpotList().get(position).getLocalBitmap(CarouselFragment.this.getContext(),0);
            Message message = Message.obtain();
            message.obj = bitmap;
            message.what = LOAD_IMAGE;
            handler.sendMessage(message);
        }
    }

    private static class CarouselHandler extends Handler {
        private WeakReference<CarouselFragment> carouselFragmentWeakReference;

        public CarouselHandler(CarouselFragment carouselFragment) {
            carouselFragmentWeakReference = new WeakReference<CarouselFragment>(carouselFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            CarouselFragment carouselFragment = carouselFragmentWeakReference.get();
            if(carouselFragment != null) {
                if(msg.what == SCROLL && carouselFragment.getPicNum() != 0) {
                    carouselFragment.scrollViewPager();
                } else if(msg.what == LOAD_IMAGE) {
                    carouselFragment.getSavePicture().setImageBitmap((Bitmap)msg.obj);
                }
            }
        }
    }

    public CarouselFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CarouselFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarouselFragment newInstance() {
        CarouselFragment fragment = new CarouselFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carousel, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewPager2 != null) {
            viewPager2.setCurrentItem(
                    carouselViewModel.getPictureNum() * carouselViewModel.getTerm() + carouselViewModel.getPosition(),
                    false);
            _startThread();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        carouselViewModel = new ViewModelProvider(this).get(ScenicSpotViewModel.class);
        List<ScenicSpot> scenicSpotList = carouselViewModel.getPictureScenicSpotList();
        picNum = carouselViewModel.getPictureNum();

        viewPager2 = view.findViewById(R.id.ViewPager2_Carousel);
        tabLayout = view.findViewById(R.id.TabLayout_Carousel);
        clickView = view.findViewById(R.id.clickLayout_Carousel);
        textView = view.findViewById(R.id.textView_warn_Carousel);

        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//销毁当前fragment
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.setCarousel(false);
                mainActivity.reInitTime();
                Log.d(TAG, "onClick: ");
                NavController navController = Navigation.findNavController(mainActivity, R.id.fragment);
                navController.navigate(R.id.functionSelectionFragment);
            }
        });

        if(picNum == 0) {
            viewPager2.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            return;
        }

        viewPager2.setAdapter(new CarouselAdapter() {
            @Override
            public void endActivity() {
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.setCarousel(false);
                mainActivity.reInitTime();
                NavController navController = Navigation.findNavController(mainActivity, R.id.fragment);
                navController.navigate(R.id.functionSelectionFragment);
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
    public void onPause() {
        super.onPause();
        _endThread();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _endThread();
        handler.removeCallbacksAndMessages(null);
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