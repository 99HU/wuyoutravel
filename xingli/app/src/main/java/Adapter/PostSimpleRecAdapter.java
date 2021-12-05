package Adapter;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Application;
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
import com.example.xingli.Activity.chooseStartPostActivity;
import com.example.xingli.R;

import java.util.ArrayList;
import java.util.List;

import base.UniteApp;
import cn.bmob.v3.datatype.BmobFile;
import db.Post;

public class PostSimpleRecAdapter extends RecyclerView.Adapter<PostSimpleRecAdapter.ViewHolder>  implements Filterable {
    private List<Post> mPostList=new ArrayList<>();
    Context context;
    private List<Post> mFilterList = new ArrayList<>();
    Application app;
    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mFilterList = mPostList;
                } else {
                    List<Post> filteredList = new ArrayList<>();
                    for (Post value : mPostList) {
                        //这里根据需求，添加匹配规则
                        if (value.getPostName().contains(charSequence)||value.getPostLoc().contains(charSequence)
                                ||value.getPostProfile().contains(charSequence)||value.getPostTel().contains(charSequence)) {
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
                mFilterList = (ArrayList<Post>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView postImage;
        TextView postName;
        TextView postLoc;
        View PostView;
        public ViewHolder(View view){
            super(view);
            PostView=view;
            postImage = (ImageView) view.findViewById(R.id.post_image);
            postName = (TextView) view.findViewById(R.id.post_name);
            postLoc = (TextView) view.findViewById(R.id.post_location);
        }
    }

    public PostSimpleRecAdapter(List<Post> postList, Context context, Application app)
    {
        mPostList=postList;
        mFilterList =postList;
        this.context=context;
        this.app=app;
    }

    @NonNull
    @Override
    public PostSimpleRecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_simple_item,parent,false);
        final PostSimpleRecAdapter.ViewHolder holder=new PostSimpleRecAdapter.ViewHolder(view);
        holder.PostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Post post=mFilterList.get(position);
                Intent intent=new Intent();
                intent.putExtra("poststart",post);
                List<Activity> activities = ((UniteApp)app).getActivities();
                for(Activity activity:activities){
                    if(activity instanceof chooseStartPostActivity) {
                        activity.setResult(RESULT_OK,intent);
                        activity.finish();
                    }
                }
            }
        });


        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull PostSimpleRecAdapter.ViewHolder holder, int position) {
        Post post=mFilterList.get(position);
        //holder.postImage.setImageResource(post.getPostImageId());


        if (post.getPostImage() != null) {

            BmobFile postImageFile = post.getPostImage();
            String stringPostImageUri = postImageFile.getFileUrl();
            //利用ImageRequest实现图片加载
            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            //构建ImageRequest 实例
            ImageRequest request = new ImageRequest(stringPostImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //给imageView设置图片
                    holder.postImage.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //设置一张错误的图片，临时用ic_launcher代替
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    holder.postImage.setImageResource(R.drawable.headimage);
                }
            });

            requestQueue.add(request);

        } else {
            holder.postImage.setImageResource(R.drawable.headimage);
        }

        holder.postName.setText(post.getPostName());
        holder.postLoc.setText("位置：" + post.getPostLoc());
        holder.postImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);;

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
