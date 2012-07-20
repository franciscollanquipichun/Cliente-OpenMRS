package dis.ufro.openRMS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * 
 * @author Francisco Llanquipichun
 *
 */
public class Principal extends Activity {

	private ListView lv_opciones;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.principal);
		
		/*
		 * Opciones del menú
		 */
		String[] opciones = { "Crear persona", "Registrar paciente", "Crear prestación clínica",
				"Ver prestaciones clínicas" };

		lv_opciones = (ListView) findViewById(R.id.lv_principal_opciones);

		/*
		 * Llena la lista
		 */
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, opciones);
		
		lv_opciones.setAdapter(adaptador);
		
		/*
		 * Acción de la lista
		 */
		lv_opciones.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v, int posicion,
					long id) {
				if(posicion==0){
					Intent i = new Intent(Principal.this, RegistroPersona.class);
					startActivity(i);
				}
				if(posicion==1){
					Intent i = new Intent(Principal.this, RegistroPaciente.class);
					startActivity(i);
				}
				if(posicion==2){
					Intent i = new Intent(Principal.this, CrearPrestacionClinica.class);
					startActivity(i);					
				}
				if(posicion==3){
					Intent i = new Intent(Principal.this, VerPrestacionClinica.class);
					startActivity(i);
				}
			}
		});

	}
}
