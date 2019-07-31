package com.stimednp.dtsmywisata;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.stimednp.dtsmywisata.mydb.WisataHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.stimednp.dtsmywisata.WisataAddUpdateActivity.REQUEST_UPDATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoadWisatasCallback {
    private RecyclerView rcvWisatas;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private Toolbar toolbar;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private WisataAdapter adapter;
    private WisataHelper wisataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        rcvWisatas = findViewById(R.id.rv_notes);
        rcvWisatas.setLayoutManager(new LinearLayoutManager(this));
        rcvWisatas.setHasFixedSize(true);
        wisataHelper = WisataHelper.getInstance(getApplicationContext());
        wisataHelper.open();
        progressBar = findViewById(R.id.progressbar);
        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);
        adapter = new WisataAdapter(this);
        rcvWisatas.setAdapter(adapter);

        if (savedInstanceState == null) {
            new LoadWisatasAsync(wisataHelper, this).execute();
        } else {
            ArrayList<Wisatas> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListWisatas(list);
            }
        }

        //callmethod
        setActionToolBar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListWisatas());
    }

    private void setActionToolBar() {
        setSupportActionBar(toolbar);
        String nameApp = getResources().getString(R.string.app_name);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(nameApp);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            Intent intent = new Intent(MainActivity.this, WisataAddUpdateActivity.class);
            startActivityForResult(intent, WisataAddUpdateActivity.REQUEST_ADD);
        }
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Wisatas> wisatas) {
        progressBar.setVisibility(View.INVISIBLE);
        adapter.setListWisatas(wisatas);
    }

    private static class LoadWisatasAsync extends AsyncTask<Void, Void, ArrayList<Wisatas>> {
        private final WeakReference<WisataHelper> weakWisataHelper;
        private final WeakReference<LoadWisatasCallback> weakCallback;

        LoadWisatasAsync(WisataHelper wisataHelper, LoadWisatasCallback callback) {
            weakWisataHelper = new WeakReference<>(wisataHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Wisatas> doInBackground(Void... voids) {
            return weakWisataHelper.get().getAllWisata();
        }

        @Override
        protected void onPostExecute(ArrayList<Wisatas> wisatas) {
            super.onPostExecute(wisatas);
            weakCallback.get().postExecute(wisatas);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == WisataAddUpdateActivity.REQUEST_ADD) {
                if (resultCode == WisataAddUpdateActivity.RESULT_ADD) {
                    Wisatas wisata = data.getParcelableExtra(WisataAddUpdateActivity.EXTRA_WISATA);
                    adapter.addItem(wisata);
                    rcvWisatas.smoothScrollToPosition(adapter.getItemCount() - 1);
                    showSnackbarMessage("Satu item berhasil ditambahkan");
                }
            } else if (requestCode == REQUEST_UPDATE) {
                if (resultCode == WisataAddUpdateActivity.RESULT_UPDATE) {
                    Wisatas wisata = data.getParcelableExtra(WisataAddUpdateActivity.EXTRA_WISATA);
                    int position = data.getIntExtra(WisataAddUpdateActivity.EXTRA_POSITION, 0);
                    adapter.updateItem(position, wisata);
                    rcvWisatas.smoothScrollToPosition(position);
                    showSnackbarMessage("Satu item berhasil diubah");
                } else if (resultCode == WisataAddUpdateActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(WisataAddUpdateActivity.EXTRA_POSITION, 0);
                    adapter.removeItem(position);
                    showSnackbarMessage("Satu item berhasil dihapus");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wisataHelper.close();
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rcvWisatas, message, Snackbar.LENGTH_SHORT).show();
    }
}
