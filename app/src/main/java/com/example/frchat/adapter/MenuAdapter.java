package com.example.frchat.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frchat.R;
import com.example.frchat.listeners.MenuListener;
import com.example.frchat.models.Menu;

import java.util.List;

public class MenuAdapter extends  RecyclerView.Adapter<MenuAdapter.MenuViewHolder>{
    private List<Menu> Menus;
    private MenuListener menuListener;

    public MenuAdapter(List<Menu> menus, MenuListener menuListener) {
        Menus = menus;
        this.menuListener = menuListener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_menu,parent,false);
        return new MenuViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        final Menu menu = Menus.get(position);
        if(menu == null){
            return;
        }
        holder.imageMenu.setImageResource(menu.getImage());
        holder.textMenu.setText(menu.getName());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuListener.onClickItemMenu(menu);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Menus.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageMenu;
        private TextView textMenu;
        private LinearLayout layoutItem;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMenu = itemView.findViewById(R.id.imageMenu);
            textMenu = itemView.findViewById(R.id.textMenu);
            layoutItem = itemView.findViewById(R.id.layoutItem);
        }
    }

}
