package com.example.photographer;

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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PhotographerPersonInfo extends AppCompatActivity {

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    private String stringImageUri;
    private String imagePath;
    private Bitmap bitmap;
    private ImageView headImage;
    private ImageView photographerShowImage1;
    private ImageView photographerShowImage2;
    private ImageView photographerShowImage3;
    private int num;
    private BmobFile headImageFile;
    private BmobFile photographerShowImageFile1;
    private BmobFile photographerShowImageFile2;
    private BmobFile photographerShowImageFile3;
    private Photographer photographer; //存储现在登录的用户

    private ImageView back2info;
    private Button changeHeadImage;
    private boolean change = false;
    private Button confirm;

    private EditText photographerName;
    private EditText photographerId;
    private EditText gender;
    private EditText age;
    private EditText email;
    private EditText tel;
    private EditText photographerProfile;
    private EditText photographerMonthNum;
    private EditText photoImageShowText1;
    private EditText photoImageShowText2;
    private EditText photoImageShowText3;
    private EditText photographerPriceIncludeUniform;
    private EditText photographerPriceNoUniform;
    private EditText photographerSay;
    private EditText photographerNotice1;
    private EditText photographerNotice2;
    private EditText photographerNotice3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_person_info);
        getSupportActionBar().hide();// 隐藏ActionBar

        back2info = (ImageView) findViewById(R.id.back2info);

        changeHeadImage = (Button) findViewById(R.id.change_headImage);
        confirm = (Button) findViewById(R.id.confirm);

        photographerName = (EditText) findViewById(R.id.photographer_name);
        photographerId = (EditText) findViewById(R.id.photographer_id);
        gender = (EditText) findViewById(R.id.gender);
        age = (EditText) findViewById(R.id.age);
        email = (EditText) findViewById(R.id.email);
        tel = (EditText) findViewById(R.id.tel);
        photographerProfile = (EditText) findViewById(R.id.photographer_profile);
        photographerMonthNum = (EditText) findViewById(R.id.photographer_month_num);
        photoImageShowText1 = (EditText) findViewById(R.id.photographer_image_show_text_1);
        photoImageShowText2 = (EditText) findViewById(R.id.photographer_image_show_text_2);
        photoImageShowText3 = (EditText) findViewById(R.id.photographer_image_show_text_3);
        photographerPriceIncludeUniform = (EditText) findViewById(R.id.photographer_price_include_uniform);
        photographerPriceNoUniform = (EditText) findViewById(R.id.photographer_price_no_uniform);
        photographerSay = (EditText) findViewById(R.id.photographer_say);
        photographerNotice1 = (EditText) findViewById(R.id.photographer_notice_1);
        photographerNotice2 = (EditText) findViewById(R.id.photographer_notice_2);
        photographerNotice3 = (EditText) findViewById(R.id.photographer_notice_3);

        headImage = (ImageView) findViewById(R.id.head_image);
        photographerShowImage1 = (ImageView) findViewById(R.id.photographer_image_show_1);
        photographerShowImage2 = (ImageView) findViewById(R.id.photographer_image_show_2);
        photographerShowImage3 = (ImageView) findViewById(R.id.photographer_image_show_3);
        confirm.setText("修改信息");

        if (BmobUser.isLogin()) {
            photographer = BmobUser.getCurrentUser(Photographer.class);

            //初始化信息
            photographerName.setText(photographer.getNickName());
            photographerId.setText(photographer.getObjectId());
            gender.setText(photographer.getGender());
            age.setText(String.valueOf(photographer.getAge()));
            email.setText(photographer.getEmail());
            tel.setText(photographer.getMobilePhoneNumber());
            photographerProfile.setText(photographer.getProfile());
            photographerMonthNum.setText(String.valueOf(photographer.getMouthNum()));
            photoImageShowText1.setText(photographer.getImageShowText1());
            photoImageShowText2.setText(photographer.getImageShowText2());
            photoImageShowText3.setText(photographer.getImageShowText3());
            photographerPriceIncludeUniform.setText(String.valueOf(photographer.getPriceIncludeUniform()));
            photographerPriceNoUniform.setText(String.valueOf(photographer.getPriceNoUniform()));
            photographerSay.setText(photographer.getPhotographerSay());
            photographerNotice1.setText(photographer.getNotice1());
            photographerNotice2.setText(photographer.getNotice2());
            photographerNotice3.setText(photographer.getNotice3());

            //读取数据库的头像，若无则设置默认头像
            if (photographer.getHeadImage() != null) {
                showImageOfDateBase(photographer.getHeadImage(), headImage);
            } else {
                headImage.setImageResource(R.drawable.headimage);
            }

            if (photographer.getImageShow1() != null) {
                showImageOfDateBase(photographer.getImageShow1(), photographerShowImage1);
            } else {
                photographerShowImage1.setImageResource(R.drawable.add_image);
            }

            if (photographer.getImageShow2() != null) {
                showImageOfDateBase(photographer.getImageShow2(), photographerShowImage2);
            } else {
                photographerShowImage2.setImageResource(R.drawable.add_image);
            }

            if (photographer.getImageShow3() != null) {
                showImageOfDateBase(photographer.getImageShow3(), photographerShowImage3);
            } else {
                photographerShowImage3.setImageResource(R.drawable.add_image);
            }
        }

        back2info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!change) {
                    confirm.setText("确认修改");

                    photographerName.setEnabled(true);
                    gender.setEnabled(true);
                    age.setEnabled(true);
                    email.setEnabled(true);
                    tel.setEnabled(true);
                    photographerProfile.setEnabled(true);
                    photoImageShowText1.setEnabled(true);
                    photoImageShowText2.setEnabled(true);
                    photoImageShowText3.setEnabled(true);
                    photographerPriceIncludeUniform.setEnabled(true);
                    photographerPriceNoUniform.setEnabled(true);
                    photographerSay.setEnabled(true);
                    photographerNotice1.setEnabled(true);
                    photographerNotice2.setEnabled(true);
                    photographerNotice3.setEnabled(true);
                    change = true;
                    Toast.makeText(PhotographerPersonInfo.this, "现在可以编辑信息", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog isExit = new AlertDialog.Builder(PhotographerPersonInfo.this).create();
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

        changeHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = 0;
                selectImage();
            }
        });

        photographerShowImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = 1;
                selectImage();
            }
        });

        photographerShowImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = 2;
                selectImage();
            }
        });

        photographerShowImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = 3;
                selectImage();
            }
        });

    }

    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    confirm.setText("修改信息");

                    //更新信息
                    photographerName.setEnabled(false);
                    gender.setEnabled(false);
                    age.setEnabled(false);
                    email.setEnabled(false);
                    tel.setEnabled(false);
                    photographerProfile.setEnabled(false);
                    photoImageShowText1.setEnabled(false);
                    photoImageShowText2.setEnabled(false);
                    photoImageShowText3.setEnabled(false);
                    photographerPriceIncludeUniform.setEnabled(false);
                    photographerPriceNoUniform.setEnabled(false);
                    photographerSay.setEnabled(false);
                    photographerNotice1.setEnabled(false);
                    photographerNotice2.setEnabled(false);
                    photographerNotice3.setEnabled(false);
                    photographer.setNickName(photographerName.getText().toString());
                    photographer.setGender(gender.getText().toString());
                    photographer.setAge(Integer.parseInt(age.getText().toString()));
                    photographer.setEmail(email.getText().toString());
                    photographer.setMobilePhoneNumber(tel.getText().toString());
                    photographer.setProfile(photographerProfile.getText().toString());
                    photographer.setImageShowText1(photoImageShowText1.getText().toString());
                    photographer.setImageShowText2(photoImageShowText2.getText().toString());
                    photographer.setImageShowText3(photoImageShowText3.getText().toString());
                    photographer.setPriceIncludeUniform(Double.parseDouble(photographerPriceIncludeUniform.getText().toString()));
                    photographer.setPriceNoUniform(Double.parseDouble(photographerPriceNoUniform.getText().toString()));
                    photographer.setPhotographerSay(photographerSay.getText().toString());
                    photographer.setNotice1(photographerNotice1.getText().toString());
                    photographer.setNotice2(photographerNotice2.getText().toString());
                    photographer.setNotice3(photographerNotice3.getText().toString());
                    photographer.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(PhotographerPersonInfo.this, "保存成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PhotographerPersonInfo.this, "保存失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    change = false;

                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    confirm.setText("修改信息");
                    break;
                default:
                    break;
            }
        }
    };

    public void showImageOfDateBase(BmobFile image, ImageView imageView) {
        stringImageUri = image.getFileUrl();
        //利用ImageRequest实现图片加载
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //构建ImageRequest 实例
        ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                //给imageView设置图片
                imageView.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //设置一张错误的图片，临时用ic_launcher代替
                Toast.makeText(PhotographerPersonInfo.this, error.toString(), Toast.LENGTH_SHORT).show();
                if (imageView.equals(headImage)) {
                    headImage.setImageResource(R.drawable.headimage);
                } else {
                    headImage.setImageResource(R.drawable.empty_image);
                }
            }
        });
        requestQueue.add(request);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        if (num == 0) {
                            headImage.setImageBitmap(bitmap);
                        } else if (num == 1) {
                            photographerShowImage1.setImageBitmap(bitmap);
                        } else if (num == 2) {
                            photographerShowImage2.setImageBitmap(bitmap);
                        } else if (num == 3) {
                            photographerShowImage3.setImageBitmap(bitmap);
                        }

                        imagePath = imageUri.getPath();
                        headImageFile = new BmobFile(imageFactory());

                        Toast.makeText(PhotographerPersonInfo.this, imagePath, Toast.LENGTH_SHORT).show();

                        headImageFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    judgeWhichImageView();
                                } else {
                                    Toast.makeText(PhotographerPersonInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        headImageFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    judgeWhichImageView();

                } else {
                    Toast.makeText(PhotographerPersonInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    judgeWhichImageView();
                } else {
                    Toast.makeText(PhotographerPersonInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            if (num == 0) {
                headImage.setImageBitmap(bitmap);
            } else if (num == 1) {
                photographerShowImage1.setImageBitmap(bitmap);
            } else if (num == 2) {
                photographerShowImage2.setImageBitmap(bitmap);
            } else if (num == 3) {
                photographerShowImage3.setImageBitmap(bitmap);
            }

        } else {
            Toast.makeText(PhotographerPersonInfo.this, "failed to get image", Toast.LENGTH_SHORT).show();
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

    private void selectImage() {
        Dialog mCameraDialog = new Dialog(PhotographerPersonInfo.this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(PhotographerPersonInfo.this).inflate(
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
                    imageUri = FileProvider.getUriForFile(PhotographerPersonInfo.this,
                            "com.example.photographer.fileprovider", outputImage);
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
                if (ContextCompat.checkSelfPermission(PhotographerPersonInfo.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PhotographerPersonInfo.this,
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

    private void judgeWhichImageView() {
        if (num == 0) {
            photographer.setHeadImage(headImageFile);
            photographer.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.w("bbb", headImageFile.getFileUrl());
                        Toast.makeText(PhotographerPersonInfo.this, "上传文件成功：" + headImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PhotographerPersonInfo.this, "头像上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (num == 1) {
            photographer.setImageShow1(headImageFile);
            photographer.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.w("bbb", headImageFile.getFileUrl());
                        Toast.makeText(PhotographerPersonInfo.this, "上传文件成功：" + headImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PhotographerPersonInfo.this, "风采照片一上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (num == 2) {
            photographer.setImageShow2(headImageFile);
            photographer.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.w("bbb", headImageFile.getFileUrl());
                        Toast.makeText(PhotographerPersonInfo.this, "上传文件成功：" + headImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PhotographerPersonInfo.this, "风采照片二上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (num == 3) {
            photographer.setImageShow3(headImageFile);
            photographer.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.w("bbb", headImageFile.getFileUrl());
                        Toast.makeText(PhotographerPersonInfo.this, "上传文件成功：" + headImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PhotographerPersonInfo.this, "风采照片三上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}