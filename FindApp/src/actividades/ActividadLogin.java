package actividades;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.findapp.R;

import controladores.CtrlUsuario;
import entidades.Usuario;

public class ActividadLogin extends Activity {
	
	private EditText editText_usuario;
	private String str_usuario;
	private EditText editText_pass;
	private String str_password;
	private Button btnLogin;
	private SharedPreferences preferencia;
	private Context contexto;
	private CtrlUsuario cUsuario;
	private Usuario usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actividad_login);
		editText_usuario = (EditText)findViewById(R.id.usuario);
		
		editText_pass = (EditText)findViewById(R.id.password);
		
		preferencia = getSharedPreferences("datos_usuario", this.MODE_PRIVATE);
		contexto = this;
		cUsuario = new CtrlUsuario(contexto);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actividad_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void validaUsuario(View view) {
		str_usuario = editText_usuario.getText().toString();
		str_password = editText_pass.getText().toString();
		if(validaFormularioLogin()){
			LoginTask tareaLogin = new LoginTask(str_usuario, str_password, contexto);
			tareaLogin.execute();
		}else{
			//Log.d("login", "nok");
		}
	}
	
	private boolean validaFormularioLogin(){		
		if(str_usuario.length() == 0){
			Toast.makeText(this, "Ingrese su usuario", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(str_password.length() == 0){
			Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private class LoginTask extends AsyncTask<Integer, Void, Integer> {
		String usuario_str;
		String password_str;
		Context contexto;
		private ProgressDialog ringProgressDialog;
		
		public LoginTask(String usuario, String password, Context contexto){
			this.usuario_str = usuario;
			this.password_str = password;
			this.contexto = contexto;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ringProgressDialog = ProgressDialog.show(ActividadLogin.this, "Inicio Sesión", "Autenticando usuario", true);
			ringProgressDialog.setCancelable(false);
			
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {
			try {
				usuario = cUsuario.Login(usuario_str, password_str);
				if(usuario != null){
					return 1;
				}else{
					return 0;
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				return -1;
			}
			
			
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			ringProgressDialog.dismiss();
			switch (result) {
			case -1: Toast.makeText(contexto, "Error en la comunicación con el servidor", Toast.LENGTH_SHORT).show();
				break;
			case 0: Toast.makeText(contexto, "Usuario no registrado o ", Toast.LENGTH_SHORT).show();
				break;
			case 1: Toast.makeText(contexto, "Usuario encontrado", Toast.LENGTH_SHORT).show();
				break;

			
			}
			
		}
		
		
		
		
	}
}
