package dis.ufro.openRMS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import dis.ufro.openmrsrest.ApiAuthRest;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class VerPrestacionClinica extends Activity {

	private Spinner sp_pacientes;
	private ListView lv_prestaciones;
	private Button btn_listar;
	
	private String[] listaPacientes;
	private String[] listaPacientesUuid;
	private String[] listaPrestaciones;
	private String[] listaPrestacionUudi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ver_prestacion_clinica);
		
		sp_pacientes = (Spinner) findViewById(R.id.sp_ver_prestacion_clinica_paciente);
		lv_prestaciones = (ListView) findViewById(R.id.lv_ver_prestacione_clinica_prestaciones);
		btn_listar = (Button) findViewById(R.id.btn_ver_prestacion_clinica_listar);
		
		listarPacientes();
		listarPrestaciones();
		/*
		 * Spinner pacientes
		 */
		if (listaPacientes != null) {
			
			ArrayAdapter<String> adaptadorPaciente = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaPacientes);
			adaptadorPaciente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_pacientes.setAdapter(adaptadorPaciente);
		} else {
			Toast.makeText(getApplicationContext(), "Lista de pacientes vacia",
					Toast.LENGTH_LONG).show();
		}
		/*
		 * ListView prestaciones
		 */
		if (listaPrestaciones != null) {
			
			ArrayAdapter<String> adaptadorPrestaciones = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaPrestaciones);
			sp_pacientes.setAdapter(adaptadorPrestaciones);
		} else {
			Toast.makeText(getApplicationContext(), "Lista de prestaciones vacia",
					Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * genera la lista de pasientes
	 */
	private void listarPacientes() {
		try {
			Object obj = JSONValue.parse(ApiAuthRest
					.getRequestGet("patient?q"));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray arrayResult = (JSONArray) jsonObject.get("results");
			int largoArray = arrayResult.size();
			listaPacientes = new String[largoArray];
			listaPacientesUuid = new String[largoArray];
			int contador;
			for (contador = 0; contador < largoArray; contador++) {
				JSONObject registro = (JSONObject) arrayResult.get(contador);
				listaPacientesUuid[contador] = ((String) registro.get("uuid"));
				listaPacientes[contador] = ((String) registro.get("display"));
				Log.d("Pacientes encontrados", "Rows " + contador
						+ " => Result Tipo UUID:" + listaPacientesUuid[contador]
						+ " Display:" + listaPacientes[contador]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void listarPrestaciones(){
		try {
			Object obj = JSONValue.parse(ApiAuthRest
					.getRequestGet("encounter?q"));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray arrayResult = (JSONArray) jsonObject.get("results");
			int largoArray = arrayResult.size();
			listaPrestaciones = new String[largoArray];
			listaPrestacionUudi = new String[largoArray];
			int contador;
			for (contador = 0; contador < largoArray; contador++) {
				JSONObject registro = (JSONObject) arrayResult.get(contador);
				listaPrestacionUudi[contador] = ((String) registro.get("uuid"));
				listaPrestaciones[contador] = ((String) registro.get("display"));
				Log.d("Prestaciones encontradass", "Rows " + contador
						+ " => Result Tipo UUID:" + listaPrestacionUudi[contador]
						+ " Display:" + listaPrestaciones[contador]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
