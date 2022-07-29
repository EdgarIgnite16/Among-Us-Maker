package com.edgar.among_us_maker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.edgar.among_us_maker.R;
import com.edgar.among_us_maker.viewCustom.iv_ItemCustom;

import java.util.ArrayList;
import java.util.List;

public class itemAdapter extends ArrayAdapter<Integer> {
    private ArrayList<Integer> listItem;
    private Context context;
    private int resource;
    private int selected;

    public itemAdapter(@NonNull Context context, int resource, @NonNull List<Integer> objects) {
        super(context, resource, objects);
        this.listItem = new ArrayList<>(objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);
        }

        if(listItem.size() > 0){
            // Truy cập vào view cần truyền dữ liệu và dùng adapter đổ dữ liệu từ database lên view và hiển thị lên màn hình
            iv_ItemCustom iv_itemCustom = convertView.findViewById(R.id.ivItemCustom);
            iv_itemCustom.setImageResource(listItem.get(position));
            if(this.selected == position) {
                iv_itemCustom.setBackgroundResource(R.drawable.border_item_withcolor);
            } else {
                iv_itemCustom.setBackgroundResource(R.drawable.border_item_withoutcolor);
            }
        }

        return convertView;
    }

    // update data
    public void updateData(ArrayList<Integer> listItem) {
        this.listItem.clear();
        this.listItem.addAll(listItem);
        notifyDataSetChanged();
    }

    // update selected
    public void updateSelected(int x) {
        this.selected = x; // thay đổi vị trí hình ảnh
        notifyDataSetChanged();
    }
}
