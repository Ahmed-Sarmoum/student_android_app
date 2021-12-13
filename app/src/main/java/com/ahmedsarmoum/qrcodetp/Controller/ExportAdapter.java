package com.ahmedsarmoum.qrcodetp.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedsarmoum.qrcodetp.R;

import java.util.List;

public class ExportAdapter  extends RecyclerView.Adapter<ExportAdapter.MyViewAdapter> {

    List<String> listFolders;
    Context context;
    DatabaseHandler databaseHandler;

    public ExportAdapter(Context context, List<String> listFolders, DatabaseHandler databaseHandler) {
        this.context = context;
        this.listFolders = listFolders;
        this.databaseHandler = databaseHandler;
    }

    @NonNull
    @Override
    public ExportAdapter.MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_export_folder, parent, false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExportAdapter.MyViewAdapter holder, int position) {
        String listFolder = listFolders.get(position);

        holder.nameFolder.setText(listFolder);
    }

    @Override
    public int getItemCount() {
        return listFolders.size();
    }

    public class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView nameFolder;
        LinearLayout parent;

        public MyViewAdapter(@NonNull View itemView) {
            super(itemView);
            nameFolder = itemView.findViewById(R.id.nameFolder);
            parent = itemView.findViewById(R.id.parents);
        }
    }
}
