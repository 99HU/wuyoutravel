package com.example.photographer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class OrderRecAdapter extends RecyclerView.Adapter<OrderRecAdapter.ViewHolder>  implements Filterable {
    private List<Order> mOrderList=new ArrayList<>();
    Context context;
    private List<Order> mFilterList = new ArrayList<>();

    private BmobFile userImageFile;
    private String stringImageUri;



    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mFilterList = mOrderList;
                } else {
                    List<Order> filteredList = new ArrayList<>();
                    for (Order str : mOrderList) {
                        //这里根据需求，添加匹配规则
                        if (str.getObjectId().contains(charSequence)) {
                            filteredList.add(str);
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
                mFilterList = (ArrayList<Order>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView UserImage;
        TextView OrderId;
        TextView OrderState;
        TextView OrderGetTime;
        TextView OrderGetLoc;
        TextView OrderCreateTime;
        TextView OrderPayState;
        TextView OrderAcceptState;
        TextView OrderFinishState;
        TextView OrderPrice;
        View PostView;
        public ViewHolder(View view){
            super(view);
            PostView=view;
            OrderId=(TextView)view.findViewById(R.id.orderlist_id);
            UserImage=(ImageView) view.findViewById(R.id.orderlist_photographer_image);
            OrderState=(TextView)view.findViewById(R.id.orderlist_task) ;
            OrderGetTime=(TextView)view.findViewById(R.id.orderlist_gettime);
            OrderGetLoc=(TextView)view.findViewById(R.id.orderlist_getLoc) ;
            OrderCreateTime=(TextView) view.findViewById(R.id.orderlist_create_time);
            OrderPayState=(TextView) view.findViewById(R.id.orderlist_pay_state);
            OrderAcceptState=(TextView) view.findViewById(R.id.orderlist_accept_state);
            OrderFinishState=(TextView) view.findViewById(R.id.orderlist_finish_state);
            OrderPrice=(TextView) view.findViewById(R.id.orderlist_order_price);
        }
    }

    public OrderRecAdapter(List<Order> orderList, Context context)
    {
        mOrderList=orderList;
        mFilterList =orderList;
        this.context=context;
    }



    @NonNull
    @Override
    public OrderRecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photographer_order,parent,false);
        final OrderRecAdapter.ViewHolder holder=new OrderRecAdapter.ViewHolder(view);
        holder.PostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Order order=mFilterList.get(position);
                PhotographerHomePage.UpdateFlag=true;
                Intent intent=new Intent(context,OrderDetailActivity.class);
                intent.putExtra("order",order);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecAdapter.ViewHolder holder, int position) {
        Order order=mFilterList.get(position);
        BmobQuery<User> bmobQueryUser = new BmobQuery<>();
        bmobQueryUser.addWhereEqualTo("objectId", order.getUser().getObjectId());
        bmobQueryUser.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    User user=list.get(0);
                    if (user.getUserImage() != null) {
                        userImageFile = user.getUserImage() ;
                        stringImageUri = userImageFile.getFileUrl();
                        //利用ImageRequest实现图片加载
                        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                        //构建ImageRequest 实例
                        ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                //给imageView设置图片
                                holder.UserImage.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //设置一张错误的图片，临时用ic_launcher代替
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                holder.UserImage.setImageResource(R.drawable.empty_image);
                            }
                        });

                        requestQueue.add(request);

                    } else {
                        holder.UserImage.setImageResource(R.drawable.empty_image);
                    }

                } else {
                    Toast.makeText(context, "获取摄影师订单失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.OrderId.setText("订单编号："+String.valueOf(order.getObjectId()));
        holder.OrderState.setText(order.getPhotographerType());
        holder.OrderGetTime.setText("拍照时间：" + order.getPutTime().getDate().substring(0,10));
        holder.OrderGetLoc.setText("拍照地点：" + order.getOrderStartLoc());
        holder.OrderCreateTime.setText(order.getCreatTime().getDate());
        holder.OrderPayState.setText(order.getOrderPayState());
        holder.OrderAcceptState.setText(order.getOrderAcceptState());
        holder.OrderFinishState.setText(order.getOrderFinishState());
        holder.OrderPrice.setText("支付金额："+String.valueOf(order.getOrderPrice())+"元");

    }
//        if (holder.OrderPayState.getText().equals("已支付"))
//            holder.OrderPayState.setTextColor(Integer.parseInt("@color/yellow"));
//        if (holder.OrderAcceptState.getText().equals("已接受"))
//            holder.OrderAcceptState.setTextColor(Integer.parseInt("@color/yellow"));
//        if (holder.OrderFinishState.getText().equals("已完成"))
//            holder.OrderFinishState.setTextColor(Integer.parseInt("@color/yellow"));



    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

}
