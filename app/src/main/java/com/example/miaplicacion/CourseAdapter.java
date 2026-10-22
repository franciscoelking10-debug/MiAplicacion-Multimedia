package com.example.miaplicacion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<CourseModel> courseModelArrayList;

    // Constructor
    public CourseAdapter(Context context, ArrayList<CourseModel> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de la tarjeta (card_layout.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {
        // Asignar los datos del modelo al ViewHolder
        CourseModel model = courseModelArrayList.get(position);
        holder.tvCourseName.setText(model.getCourseName());
        holder.tvCourseRating.setText(String.format("%.1f ★", model.getCourseRating()));
        holder.tvCourseStudents.setText(model.getCourseDetail());
        holder.ivCourseImage.setImageResource(model.getCourseImage());

        // Interacción al pulsar la tarjeta
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Curso seleccionado: " + model.getCourseName() +
                    " (" + model.getCourseRating() + " ★)", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }

    // ViewHolder que almacena las referencias a las vistas de cada tarjeta
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivCourseImage;
        private final TextView tvCourseName;
        private final TextView tvCourseRating;
        private final TextView tvCourseStudents;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCourseImage = itemView.findViewById(R.id.ivCourseImage);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseRating = itemView.findViewById(R.id.tvCourseRating);
            tvCourseStudents = itemView.findViewById(R.id.tvCourseStudents);
        }
    }
}
