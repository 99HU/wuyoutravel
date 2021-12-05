package com.example.poststation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class OrderRecAdapter extends RecyclerView.Adapter<OrderRecAdapter.ViewHolder>  implements Filterable {
    private List<Order> mOrderList=new ArrayList<>();
    Context context;
    private List<Order> mFilterList = new ArrayList<>();

    private BmobFile packageImageFile;
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
                            if (str.getObjectId().contains(charSequence)||String.valueOf(str.getOrderMessage()).equals(charSequence)) {
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
        ImageView PeckageImage;
        TextView OrderId;
        TextView OrderBigNum;
        TextView OrderSmallNum;
        TextView OrderGetTime;
        TextView OrderCreateTime;
        TextView OrderPayState;
        TextView OrderAcceptState;
        TextView OrderFinishState;
        TextView Message;
        TextView OrderPrice;
        Button SeeMessage;
        View PostView;
        public ViewHolder(View view){
            super(view);
            PostView=view;
            OrderId=(TextView)view.findViewById(R.id.orderlist_id);
            PeckageImage=(ImageView) view.findViewById(R.id.orderlist_post_image);
            OrderBigNum=(TextView) view.findViewById(R.id.orderlist_bignum);
            OrderSmallNum=(TextView)view.findViewById(R.id.orderlist_smallnum);
            OrderGetTime=(TextView)view.findViewById(R.id.orderlist_gettime);
            OrderCreateTime=(TextView) view.findViewById(R.id.orderlist_create_time);
            OrderPayState=(TextView) view.findViewById(R.id.orderlist_pay_state);
            OrderAcceptState=(TextView) view.findViewById(R.id.orderlist_accept_state);
            OrderFinishState=(TextView) view.findViewById(R.id.orderlist_finish_state);
            SeeMessage=(Button) view.findViewById(R.id.see_Message);
            Message =(TextView)view.findViewById(R.id.Message);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_order,parent,false);
        final OrderRecAdapter.ViewHolder holder=new OrderRecAdapter.ViewHolder(view);
        holder.PostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Order order=mFilterList.get(position);
                PostHomePage.UpdateFlag=true;
                Intent intent=new Intent(context,OrderDetailActivity.class);
                intent.putExtra("order",order);
                context.startActivity(intent);
            }
        });
        holder.Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.Message.setVisibility(View.GONE);
                holder.SeeMessage.setVisibility(View.VISIBLE);

            }
        });
        holder.SeeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.SeeMessage.setVisibility(View.GONE);
                holder.Message.setVisibility(View.VISIBLE);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecAdapter.ViewHolder holder, int position) {
        Order order=mFilterList.get(position);
        if (order.getPackageImage() != null) {

            packageImageFile = order.getPackageImage() ;
            stringImageUri = packageImageFile.getFileUrl();
            //利用ImageRequest实现图片加载
            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            //构建ImageRequest 实例
            ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //给imageView设置图片
                    holder.PeckageImage.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //设置一张错误的图片，临时用ic_launcher代替
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    holder.PeckageImage.setImageResource(R.drawable.empty_image);
                }
            });

            requestQueue.add(request);

        } else {
            holder.PeckageImage.setImageResource(R.drawable.empty_image);
        }
           // holder.PeckageImage.setImageResource(order.getPackageImage());

            holder.OrderId.setText("订单编号："+String.valueOf(order.getObjectId()));
            holder.OrderBigNum.setText(String.valueOf(order.getOrderBigNum())+"X");
            holder.OrderSmallNum.setText(String.valueOf(order.getOrderSmallNum())+"X");
            Manager   manager = BmobUser.getCurrentUser(Manager.class);
            //当前是起始驿站
            if (manager.getPost().getObjectId().equals(order.getStartPost().getObjectId()))
            {
                holder.OrderGetTime.setText("寄存时间："+order.getPutTime().getDate().substring(0,10));
            }
            //当前是重点驿站
            if (manager.getPost().getObjectId().equals(order.getEndPost().getObjectId()))
            {
                holder.OrderGetTime.setText("取件时间："+order.getGetTime().getDate().substring(0,10));

             }
            holder.OrderCreateTime.setText(order.getCreatTime().getDate());
            holder.OrderPayState.setText(order.getOrderPayState());
            holder.OrderAcceptState.setText(order.getOrderAcceptState());
            holder.OrderFinishState.setText(order.getOrderFinishState());
            holder.Message.setText(String.valueOf(order.getOrderMessage()));
            holder.OrderPrice.setText("支付金额："+String.valueOf(order.getOrderPrice())+"元");
            if (order.getOrderAcceptState().equals(Order.notAccept))
            {
                holder.Message.setVisibility(View.GONE);
                holder.SeeMessage.setVisibility(View.GONE);
            }
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
