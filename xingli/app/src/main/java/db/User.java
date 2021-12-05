package db;

import android.util.Log;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.disposables.Disposable;

public class User extends BmobUser implements Serializable {

    //账号
    //用户名
    //密码
    //邮箱
    //邮箱认证状态
    //手机号码
    //手机号码认证状态

    //头像
    private BmobFile headImage;
    //性别
    private String gender;
    //位置
    private BmobGeoPoint userLoc;
    //年龄
    private int age;
    //昵称
    private String nickName = "旅行者";
    //订单
    private Order order;

    public static User fetchUserInfo() {
        final User[] myUser = new User[1];
        // 当继承了BmobUser扩展了自定义属性时，FetchUserInfoListener中请使用自定义的类名
        BmobUser.fetchUserInfo(new FetchUserInfoListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    myUser[0] = BmobUser.getCurrentUser(User.class);
                } else {
                    Log.e("error",e.getMessage());
                }
            }
        });
        return myUser[0];
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public <T> Disposable signUp(SaveListener<T> listener) {
        return super.signUp(listener);
    }

    public BmobFile getUserImage() {
        return headImage;
    }

    public void setUserImage(BmobFile userImage) {
        this.headImage = userImage;
    }

    public String getUserGender() {
        return gender;
    }

    public void setUserGender(String userGender) {
        this.gender = userGender;
    }

    public BmobGeoPoint getUserLoc() {
        return userLoc;
    }

    public void setUserLoc(BmobGeoPoint userLoc) {
        this.userLoc = userLoc;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
