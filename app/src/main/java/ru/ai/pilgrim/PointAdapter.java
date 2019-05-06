package ru.ai.pilgrim;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

public class PointAdapter extends RecyclerView.Adapter<PointsHolder> {
    Context c;
    ArrayList<Point> points;

    public PointAdapter (Context context, ArrayList<Point> points) {
        this.c = context;
        this.points = points;
    }
    @Override
    public PointsHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v =
                LayoutInflater.from
                        (parent.getContext()).inflate(R.layout.pointscard,null);
        PointsHolder holder = new PointsHolder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(PointsHolder holder, final int position) {
        holder.description.setText(points.get(position).getDescription());
        holder.img.setImageBitmap(points.get(position).getPhoto());
        holder.setItemClickListener(new View.OnClickListener() {
            @Override public void onClick (View v) {
                Intent i = new Intent(c,PointActivity.class);
                i.putExtra("callRule",2);
                i.putExtra("pos",position);
                c.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return points.size();
    }
}
