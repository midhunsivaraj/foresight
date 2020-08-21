package com.sumgeeks.foresight.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sumgeeks.foresight.R;
/**
 * Created by yarolegovich on 25.03.2017.
 */
public class SimpleItem extends DrawerItem<SimpleItem.ViewHolder> {

    private String title;

    public SimpleItem(String title) {
        this.title = title;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_option, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void bindViewHolder(ViewHolder holder) {
        holder.title.setText(title);
    }


    static class ViewHolder extends DrawerAdapter.ViewHolder {

        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
