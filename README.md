---
title: 安卓数据库调用说明
author: 吴静迪
date: 2020年9月24日
# 指定汉字字体，如果缺少中文显示将不正常
CJKmainfont: 黑体
# latex 选项
fontsize: 12pt
linkcolor: blue
urlcolor: green
citecolor: cyan
filecolor: magenta
toccolor: red
geometry: margin=0.3in
papersize: A4
documentclass: article

# pandoc设置
output:
  pdf_document:
    toc: true
    toc_depth: 2
    number_sections: true
    highlight: tango
# 打印背景色
# 保存文件时自动生成
# export_on_save:
#   pandoc: true
---

# 安卓数据库调用说明

<!-- @import "[TOC]" {cmd="toc" depthFrom=2 depthTo=4 orderedList=true} -->

<!-- code_chunk_output -->

1. [调用说明](#调用说明)
2. [调用代码示例](#调用代码示例)
3. [代码Git提交说明](#代码git提交说明)
4. [提示](#提示)

<!-- /code_chunk_output -->

## 调用说明

1. 所有的景点数据被封装在`ScenicSpot`类中，包括景点名称，简介，开放时间，介绍图片的路径等数据
2. 介绍图片只存储了路径，并没有存储实际的Bitmap位图图像，需要调用`ScenicSpot`类中的`getLocalBitmap()` 方法，传入一个`int`类型的值作为参数，该参数指示需要生成的`Bitmap`类对应的是该景点介绍图片列表的哪一个下标，最后通过`ImageView`类中的`setImageBitmap`方法传入`Bitmap`类显示图片
3. 全部页面使用`fragment`作为显示方法，[不清楚看这个-Navigation组件的使用方法](https://www.bilibili.com/video/BV1w4411t7UQ?p=17)，调用的时候先在类中声明`ScenicSpotViewModel scenicSpotViewModel;`这一我自己定义的ViewModel对象，再使用`scenicSpotViewModel = new ViewModelProvider(this).get(ScenicSpotViewModel.class);`这一个方法获取对象。
4. `scenicSpotViewModel` 提供了`getScenicSpotList`方法用于获得所有景点的对象，`insertScenicSpot`方法用于插入一个新的景点对象。`ScenicSpot`提供了默认构造方法，用于构造一个默认景点，但是因为图片路径存储在本机，不是每个人在那个位置上都有图片存在，建议在AVD中上传图片后再修改`ScenicSpot`提供了默认构造景点的函数对象中关于景点路径的部分，避免无法显示。

## 调用代码示例

参看MainActivity中的代码片段，我在界面里写了一个按钮，每次按动按钮会增加一个默认的景点，并更新显示，将图片替换为指定的一张图片（主要是为了展示如何从外存显示图片 ）

```java
public class MainActivity extends AppCompatActivity {

    ScenicSpotViewModel scenicSpotViewModel;
    ImageView imageView;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.scenicInfo);
        imageView = findViewById(R.id.imageTestView);
        button = findViewById(R.id.insertButton);
        //获取ScenicSpotViewModel对象
        scenicSpotViewModel = new ViewModelProvider(this).get(ScenicSpotViewModel.class);
        //通过ScenicSpotViewModel对象获取所有景点信息
        List<ScenicSpot> scenicSpotList = scenicSpotViewModel.getScenicSpotList();
        //根据景点信息更新textView的显示
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < scenicSpotList.size(); i++)
            text.append(scenicSpotList.get(i).toString()).append('\n');
        textView.setText(text);
        //尝试插入新的景点
        scenicSpotViewModel.insertScenicSpot(new ScenicSpot());
        //设置按键监听
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //插入一个新的景点
                scenicSpotViewModel.insertScenicSpot(new ScenicSpot());
                //更新显示
                StringBuilder text = new StringBuilder();
                List<ScenicSpot> scenicSpotList = scenicSpotViewModel.getScenicSpotList();
                for (int i = 0; i < scenicSpotList.size(); i++)
                    text.append(scenicSpotList.get(i).toString()).append('\n');
                textView.setText(text);
                //设置ImageView的图片（从外存调用）
                imageView.setImageBitmap(scenicSpotList.get(0).getLocalBitmap(0)); //设置Bitmap
            }
        });
    }
}
```

## 代码Git提交说明

先在本地的git仓库进行更新，待完成功能后我们统一协调后再进行git库的更新。

## 提示

调用外部存储需要在AVD中手动进行授权，否则会导致文件禁止访问，选择APP Info完成外部存储访问授权。
