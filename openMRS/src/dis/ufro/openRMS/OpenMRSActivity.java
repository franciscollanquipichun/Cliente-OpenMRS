package dis.ufro.openRMS;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import dis.ufro.openmrsrest.ApiAuthRest;
/**
 * 
 * @author Francisco Llanquipichun
 *
 */
public class OpenMRSActivity extends Activity {

	private EditText et_usr;
	private EditText et_pass;
	private Button btn_login;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		et_usr = (EditText) findViewById(R.id.et_login_usr);
		et_pass = (EditText) findViewById(R.id.et_login_pass);
		btn_login = (Button) findViewById(R.id.btn_login);
		
		/*
		 * Variables por defecto
		 */
		et_usr.setText("admin");
		et_pass.setText("Admin123");
		
		/*
		 * Accion del boton btn_login
		 */
		btn_login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (validarCampos() == true) {

					ApiAuthRest.setURLBase("http://192.168.1.2:8080/openmrs/ws/rest/v1/");
					ApiAuthRest.setUsername(et_usr.getText().toString());
					ApiAuthRest.setPassword(et_pass.getText().toString());
					login();
				}
			}
		});
	}

	/**
	 * Inicio de sesión
	 */
	public void login() {
		try {
			Object objSessionJson = JSONValue.parse(ApiAuthRest
					.getRequestGet("session"));
			JSONObject jsonObjectSessionJson = (JSONObject) objSessionJson;
			String sessionId = (String) jsonObjectSessionJson.get("sessionId");
			Boolean authenticated = (Boolean) jsonObjectSessionJson
					.get("authenticated");
			if (authenticated == true) {
				Toast.makeText(getApplicationContext(),
						"Ha iniciado sesión correctamente"
								+ authenticated, Toast.LENGTH_LONG).show();
				Intent i = new Intent(OpenMRSActivity.this, Principal.class);
				startActivity(i);
			}else{
				Toast.makeText(getApplicationContext(),
						"Inicio de sesión fallido"
								+ authenticated, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Valida los campos et_usr y et_pass
	 * 
	 * @return true si los campos contienen texto
	 */
	private boolean validarCampos() {
		int largoUsr = et_usr.getText().toString().length();
		int largoPass = et_pass.getText().toString().length();
		if (largoUsr < 1 || largoPass < 1) {
			Toast.makeText(getApplicationContext(),
					"Por favor complete ambos campos", Toast.LENGTH_LONG)
					.show();
			return false;
		} else if (largoUsr > 255 || largoPass > 255) {
			Toast.makeText(getApplicationContext(),
					"Error: a sobrepasado el máximo de caracteres",
					Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}
	}
}