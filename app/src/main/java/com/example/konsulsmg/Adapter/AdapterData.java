package com.example.konsulsmg.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.konsulsmg.API.APIRequestData;
import com.example.konsulsmg.API.RetroServer;
import com.example.konsulsmg.Activity.MainActivity;
import com.example.konsulsmg.Model.DataModel;
import com.example.konsulsmg.Model.ResponseModel;
import com.example.konsulsmg.R;

import java.security.PrivateKey;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{
    private Context ctx;
    private List<DataModel> listKonsul;
    private int idKonsul;

    public AdapterData(Context ctx, List<DataModel> listKonsul) {
        this.ctx = ctx;
        this.listKonsul = listKonsul;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        HolderData holder =new HolderData(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {

        DataModel dm = listKonsul.get(position);
        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvNama.setText(dm.getNama());
        holder.tvAlamat.setText(dm.getAlamat());
        holder.tvTelepon.setText(dm.getTelepon());

    }

    @Override
    public int getItemCount() {
        return listKonsul.size();
    }

    public class HolderData extends RecyclerView.ViewHolder
    {
        TextView tvId, tvNama, tvAlamat, tvTelepon;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat= itemView.findViewById(R.id.tv_alamat);
            tvTelepon= itemView.findViewById(R.id.tv_telepon);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setMessage("Pilih Operasi yang diingikan");
                   // dialogPesan.setIcon(R.mipmap.ic_launcher);
                    dialogPesan.setCancelable(true);

                    idKonsul = Integer.parseInt(tvId.getText().toString());

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            ((MainActivity)ctx).retrieveData();


                        }
                    });

                    dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    });
                    dialogPesan.show();


                    return false;
                }
            });
        }

        private void  deleteData ()
        {
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(idKonsul);
            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan =response.body().getPesan();

                    Toast.makeText(ctx, "Kode : "+kode+"|Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {

                    Toast.makeText(ctx, "Gagal Menghubungi Pesan"+t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
