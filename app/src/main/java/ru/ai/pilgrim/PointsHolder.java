package ru.ai.pilgrim;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PointsHolder extends RecyclerView.ViewHolder
         implements AdapterView.OnClickListener{
    ImageView img;
    TextView description;
    View.OnClickListener itemClickListener;
    public PointsHolder(View itemView) {
        super(itemView);
        img = (ImageView) itemView.findViewById(R.id.photo);
        description =( TextView) itemView.findViewById(R.id.description);
        itemView.setOnClickListener(this);
    }
    @Override public void onClick(View v) {
       // onClick(v);
        Intent intent = new Intent(v.getContext(),PointActivity.class);
        intent.putExtra("callRule",2);
        intent.putExtra("pos",getLayoutPosition());
        v.getContext().startActivity(intent);
    }
    public void setItemClickListener(View.OnClickListener icl){
        this.itemClickListener =icl;
    }
}
