package com.stimednp.dtsmywisata;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.stimednp.dtsmywisata.mydb.WisataHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WisataAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText edtTitle, edtDescription, edtUrlImage, edtLat, edtLng;
    private MaterialButton btnSubmit;
    Toolbar toolbar;
    public static final String EXTRA_WISATA = "extra_wisata";
    public static final String EXTRA_POSITION = "extra_position";
    private boolean isEdit = false;
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;

    private Wisatas wisatas;
    private int position;
    private WisataHelper wisataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata_add_update);
        toolbar = findViewById(R.id.toolbar);
        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);
        edtUrlImage = findViewById(R.id.edt_url_image);
        edtLat = findViewById(R.id.edt_lat);
        edtLng = findViewById(R.id.edt_lng);
        btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(this);
        wisataHelper = WisataHelper.getInstance(getApplicationContext());
        wisatas = getIntent().getParcelableExtra(EXTRA_WISATA);
        if (wisatas != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            wisatas = new Wisatas();
        }


        setSupportActionBar(toolbar);
        setActionToolBar();
    }

    private void setActionToolBar() {
        //change teks button
        String actionBarTitle;
        String btnTitle;
        if (isEdit) {
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            if (wisatas != null) {
                edtTitle.setText(wisatas.getTitle());
                edtDescription.setText(wisatas.getDesc());
                edtUrlImage.setText(wisatas.getUrl_image());
                edtLat.setText(wisatas.getCoor_latitude());
                edtLng.setText(wisatas.getCoor_longitude());
            }
        } else {
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        btnSubmit.setText(btnTitle);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String urlImage = edtUrlImage.getText().toString().trim();
            String coor_latitude = edtLat.getText().toString().trim();
            String coor_longitude = edtLng.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(urlImage)
                    || TextUtils.isEmpty(coor_latitude) || TextUtils.isEmpty(coor_longitude)) {
                Toast.makeText(getApplicationContext(), "Masih data yang kosong !!", Toast.LENGTH_SHORT).show();
                return;
            }
            wisatas.setTitle(title);
            wisatas.setDesc(description);
            wisatas.setUrl_image(urlImage);
            wisatas.setCoor_latitude(coor_latitude);
            wisatas.setCoor_longitude(coor_longitude);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_WISATA, wisatas);
            intent.putExtra(EXTRA_POSITION, position);

            if (isEdit) {
                long result = wisataHelper.updateWisata(wisatas);
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent);
                    finish();
                } else {
                    Toast.makeText(WisataAddUpdateActivity.this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
                }
            } else {
                wisatas.setDate(getCurrentDate());
                long result = wisataHelper.insertWisata(wisatas);
                if (result > 0) {
                    wisatas.setId((int) result);
                    setResult(RESULT_ADD, intent);
                    finish();
                } else {
                    Toast.makeText(WisataAddUpdateActivity.this, "Gagal menambah data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;
        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
            dialogTitle = "Hapus Wisata";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            long result = wisataHelper.deleteWisata(wisatas.getId());
                            if (result > 0) {
                                Intent intent = new Intent();
                                intent.putExtra(EXTRA_POSITION, position);
                                setResult(RESULT_DELETE, intent);
                                finish();
                            } else {
                                Toast.makeText(WisataAddUpdateActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
