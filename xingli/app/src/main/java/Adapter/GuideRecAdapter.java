package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.xingli.Activity.GuideDetailActivity;
import com.example.xingli.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import db.Guide;

public class GuideRecAdapter extends RecyclerView.Adapter<GuideRecAdapter.ViewHolder>  implements Filterable {
    private List<Guide> mGuideList=new ArrayList<>();
    Context context;
    private List<Guide> mFilterList = new ArrayList<>();

    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence prefixString) {
                String charString = prefixString.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mFilterList = mGuideList;
                } else {
                    List<Guide> filteredList = new ArrayList<>();
                    for (Guide value : mGuideList) {
                        //这里根据需求，添加匹配规则
                        if ((value.getNickName().contains(prefixString) ||value.getProfile().contains(prefixString)
                                ||value.getMobilePhoneNumber().contains(prefixString) ||String.valueOf(value.getAge()).contains(prefixString)
                                ||value.getGender().contains(prefixString)))
                        {
                            filteredList.add(value);
                        }


                    }

                    mFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }
            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (ArrayList<Guide>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView GuideImage;
        TextView GuideName;
        TextView GuideLoc;
        TextView GuidePrice;
        TextView GuideNum;
        TextView GuideGender;
        TextView GuideAge;
        View PostView;
        public ViewHolder(View view){
            super(view);
            PostView=view;
            GuideImage=(ImageView)view.findViewById(R.id.guide_image);
            GuideName=(TextView)view.findViewById(R.id.guide_name);
            GuideLoc=(TextView)view.findViewById(R.id.guide_location);
            GuidePrice=(TextView)view.findViewById(R.id.guide_price);
            GuideNum=(TextView)view.findViewById(R.id.guide_ordernum);
            GuideGender=(TextView)view.findViewById(R.id.guide_gender);
            GuideAge=(TextView)view.findViewById(R.id.guide_age);
        }
    }

    public GuideRecAdapter(List<Guide> GuideList, Context context)
    {
        mGuideList=GuideList;
        mFilterList =GuideList;
        this.context=context;
    }

    @NonNull
    @Override
    public GuideRecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide,parent,false);
        final GuideRecAdapter.ViewHolder holder=new GuideRecAdapter.ViewHolder(view);
        holder.PostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Guide guide=mFilterList.get(position);
                Intent intent = new Intent(context, GuideDetailActivity.class);
                intent.putExtra("guide",guide);
                intent.putExtra("choose","start");
                context.startActivity(intent);

            }
        });


        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull GuideRecAdapter.ViewHolder holder, int position) {
        Guide guide=mFilterList.get(position);
//        holder.GuideImage.setImageResource(guide.getGuideImageId());

        if (guide.getHeadImage() != null) {

            BmobFile guideImageFile = guide.getHeadImage();
            String stringPostImageUri = guideImageFile.getFileUrl();
            //利用ImageRequest实现图片加载
            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            //构建ImageRequest 实例
            ImageRequest request = new ImageRequest(stringPostImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //给imageView设置图片
                    holder.GuideImage.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //设置一张错误的图片，临时用ic_launcher代替
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    holder.GuideImage.setImageResource(R.drawable.headimage);
                }
            });

            requestQueue.add(request);

        } else {
            holder.GuideImage.setImageResource(R.drawable.headimage);
        }
        
        holder.GuideName.setText(guide.getNickName());
        holder.GuideLoc.setText(guide.getGuideLoc());
        holder.GuidePrice.setText(String.valueOf(guide.getPriceNoUniform())+"元起");
        holder.GuideNum.setText("服务了"+String.valueOf(guide.getMouthNum())+"位旅行者");
        holder.GuideGender.setText("性别："+guide.getGender());
        holder.GuideAge.setText("年龄："+guide.getAge());
        holder.GuideImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

//        if (holder.OrderPayState.getText().equals("已支付"))
//            holder.OrderPayState.setTextColor(Integer.parseInt("@color/yellow"));
//        if (holder.OrderAcceptState.getText().equals("已接受"))
//            holder.OrderAcceptState.setTextColor(Integer.parseInt("@color/yellow"));
//        if (holder.OrderFinishState.getText().equals("已完成"))
//            holder.OrderFinishState.setTextColor(Integer.parseInt("@color/yellow"));
    }


    @Override
    public int getItemCount() {
        return mFilterList.size();
    }
}
