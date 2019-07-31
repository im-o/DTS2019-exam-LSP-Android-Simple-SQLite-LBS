package com.stimednp.dtsmywisata;

import android.view.View;

/**
 * Created by rivaldy on 7/29/2019.
 */

public class CustomeOnItemClickListener implements View.OnClickListener {
    private int position;
    private OnItemClickCallback onItemClickCallback;

    CustomeOnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
        this.position = position;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onClick(View v) {
        onItemClickCallback.onItemClicked(v, position);
    }

    public interface OnItemClickCallback {
        void onItemClicked(View view, int position);
    }
}
