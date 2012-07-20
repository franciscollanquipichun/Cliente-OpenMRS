package dis.ufro.openRMS;

import org.apache.http.entity.StringEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import dis.ufro.openmrsrest.ApiAuthRest;

/**
 * 
 * @author Francisco Llanquipichun
 * 
 */
public class CrearPrestacionClinica extends Activity {

	private Spinner sp_pacientes;
	private Spinner sp_tipoencuentro;
	private Button btn_crear;

	private String[] listaPersonas;
	private String[] listaPersonasUudi;
	private String[] listaTipo;
	private String[] listaTipoUuid;
	private String pacienteUuid;
	private String tipoUuid;
	private String date = "2011-1-21 2:00:00";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crear_prestacion_clinica);

		sp_pacientes = (Spinner) findViewById(R.id.sp_creacion_prestacion_clinica_paciente);
		sp_tipoencuentro = (Spinner) findViewById(R.id.sp_creacion_prestacion_cliente_tipoencuentro);
		btn_crear = (Button) findViewById(R.id.btn_crear_prestacion_clinica_crearNueva);

		listarPersonas();
		listarTipo();
		/*
		 * Spinner Personas
		 */
		if (listaPersonas != null) {
			ArrayAdapter<String> adaptadorPaciente = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, listaPersonas);
			adaptadorPaciente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_pacientes.setAdapter(adaptadorPaciente);

			sp_pacientes.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> parent,
								View v, int posicion, long id) {
							pacienteUuid = listaPersonasUudi[posicion];
						}

						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
		}

		/*
		 * Spinner tipo de prestaciones
		 */
		if (listaTipo != null) {
			ArrayAdapter<String> adaptadorTipo = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, listaTipo);
			adaptadorTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_tipoencuentro.setAdapter(adaptadorTipo);

			sp_tipoencuentro.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> arg0, View v,
								int posicion, long id) {
							tipoUuid = listaTipoUuid[posicion];
						}

						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
		}

		/*
		 * Button crear prestación
		 */
		btn_crear.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					crearPrestacion();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Genera listas de tipos de encuentros
	 */
	private void listarTipo() {
		try {
			Object obj = JSONValue.parse(ApiAuthRest
					.getRequestGet("encountertype?q"));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray arrayResult = (JSONArray) jsonObject.get("results");
			int largoArray = arrayResult.size();
			listaTipo = new String[largoArray];
			listaTipoUuid = new String[largoArray];
			int contador;
			for (contador = 0; contador < largoArray; contador++) {
				JSONObject registro = (JSONObject) arrayResult.get(contador);
				listaTipoUuid[contador] = ((String) registro.get("uuid"));
				listaTipo[contador] = ((String) registro.get("display"));
				Log.d("Tipos encontrados", "Rows " + contador
						+ " => Result Tipo UUID:" + listaTipoUuid[contador]
						+ " Display:" + listaTipo[contador]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Genera listas de personas
	 */
	private void listarPersonas() {
		try {
			Object obj = JSONValue.parse(ApiAuthRest.getRequestGet("person?q"));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray arrayResult = (JSONArray) jsonObject.get("results");
			int largoArray = arrayResult.size();
			listaPersonas = new String[largoArray];
			listaPersonasUudi = new String[largoArray];
			int contador;
			for (contador = 0; contador < largoArray; contador++) {
				JSONObject registro = (JSONObject) arrayResult.get(contador);
				listaPersonasUudi[contador] = ((String) registro.get("uuid"));
				listaPersonas[contador] = ((String) registro.get("display"));
				Log.d("Pacientes encontrados", "Rows " + contador
						+ " => Result Tipo UUID:" + listaPersonasUudi[contador]
						+ " Display:" + listaPersonas[contador]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crea una nueva prestación
	 * 
	 * @throws Exception
	 */
	private void crearPrestacion() throws Exception {

		StringEntity input = new StringEntity("{\"encounterDatetime\": \""
				+ date + "\", " + "\"patient\" : \"" + pacienteUuid + "\", "
				+ "\"encounterType\": \"" + tipoUuid + "\"" + "}");
		
		input.setContentType("application/json");
		
		if (ApiAuthRest.getRequestPost("encounter", input) == true) {
			Toast.makeText(getApplicationContext(),
					"Prestacion creada correctamente ", Toast.LENGTH_LONG)
					.show();
		}
	}
}
