package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xingli.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import db.Scenery;

public class SceneryRecAdapter extends RecyclerView.Adapter<SceneryRecAdapter.ViewHolder>{
    private List<Scenery> mCityList=new ArrayList<>();
    Context context;
    public int getHour(int a)
    {
        switch (a){
            case 0:
                return 0;
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 2;
            case 4:
                return 4;
            case 5:
                return 4;
            case 6:
                return 6;
            case 7:
                return 6;
            case 8:
                return 8;
            case 9:
                return 8;
            case 10:
                return 10;
            case 11:
                return 10;
        }
        return 0;
    }
    public String randomTime()
    {
        String s1="09:00-21:30";
        String s2="09:00-18:00";
        String s3="09:00-17:00";
        String s4="09:00-22:00";
        String s5="09:00-21:00";
        String s6="08:30-17:00";
        String s7="09:00-16:30";
        String s8="08:30-21:30";
        String s9="08:30-16:30";
        String s10="00:00-17:00";
        List<String> timeList=new ArrayList<>();
        timeList.add(s1); timeList.add(s2); timeList.add(s3); timeList.add(s4); timeList.add(s5);
        timeList.add(s6); timeList.add(s7); timeList.add(s8); timeList.add(s9); timeList.add(s10);
        Random random = new Random();
        int i=random.nextInt(10)+1;
        return timeList.get(i-1);
    }
    public String getFillNumString(int i)
    {
        String s1="人/"+String.valueOf(i)+"人";
        return s1;
    }
    public int  randomFillNum()
    {
        int i1=15000;
        int i2=96000;
        int i3=13500;
        int i4=15000;
        int i5=5900;
        int i6=3161;
        int i7=9533;
        int i8=4933;
        int i9=1200;
        int i10=45000;
        List<Integer> numList=new ArrayList<>();
        numList.add(i1); numList.add(i2); numList.add(i3); numList.add(i4); numList.add(i5);
        numList.add(i6); numList.add(i7); numList.add(i8); numList.add(i9); numList.add(i10);
        Random random = new Random();
        int i=random.nextInt(10)+1;
        return numList.get(i-1);
    }
    public int randomNowNum(int i)
    {
        Random random = new Random();
        int x=random.nextInt(i);
        return  x;
    }

    public SceneryRecAdapter(List<Scenery> CityList, Context context)
    {
        mCityList=CityList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scenery_num,parent,false);
        final SceneryRecAdapter.ViewHolder holder=new SceneryRecAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Scenery scenery=mCityList.get(position);
        holder.sceneryName.setText(scenery.getSceneryName());
        holder.sceneryLevel.setText(scenery.getSceneryLevel());
        holder.sceneryDistrict.setText(scenery.getSceneryDistrict());
        holder.sceneryTime.setText(randomTime());
        int a= randomFillNum();
        int b=randomNowNum(a);
        holder.sceneryNowNum.setText(String.valueOf(b));
        holder.sceneryFillNum.setText(getFillNumString(a));
        Calendar c = Calendar.getInstance();
        holder.sceneryUpdateTime.setText(String.valueOf(c.get(Calendar.YEAR))+"-"+String.valueOf(c.get(Calendar.MONTH)+1)
                +"-"+String.valueOf(c.get(Calendar.DAY_OF_MONTH))+" "+String.valueOf(getHour(c.get(Calendar.HOUR)))
                +":00:00");
        //设置图片的位置
        ViewGroup.MarginLayoutParams margin9 = new ViewGroup.MarginLayoutParams(
                holder.greenImage.getLayoutParams());
        margin9.setMargins(15, 0, 0, 0);//在左边距400像素，顶边距10像素的位置显示图片
        RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(margin9);
        layoutParams9.height = 82;//设置图片的高度
        layoutParams9.width = 400*b/a; //设置图片的宽度
        holder.greenImage.setLayoutParams(layoutParams9);
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sceneryName;
        TextView sceneryLevel;
        TextView sceneryDistrict;
        TextView sceneryTime;
        ImageView greenImage;
        TextView sceneryNowNum;
        TextView sceneryFillNum;
        TextView sceneryUpdateTime;
        RelativeLayout relativeLayout;
        View sceneryView;

        public ViewHolder(@NonNull View view) {
            super(view);
            sceneryView=view;
            sceneryName=(TextView) view.findViewById(R.id.scenery_name);
            sceneryLevel=(TextView) view.findViewById(R.id.scenery_level);
            sceneryDistrict=(TextView) view.findViewById(R.id.scenery_district);
            sceneryTime=(TextView) view.findViewById(R.id.scenery_opentime);
            greenImage=(ImageView) view.findViewById(R.id.green_image);
            sceneryNowNum=(TextView) view.findViewById(R.id.now_num);
            sceneryFillNum=(TextView) view.findViewById(R.id.fill_num);
            sceneryUpdateTime=(TextView) view.findViewById(R.id.update_time);
            relativeLayout=(RelativeLayout) view.findViewById(R.id.relative);
        }

    }

}
