package com.ahmedsarmoum.qrcodetp.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedsarmoum.qrcodetp.Model.Presence;
import com.ahmedsarmoum.qrcodetp.R;
import com.ahmedsarmoum.qrcodetp.View.NoteActivity;
import com.ahmedsarmoum.qrcodetp.View.PresonceActivity;

import java.util.List;

public class PresonceAdapter extends RecyclerView.Adapter<PresonceAdapter.MyViewHolder> {

    private Context context;
    private List<Presence> presences;
    private DatabaseHandler databaseHandler;

    public PresonceAdapter(Context context, List<Presence> presences, DatabaseHandler databaseHandler) {
        this.context = context;
        this.presences = presences;
        this.databaseHandler = databaseHandler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_presonce, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Presence presence = presences.get(position);
        //
        String fullname = databaseHandler.getStudent(presence.getStudent_id()).getFirstName() + " "+databaseHandler.getStudent(presence.getStudent_id()).getLastName();
        holder.nomeStudent.setText(fullname);
        holder.date.setText(presence.getCreated_at());

        holder.deletePresence.setOnClickListener(view -> {
            View dialog = LayoutInflater.from(context).inflate(R.layout.logout_dialog,null, false);
            AlertDialog.Builder dBuilder = new AlertDialog.Builder(context)
                    .setView(dialog);

            final AlertDialog dAlert = dBuilder.create();
            if (dAlert.getWindow() != null)
                dAlert.getWindow().getAttributes().windowAnimations = R.style.FideingDialogAnimation;
            dAlert.show();

            dialog.findViewById(R.id.oui).setOnClickListener(view12 -> {
                databaseHandler.deletePresence(presence);
                dAlert .dismiss();
                presences.remove(position);
                Toast.makeText(context, "the presence of " + fullname+ " has been deleted", Toast.LENGTH_LONG).show();
                PresonceActivity.notifyAdapter();
            });
            dialog.findViewById(R.id.non).setOnClickListener(view12 -> {
                dAlert .dismiss();
            });

        });
    }

    @Override
    public int getItemCount() {
        return presences.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeStudent, date;
        public ImageView deletePresence;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeStudent = itemView.findViewById(R.id.nameStudent);
            date = itemView.findViewById(R.id.date);
            deletePresence = itemView.findViewById(R.id.btnDeleteAction);
        }
    }
}
