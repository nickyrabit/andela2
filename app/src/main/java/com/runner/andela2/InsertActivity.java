package com.runner.andela2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.Resource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.runner.andela2.model.TravelDeal;
import com.squareup.picasso.Picasso;

public class InsertActivity extends AppCompatActivity {
private FirebaseDatabase mFirebaseDtabase;
private DatabaseReference mDatabaseReference;
EditText titleEditText, descriptionEditText,priceEditText;
TravelDeal deal;
public  static int PICTURE_RESULT =100;
Button btnImage;
ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        FirebaseApp.initializeApp(this);

        FirebaseUtil.openFbReference("traveldeals",this);
        mFirebaseDtabase = FirebaseDatabase.getInstance();
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
image = findViewById(R.id.imageViewTODsiplay);
     titleEditText = findViewById(R.id.textTitle);
     descriptionEditText = findViewById(R.id.textDescription);
     priceEditText = findViewById(R.id.textPrice);

     btnImage = findViewById(R.id.buttonImage);
     btnImage.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent i = new Intent(Intent.ACTION_GET_CONTENT);
             i.setType("image/jpeg");
             i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
             startActivityForResult(getIntent().createChooser(i, "INsert Picture"),PICTURE_RESULT);
         }
     });

     Intent intent = getIntent();
     TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
     if(deal==null){
         deal = new TravelDeal();
     }
     this.deal = deal;

        titleEditText.setText(deal.getTitle());
        descriptionEditText.setText(deal.getDescription());
        priceEditText.setText(deal.getPrice());

        showImage(deal.getImageUrl());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICTURE_RESULT && resultCode== RESULT_OK){

            assert data != null;
            Uri imageURL  = data.getData();
            StorageReference ref = FirebaseUtil.mStorageRef.child(imageURL.getLastPathSegment());
            ref.putFile(imageURL).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url = taskSnapshot.getMetadata().getPath();
                    String picName = taskSnapshot.getStorage().getPath();

                    deal.setImageUrl(url);
                    deal.setImageName(picName);


                    showImage(url);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this,"Deal Saved", Toast.LENGTH_LONG).show();
                clean();
                return true;

            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this,"Removed THe Deal", Toast.LENGTH_LONG).show();
                backTOList();
                return true;


            default:
                    return super.onOptionsItemSelected(item);

        }
      }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu) ;
        return true;
    }

    public void saveDeal(){
        deal.setTitle(titleEditText.getText().toString());
        deal.setDescription(descriptionEditText.getText().toString());
        deal.setPrice(priceEditText.getText().toString());

if(deal.getId()==null){
    mDatabaseReference.push().setValue(deal);
}
else{

    mDatabaseReference.child(deal.getId()).setValue(deal);

}



    }


    private void deleteDeal(){

        if(deal==null){
            Toast.makeText(this,"Please saevt e deal before deleting",Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabaseReference.child(deal.getId()).removeValue();


    if(deal.getImageName() != null && deal.getImageName().isEmpty()==false){
        StorageReference pickRef = FirebaseUtil.mStorage.getReference().child(deal.getImageName());
        pickRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        }
    }




        private void backTOList(){
        Intent intent = new Intent(this,ListActivity.class);
        startActivity(intent);
        }




    public void clean(){
        titleEditText.setText("");
        descriptionEditText.setText("");
        priceEditText.setText("");


    }
    private void showImage(String url){
        if(url != null && url.isEmpty()==false){
int width = Resources.getSystem().getDisplayMetrics().widthPixels;
               Picasso.get()
                    .load(url)
                    .resize(50, 50)
                    .centerCrop()
                    .into(image);

        }

    }

}
