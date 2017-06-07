package cn.sampson.android.xiandou.utils.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.sampson.android.xiandou.R;

/**
 * Created by Administrator on 2016/4/6.
 */
public class LetterSortAdapter extends BaseAdapter {

    private Context ct;
    private List<LetterSortBaseBean> data;

    public LetterSortAdapter(Context ct,List<LetterSortBaseBean> datas){
        this.ct = ct;
        this.data = datas;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<LetterSortBaseBean> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    public void remove(LetterSortBaseBean bean) {
        this.data.remove(bean);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(ct).inflate(R.layout._adapter_letter_sort, null);
            viewHolder = new ViewHolder();
            viewHolder.column_title = (TextView) convertView.findViewById(R.id.column_title);
            convertView.setTag(viewHolder);
        }

        LetterSortBaseBean bean = data.get(position);
        viewHolder.column_title.setText(bean.getName());

        //根据position获取分类的首字母char
//        int section = getSectionForPosition(position);
//        if (position == getPositionForSection(section)) {
//
//        }
        return convertView;
    }

    static class ViewHolder {
        TextView column_title;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return data.get(position).getFirstPY().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = data.get(i).getFirstPY();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }
}
