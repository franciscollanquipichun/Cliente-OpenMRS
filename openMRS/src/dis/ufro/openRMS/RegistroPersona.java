package dis.ufro.openRMS;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import dis.ufro.openmrsrest.ApiAuthRest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistroPersona extends Activity {

	private EditText et_nombre;
	private EditText et_apellido;
	private EditText et_edad;
	private Spinner sp_genero;
	private Button btn_crear;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro_persona);

		et_nombre = (EditText) findViewById(R.id.et_registro_paciente_nombre);
		et_apellido = (EditText) findViewById(R.id.et_registro_paciente_apellido);
		et_edad = (EditText) findViewById(R.id.et_registro_paciente_edad);
		sp_genero = (Spinner) findViewById(R.id.sp_registro_paciente_genero);
		btn_crear = (Button) findViewById(R.id.btn_registro_paciente_crear);

		// Spinner Seleccion de genero
		String[] genero = { "Masculino", "Femenino" };
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, genero);
		adaptador
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_genero.setAdapter(adaptador);

		// Acción del botón
		btn_crear.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (validarNombre() == true && validarEdad() == true) {
					try {
						agregarPersona();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	/**
	 * agrega un nuevo persona
	 * @throws Exception 
	 */
	private void agregarPersona() throws Exception {
		String nombre = et_nombre.getText().toString();
		String apellido = et_apellido.getText().toString();
		String edad = et_edad.getText().toString();
		String genero = genero();
		try {
			StringEntity inputAddPerson = new StringEntity(
					"{\"names\":[{\"givenName\": \"" + nombre
							+ "\",\"familyName\":\"" + apellido
							+ "\"}],\"gender\":\"" + genero + "\",\"age\":"
							+ edad + "}");
			inputAddPerson.setContentType("application/json");
			//respuesta
			if(ApiAuthRest.getRequestPost("person",inputAddPerson)==true){
				Toast.makeText(getApplicationContext(),
						"Persona: "+nombre+" "+apellido+" creada correctamente",
						Toast.LENGTH_LONG).show();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @return genero seleccionado
	 */
	private String genero() {
		String genero = null;
		int seleccion = sp_genero.getSelectedItemPosition();
		if (seleccion == 0) {
			genero = "M";
		} else {
			genero = "F";
		}
		return genero;
	}

	/**
	 * valida el campo nombre
	 * 
	 * @return true si se ha ingresado un nombre
	 */
	private boolean validarNombre() {
		if (et_nombre.getText().toString().length() < 0) {
			return false;
		} else if (et_apellido.getText().toString().length() < 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * valida el campo edad
	 * 
	 * @return true si la edad ingresada es valida
	 */
	private boolean validarEdad() {
		String edad = et_edad.getText().toString();
		if (edad.length() < 0) {
			return false;
		} else if (esNumero(edad) == false) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * verifica si es un número
	 * 
	 * @param cadena
	 *            String valor a verificar
	 * @return true si es un número
	 */
	private static boolean esNumero(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
}