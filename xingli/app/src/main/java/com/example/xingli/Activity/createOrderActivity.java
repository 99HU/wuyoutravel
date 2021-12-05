package com.example.xingli.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.xingli.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import base.BaseActivity1;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import db.Order;
import db.Post;
import db.User;

public class createOrderActivity extends BaseActivity1 {
    public Post post;
    public Post startPost;
    public User user;
    public ImageView postImage;
    public TextView postName;
    public TextView postLoc;
    public TextView postOpenTime;
    public EditText putTime;
    public EditText getTime;
    public TextView postBigNum;
    public TextView postSmallNum;
    public TextView cancalTime;
    public TextView orderNotice;
    public AmountView amountBig;
    public AmountView amountSmall;
    public TextView orderPrice;
    public BmobDate start;
    public BmobDate end;
    public Date startDate;
    public Date endDate;
    public CheckBox checkOrderNotice;
    public TextView startPostLoc;
    public Button payButton;
    public ImageView packageImage;

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    private String stringImageUri;
    private String imagePath;
    private Bitmap bitmap;

    private BmobFile packageImageFile;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        Intent intent=getIntent();

        user = BmobUser.getCurrentUser(User.class);

        post=(Post) intent.getSerializableExtra("post");
        startPost=(Post)intent.getSerializableExtra("poststart");
        postImage=(ImageView) findViewById(R.id.order_post_image);
        postName=(TextView) findViewById(R.id.order_post_name);
        postLoc=(TextView)findViewById(R.id.order_post_Loc);
        postOpenTime=(TextView)findViewById(R.id.order_post_opentime);
        putTime=(EditText)findViewById(R.id.put_time);
        getTime=(EditText)findViewById(R.id.get_time);
        postBigNum=(TextView)findViewById(R.id.big_num);
        postSmallNum=(TextView)findViewById(R.id.small_num);
        cancalTime=(TextView)findViewById(R.id.cancel_time);
        orderNotice=(TextView)findViewById(R.id.order_notice);
        checkOrderNotice=(CheckBox)findViewById(R.id.check_rules);
        payButton=(Button)findViewById(R.id.pay_button);

        packageImage=(ImageView) findViewById(R.id.create_order_package_image);

        amountBig=(AmountView)findViewById(R.id.amount_big_view);
        amountSmall=(AmountView)findViewById(R.id.amount_small_view);
        orderPrice=(TextView)findViewById(R.id.order_price);
        startPostLoc=(TextView)findViewById(R.id.start_post_Loc);


        startPostLoc.setText(startPost.getPostName()+"  "+startPost.getPostLoc());
        amountBig.setGoods_storage(post.getLargerNum());
        amountSmall.setGoods_storage(post.getSmallNum());

        //postImage.setImageResource(post.getPostImageId());

        packageImage.setImageResource(R.drawable.add_image);

        if (post.getPostImage() != null) {

            BmobFile postImageFile = post.getPostImage();
            String stringPostImageUri = postImageFile.getFileUrl();
            //利用ImageRequest实现图片加载
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            //构建ImageRequest 实例
            ImageRequest request = new ImageRequest(stringPostImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //给imageView设置图片
                    postImage.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //设置一张错误的图片，临时用ic_launcher代替
                    Toast.makeText(createOrderActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    postImage.setImageResource(R.drawable.headimage);
                }
            });

            requestQueue.add(request);

        } else {
            postImage.setImageResource(R.drawable.headimage);
        }

        postName.setText(post.getPostName());
        postLoc.setText(post.getPostLoc());
        postOpenTime.setText(post.getPostOpenTime());
        postBigNum.setText("可存余量："+String.valueOf(post.getLargerNum()));
        postSmallNum.setText("可存余量："+String.valueOf(post.getSmallNum()));
        Calendar c = Calendar.getInstance();
        putTime.setText(c.get(Calendar.YEAR)+"-"+String.valueOf(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH)+"(最早可存入时间07:00)");
        cancalTime.setText(c.get(Calendar.YEAR)+"-"+String.valueOf(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH)+"  24点前取消订单收取订单金额20%取消费，之后不可取消");
        start=new BmobDate(new Date(c.get(Calendar.YEAR)-1900,c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)));
        //后续优化： 将时间选择器的时间范围限制一下
        putTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        createOrderActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year,
                                          int monthOfYear, int dayOfMonth) {
                        putTime.setText(year+"-"+String.valueOf(monthOfYear+1)+"-"+dayOfMonth+"(最早可存入时间07:00)");
                        start=new BmobDate(new Date(year-1900,monthOfYear,dayOfMonth));
                        cancalTime.setText(year+"-"+String.valueOf(monthOfYear+1)+"-"+dayOfMonth+"  24点前取消订单收取订单金额20%取消费，之后不可取消");
                        if (!getTime.getText().toString().isEmpty()) {
                            double money = 0;
                            try {
                                money = moneyCal();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            orderPrice.setText(String.valueOf(money) + "元");
                        }
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        getTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        createOrderActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year,
                                          int monthOfYear, int dayOfMonth) {

                        getTime.setText(year+"-"+String.valueOf(monthOfYear+1)+"-"+dayOfMonth+"(最晚可取回时间23:30)");
                        end=new BmobDate(new Date(year-1900,monthOfYear,dayOfMonth));
                        double money= 0;
                        try {
                            money = moneyCal();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        orderPrice.setText(String.valueOf(money)+"元");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        orderNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(createOrderActivity.this,NoticeActivity.class);
                startActivity(intent1);
            }
        });
        amountBig.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (!getTime.getText().toString().isEmpty()) {
                    double money = 0;
                    try {
                        money = moneyCal();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    orderPrice.setText(String.valueOf(money) + "元");
                }
            }
        });
        amountSmall.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (!getTime.getText().toString().isEmpty()) {
                    double money = 0;
                    try {
                        money = moneyCal();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    orderPrice.setText(String.valueOf(money) + "元");
                }
            }
        });
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderPrice.getText().toString().equals( "0.0元")||orderPrice.getText().toString().equals( "0元"))
                {
                    Toast.makeText(createOrderActivity.this,"请完善订单信息",Toast.LENGTH_SHORT).show();
                }
                if(checkOrderNotice.isChecked()!=true)
                {
                    Toast.makeText(createOrderActivity.this,"请接受预订须知",Toast.LENGTH_SHORT).show();
                }
                if(checkOrderNotice.isChecked()==true&&!orderPrice.getText().toString().equals( "0.0元")&&!orderPrice.getText().toString().equals( "0元"))
                {
                    ProgressDialog progressDialog=new ProgressDialog(createOrderActivity.this);
                    progressDialog.setTitle("正在下单");
                    progressDialog.setTitle("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Calendar c = Calendar.getInstance();
                    Order order = new Order();
//                    Order order = new Order(new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND)),
//                            start, end, Integer.parseInt(amountBig.getAmount()), Integer.parseInt(amountSmall.getAmount()),
//                             moneyCal(), startPost.getPostLoc(), post.getPostLoc(), 12314564, Order.Pay, Order.notAccept, Order.notFinish, Order.Post, startPost, post, user);

                    order.setCreatTime(new BmobDate(new Date(c.get(Calendar.YEAR)-1900,c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND))));
                    order.setPutTime(start);
                    order.setGetTime(end);
                    order.setOrderBigNum(Integer.parseInt(amountBig.getAmount()));
                    order.setOrderSmallNum(Integer.parseInt(amountSmall.getAmount()));
                    try {
                        order.setOrderPrice(moneyCal());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    order.setOrderMessage(12314564);
                    order.setOrderPayState(Order.Pay);
                    order.setOrderAcceptState(Order.notAccept);
                    order.setOrderFinishState(Order.notFinish);
                    order.setOrderType(Order.Post);


                    order.setStartPost(startPost);
                    order.setEndPost(post);
                    order.setUser(user);
                    order.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(createOrderActivity.this,"下单成功",Toast.LENGTH_SHORT).show();
                                packageImageFile.uploadblock(new UploadFileListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            order.setPackageImage(packageImageFile);
                                            order.update(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                                        Log.w("bbb", packageImageFile.getFileUrl());
                                                        Toast.makeText(createOrderActivity.this, "上传文件成功:" + packageImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                        Intent intent1=new Intent(createOrderActivity.this,createOrderSuccess.class);
                                                        intent1.putExtra("order",order);
                                                        intent1.putExtra("type","post");
                                                        startActivity(intent1);
                                                    } else {
                                                        Toast.makeText(createOrderActivity.this, "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        } else {
                                            Toast.makeText(createOrderActivity.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                });

                            } else {
                                Toast.makeText(createOrderActivity.this,"下单失败" + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    Order order=new Order( 1,1, post.getPostId(),startPost.getPostId(),new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND)),
//                            start,end,Integer.parseInt(amountBig.getAmount()),Integer.parseInt(amountSmall.getAmount()),
//                            moneyCal(),startPost.getPostLoc(),post.getPostLoc(), 12314564,Order.Pay,Order.notAccept,Order.notFinish,Order.Post,startPost,post);
                }
            }
        });

        packageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog mCameraDialog = new Dialog(createOrderActivity.this, R.style.BottomDialog);
                LinearLayout root = (LinearLayout) LayoutInflater.from(createOrderActivity.this).inflate(
                        R.layout.change_package_image_bottom_dialog, null);

                //初始化视图
                //打开相机
                root.findViewById(R.id.open_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(createOrderActivity.this,
                                    "com.example.xingli.fileprovider", outputImage);
                        } else {
                            imageUri = Uri.fromFile(outputImage);
                        }

                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        //以下两行代码适配Android 7.0 解决了无法加载图片的问题
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(intent, TAKE_PHOTO);
                        mCameraDialog.cancel();
                    }
                });
                //相册选择
                root.findViewById(R.id.choose_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(createOrderActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(createOrderActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            openAlbum();
                            mCameraDialog.cancel();
                        }
                    }
                });
                //取消
                root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCameraDialog.cancel();
                    }
                });


                mCameraDialog.setContentView(root);
                Window dialogWindow = mCameraDialog.getWindow();
                dialogWindow.setGravity(Gravity.BOTTOM);
                //dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
                WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                lp.x = 0; // 新位置X坐标
                lp.y = 0; // 新位置Y坐标
                lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
                root.measure(0, 0);
                lp.height = root.getMeasuredHeight();

                lp.alpha = 9f; // 透明度
                dialogWindow.setAttributes(lp);
                mCameraDialog.show();


            }
        });
    }
    public int dayCal() throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        startDate = sf.parse(start.getDate());
        endDate = sf.parse(end.getDate());
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000*3600*24));

        Log.d("day", String.valueOf(days));
        if (startDate.getTime()==endDate.getTime())
            return 1;
        else
            return days;

    }
    public double moneyCal() throws ParseException {
        int day=dayCal();
        double money;
        money=day*(Integer.parseInt(amountBig.getAmount())*post.getLargePrice()+Integer.parseInt(amountSmall.getAmount())*post.getSmallPrice());
        Log.d("money",String.valueOf(money));
        return money;
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        packageImage.setImageBitmap(bitmap);

                        imagePath = imageUri.getPath();
                        packageImageFile = new BmobFile(imageFactory());

                        Log.d("testtest", imagePath);
                        Toast.makeText(createOrderActivity.this, imagePath, Toast.LENGTH_SHORT).show();

                        //headImageFile = new BmobFile(new File(imagePath));
//                        packageImageFile.uploadblock(new UploadFileListener() {
//                            @Override
//                            public void done(BmobException e) {
//                                if (e == null) {
//
//                                    manager.setManagerImage(packageImageFile);
//                                    manager.update(new UpdateListener() {
//                                        @Override
//                                        public void done(BmobException e) {
//                                            if (e == null) {
//                                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                                                Log.w("bbb", packageImageFile.getFileUrl());
//                                                Toast.makeText(createOrderActivity.this, "上传文件成功:" + packageImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                Toast.makeText(createOrderActivity.this, "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//
//                                } else {
//                                    Toast.makeText(createOrderActivity.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        //重复，后期封装一下
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content//downloads//public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }


        displayImage(imagePath);
        //图片上传
        packageImageFile = new BmobFile(new File(imagePath));
        Log.d("testtest", imagePath);
//        packageImageFile.uploadblock(new UploadFileListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e == null) {
//                    manager.setManagerImage(packageImageFile);
//                    manager.update(new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e == null) {
//                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                                Log.w("bbb", packageImageFile.getFileUrl());
//                                Toast.makeText(createOrderActivity.this, "上传文件成功:" + packageImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(createOrderActivity.this, "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                } else {
//                    Toast.makeText(createOrderActivity.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        displayImage(imagePath);
        //TODO:图片上传
        packageImageFile = new BmobFile(new File(imagePath));
//        packageImageFile.uploadblock(new UploadFileListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e == null) {
//                    manager.setManagerImage(packageImageFile);
//                    manager.update(new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e == null) {
//                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                                Log.w("bbb", packageImageFile.getFileUrl());
//                                Toast.makeText(createOrderActivity.this, "上传文件成功:" + packageImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(createOrderActivity.this, "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                } else {
//                    Toast.makeText(createOrderActivity.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            packageImage.setImageBitmap(bitmap);
        } else {
            Toast.makeText(createOrderActivity.this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private File imageFactory(){
        File root= getExternalCacheDir();
        File pic=new File(root,"test.jpg");
        try {
            FileOutputStream fos=new FileOutputStream(pic);
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pic;
    }
}