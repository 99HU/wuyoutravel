package frag_record;

import androidx.fragment.app.Fragment;

import com.example.xingli.R;

import java.util.List;

import db.DBMangerBudget;
import db.TypeBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutcomeFragment extends BaseRecordFragment {


    // 重写
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库当中的数据源
        List<TypeBean> outlist = DBMangerBudget.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBMangerBudget.insertItemToAccounttb(accountBean);
    }
}
