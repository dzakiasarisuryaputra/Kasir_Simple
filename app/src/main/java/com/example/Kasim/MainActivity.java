package com.example.Kasim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.view.KeyEvent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable{

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;
    private ImageView logout;

    final static int PRODUCT_REQUEST = 100;
    final static int PRODUCT_RESULT = 101;
    private RecyclerView rvMenuMakanan;

    private final ArrayList<ModelMenuMakanan> listCart = new ArrayList<>();
    int count;

    private ArrayList<ModelMenuMakanan> productArray;
    private AdapterMenuMakanan adapter;
    TextView tvCount;
    ImageView ivC;
    Boolean exist = false;

    private Button btGrid, btList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = (ImageView) findViewById(R.id.logOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        userID = user.getUid();

        final TextView namaTV = (TextView) findViewById(R.id.kasimText);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    String nama = userProfile.nama;
                    namaTV.setText(nama);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Ada kesalahan sistem!",Toast.LENGTH_LONG)
                        .show();
            }
        });
        rvMenuMakanan = findViewById(R.id.rvMenuMakanan);
        tvCount = findViewById(R.id.tv_countItem);
        ivC = findViewById(R.id.iv_count);
        btGrid = findViewById(R.id.btGrid);
        btList = findViewById(R.id.btList);

        ivC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ModelMenuMakanan> productArray = new ArrayList<>();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < listCart.size(); i++) {
                            if (listCart.get(i).getiAmount() > 0) {
                                productArray.add(listCart.get(i)); //addData to productArray
                                exist = true;
                            }
                        }
                    }
                };
                runnable.run();
                if (exist) {
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    intent.putExtra("productArray", productArray);
                    Log.d("productArray", productArray.toString());
                    startActivityForResult(intent, PRODUCT_REQUEST);
                }
            }
        });

        initData();
        btGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSwitched = adapter.toggleItemViewType();
                rvMenuMakanan.setLayoutManager(isSwitched ? new LinearLayoutManager(MainActivity.this) : new GridLayoutManager(MainActivity.this, 2));
                adapter.notifyDataSetChanged();
            }
        });

        btList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSwitched = adapter.toggleItemViewType();
                rvMenuMakanan.setLayoutManager(isSwitched ? new LinearLayoutManager(MainActivity.this) : new GridLayoutManager(MainActivity.this, 2));
                adapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PRODUCT_RESULT) {
            productArray = new ArrayList<>();
            productArray = (ArrayList<ModelMenuMakanan>) data.getSerializableExtra("productArray");

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < listCart.size(); i++) {
                        listCart.get(i).setiAmount(0);
                        for (int j = 0; j < productArray.size(); j++) {
                            if (listCart.get(i).getNama().equals(productArray.get(j).getNama())) {
                                listCart.get(i).setiAmount(productArray.get(j).getiAmount());
                            }
                        }
                    }
                }
            };
            runnable.run();
            adapter.notifyDataSetChanged();
        }
    }

    public void  showProducts() {
        rvMenuMakanan.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new AdapterMenuMakanan(MainActivity.this, listCart);
        rvMenuMakanan.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterMenuMakanan.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });
    }

    private void initData(){
        listCart.add(new ModelMenuMakanan("Bakso",
                "Bakso Sapi",
                R.drawable.bakso,
                0,
                0,
                13000));

        listCart.add(new ModelMenuMakanan("Bubur",
                "Bubur Ayam",
                R.drawable.bubur,
                1,
                0,
                10000));

        listCart.add(new ModelMenuMakanan("Dendeng Batok",
                "Dendeng Batok & Nasi",
                R.drawable.dendengbatok,
                2,
                0,
                16000));

        listCart.add(new ModelMenuMakanan("Sate",
                "Sate Kambing & Lontong",
                R.drawable.sate,
                3,
                0,
                15000));

        listCart.add(new ModelMenuMakanan("Soto",
                "Nasi Soto",
                R.drawable.soto,
                4,
                0,
                12000));

        listCart.add(new ModelMenuMakanan("Sushi",
                "Japanese Food",
                R.drawable.sushi,
                5,
                0,
                20000));

        showProducts();
    }
}