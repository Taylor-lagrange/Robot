package com.blueprint.robot.ui.goods;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueprint.robot.R;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder> {

    @NonNull
    @Override
    public GoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_goods, parent, false);
        GoodsViewHolder viewHolder = new GoodsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {
        switch (position % GoodsFragment.PIC_NUM) {
            case 0:
                holder.textViewTitle.setText("绿壳鸡蛋");
                holder.imageView.setImageResource(R.drawable.pic_egg);
                holder.textViewIntro.setText("长顺绿壳蛋鸡原产于长顺县鼓扬镇，早在20世纪60年代，在鼓扬镇马场村、岩腊村、纪堵村一带，人们就发现有产绿壳蛋鸡的鸡群，世袭流传利用这种鸡蛋能治伤风感冒，因此绿壳蛋鸡倍受保护，长期得以繁衍。");
                holder.textViewDetail.setText("包装：简装    枚数：21-40枚");
                holder.textViewAddr.setText("产地：贵州长顺 保质期：30天");
                holder.textViewSave.setText("储藏方式：阴凉或冰箱冷藏保鲜");
                break;
            case 1:
                holder.textViewTitle.setText("高钙苹果");
                holder.imageView.setImageResource(R.drawable.pic_apple);
                holder.textViewIntro.setText("作为典型的山区农业县，长顺却拥有得天独厚的自然环境。长顺独特的喀斯特地貌使得土壤富含钙质及氮、磷、钾等元素，降雨量充沛，春、夏、秋果树生长 期的日照充足，因而产出特有的富含硒、钙的精品苹果，同时比山东、陕西的红富士早熟二十余天。");
                holder.textViewDetail.setText("包装：纸盒装    个数：15个");
                holder.textViewAddr.setText("产地：贵州长顺 保质期：45天");
                holder.textViewSave.setText("储藏方式：常温");
                break;
            case 2:
                holder.textViewTitle.setText("麻山油核桃");
                holder.imageView.setImageResource(R.drawable.pic_walnut);
                holder.textViewIntro.setText("长顺县坚持把核桃产业发展作为农业产业结构调整、壮大农村经济、促进农民增收致富的优势产业来抓，通过示范引导、典型带动、科技支撑、利益驱动等方式，全力做大做强核桃产业。目前，全县已发展核桃10.3万亩,231万多株，年产量达到88万公斤，实现产值3520万元，人均增收136.5元。");
                holder.textViewDetail.setText("包装：罐装    斤数：2斤");
                holder.textViewAddr.setText("产地：贵州长顺 保质期：6个月");
                holder.textViewSave.setText("储藏方式：常温干燥处");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDetail;
        private TextView textViewIntro;
        private TextView textViewAddr;
        private TextView textViewSave;
        private ImageView imageView;

        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textView_name_goods);
            textViewDetail = itemView.findViewById(R.id.textView_pack_goods);
            textViewIntro = itemView.findViewById(R.id.textView_intro_goods);
            textViewAddr = itemView.findViewById(R.id.textView_addr_goods);
            textViewSave = itemView.findViewById(R.id.textView_save_goods);
            imageView = itemView.findViewById(R.id.imageView_pic_goods);
        }
    }
}
