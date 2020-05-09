package com.example.page2_200503_mj;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com_page2_1.Recycler_item;

public class Page2_CardView_adapter extends RecyclerView.Adapter<Page2_CardView_adapter.ViewHolder> {

    private Page2_MainActivity mainActivity;
    private String[] stay = new String[5];  // 하트의 클릭 여부
    private List<Recycler_item> cardview_items;  //리사이클러뷰 안에 들어갈 값 저장



    //메인에서 불러올 때, 이 함수를 씀
    public Page2_CardView_adapter(List<Recycler_item> items, Page2_MainActivity mainActivity) {
        this.cardview_items = items;
        this.mainActivity=mainActivity;
    }

    @NonNull
    @Override
    public Page2_CardView_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.page2_pic_view, null);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final Page2_CardView_adapter.ViewHolder holder, final int position) {
        final Recycler_item item = cardview_items.get(position);

        if (item.getImage() == null) {
            Log.i("이미지가 없을때", "널이다 이녀석아,,");
        } else {
            //이미지뷰에 url 이미지 넣기.
            //Glide.with(mainActivity).load(item.getImage()).centerCrop().into(holder.image);

        }

        holder.title.setText(item.getTitle());
        //holder.title.setSelected(true); //적용하면 텍스트 흐르면서 전체보여줌

        holder.type.setText(item.getType());


        //하트누르면 내부 데이터에 저장
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (stay[position] == null) {
                    holder.heart.setBackgroundResource(R.drawable.ic_icon_addmy);
//                    mCallback.make_db(item.getContentviewID(), item.getTitle());   //countId랑 title을 db에 넣으려고 함( make_db라는 인터페이스 이용)
//                    mCallback.make_dialog();                                       //db에 잘 넣으면 띄우는 다이얼로그(위와 마찬가지로 인터페이스 이용
                    stay[position] = "ON";
                    // Toast.makeText(context,"관심관광지를 눌렀습니다",Toast.LENGTH_SHORT).show();

                } else {
                    holder.heart.setBackgroundResource(R.drawable.ic_heart_off);
                    stay[position] = null;
                    // Toast.makeText(context,"관심관광지를 취소했습니다",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public int getItemCount () {
        return this.cardview_items.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title, type;
            CardView cardview;
            Button heart;

            public ViewHolder(View itemView) {
                super(itemView);
                heart = (Button) itemView.findViewById(R.id.page2_cardview_heart);
                image = (ImageView) itemView.findViewById(R.id.page2_no_image);
                title = (TextView) itemView.findViewById(R.id.page2_cardview_title);
                type = (TextView) itemView.findViewById(R.id.page2_cardview_type);
                cardview = (CardView) itemView.findViewById(R.id.page2_cardview);


            }
        }


}
