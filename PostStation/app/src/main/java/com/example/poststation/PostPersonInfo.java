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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PostPersonInfo extends AppCompatActivity {

    private String sdCardDir = Environment.getExternalStorageDirectory() + "/fingerprintimages/";

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    private String stringImageUri;
    private String imagePath;
    private Bitmap bitmap;
    private ImageView headImage;
    private BmobFile headImageFile;
    private Manager manager; //存储现在登录的用户

    private ImageView back2info;
    private Button change_headImage;
    private Button confirm_changes;
    private boolean change = false;

    private EditText managerName;
    private EditText managerGender;
    private EditText managerAge;
    private EditText managerEmail;
    private EditText managerTel;

    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    confirm_changes.setText("修改信息");
                    //更新信息
                    managerName.setEnabled(false);
                    managerGender.setEnabled(false);
                    managerAge.setEnabled(false);
                    managerEmail.setEnabled(false);
                    managerTel.setEnabled(false);
                    manager.setNickName(managerName.getText().toString());
                    manager.setGender(managerGender.getText().toString());
                    manager.setAge(Integer.parseInt(managerAge.getText().toString()));
                    manager.setEmail(managerEmail.getText().toString());
                    manager.setMobilePhoneNumber(managerTel.getText().toString());
                    manager.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(PostPersonInfo.this, "保存成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PostPersonInfo.this, "保存失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_person_info);

        back2info = (ImageView) findViewById(R.id.back2info);
        change_headImage = (Button) findViewById(R.id.change_headImage);
        confirm_changes = (Button) findViewById(R.id.confirm);

        headImage = (ImageView) findViewById(R.id.head_image);

        managerName = (EditText) findViewById(R.id.managerName);
        managerGender = (EditText) findViewById(R.id.gender);
        managerAge = (EditText) findViewById(R.id.age);
        managerEmail = (EditText) findViewById(R.id.email);
        managerTel = (EditText) findViewById(R.id.tel);
        confirm_changes.setText("修改信息");
        if (BmobUser.isLogin()) {
            manager = BmobUser.getCurrentUser(Manager.class);

            //初始化信息
            managerName.setText(manager.getNickName());
            managerGender.setText(manager.getGender());
            managerAge.setText(String.valueOf(manager.getAge()));
            managerEmail.setText(manager.getEmail());
            managerTel.setText(manager.getMobilePhoneNumber());

            //读取数据库的头像，若无则设置默认头像
            if (manager.getManagerImage() != null) {

                headImageFile = manager.getManagerImage();
                stringImageUri = headImageFile.getFileUrl();
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
                        Toast.makeText(PostPersonInfo.this, error.toString(), Toast.LENGTH_SHORT).show();
                        headImage.setImageResource(R.drawable.headimage);
                    }
                });

                requestQueue.add(request);

            } else {
                headImage.setImageResource(R.drawable.headimage);
            }
        } else {
            finish();
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
                Dialog mCameraDialog = new Dialog(PostPersonInfo.this, R.style.BottomDialog);
                LinearLayout root = (LinearLayout) LayoutInflater.from(PostPersonInfo.this).inflate(
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
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(PostPersonInfo.this,
                                    "com.example.poststation.fileprovider", outputImage);
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
                        if (ContextCompat.checkSelfPermission(PostPersonInfo.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PostPersonInfo.this,
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
                    managerName.setEnabled(true);
                    managerGender.setEnabled(true);
                    managerAge.setEnabled(true);
                    managerEmail.setEnabled(true);
                    managerTel.setEnabled(true);
                    change = true;
                    confirm_changes.setText("确认修改");
                    Toast.makeText(PostPersonInfo.this, "现在可以编辑信息", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog isExit = new AlertDialog.Builder(PostPersonInfo.this).create();
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
        });        back2info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        change_headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog mCameraDialog = new Dialog(PostPersonInfo.this, R.style.BottomDialog);
                LinearLayout root = (LinearLayout) LayoutInflater.from(PostPersonInfo.this).inflate(
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
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(PostPersonInfo.this,
                                    "com.example.poststation.fileprovider", outputImage);
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
                        if (ContextCompat.checkSelfPermission(PostPersonInfo.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PostPersonInfo.this,
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
                    managerName.setEnabled(true);
                    managerGender.setEnabled(true);
                    managerAge.setEnabled(true);
                    managerEmail.setEnabled(true);
                    managerTel.setEnabled(true);
                    change = true;
                    confirm_changes.setText("确认修改");
                    Toast.makeText(PostPersonInfo.this, "现在可以编辑信息", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog isExit = new AlertDialog.Builder(PostPersonInfo.this).create();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        headImage.setImageBitmap(bitmap);

                        imagePath = imageUri.getPath();
                        headImageFile = new BmobFile(imageFactory());

                        Log.d("testtest", imagePath);
                        Toast.makeText(PostPersonInfo.this, imagePath, Toast.LENGTH_SHORT).show();

                        //headImageFile = new BmobFile(new File(imagePath));
                        headImageFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    manager.setManagerImage(headImageFile);
                                    manager.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                                Log.w("bbb", headImageFile.getFileUrl());
                                                Toast.makeText(PostPersonInfo.this, "上传文件成功:" + headImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(PostPersonInfo.this, "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(PostPersonInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        headImageFile = new BmobFile(new File(imagePath));
        Log.d("testtest", imagePath);
        headImageFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    manager.setManagerImage(headImageFile);
                    manager.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                Log.w("bbb", headImageFile.getFileUrl());
                                Toast.makeText(PostPersonInfo.this, "上传文件成功:" + headImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PostPersonInfo.this, "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(PostPersonInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        displayImage(imagePath);
        //TODO:图片上传
        headImageFile = new BmobFile(new File(imagePath));
        headImageFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    manager.setManagerImage(headImageFile);
                    manager.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                Log.w("bbb", headImageFile.getFileUrl());
                                Toast.makeText(PostPersonInfo.this, "上传文件成功:" + headImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PostPersonInfo.this, "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(PostPersonInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(PostPersonInfo.this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadFile(BmobFile file) {
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
                Toast.makeText(PostPersonInfo.this, "开始下载", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {
                    Toast.makeText(PostPersonInfo.this, "下载成功,保存路径:" + savePath, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostPersonInfo.this, "下载失败：" + e.getErrorCode() + "," + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob", "下载进度：" + value + "," + newworkSpeed);
            }

        });

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