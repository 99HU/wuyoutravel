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
import com.example.xingli.Activity.photographerDetailActivity;
import com.example.xingli.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import db.Photographer;

public class PhotographerRecAdapter extends RecyclerView.Adapter<PhotographerRecAdapter.ViewHolder>  implements Filterable {
    private List<Photographer> mPhotographerList=new ArrayList<>();
    Context context;
    private List<Photographer> mFilterList = new ArrayList<>();

    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence prefixString) {
                String charString = prefixString.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mFilterList = mPhotographerList;
                } else {
                    List<Photographer> filteredList = new ArrayList<>();
                    for (Photographer value : mPhotographerList) {
                        //这里根据需求，添加匹配规则
                        if ((value.getNickName().contains(prefixString)|| value.getProfile().contains(prefixString)||value.getMobilePhoneNumber().contains(prefixString)
                                ||String.valueOf(value.getAge()).contains(prefixString)||value.getGender().contains(prefixString)))
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
                mFilterList = (ArrayList<Photographer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photographerImage;
        TextView photographName;
        TextView photographLoc;
        TextView photographerPrice;
        TextView photographNum;
        TextView photographerGender;
        TextView photographerAge;
        View PostView;
        public ViewHolder(View view){
            super(view);
            PostView=view;
            photographerImage=(ImageView)view.findViewById(R.id.photographer_image);
            photographName=(TextView)view.findViewById(R.id.photographer_name);
            photographLoc=(TextView)view.findViewById(R.id.photographer_location);
            photographerPrice=(TextView)view.findViewById(R.id.photographer_price);
            photographNum=(TextView)view.findViewById(R.id.photographer_ordernum);
            photographerGender=(TextView)view.findViewById(R.id.photographer_gender);
            photographerAge=(TextView)view.findViewById(R.id.photographer_age);
        }
    }

    public PhotographerRecAdapter(List<Photographer> postList, Context context)
    {
        mPhotographerList=postList;
        mFilterList =postList;
        this.context=context;
    }

    @NonNull
    @Override
    public PhotographerRecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photographer,parent,false);
        final PhotographerRecAdapter.ViewHolder holder=new PhotographerRecAdapter.ViewHolder(view);
        holder.PostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Photographer photographer=mFilterList.get(position);
                Intent intent = new Intent(context, photographerDetailActivity.class);
                intent.putExtra("photographer",photographer);
                intent.putExtra("choose","start");
                context.startActivity(intent);

            }
        });


        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull PhotographerRecAdapter.ViewHolder holder, int position) {
        Photographer photographer=mFilterList.get(position);

        if (photographer.getHeadImage() != null) {

            BmobFile photographerImageFile = photographer.getHeadImage();
            String stringPostImageUri = photographerImageFile.getFileUrl();
            //利用ImageRequest实现图片加载
            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            //构建ImageRequest 实例
            ImageRequest request = new ImageRequest(stringPostImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //给imageView设置图片
                    holder.photographerImage.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //设置一张错误的图片，临时用ic_launcher代替
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    holder.photographerImage.setImageResource(R.drawable.headimage);
                }
            });

            requestQueue.add(request);

        } else {
            holder.photographerImage.setImageResource(R.drawable.headimage);
        }


        holder.photographName.setText(photographer.getNickName());
        holder.photographLoc.setText(photographer.getPhotographerLoc());
        holder.photographerPrice.setText(String.valueOf(photographer.getPriceNoUniform())+"元起");
        holder.photographNum.setText("服务了"+String.valueOf(photographer.getMouthNum())+"位旅行者");
        holder.photographerGender.setText("性别："+photographer.getGender());
        holder.photographerAge.setText("年龄："+photographer.getAge());
        holder.photographerImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

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
