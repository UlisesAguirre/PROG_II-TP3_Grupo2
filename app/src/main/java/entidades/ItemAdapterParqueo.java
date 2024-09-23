package entidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tp3_grupo2.R;

import java.util.ArrayList;

public class ItemAdapterParqueo extends BaseAdapter {
    private Context context;
    private ArrayList<Parqueo> items;

    public ItemAdapterParqueo(Context context, ArrayList<Parqueo> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        }

        // Obtener el TextView y asignar los datos
        TextView txtMatricula = convertView.findViewById(R.id.txt_matricula);
        TextView txtTiempo = convertView.findViewById(R.id.txt_tiempo);

        Parqueo item = items.get(position);

        txtMatricula.setText(item.getMatricula());
        txtTiempo.setText(Integer.toString(item.getTiempo()));

        return convertView;
    }
}