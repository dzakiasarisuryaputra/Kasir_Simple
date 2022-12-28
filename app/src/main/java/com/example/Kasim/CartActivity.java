package com.example.Kasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements Serializable {

    SQLite DB;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID,namaUser;
    private ImageView logout;

    final static int PRODUCT_REQUEST = 100;

    private TextView tvtotal;
    private Button btnSubmit;
    Bundle bundle;
    int finalPrice = 0;
    private RecyclerView recyclerView;
    AdapterCart adapterCart;
    private ArrayList<ModelMenuMakanan> modelMenuMakanan;
    Boolean exist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        logout = (ImageView) findViewById(R.id.logOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CartActivity.this,Login.class);
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
                    namaUser = userProfile.nama;
                    namaTV.setText(namaUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this,"Ada kesalahan sistem!",Toast.LENGTH_LONG)
                        .show();
            }
        });

        recyclerView = findViewById(R.id.rv_list_cart);
        tvtotal = findViewById(R.id.tv_cart_final_price);
        btnSubmit = findViewById(R.id.btn_submitCart);

        DB = new SQLite(this);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            modelMenuMakanan = new ArrayList<>();
            modelMenuMakanan = (ArrayList<ModelMenuMakanan>) getIntent().getSerializableExtra("productArray");
            addData();
            hitung();
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ModelMenuMakanan> productArray = new ArrayList<>();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < modelMenuMakanan.size(); i++) {
                            if (modelMenuMakanan.get(i).getiAmount() > 0) {
                                productArray.add(modelMenuMakanan.get(i));
                                String namaPembeli = namaUser;
                                String nama = modelMenuMakanan.get(i).getNama();
                                Integer harga = modelMenuMakanan.get(i).getiPrice();
                                Integer jumlah = modelMenuMakanan.get(i).getiAmount();
                                Boolean insertInvoice = DB.insertData(namaPembeli,nama,harga,jumlah);

                                if (insertInvoice){
                                    Toast.makeText(CartActivity.this,"Terima kasih sudah berbelanja!",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(CartActivity.this,"Gagal melakukan transaksi!",Toast.LENGTH_LONG).show();
                                }

                                exist = true;
                            }
                        }
                    }
                };

                runnable.run();
                if (exist) {
                    Intent intent = new Intent(CartActivity.this, Invoice.class);
                    intent.putExtra("productArray", productArray);
                    Log.d("productArray", productArray.toString());
                    startActivityForResult(intent, PRODUCT_REQUEST);
                }
            }
        });
    }

    private void addData() {
        adapterCart = new AdapterCart(CartActivity.this, modelMenuMakanan);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterCart);
    }

    public void hitung() {
        modelMenuMakanan = new ArrayList<>();
        modelMenuMakanan = (ArrayList<ModelMenuMakanan>) getIntent().getSerializableExtra("productArray");
        for (int i = 0; i < modelMenuMakanan.size(); i++) {
            finalPrice += modelMenuMakanan.get(i).getiAmount() * modelMenuMakanan.get(i).getiPrice();
        }
        tvtotal.setText("Total Belanja : Rp. " + finalPrice+" ");
    }

//    public void invoice(){
//        Intent intent = new Intent(this,Invoice.class);
//        startActivity(intent);
//    }
}