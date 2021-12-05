package com.example.xingli.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xingli.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.OrderRecAdapter;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import db.Order;
import db.User;

public class OrderFragment extends Fragment {
    public static boolean updateFlag=false;
    private List<Order> orderList=new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText editText;
    private OrderViewModel mViewModel;
    private Context context;
    private User user;
    OrderRecAdapter adapterRec;
    private void init(){

        user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Order> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("user", user);
        bmobQuery.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                if (e == null) {
                    Toast.makeText(context, "用户的订单查询成功", Toast.LENGTH_SHORT).show();
                    for (Order o : list) {
                        orderList.add(o);
                    }
                    LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    adapterRec=new OrderRecAdapter(orderList,getActivity());
                    recyclerView.setAdapter(adapterRec);

                } else {
                    Toast.makeText(context, "用户的订单查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public void onResume() {
        if (updateFlag)
        {
            orderList.clear();
            init();
        }
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.order_fragment, container, false);
        context = view.getContext();
        recyclerView=(RecyclerView) view.findViewById(R.id.post_recycler_view);
        editText=(EditText) view.findViewById(R.id.search_postInfo);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        init();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterRec.getFilter().filter(s.toString());
                Log.d("Change","变");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // TODO: Use the ViewModel
    }

}