package com.example.guide;

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

public class GuidePersonInfo extends AppCompatActivity {

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    private String stringImageUri;
    private String imagePath;
    private Bitmap bitmap;
    private ImageView headImage;
    private ImageView guideShowImage1;
    private ImageView guideShowImage2;
    private ImageView guideShowImage3;
    private int num;
    private BmobFile headImageFile;
    private BmobFile guideShowImageFile1;
    private BmobFile guideShowImageFile2;
    private BmobFile guideShowImageFile3;
    private Guide guide; //存储现在登录的用户

    private ImageView back2info;
    private Button changeHeadImage;
    private boolean change = false;
    private Button confirm;

    private EditText guideName;
    private EditText guideId;
    private EditText gender;
    private EditText age;
    private EditText email;
    private EditText tel;
    private EditText guideProfile;
    private EditText guideMonthNum;
    private EditText guideImageShowText1;
    private EditText guideImageShowText2;
    private EditText guideImageShowText3;
    private EditText guidePriceNoUniform;
    private EditText guideSay;
    private EditText guideNotice1;
    private EditText guideNotice2;
    private EditText guideNotice3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_person_info);
        getSupportActionBar().hide();// 隐藏ActionBar

        back2info = (ImageView) findViewById(R.id.back2info);

        changeHeadImage = (Button) findViewById(R.id.change_headImage);
        confirm = (Button) findViewById(R.id.confirm);

        guideName = (EditText) findViewById(R.id.guide_name);
        guideId = (EditText) findViewById(R.id.guide_id);
        gender = (EditText) findViewById(R.id.gender);
        age = (EditText) findViewById(R.id.age);
        email = (EditText) findViewById(R.id.email);
        tel = (EditText) findViewById(R.id.tel);
        guideProfile = (EditText) findViewById(R.id.guide_profile);
        guideMonthNum = (EditText) findViewById(R.id.guide_month_num);
        guideImageShowText1 = (EditText) findViewById(R.id.guide_image_show_text_1);
        guideImageShowText2 = (EditText) findViewById(R.id.guide_image_show_text_2);
        guideImageShowText3 = (EditText) findViewById(R.id.guide_image_show_text_3);
        guidePriceNoUniform = (EditText) findViewById(R.id.guide_price);
        guideSay = (EditText) findViewById(R.id.guide_say);
        guideNotice1 = (EditText) findViewById(R.id.guide_notice_1);
        guideNotice2 = (EditText) findViewById(R.id.guide_notice_2);
        guideNotice3 = (EditText) findViewById(R.id.guide_notice_3);

        headImage = (ImageView) findViewById(R.id.head_image);
        guideShowImage1 = (ImageView) findViewById(R.id.guide_image_show_1);
        guideShowImage2 = (ImageView) findViewById(R.id.guide_image_show_2);
        guideShowImage3 = (ImageView) findViewById(R.id.guide_image_show_3);

        if (BmobUser.isLogin()) {
            guide = BmobUser.getCurrentUser(Guide.class);

            //初始化信息
            guideName.setText(guide.getNickName());
            guideId.setText(guide.getObjectId());
            gender.setText(guide.getGender());
            age.setText(String.valueOf(guide.getAge()));
            email.setText(guide.getEmail());
            tel.setText(guide.getMobilePhoneNumber());
            guideProfile.setText(guide.getProfile());
            guideMonthNum.setText(String.valueOf(guide.getMouthNum()));
            guideImageShowText1.setText(guide.getImageShowText1());
            guideImageShowText2.setText(guide.getImageShowText2());
            guideImageShowText3.setText(guide.getImageShowText3());
            guidePriceNoUniform.setText(String.valueOf(guide.getPriceNoUniform()));
            guideSay.setText(guide.getPhotographerSay());
            guideNotice1.setText(guide.getNotice1());
            guideNotice2.setText(guide.getNotice2());
            guideNotice3.setText(guide.getNotice3());

            //读取数据库的头像，若无则设置默认头像
            if (guide.getHeadImage() != null) {
                showImageOfDateBase(guide.getHeadImage(), headImage);
            } else {
                headImage.setImageResource(R.drawable.headimage);
            }

            if (guide.getImageShow1() != null) {
                showImageOfDateBase(guide.getImageShow1(), guideShowImage1);
            } else {
                guideShowImage1.setImageResource(R.drawable.add_image);
            }

            if (guide.getImageShow2() != null) {
                showImageOfDateBase(guide.getImageShow2(), guideShowImage2);
            } else {
                guideShowImage2.setImageResource(R.drawable.add_image);
            }

            if (guide.getImageShow3() != null) {
                showImageOfDateBase(guide.getImageShow3(), guideShowImage3);
            } else {
                guideShowImage3.setImageResource(R.drawable.add_image);
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
                    guideName.setEnabled(true);
                    gender.setEnabled(true);
                    age.setEnabled(true);
                    email.setEnabled(true);
                    tel.setEnabled(true);
                    guideProfile.setEnabled(true);
                    guideImageShowText1.setEnabled(true);
                    guideImageShowText2.setEnabled(true);
                    guideImageShowText3.setEnabled(true);
                    guidePriceNoUniform.setEnabled(true);
                    guideSay.setEnabled(true);
                    guideNotice1.setEnabled(true);
                    guideNotice2.setEnabled(true);
                    guideNotice3.setEnabled(true);
                    change = true;
                    Toast.makeText(GuidePersonInfo.this, "现在可以编辑信息", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog isExit = new AlertDialog.Builder(GuidePersonInfo.this).create();
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

        guideShowImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = 1;
                selectImage();
            }
        });

        guideShowImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = 2;
                selectImage();
            }
        });

        guideShowImage3.setOnClickListener(new View.OnClickListener() {
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

                    //更新信息
                    guideName.setEnabled(false);
                    gender.setEnabled(false);
                    age.setEnabled(false);
                    email.setEnabled(false);
                    tel.setEnabled(false);
                    guideProfile.setEnabled(false);
                    guideImageShowText1.setEnabled(false);
                    guideImageShowText2.setEnabled(false);
                    guideImageShowText3.setEnabled(false);
                    guidePriceNoUniform.setEnabled(false);
                    guideSay.setEnabled(false);
                    guideNotice1.setEnabled(false);
                    guideNotice2.setEnabled(false);
                    guideNotice3.setEnabled(false);
                    guide.setNickName(guideName.getText().toString());
                    guide.setGender(gender.getText().toString());
                    guide.setAge(Integer.parseInt(age.getText().toString()));
                    guide.setEmail(email.getText().toString());
                    guide.setMobilePhoneNumber(tel.getText().toString());
                    guide.setProfile(guideProfile.getText().toString());
                    guide.setImageShowText1(guideImageShowText1.getText().toString());
                    guide.setImageShowText2(guideImageShowText2.getText().toString());
                    guide.setImageShowText3(guideImageShowText3.getText().toString());
                    guide.setPriceNoUniform(Double.parseDouble(guidePriceNoUniform.getText().toString()));
                    guide.setPhotographerSay(guideSay.getText().toString());
                    guide.setNotice1(guideNotice1.getText().toString());
                    guide.setNotice2(guideNotice2.getText().toString());
                    guide.setNotice3(guideNotice3.getText().toString());
                    guide.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(GuidePersonInfo.this, "保存成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(GuidePersonInfo.this, "保存失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    change = false;

                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
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
                Toast.makeText(GuidePersonInfo.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                            guideShowImage1.setImageBitmap(bitmap);
                        } else if (num == 2) {
                            guideShowImage2.setImageBitmap(bitmap);
                        } else if (num == 3) {
                            guideShowImage3.setImageBitmap(bitmap);
                        }

                        imagePath = imageUri.getPath();
                        headImageFile = new BmobFile(imageFactory());

                        Toast.makeText(GuidePersonInfo.this, imagePath, Toast.LENGTH_SHORT).show();

                        headImageFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    judgeWhichImageView();
                                } else {
                                    Toast.makeText(GuidePersonInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(GuidePersonInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(GuidePersonInfo.this, "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                guideShowImage1.setImageBitmap(bitmap);
            } else if (num == 2) {
                guideShowImage2.setImageBitmap(bitmap);
            } else if (num == 3) {
                guideShowImage3.setImageBitmap(bitmap);
            }

        } else {
            Toast.makeText(GuidePersonInfo.this, "failed to get image", Toast.LENGTH_SHORT).show();
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
        Dialog mCameraDialog = new Dialog(GuidePersonInfo.this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(GuidePersonInfo.this).inflate(
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
                    imageUri = FileProvider.getUriForFile(GuidePersonInfo.this,
                            "com.example.guide.fileprovider", outputImage);
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
                if (ContextCompat.checkSelfPermission(GuidePersonInfo.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GuidePersonInfo.this,
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
            guide.setHeadImage(headImageFile);
            guide.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.w("bbb", headImageFile.getFileUrl());
                        Toast.makeText(GuidePersonInfo.this, "上传文件成功：" + headImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GuidePersonInfo.this, "头像上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (num == 1) {
            guide.setImageShow1(headImageFile);
            guide.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.w("bbb", headImageFile.getFileUrl());
                        Toast.makeText(GuidePersonInfo.this, "上传文件成功：" + headImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GuidePersonInfo.this, "风采照片一上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (num == 2) {
            guide.setImageShow2(headImageFile);
            guide.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.w("bbb", headImageFile.getFileUrl());
                        Toast.makeText(GuidePersonInfo.this, "上传文件成功：" + headImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GuidePersonInfo.this, "风采照片二上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (num == 3) {
            guide.setImageShow3(headImageFile);
            guide.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.w("bbb", headImageFile.getFileUrl());
                        Toast.makeText(GuidePersonInfo.this, "上传文件成功：" + headImageFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GuidePersonInfo.this, "风采照片三上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}