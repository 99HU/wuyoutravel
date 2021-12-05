package Adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.xingli.Activity.GuideDetailActivity;
import com.example.xingli.Activity.GuideOrderDetailActivity;
import com.example.xingli.Activity.OrderFragment;
import com.example.xingli.Activity.PhotographerOrderDetailActivity;
import com.example.xingli.Activity.orderDetailActivity;
import com.example.xingli.Activity.photographerDetailActivity;
import com.example.xingli.Activity.postDetailActivity;
import com.example.xingli.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import db.Guide;
import db.Order;
import db.Photographer;
import db.Post;

public class OrderRecAdapter extends RecyclerView.Adapter<OrderRecAdapter.ViewHolder>  implements Filterable {
    private List<Order> mOrderList=new ArrayList<>();
    Context context;
    private List<Order> mFilterList = new ArrayList<>();

    private Post startPost;
    private Post endPost;

    private Photographer photographer;
    private Guide guide;
    private Date createDate;


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
                        if (str.getOrderType().equals(Order.Post)) {

                            BmobQuery<Post> bmobQueryStart = new BmobQuery<>();
                            bmobQueryStart.addWhereEqualTo("objectId", str.getEndPost().getObjectId());
                            BmobQuery<Post> bmobQueryEnd = new BmobQuery<>();
                            bmobQueryEnd.addWhereEqualTo("objectId", str.getStartPost().getObjectId());
                            List<BmobQuery<Post>> queries = new ArrayList<BmobQuery<Post>>();
                            queries.add(bmobQueryStart);
                            queries.add(bmobQueryEnd);
                            BmobQuery<Post> mainQuery = new BmobQuery<>();
                            mainQuery.or(queries);
                            mainQuery.findObjects(new FindListener<Post>() {
                                @Override
                                public void done(List<Post> list, BmobException e) {
                                    if (e == null) {
                                        if (list.get(0).getObjectId().equals(str.getStartPost().getObjectId())) {
                                            startPost = list.get(0);
                                            endPost = list.get(1);
                                        } else {
                                            startPost = list.get(1);
                                            endPost = list.get(0);
                                        }
                                        if (startPost.getPostName().contains(charSequence) ||
                                                endPost.getPostName().contains(charSequence) || startPost.getPostLoc().contains(charSequence)
                                                || endPost.getPostLoc().contains(charSequence)) {
                                            filteredList.add(str);
                                        }
                                    } else {
                                        Toast.makeText(context, "Post相关信息查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else if (str.getOrderType().equals(Order.PhotoGrapher)){
                            BmobQuery<Photographer> bmobQuery = new BmobQuery<>();
                            bmobQuery.addWhereEqualTo("objectId", str.getPhotographer().getObjectId());
                            bmobQuery.findObjects(new FindListener<Photographer>() {
                                @Override
                                public void done(List<Photographer> list, BmobException e) {
                                    if (e == null) {
                                        photographer=list.get(0);
                                        if (photographer.getNickName().contains(charSequence)) {
                                            filteredList.add(str);
                                        }
                                    } else {
                                        Toast.makeText(context, "Post相关信息查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                        else {
                            BmobQuery<Guide> bmobQuery = new BmobQuery<>();
                            bmobQuery.addWhereEqualTo("objectId", str.getguide().getObjectId());
                            bmobQuery.findObjects(new FindListener<Guide>() {
                                @Override
                                public void done(List<Guide> list, BmobException e) {
                                    if (e == null) {
                                        guide=list.get(0);
                                        if (guide.getNickName().contains(charSequence)) {
                                            filteredList.add(str);
                                        }
                                    } else {
                                        Toast.makeText(context, "guide相关信息查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
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
        ImageView PostImage;
        TextView OrderType;
        TextView OrderMainName;
        TextView PostEndName;
        TextView OrderGetTime;
        TextView OrderGetLoc;
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
            OrderType=(TextView)view.findViewById(R.id.orderlist_type);
            PostImage=(ImageView) view.findViewById(R.id.orderlist_post_image);
            OrderMainName=(TextView) view.findViewById(R.id.orderlist_mian_name);
            PostEndName=(TextView)view.findViewById(R.id.orderlist_end_post_name);
            OrderGetTime=(TextView)view.findViewById(R.id.orderlist_getTime);
            OrderGetLoc=(TextView)view.findViewById(R.id.orderlist_getLoc);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_rec,parent,false);
        final OrderRecAdapter.ViewHolder holder=new OrderRecAdapter.ViewHolder(view);
        holder.PostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Order order=mFilterList.get(position);
                OrderFragment.updateFlag=true;
                //当前是驿站订单
                if (order.getOrderType().equals(Order.Post)) {
                    Intent intent = new Intent(context, orderDetailActivity.class);

                    BmobQuery<Post> bmobQueryStart = new BmobQuery<>();
                    bmobQueryStart.addWhereEqualTo("objectId", order.getEndPost().getObjectId());
                    BmobQuery<Post> bmobQueryEnd = new BmobQuery<>();
                    bmobQueryEnd.addWhereEqualTo("objectId", order.getStartPost().getObjectId());
                    List<BmobQuery<Post>> queries = new ArrayList<BmobQuery<Post>>();
                    queries.add(bmobQueryStart);
                    queries.add(bmobQueryEnd);
                    BmobQuery<Post> mainQuery = new BmobQuery<>();
                    mainQuery.or(queries);
                    mainQuery.findObjects(new FindListener<Post>() {
                        @Override
                        public void done(List<Post> list, BmobException e) {
                            if (e == null) {
                                if (list.get(0).getObjectId().equals(order.getStartPost().getObjectId())) {
                                    startPost = list.get(0);
                                    endPost = list.get(1);
                                } else {
                                    startPost = list.get(1);
                                    endPost = list.get(0);
                                }
                                intent.putExtra("endpost", endPost);
                                intent.putExtra("startpost", startPost);

                                intent.putExtra("choose", "Post");
                                intent.putExtra("order", order);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "Post相关信息查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                //当前是摄影师订单
                if (order.getOrderType().equals(Order.PhotoGrapher)){
                    Intent intent = new Intent(context, PhotographerOrderDetailActivity.class);
                    intent.putExtra("order", order);
                    context.startActivity(intent);
                }

                if (order.getOrderType().equals(Order.Guide)){
                    Intent intent = new Intent(context, GuideOrderDetailActivity.class);
                    intent.putExtra("order", order);
                    context.startActivity(intent);
                }
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
        holder.OrderMainName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Order order=mFilterList.get(position);

                //如果当前是驿站订单
                if (order.getOrderType().equals(Order.Post)) {
                    Intent intent = new Intent(context, postDetailActivity.class);

                    BmobQuery<Post> bmobQuery1 = new BmobQuery<>();
                    bmobQuery1.addWhereEqualTo("objectId", order.getStartPost().getObjectId());
                    bmobQuery1.findObjects(new FindListener<Post>() {
                        @Override
                        public void done(List<Post> list, BmobException e) {
                            if (e == null) {
                                startPost = list.get(0);

                                intent.putExtra("choose", "see");
                                intent.putExtra("post", startPost);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "Post相关信息查询失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                //当前是摄影师订单
                if (order.getOrderType().equals(Order.PhotoGrapher)) {
                    Intent intent = new Intent(context, photographerDetailActivity.class);
                    BmobQuery<Photographer> bmobQuery1 = new BmobQuery<>();
                    bmobQuery1.addWhereEqualTo("objectId", order.getPhotographer().getObjectId());
                    bmobQuery1.findObjects(new FindListener<Photographer>() {
                        @Override
                        public void done(List<Photographer> list, BmobException e) {
                            if (e == null) {
                                photographer = list.get(0);
                                intent.putExtra("choose", "see");
                                intent.putExtra("photographer", photographer);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "Post相关信息查询失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
                if (order.getOrderType().equals(Order.Guide)) {
                    Intent intent = new Intent(context, GuideDetailActivity.class);

                    BmobQuery<Guide> bmobQuery1 = new BmobQuery<>();
                    bmobQuery1.addWhereEqualTo("objectId", order.getguide().getObjectId());
                    bmobQuery1.findObjects(new FindListener<Guide>() {
                        @Override
                        public void done(List<Guide> list, BmobException e) {
                            if (e == null) {
                                guide = list.get(0);

                                intent.putExtra("choose", "see");
                                intent.putExtra("guide", guide);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "Post相关信息查询失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
        holder.PostEndName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Order order=mFilterList.get(position);

                BmobQuery<Post> bmobQuery2 = new BmobQuery<>();
                bmobQuery2.addWhereEqualTo("objectId", order.getEndPost().getObjectId());
                bmobQuery2.findObjects(new FindListener<Post>() {
                    @Override
                    public void done(List<Post> list, BmobException e) {
                        if (e == null) {
                            endPost = list.get(0);
                            Intent intent=new Intent(context,postDetailActivity.class);
                            intent.putExtra("choose","see");
                            intent.putExtra("post", endPost);
                            context.startActivity(intent);

                        } else {
                            Toast.makeText(context, "Post相关信息查询失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecAdapter.ViewHolder holder, int position) {
        Order order=mFilterList.get(position);
        //如果是驿站订单
        if (order.getOrderType().equals(Order.Post)) {
            holder.OrderType.setText("驿站订单");
            holder.SeeMessage.setVisibility(View.VISIBLE);
            holder.OrderGetTime.setVisibility(View.GONE);
            holder.OrderGetLoc.setVisibility(View.GONE);
            holder.PostEndName.setVisibility(View.VISIBLE);
            holder.PostImage.setImageResource(order.getEndPost().getPostImageId());

            //读取数据库的头像，若无则设置默认头像
            if (order.getPackageImage() != null) {

                BmobFile PostImageFile = order.getPackageImage();
                String stringImageUri = PostImageFile.getFileUrl();
                //利用ImageRequest实现图片加载
                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                //构建ImageRequest 实例
                ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        //给imageView设置图片
                        holder.PostImage.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //设置一张错误的图片，临时用ic_launcher代替
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        holder.PostImage.setImageResource(R.drawable.headimage);
                    }
                });

                requestQueue.add(request);

            } else {
                holder.PostImage.setImageResource(R.drawable.headimage);
            }
            BmobQuery<Post> bmobQuery1 = new BmobQuery<>();
            bmobQuery1.addWhereEqualTo("objectId", order.getStartPost().getObjectId());
            bmobQuery1.findObjects(new FindListener<Post>() {
                @Override
                public void done(List<Post> list, BmobException e) {
                    if (e == null) {
                        startPost = list.get(0);
                        holder.OrderMainName.setText("起始:" + startPost.getPostName());
                    } else {
                        Toast.makeText(context, "Post相关信息查询失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            BmobQuery<Post> bmobQuery2 = new BmobQuery<>();
            bmobQuery2.addWhereEqualTo("objectId", order.getEndPost().getObjectId());
            bmobQuery2.findObjects(new FindListener<Post>() {
                @Override
                public void done(List<Post> list, BmobException e) {
                    if (e == null) {
                        endPost = list.get(0);
                        holder.PostEndName.setText("终点:" +endPost.getPostName());
                    } else {
                        Toast.makeText(context, "Post相关信息查询失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.OrderCreateTime.setText(order.getCreatTime().getDate());
            holder.OrderPayState.setText(order.getOrderPayState());
            holder.OrderAcceptState.setText(order.getOrderAcceptState());
            holder.OrderFinishState.setText(order.getOrderFinishState());
            holder.Message.setText(String.valueOf(order.getOrderMessage()));
            holder.OrderPrice.setText("支付金额："+String.valueOf(order.getOrderPrice())+"元");
        }
        //如果是摄影师订单
        if (order.getOrderType().equals(Order.PhotoGrapher))
        {
            holder.OrderType.setText("摄影师订单");
            //holder.PostImage.setImageResource(R.drawable.feng);
            holder.OrderGetLoc.setVisibility(View.VISIBLE);
            holder.OrderGetTime.setVisibility(View.VISIBLE);
            holder.OrderMainName.setVisibility(View.VISIBLE);
            holder.PostEndName.setVisibility(View.GONE);
            holder.Message.setVisibility(View.GONE);
            holder.SeeMessage.setVisibility(View.GONE);

            BmobQuery<Photographer> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("objectId", order.getPhotographer().getObjectId());
            bmobQuery.findObjects(new FindListener<Photographer>() {
                @Override
                public void done(List<Photographer> list, BmobException e) {
                    photographer = list.get(0);
                    if (photographer.getHeadImage() != null) {

                        //读取数据库的头像，若无则设置默认头像
                        BmobFile PostImageFile = photographer.getHeadImage();
                        String stringImageUri = PostImageFile.getFileUrl();
                        //利用ImageRequest实现图片加载
                        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                        //构建ImageRequest 实例
                        ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                //给imageView设置图片
                                holder.PostImage.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //设置一张错误的图片，临时用ic_launcher代替
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                holder.PostImage.setImageResource(R.drawable.headimage);
                            }
                        });

                        requestQueue.add(request);

                    } else {
                        holder.PostImage.setImageResource(R.drawable.headimage);
                    }

                    holder.OrderMainName.setText(photographer.getNickName());
                    holder.OrderGetTime.setText("拍摄时间"+order.getPutTime().getDate().substring(0,10));
                    holder.OrderGetLoc.setText("拍摄地点"+order.getOrderStartLoc());
                    holder.OrderCreateTime.setText(order.getCreatTime().getDate());
                    holder.OrderPayState.setText(order.getOrderPayState());
                    holder.OrderAcceptState.setText(order.getOrderAcceptState());
                    holder.OrderFinishState.setText(order.getOrderFinishState());
                    holder.OrderPrice.setText("支付金额："+String.valueOf(order.getOrderPrice())+"元");


                }
            });


        }
        if (order.getOrderType().equals(Order.Guide))
        {
            holder.OrderType.setText("向导订单");

            //holder.PostImage.setImageResource(R.drawable.feng);
            holder.OrderGetLoc.setVisibility(View.VISIBLE);
            holder.OrderGetTime.setVisibility(View.VISIBLE);
            holder.OrderMainName.setVisibility(View.VISIBLE);
            holder.PostEndName.setVisibility(View.GONE);
            holder.Message.setVisibility(View.GONE);
            holder.SeeMessage.setVisibility(View.GONE);

            BmobQuery<Guide> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("objectId", order.getguide().getObjectId());
            bmobQuery.findObjects(new FindListener<Guide>() {
                @Override
                public void done(List<Guide> list, BmobException e) {
                    guide = list.get(0);
                    if (guide.getHeadImage() != null) {

                        //读取数据库的头像，若无则设置默认头像
                        BmobFile PostImageFile = guide.getHeadImage();
                        String stringImageUri = PostImageFile.getFileUrl();
                        //利用ImageRequest实现图片加载
                        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                        //构建ImageRequest 实例
                        ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                //给imageView设置图片
                                holder.PostImage.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //设置一张错误的图片，临时用ic_launcher代替
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                holder.PostImage.setImageResource(R.drawable.headimage);
                            }
                        });

                        requestQueue.add(request);

                    } else {
                        holder.PostImage.setImageResource(R.drawable.headimage);
                    }

                    holder.OrderMainName.setText(guide.getNickName());
                    holder.PostEndName.setVisibility(View.GONE);
                    holder.OrderCreateTime.setText(order.getCreatTime().getDate());
                    holder.OrderPayState.setText(order.getOrderPayState());
                    holder.OrderAcceptState.setText(order.getOrderAcceptState());
                    holder.OrderFinishState.setText(order.getOrderFinishState());
                    holder.OrderPrice.setText("支付金额："+String.valueOf(order.getOrderPrice())+"元");

                    holder.OrderGetTime.setText("游玩时间"+order.getPutTime().getDate().substring(0,10));
                    holder.OrderGetLoc.setText("游玩地点"+order.getOrderStartLoc());

                }
            });


        }
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
