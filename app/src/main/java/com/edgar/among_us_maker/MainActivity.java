package com.edgar.among_us_maker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.edgar.among_us_maker.Adapter.itemAdapter;
import com.edgar.among_us_maker.Database.LocalData;

public class MainActivity extends AppCompatActivity {
    private ImageView ivSelectBackground, ivSelectBody, ivSelectHat,
            ivSelectClothes, ivSelectPet, ivSelectTag, ivDownloadImage;
    private RelativeLayout layoutBackgroundDraw;
    private ImageView ivBodyDraw, ivClothesDraw, ivHatDraw, ivPetDraw, ivTagDraw;
    private GridView gvListItemResource;

    private ArrayList<ImageView> listButtonSelect;
    private ArrayList<Integer> listImageResource_BG, listImageResource_Body, listImageResource_Hat,
            listImageResource_Clothes, listImageResource_Pet,listImageResource_Tag;

    private itemAdapter itemAdapter;
    private String keySelected; // frag key layout
    private int keyPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
        CreateMapping();
        SetUp();
    }

    // Hàm khởi tạo
    private void Init() {
        // base values
        keySelected = "background";

        // init list data
        listButtonSelect = new ArrayList<>();
        listImageResource_BG = this.getData_BG();
        listImageResource_Body = this.getData_Body();
        listImageResource_Hat = this.getData_Hat();
        listImageResource_Clothes = this.getData_Clothes();
        listImageResource_Pet = this.getData_Pet();
        listImageResource_Tag = this.getData_Tag();
    }

    // Hàm ánh xạ
    private void CreateMapping() {
        ivSelectBackground = (ImageView) findViewById(R.id.ivSelectBackground);
        ivSelectBody = (ImageView) findViewById(R.id.ivSelectBody);
        ivSelectHat = (ImageView) findViewById(R.id.ivSelectHat);
        ivSelectClothes = (ImageView) findViewById(R.id.ivSelectClothes);
        ivSelectPet = (ImageView) findViewById(R.id.ivSelectPet);
        ivSelectTag = (ImageView) findViewById(R.id.ivSelectTag);
        ivDownloadImage = (ImageView) findViewById(R.id.ivDownloadImage);

        layoutBackgroundDraw = (RelativeLayout) findViewById(R.id.layoutBackgroundDraw);
        ivBodyDraw = (ImageView) findViewById(R.id.ivBodyDraw);
        ivClothesDraw = (ImageView) findViewById(R.id.ivClothesDraw);
        ivPetDraw = (ImageView) findViewById(R.id.ivPetDraw);
        ivHatDraw = (ImageView) findViewById(R.id.ivHatDraw);
        ivTagDraw = (ImageView) findViewById(R.id.ivTagDraw);

        gvListItemResource = (GridView) findViewById(R.id.gvListItemResource);
    }

    // Hàm cấu hình - cài đặt
    private void SetUp() {
        // Load đối tượng đã vẽ từ trước lên màn hình
        DrawAllObjectWhenStart();

        // Thêm các button vào list để xử lý click event
        listButtonSelect.add(ivSelectBackground);
        listButtonSelect.add(ivSelectBody);
        listButtonSelect.add(ivSelectHat);
        listButtonSelect.add(ivSelectClothes);
        listButtonSelect.add(ivSelectPet);
        listButtonSelect.add(ivSelectTag);

        HandleButtonsSelect(); // xử lí hình ảnh và sự kiện  các nút bấm tác đối tượng

        // setup Adapter và hiển thị list item mặc định sẽ là background
        itemAdapter = new itemAdapter(MainActivity.this, R.layout.iv_item_custom, listImageResource_BG);
        gvListItemResource.setAdapter(itemAdapter);
        itemAdapter.updateSelected(new LocalData(MainActivity.this, keySelected).getPos()); // cập nhật vị trí pos đã chọn từ trước
    }

    //=============================================================================================//
    // Hàm xử lí hình ảnh và sự kiện cho các nút bấm thao tác đối tượng
    private void HandleButtonsSelect() {
        for(ImageView item : listButtonSelect) {
            item.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public void onClick(View view) {
                    // đầu tiên cho tất cả button về lại background trắng
                    for(ImageView item : listButtonSelect) {
                        item.setBackgroundResource(R.drawable.border_select_item_withoutcolor);
                    }
                    // sau đó set ảnh được chọn là border tương ứng
                    item.setBackgroundResource(R.drawable.border_select_item_withcolor);

                    // xử lí sự kiện
                    switch (item.getId()) {
                        case R.id.ivSelectBackground:
                            // update hình ảnh trên layout
                            keySelected = "background"; // set key
                            itemAdapter.updateData(getData_BG()); // set data mới lên màn hình
                            itemAdapter.updateSelected(new LocalData(MainActivity.this, keySelected).getPos()); // set lại item đã chọn từ trước
                            break;

                        case R.id.ivSelectBody:
                            keySelected = "body";
                            itemAdapter.updateData(getData_Body());
                            itemAdapter.updateSelected(new LocalData(MainActivity.this, keySelected).getPos());
                            break;

                        case R.id.ivSelectHat:
                            keySelected = "hat";
                            itemAdapter.updateData(getData_Hat());
                            itemAdapter.updateSelected(new LocalData(MainActivity.this, keySelected).getPos());
                            break;

                        case R.id.ivSelectClothes:
                            keySelected = "clothes";
                            itemAdapter.updateData(getData_Clothes());
                            itemAdapter.updateSelected(new LocalData(MainActivity.this, keySelected).getPos());
                            break;

                        case R.id.ivSelectPet:
                            keySelected = "pet";
                            itemAdapter.updateData(getData_Pet());
                            itemAdapter.updateSelected(new LocalData(MainActivity.this, keySelected).getPos());
                            break;

                        case R.id.ivSelectTag:
                            keySelected = "tag";
                            itemAdapter.updateData(getData_Tag());
                            itemAdapter.updateSelected(new LocalData(MainActivity.this, keySelected).getPos());
                            break;
                    }
                }
            });
        }

        // xử lí click chọn trên danh sách item
        gvListItemResource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemAdapter.updateSelected(i); // update hình ảnh cho vị trí được chọn
                DrawObjectToScreen(keySelected, i); // lấy đối tượng được click vào và vẽ lên màn hình
                new LocalData(MainActivity.this, keySelected).setPos(i); // lưu lại vị trí click
            }
        });

        // xử lí click nút lưu hình ảnh
        ivDownloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SaveImageToGallery(R.id.layoutBackgroundDraw)) {
                    Toast.makeText(MainActivity.this, "Lưu ảnh thành công!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Lưu ảnh thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Hàm xử lí vẽ hình ảnh đối tượng lên màn hình
    private void DrawObjectToScreen(String key, int i) {
        switch (key) {
            case "background":
                layoutBackgroundDraw.setBackgroundResource(listImageResource_BG.get(i));
                break;
            case "body":
                ivBodyDraw.setImageResource(listImageResource_Body.get(i));
                break;
            case "clothes":
                if (i != 0) {
                    ivClothesDraw.setImageResource(listImageResource_Clothes.get(i));
                } else {
                    ivClothesDraw.setImageResource(0); // clear the object
                }
                break;
            case "hat":
                if (i != 0) {
                    ivHatDraw.setImageResource(listImageResource_Hat.get(i));
                } else {
                    ivHatDraw.setImageResource(0); // clear the object
                }
                break;
            case "pet":
                if (i != 0) {
                    ivPetDraw.setImageResource(listImageResource_Pet.get(i));
                } else {
                    ivPetDraw.setImageResource(0); // clear the object
                }
                break;
            case "tag":
                ivTagDraw.setImageResource(listImageResource_Tag.get(i));
                break;
        }
    }

    // Hàm load lại object nhân vật ở state trước
    private void DrawAllObjectWhenStart() {
        DrawObjectToScreen("background", new LocalData(MainActivity.this, "background").getPos());
        DrawObjectToScreen("body", new LocalData(MainActivity.this, "body").getPos());
        DrawObjectToScreen("clothes", new LocalData(MainActivity.this, "clothes").getPos());
        DrawObjectToScreen("hat", new LocalData(MainActivity.this, "hat").getPos());
        DrawObjectToScreen("pet", new LocalData(MainActivity.this, "pet").getPos());
        DrawObjectToScreen("tag", new LocalData(MainActivity.this, "tag").getPos());
    }

    // Hàm lưu hình ảnh đến thư viện
    public boolean SaveImageToGallery(int idlayout) {
        RelativeLayout rlMain = findViewById(idlayout);
        rlMain.setDrawingCacheEnabled(false);
        rlMain.setDrawingCacheEnabled(true);
        Bitmap bmp = rlMain.getDrawingCache();

        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.PNG, 60, fos);
            fos.flush();
            fos.close();

            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), fileName, null);

            Uri uri = Uri.fromFile(file);
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                Toast.makeText(this,"Lưu Thành Công",Toast.LENGTH_LONG).show();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //=============================================================================================//
    // Thao tác database
    // Lấy dữ liệu có liên quan đến background
    private ArrayList<Integer> getData_BG() {
        ArrayList<Integer> listImageResource_BG = new ArrayList<>();
        TypedArray list = this.getResources().obtainTypedArray(R.array.background);
        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                int id = list.getResourceId(i, -1);
                listImageResource_BG.add(id);
            }
            list.recycle();
        }
        return listImageResource_BG;
    }

    // Lấy dữ liệu có liên quan đến body
    private ArrayList<Integer> getData_Body() {
        ArrayList<Integer> listImageResource_BG = new ArrayList<>();
        TypedArray list = this.getResources().obtainTypedArray(R.array.body);
        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                int id = list.getResourceId(i, -1);
                listImageResource_BG.add(id);
            }
            list.recycle();
        }
        return listImageResource_BG;
    }

    // Lấy dữ liệu có liên quan đến clothes
    private ArrayList<Integer> getData_Clothes() {
        ArrayList<Integer> listImageResource_BG = new ArrayList<>();
        TypedArray list = this.getResources().obtainTypedArray(R.array.clothes);
        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                int id = list.getResourceId(i, -1);
                listImageResource_BG.add(id);
            }
            list.recycle();
        }
        return listImageResource_BG;
    }

    // Lấy dữ liệu có liên quan đến hat
    private ArrayList<Integer> getData_Hat() {
        ArrayList<Integer> listImageResource_BG = new ArrayList<>();
        TypedArray list = this.getResources().obtainTypedArray(R.array.hat);
        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                int id = list.getResourceId(i, -1);
                listImageResource_BG.add(id);
            }
            list.recycle();
        }
        return listImageResource_BG;
    }

    // Lấy dữ liệu có liên quan đến pet
    private ArrayList<Integer> getData_Pet() {
        ArrayList<Integer> listImageResource_BG = new ArrayList<>();
        TypedArray list = this.getResources().obtainTypedArray(R.array.pet);
        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                int id = list.getResourceId(i, -1);
                listImageResource_BG.add(id);
            }
            list.recycle();
        }
        return listImageResource_BG;
    }

    // Lấy dữ liệu có liên quan đến tag
    private ArrayList<Integer> getData_Tag() {
        ArrayList<Integer> listImageResource_BG = new ArrayList<>();
        TypedArray list = this.getResources().obtainTypedArray(R.array.tag);
        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                int id = list.getResourceId(i, -1);
                listImageResource_BG.add(id);
            }
            list.recycle();
        }
        return listImageResource_BG;
    }
}