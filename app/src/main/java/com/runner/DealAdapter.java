package com.runner;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.runner.andela2.FirebaseUtil;
import com.runner.andela2.InsertActivity;
import com.runner.andela2.R;
import com.runner.andela2.model.TravelDeal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {
    private FirebaseDatabase mFirebaseDtabase;
    private DatabaseReference mDtavasereference;
    private ChildEventListener mChildListener;
    ArrayList<TravelDeal> dealArrayList;

    public DealAdapter(){
        //FirebaseUtil.openFbReference("traveldeals",);
        mFirebaseDtabase = FirebaseDatabase.getInstance();
        mDtavasereference = FirebaseUtil.mDatabaseReference;
        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal", td.getTitle());
                td.setId(dataSnapshot.getKey());
                dealArrayList.add(td);
                notifyItemInserted(dealArrayList.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dealArrayList = FirebaseUtil.mDeals;
        mDtavasereference.addChildEventListener(mChildListener);



    }

    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        Context context = parent.getContext();
        final View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row,parent,false);
        final DealViewHolder holder= new DealViewHolder(itemView);

//        itemView.setOnClickListener((View.OnClickListener) context);
        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {

        TravelDeal deal= dealArrayList.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return dealArrayList.size();
    }


    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
TextView tvTitle, txtDescription,tprice;
ImageView imageDeal;
    public DealViewHolder(@NonNull View itemView) {
        super(itemView);

        tvTitle = itemView.findViewById(R.id.textViewtitle);
        txtDescription= itemView.findViewById(R.id.textViewDescription);
        tprice = itemView.findViewById(R.id.textviewPrice);
        imageDeal = itemView.findViewById(R.id.imageViewTODsiplay);
        itemView.setOnClickListener(this);
    }

    public void bind(TravelDeal deal){
        tvTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        tprice.setText((deal.getPrice()));

    }

        @Override
        public void onClick(View v) {
//            int position = getAdapterPosition();
            Log.d(" ","CLICKED ") ;
//            TravelDeal selectDeal = dealArrayList.get(position);
//            Intent intent = new Intent(itemView.getContext(), InsertActivity.class);
//            intent.putExtra("Deal", selectDeal);
//            v.getContext().startActivity(intent);

        }


        private void showImage(String url){
        if(url != null && url.isEmpty()==false){

            Picasso.get()
                    .load(url)
                    .resize(160,160)
                    .centerCrop()
                    .into(imageDeal);


        }
        }
    }
}
