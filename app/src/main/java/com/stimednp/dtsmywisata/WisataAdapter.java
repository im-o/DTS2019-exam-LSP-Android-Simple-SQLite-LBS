package com.stimednp.dtsmywisata;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rivaldy on 7/29/2019.
 */

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.WisataViewHolder> {
    private ArrayList<Wisatas> listWisatas = new ArrayList<>();
    private Activity activity;

    WisataAdapter(Activity activity) {
        this.activity = activity;
    }

    ArrayList<Wisatas> getListWisatas() {
        return listWisatas;
    }

    void setListWisatas(ArrayList<Wisatas> listWisatas) {
        if (listWisatas.size() > 0) {
            this.listWisatas.clear();
        }
        this.listWisatas.addAll(listWisatas);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public WisataAdapter.WisataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wisata, viewGroup, false);
        return new WisataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WisataAdapter.WisataViewHolder holder, int position) {
        holder.tvTitle.setText(listWisatas.get(position).getTitle());
        holder.tvDesc.setText(listWisatas.get(position).getDesc());
        holder.tvDate.setText(listWisatas.get(position).getDate());
        Picasso.get().
                load(listWisatas.get(position).getUrl_image())
                .into(holder.imgvUrlImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBarImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.progressBarImage.setVisibility(View.GONE);
                    }
                });
        holder.cardViewMain.setOnClickListener(new CustomeOnItemClickListener(position, new CustomeOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, final int position) {
                PopupMenu popupMenu = new PopupMenu(activity, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_show_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.menu_update) {
                            Intent intent = new Intent(activity, WisataAddUpdateActivity.class);
                            intent.putExtra(WisataAddUpdateActivity.EXTRA_POSITION, position);
                            intent.putExtra(WisataAddUpdateActivity.EXTRA_WISATA, listWisatas.get(position));
                            activity.startActivityForResult(intent, WisataAddUpdateActivity.REQUEST_UPDATE);
                        } else if (id == R.id.menu_map) {
                            Intent intent = new Intent(activity, MapsActivity.class);
                            intent.putExtra(MapsActivity.EXTRA_WISATA, listWisatas.get(position));
//
//                            intent.putExtra("latitude", listWisatas.get(position).getCoor_longitude());
//                            intent.putExtra("longitude", listWisatas.get(position).getCoor_latitude());
//                            intent.putExtra("judul", listWisatas.get(position).getTitle());
                            activity.startActivity(intent);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listWisatas.size();
    }

    class WisataViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvDate;
        ImageView imgvUrlImage;
        CardView cardViewMain;
        ProgressBar progressBarImage;

        WisataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDesc = itemView.findViewById(R.id.tv_item_description);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            cardViewMain = itemView.findViewById(R.id.cv_item_wisata);
            imgvUrlImage = itemView.findViewById(R.id.imgv_item_wisata);
            progressBarImage = itemView.findViewById(R.id.progress_img);
        }
    }

    void addItem(Wisatas wisatas) {
        this.listWisatas.add(wisatas);
        notifyItemInserted(listWisatas.size() - 1);
    }

    void updateItem(int position, Wisatas wisatas) {
        this.listWisatas.set(position, wisatas);
        notifyItemChanged(position, wisatas);
    }

    void removeItem(int position) {
        this.listWisatas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listWisatas.size());
    }
}
