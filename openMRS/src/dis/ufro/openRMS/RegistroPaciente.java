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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import dis.ufro.openmrsrest.ApiAuthRest;

/**
 * 
 * @author Francisco Llanquipichun
 * 
 */
public class RegistroPaciente extends Activity {

	private Spinner sp_personas;
	private EditText et_identificador;
	private Button btn_registrar;

	private String[] listaPersonas;
	private String[] listaPersonasUudi;

	private String persona;
	private String personaUudi;
	private String identificador;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro_paciente);

		sp_personas = (Spinner) findViewById(R.id.sp_registro_paciente_persona);
		et_identificador = (EditText) findViewById(R.id.et_registro_paciente_identificador);
		btn_registrar = (Button) findViewById(R.id.btn_registro_paciente_registrar);

		listarPersonas();
		/*
		 * Spinner personas
		 */
		if (listaPersonas != null) {

			ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, listaPersonas);
			adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_personas.setAdapter(adaptador);

			sp_personas.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View v,
						int posicion, long id) {
					personaUudi = listaPersonasUudi[posicion];
					persona = listaPersonas[posicion];
					identificador = et_identificador.getText().toString();
				}

				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

		} else {
			Toast.makeText(getApplicationContext(), "Lista de personas vacía",
					Toast.LENGTH_LONG).show();
		}

		/*
		 * Acción del botón
		 */
		btn_registrar.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					agregarPaciente();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Agrega un nuevo paciente
	 * 
	 * @throws Exception
	 * 
	 */
	private void agregarPaciente() throws Exception {

		String output = ApiAuthRest.getRequestGet("patientidentifiertype");

		Object obj = JSONValue.parse(output);
		JSONObject json = (JSONObject) obj;

		JSONArray jsonArray = (JSONArray) json.get("results");
		JSONObject rec = (JSONObject) jsonArray.get(0);

		String uuidPatientidentifiertype = (String) rec.get("uuid");

		output = ApiAuthRest.getRequestGet("location");

		obj = JSONValue.parse(output);
		json = (JSONObject) obj;

		jsonArray = (JSONArray) json.get("results");
		rec = (JSONObject) jsonArray.get(0);
		String uuidLocationUnknow = (String) rec.get("uuid");

		StringEntity input = new StringEntity("{\"person\": \"" + personaUudi
				+ "\", \"identifiers\": [{\"identifier\":\"" + identificador
				+ "\", \"identifierType\":\"" + uuidPatientidentifiertype
				+ "\", \"location\":\"" + uuidLocationUnknow
				+ "\", \"preferred\":true}]}");

		input.setContentType("application/json");

		if (ApiAuthRest.getRequestPost("patient", input) == true) {
			Toast.makeText(getApplicationContext(),
					"Paciente: " + persona + " creada correctamente",
					Toast.LENGTH_LONG).show();
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

}