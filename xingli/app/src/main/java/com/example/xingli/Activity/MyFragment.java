package com.example.xingli.Activity;

import static android.app.Activity.RESULT_OK;
import static cn.bmob.v3.Bmob.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import db.User;

public class  MyFragment extends Fragment {

    private MyViewModel mViewModel;


    private String sdCardDir = Environment.getExternalStorageDirectory() + "/fingerprintimages/";

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    private String stringImageUri;
    private String imagePath;
    private Bitmap bitmap;
    private ImageView headImage;
    private BmobFile headImageFile;
    private User user; //存储现在登录的用户

    private ImageView back2info;
    private Button change_headImage;
    private Button confirm_changes;
    private boolean change = false;

    private EditText userName;
    private EditText userGender;
    private EditText userAge;
    private EditText userEmail;
    private EditText userTel;


    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    confirm_changes.setText("修改信息");
                    //更新信息
                    userName.setEnabled(false);
                    userGender.setEnabled(false);
                    userAge.setEnabled(false);
                    userEmail.setEnabled(false);
                    userTel.setEnabled(false);
                    user.setNickName(userName.getText().toString());
                    user.setUserGender(userGender.getText().toString());
                    user.setAge(Integer.parseInt(userAge.getText().toString()));
                    user.setEmail(userEmail.getText().toString());
                    user.setMobilePhoneNumber(userTel.getText().toString());
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "保存失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void init(){
        if (BmobUser.isLogin()) {
            user = BmobUser.getCurrentUser(User.class);

            //初始化信息
            userName.setText(user.getNickName());
            userGender.setText(user.getUserGender());
            userAge.setText(String.valueOf(user.getAge()));
            userEmail.setText(user.getEmail());
            userTel.setText(user.getMobilePhoneNumber());

            //读取数据库的头像，若无则设置默认头像
            if (user.getUserImage() != null) {

                ProgressDialog progressDialog=new ProgressDialog(getActivity());
                progressDialog.setTitle("正在加载");
                progressDialog.setTitle("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                headImageFile = user.getUserImage();
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
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                        headImage.setImageResource(R.drawable.headimage);
                    }
                });

                requestQueue.add(request);

                progressDialog.dismiss();

            } else {
                headImage.setImageResource(R.drawable.headimage);
            }
        }

    }
    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.my_fragment, container, false);

        change_headImage = (Button)view.findViewById(R.id.change_headImage);
        confirm_changes = (Button)view. findViewById(R.id.confirm);

        headImage = (ImageView)view. findViewById(R.id.head_image);

        userName = (EditText)view. findViewById(R.id.userName);
        userGender = (EditText)view. findViewById(R.id.gender);
        userAge = (EditText)view. findViewById(R.id.age);
        userEmail = (EditText) view.findViewById(R.id.email);
        userTel = (EditText)view. findViewById(R.id.tel);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        confirm_changes.setText("修改信息");
        mViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        init();
        confirm_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!change) {
                    userName.setEnabled(true);
                    userGender.setEnabled(true);
                    userAge.setEnabled(true);
                    userEmail.setEnabled(true);
                    userTel.setEnabled(true);
                    change = true;
                    confirm_changes.setText("确认更改");
                    Toast.makeText(getActivity(), "现在可以编辑信息", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog isExit = new AlertDialog.Builder(getActivity()).create();
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

        change_headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog mCameraDialog = new Dialog(getActivity(), R.style.BottomDialog);
                LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                        R.layout.change_headimage_bottom_dialog, null);

                //初始化视图
                //打开相机
                root.findViewById(R.id.open_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(getActivity(),
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
                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(),
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
        // TODO: Use the ViewModel

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        headImage.setImageBitmap(bitmap);

                        imagePath = imageUri.getPath();
                        headImageFile = new BmobFile(imageFactory());

                        BmobFile bmobFile = new BmobFile(imageFactory());

                        ProgressDialog progressDialog=new ProgressDialog(getActivity());
                        progressDialog.setTitle("正在上传");
                        progressDialog.setTitle("Loading...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    user.setUserImage(bmobFile);
                                    user.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                                headImage.setImageBitmap(bitmap);
                                                Toast.makeText(getActivity(), "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            } else {
                                                Toast.makeText(getActivity(), "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(getActivity(), "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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



    //打开相册
    @SuppressWarnings("deprecation")
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();

        //重复，后期封装一下
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
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
        //图片上传
        BmobFile bmobFile = new BmobFile(new File(imagePath));

        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("正在上传");
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    user.setUserImage(bmobFile);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                displayImage(imagePath);
                                Toast.makeText(getActivity(), "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(), "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        //TODO:图片上传
        BmobFile bmobFile = new BmobFile(new File(imagePath));

        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("正在上传");
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    user.setUserImage(bmobFile);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                displayImage(imagePath);
                                Toast.makeText(getActivity(), "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(), "done失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "upload失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
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
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private File imageFactory(){
        File root= getActivity().getExternalCacheDir();
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