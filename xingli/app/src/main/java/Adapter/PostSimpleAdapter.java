package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xingli.R;

import java.util.ArrayList;
import java.util.List;

import db.Post;

public class PostSimpleAdapter extends ArrayAdapter<Post> implements Filterable  {
    private int resourceId;

    private List<Post> postList;
    private PostSimpleAdapter.MyFilter myFilter;

    private ArrayList<Post> mOriginalValues;
    private final Object mLock = new Object();


    public PostSimpleAdapter(Context context, int textViewRouseId, List<Post> objects) {
        super(context, textViewRouseId, objects);
        this.resourceId = textViewRouseId;
        this.postList=objects;
    }

    @Nullable
    @Override
    public Post getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Post post = (Post) getItem(position);//获取当前项的实例
        View view;
        PostSimpleAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new PostSimpleAdapter.ViewHolder();
            viewHolder.postImage = (ImageView) view.findViewById(R.id.post_image);
            viewHolder.postName = (TextView) view.findViewById(R.id.post_name);
            viewHolder.postLoc = (TextView) view.findViewById(R.id.post_location);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (PostSimpleAdapter.ViewHolder) view.getTag();
        }

        viewHolder.postImage.setImageResource(post.getPostImageId());
        viewHolder.postName.setText(post.getPostName());
        viewHolder.postLoc.setText("位置：" + post.getPostLoc());
        viewHolder.postImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return view;
    }

    class ViewHolder {
        ImageView postImage;
        TextView postName;
        TextView postLoc;

    }
    @Override
    public Filter getFilter(){
        if (myFilter==null){
            myFilter= new PostSimpleAdapter.MyFilter();
        }
        return myFilter;
    }
    class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            // 持有过滤操作完成之后的数据。该数据包括过滤操作之后的数据的值以及数量。 count:数量 values包含过滤操作之后的数据的值
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    // 将list的用户 集合转换给这个原始数据的ArrayList
                    mOriginalValues = new ArrayList<Post>(postList);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                Log.d("PakegeActivity","检测到是空的");
                ArrayList<Post> list;
                synchronized (mLock) {
                    list = new ArrayList<Post>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                // 做正式的筛选
                Log.d("PakegeActivity","开始筛选");
                String prefixString = prefix.toString().toLowerCase();

                // 声明一个临时的集合对象 将原始数据赋给这个临时变量
                final ArrayList<Post> values;
                synchronized (mLock) {
                    values = new ArrayList<Post>(mOriginalValues);
                }
                final int count = values.size();

                // 新的集合对象
                final ArrayList<Post> newValues = new ArrayList<Post>(
                        count);

                for (int i = 0; i < count; i++) {
                    // 如果姓名的前缀相符或者电话相符就添加到新的集合
                    final Post value = (Post) values.get(i);
                    if (value.getPostName().contains(prefixString)||value.getPostLoc().contains(prefixString)) {
                        Log.d("PakegeActivity",value.getPostName());
                        newValues.add(value);
                    }
                }
                // 然后将这个新的集合数据赋给FilterResults对象
                results.values = newValues;
                results.count = newValues.size();
                Log.d("PakegeActivity",String.valueOf(results.count));
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            postList = (List<Post>) results.values;
            System.out.println(results.count);
            if (results.count > 0) {
                notifyDataSetChanged();
                Log.d("PakegeActivity","刷新listview");
            } else {
                notifyDataSetChanged();
            }
        }
    }
}
