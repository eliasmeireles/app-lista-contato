package systemplus.com.br.listadecontatos.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import systemplus.com.br.listadecontatos.R;
import systemplus.com.br.listadecontatos.model.ContextMenuItem;

/**
 * Created by elias on 27/12/17.
 */

public class ContextMenuAdapter extends BaseAdapter {

    private Activity activity;
    private List<ContextMenuItem> contextMenuItems;


    public ContextMenuAdapter(Activity activity, List<ContextMenuItem> contextMenuItems) {
        this.activity = activity;
        this.contextMenuItems = contextMenuItems;
    }

    @Override
    public int getCount() {
        return contextMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return contextMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.context_menu_layout, viewGroup, false);
            holder = new ViewHolder();

            view.setTag(holder);

            holder.holderIcon = view.findViewById(R.id.context_ico);
            holder.holderLabel = view.findViewById(R.id.context_label);
            holder.holderDivider = view.findViewById(R.id.context_divider);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        Glide.with(activity)
                .load(contextMenuItems.get(position).getIcon())
                .into(holder.holderIcon);

        holder.holderLabel.setText(contextMenuItems.get(position).getLabel());

        holder.holderDivider.setVisibility(position == contextMenuItems.size() - 2 ? View.VISIBLE : View.GONE);

        return view;
    }


    public static class ViewHolder {

        public ImageView holderIcon;
        public TextView holderLabel;
        public View holderDivider;

    }
}
