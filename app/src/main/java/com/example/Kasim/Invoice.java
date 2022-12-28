package com.example.Kasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

public class Invoice extends AppCompatActivity implements Serializable , View.OnClickListener{

    private Button home;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;
    private ImageView logout;

    private TextView tvTotal;
    private Button btnBayar;
    Bundle bundle;
    int finalPrice = 0;
    private RecyclerView recyclerView;
    AdapterInvoice adapterCart;
    private ArrayList<ModelMenuMakanan> modelMenu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        home = (Button) findViewById(R.id.btnHome);
        home.setOnClickListener(this);

        logout = (ImageView) findViewById(R.id.ivLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Invoice.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        userID = user.getUid();

        final TextView namaTV = (TextView) findViewById(R.id.tvUser);
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
                Toast.makeText(Invoice.this,"Ada kesalahan sistem!",Toast.LENGTH_LONG)
                        .show();
            }
        });

        recyclerView = findViewById(R.id.rvInvoice);
        tvTotal = findViewById(R.id.txtTotalinv);
        btnBayar = findViewById(R.id.btnHome);

        bundle = getIntent().getExtras();

        modelMenu = new ArrayList<>();
        modelMenu = (ArrayList<ModelMenuMakanan>) getIntent().getSerializableExtra("productArray");
        addData();
        hitung();
    }

    private void addData(){
        adapterCart = new AdapterInvoice(Invoice.this, modelMenu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterCart);
    }

    public void hitung(){
        modelMenu = new ArrayList<>();
        modelMenu = (ArrayList<ModelMenuMakanan>) getIntent().getSerializableExtra("productArray");
        for (int i = 0; i < modelMenu.size(); i++) {
            finalPrice += modelMenu.get(i).getiAmount() * modelMenu.get(i).getiPrice();
        }
        tvTotal.setText("Total Belanja : Rp. " + finalPrice+" ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnHome:
               startActivity(new Intent(this,MainActivity.class));
               finish();
               break;
        }
    }
}