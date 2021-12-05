package com.example.poststation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PostStationInfo extends AppCompatActivity {

    private Manager manager;

    private Post post;

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    private String stringImageUri;
    private String imagePath;
    private Bitmap bitmap;
    private ImageView headImage;
    private BmobFile PostImage;

    private ImageView back2info;
    private Button change_headImage;
    private Button confirm_changes;
    private boolean change = false;



    private EditText PostName;
    private EditText PostId;
    private EditText PostTel;
    private EditText PostLoc;
    private EditText PostProfile;
    private EditText PostOrderNum;
    private EditText largePrice;
    private EditText smallPrice;
    private EditText PostNotice1;
    private EditText PostNotice2;
    private EditText PostNotice3;
    private EditText PostHolderSay;
    private EditText PostOpenTime;
    private EditText largerNum;
    private EditText smallNum;

    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    confirm_changes.setText("修改信息");

                    //更新信息
                    PostName.setEnabled(false);
                    PostTel.setEnabled(false);
                    PostLoc.setEnabled(false);
                    PostProfile.setEnabled(false);
                    PostOrderNum.setEnabled(false);
                    largePrice.setEnabled(false);
                    smallPrice.setEnabled(false);
                    PostNotice1.setEnabled(false);
                    PostNotice2.setEnabled(false);
                    PostNotice3.setEnabled(false);
                    PostHolderSay.setEnabled(false);
                    PostOpenTime.setEnabled(false);
                    largerNum.setEnabled(false);
                    smallNum.setEnabled(false);
                    post.setPostName(PostName.getText().toString());
                    post.setPostTel(PostTel.getText().toString());
                    post.setPostLoc(PostLoc.getText().toString());
                    post.setPostProfile(PostProfile.getText().toString());
                    post.setPostOrderNum(Integer.parseInt(PostOrderNum.getText().toString()));
                    post.setLargePrice(Double.parseDouble(largePrice.getText().toString()));
                    post.setSmallPrice(Double.parseDouble(smallPrice.getText().toString()));
                    post.setPostNotice1(PostNotice1.getText().toString());
                    post.setPostNotice2(PostNotice2.getText().toString());
                    post.setPostNotice3(PostNotice3.getText().toString());
                    post.setPostHolderSay(PostHolderSay.getText().toString());
                    post.setPostOpenTime(PostOpenTime.getText().toString());
                    post.setLargerNum(Integer.parseInt(largerNum.getText().toString()));
                    post.setSmallNum(Integer.parseInt(smallNum.getText().toString()));
                    post.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(PostStationInfo.this, "保存成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PostStationInfo.this, "保存失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    change = false;
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    confirm_changes.setText("修改信息");

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_station_info);

        headImage = (ImageView) findViewById(R.id.head_image);

        back2info = (ImageView) findViewById(R.id.back2info);
        change_headImage = (Button) findViewById(R.id.change_headImage);
        confirm_changes = (Button) findViewById(R.id.confirm);

        PostName = (EditText) findViewById(R.id.postName);
        PostId = (EditText) findViewById(R.id.postId);
        PostTel = (EditText) findViewById(R.id.postTel);
        PostLoc = (EditText) findViewById(R.id.postLoc);
        PostProfile = (EditText) findViewById(R.id.postProfile);
        PostOrderNum = (EditText) findViewById(R.id.postOrderNum);
        largePrice = (EditText) findViewById(R.id.largePrice);
        smallPrice = (EditText) findViewById(R.id.samllPrice);
        PostNotice1 = (EditText) findViewById(R.id.postNotice1);
        PostNotice2 = (EditText) findViewById(R.id.postNotice2);
        PostNotice3 = (EditText) findViewById(R.id.postNotice3);
        PostHolderSay = (EditText) findViewById(R.id.postHolderSay);
        PostOpenTime = (EditText) findViewById(R.id.postOpenTime);
        largerNum = (EditText) findViewById(R.id.largeNum);
        smallNum = (EditText) findViewById(R.id.smallNum);
        confirm_changes.setText("修改信息");

        if (BmobUser.isLogin()) {
            manager = BmobUser.getCurrentUser(Manager.class);

            if (manager.getPost() == null) {
                //新建驿站并与之关联
                BmobQuery<Post> query = new BmobQuery<>();
                query.addWhereEqualTo("manager", manager);
                query.findObjects(new FindListener<Post>() {
                    @Override
                    public void done(List<Post> list, BmobException e) {
                        if (e == null) {
                            updateManager(manager, list.get(0));
                        } else {
                            Toast.makeText(PostStationInfo.this, "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //设置初始图片
                headImage.setImageResource(R.drawable.headimage);

            } else {
                post = manager.getPost();
                queryPost();

            }
        }

        back2info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        change_headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog mCameraDialog = new Dialog(PostStationInfo.this, R.style.BottomDialog);
                LinearLayout root = (LinearLayout) LayoutInflater.from(PostStationInfo.this).inflate(
                        R.layout.change_headimage_bottom_dialog, null);

                //初始化视图
                //打开相机
                root.findViewById(R.id.open_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            } outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(PostStationInfo.this,
                                    "com.example.poststation.fileprovider", outputImage);
                        } else {
                            imageUri = Uri.fromFile(outputImage);
                        }

                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        //以下两行代码适配Android 7.0 解决了无法加载图片的问题
                        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        //intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(intent, TAKE_PHOTO);
                        mCameraDialog.cancel();
                    }
                });
                //相册选择
                root.findViewById(R.id.choose_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(PostStationInfo.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PostStationInfo.this,
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

        confirm_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!change) {
                    confirm_changes.setText("确认更改");
                    PostName.setEnabled(true);
                    PostTel.setEnabled(true);
                    PostLoc.setEnabled(true);
                    PostProfile.setEnabled(true);
                    largePrice.setEnabled(true);
                    smallPrice.setEnabled(true);
                    PostNotice1.setEnabled(true);
                    PostNotice2.setEnabled(true);
                    PostNotice3.setEnabled(true);
                    PostHolderSay.setEnabled(true);
                    PostOpenTime.setEnabled(true);
                    largerNum.setEnabled(true);
                    smallNum.setEnabled(true);
                    change = true;
                    Toast.makeText(PostStationInfo.this, "现在可以编辑信息", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog isExit = new AlertDialog.Builder(PostStationInfo.this).create();
                    isExit.setTitle("系统提示");
                    // 设置对话框消息
                    isExit.setMessage("确定修改信息？");
                    // 添加选择按钮并注册监听
                    isExit.setButton("确定", listener2);
                    isExit.setButton2("取消", listener2);
                    // 显示对话框
                    isExit.show();
                }
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        headImage.setImageBitmap(bitmap);
                        //TODO:图片上传

                        imagePath = imageUri.getPath();
                        PostImage = new BmobFile(imageFactory());

                        Log.d("testtest", imagePath);
                        Toast.makeText(PostStationInfo.this, imagePath, Toast.LENGTH_SHORT).show();

                        //headImageFile = new BmobFile(new File(imagePath));
                        PostImage.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    post.setPostImage(PostImage);
                                    post.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                                Log.w("bbb", PostImage.getFileUrl());
                                                Toast.makeText(PostStationInfo.this, "上传文件成功:" + PostImage.getFileUrl(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(PostStationInfo.this, "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(PostStationInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });





                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat (data);
                    } else {
                        handleImageBeforeKitKat (data);
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
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
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
        PostImage = new BmobFile(new File(imagePath));
        //TODO:图片上传
        PostImage = new BmobFile(new File(imagePath));
        Log.d("testtest", imagePath);
        PostImage.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    post.setPostImage(PostImage);
                    post.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                Log.w("bbb", PostImage.getFileUrl());
                                Toast.makeText(PostStationInfo.this, "上传文件成功:" + PostImage.getFileUrl(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PostStationInfo.this, "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(PostStationInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
        //TODO:图片上传

        PostImage = new BmobFile(new File(imagePath));
        Log.d("testtest", imagePath);
        PostImage.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    post.setPostImage(PostImage);
                    post.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                Log.w("bbb", PostImage.getFileUrl());
                                Toast.makeText(PostStationInfo.this, "上传文件成功:" + PostImage.getFileUrl(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PostStationInfo.this, "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(PostStationInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });

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
            headImage.setImageBitmap(bitmap);
        } else {
            Toast.makeText(PostStationInfo.this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateManager(Manager manager, Post post) {
        manager.setPost(post);
        manager.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(PostStationInfo.this, "专属驿站保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostStationInfo.this, "专属驿站保存失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void queryPost() {
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", post.getObjectId());
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (list != null) {
                    post = list.get(0);
                    postInfoUpdate();
                } else {
                    Toast.makeText(PostStationInfo.this, "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void postInfoUpdate() {
        PostName.setText(post.getPostName());
        PostId.setText(post.getObjectId());
        PostTel.setText(post.getPostTel());
        PostLoc.setText(post.getPostLoc());
        PostProfile.setText(post.getPostProfile());
        PostOrderNum.setText(String.valueOf(post.getPostOrderNum()));
        largePrice.setText(String.valueOf(post.getLargePrice()));
        smallPrice.setText(String.valueOf(post.getSmallPrice()));
        PostNotice1.setText(post.getPostNotice1());
        PostNotice2.setText(post.getPostNotice2());
        PostNotice3.setText(post.getPostNotice3());
        PostHolderSay.setText(post.getPostHolderSay());
        PostOpenTime.setText(post.getPostOpenTime());
        largerNum.setText(String.valueOf(post.getLargerNum()));
        smallNum.setText(String.valueOf(post.getSmallNum()));
        //TODO:下载头像并显示


        if (post.getPostImage() != null) {
            PostImage = post.getPostImage();
            stringImageUri = PostImage.getFileUrl();
            //利用ImageRequest实现图片加载
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            //构建ImageRequest 实例
            ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //给imageView设置图片
                    headImage.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //设置一张错误的图片，临时用ic_launcher代替
                    Toast.makeText(PostStationInfo.this, error.toString(), Toast.LENGTH_SHORT).show();
                    headImage.setImageResource(R.drawable.headimage);
                }
            });
        requestQueue.add(request);
        } else {
            headImage.setImageResource(R.drawable.headimage);
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