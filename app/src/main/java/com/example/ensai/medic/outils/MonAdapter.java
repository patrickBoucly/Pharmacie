package com.example.ensai.medic.outils;

/**
 * Created by ensai on 19/05/17.
 */



        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ListView;

        import com.example.ensai.medic.Medic;
        import com.example.ensai.medic.R;
        import com.example.ensai.medic.outils.ContextProvider;
        import java.util.List;


public class MonAdapter extends BaseAdapter {
    private List<Medic> medics;

    public MonAdapter(List<Medic> medics) {
        this.medics = medics;


    }

    public int getCount() {
        return this.getMedics().size();
    }

    @Override
    public Medic getItem(int position) {
        return this.getMedics().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        View vue;
        if(convertView==null) {
            Context sContext = ContextProvider.getContext();
            LayoutInflater li = (LayoutInflater) sContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            vue = li.inflate(R.layout.mapharma, null);

        } else{
            vue=convertView;
        }
        Medic medic=(Medic) getItem(position);
        ListView mes_medic=(ListView) vue.findViewById(R.id.mes_medic);
        return vue;
    }


    public List<Medic> getMedics(){
        return this.medics;

    }
}