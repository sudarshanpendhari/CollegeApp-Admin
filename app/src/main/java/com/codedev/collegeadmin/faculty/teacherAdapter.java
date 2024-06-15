package com.codedev.collegeadmin.faculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codedev.collegeadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class teacherAdapter extends RecyclerView.Adapter<teacherAdapter.teacherViewAdapter> {
    private List<teacherData> list;
    private Context context;
    private String category;

    public teacherAdapter(List<teacherData> list, Context context,String category) {
        this.list = list;
        this.context = context;
        this.category=category;
    }

    @NonNull
    @Override
    public teacherViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.faculty_item_layout,parent,false);
        return new teacherViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull teacherViewAdapter holder, int position) {
            teacherData item= list.get(position);
            holder.name.setText(item.getName());
            holder.email.setText(item.getEmail());
            holder.post.setText(item.getPost());
            holder.blog.setText(item.getBlog());
        try {
            Picasso.get().load(item.getImage()).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,updateTeacher.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("email",item.getEmail());
                intent.putExtra("post",item.getPost());
                intent.putExtra("image",item.getImage());
                intent.putExtra("key",item.getKey());
                intent.putExtra("blog",item.getBlog());
                intent.putExtra("category",category);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class teacherViewAdapter extends RecyclerView.ViewHolder {
        private TextView name,email,post,blog;
        private Button update;
        private ImageView imageView;

        public teacherViewAdapter(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.teacherName);
            email=itemView.findViewById(R.id.teacherEmail);
            post=itemView.findViewById(R.id.teacherPost);
            imageView=itemView.findViewById(R.id.teacherImage);
            update=itemView.findViewById(R.id.teacherUpdate);
            blog=itemView.findViewById(R.id.teacherBlog);


        }
    }
}
