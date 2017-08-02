package jhm.ufam.br.epulum.Classes;

/**
 * Created by jonathascavalcante on 01/08/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import android.content.Context.*;
import android.widget.Toast;

import jhm.ufam.br.epulum.Activities.ActivityLogin;

/**
 * Created by jonathascavalcante on 31/07/17.
 */

public class LoginUsuarioTask extends AsyncTask<Void, Void, Boolean> {

    ActivityLogin login = new ActivityLogin();

    Usuario user = new Usuario();
    private String mEmail;
    private final String mPassword;

    public LoginUsuarioTask(String email, String password) {
        mEmail = email;
        mPassword = password;
    }


    @Override
    public Boolean doInBackground(Void... voids) {
        HttpHandler sh = new HttpHandler();
        String url = "https://epulum.000webhostapp.com/epulumDev/getController.php?acao=login&email="+mEmail+"&senha="+mPassword;
        String jsonResponse = sh.makeServiceCall(url);
        Log.d("debug","resposta do json:"  + jsonResponse);
        if(objectJson(jsonResponse)){
            Log.d("debug", "Deveria abrir o activity fake a partir do LoginUsuarioTask");
            return true;
        }else{
            Log.d("debug", "Deveria permanecer na activity de login");
            return false;
        }
    }

    boolean objectJson(String jsonResponse){
        boolean sucesso;
        try {
            JSONObject response = new JSONObject(jsonResponse);
            // Getting JSON Array node
            sucesso = Boolean.valueOf(response.getString("Sucess"));
            //final String MENSAGEM  = response.getString("Mensagem");
            if(sucesso){
                Log.d("Debug:: ", "Entrou no bloco if(sucesso) :" + sucesso);
                JSONObject usuario  = response.getJSONObject("Usuario");
                String id = usuario.getString("Id");
                String email = usuario.getString("Email");
                String nome = usuario.getString("Nome");
                String senha = usuario.getString("Senha");
                String foto = usuario.getString("Foto");
                user = new Usuario(id,email,nome,senha,foto);
                login.mensagemLogin(user);
                Log.d("Debug", "retornei true na função objectJson");
                return true;
            }else{
                Log.d("Debug", "retornei false na função objectJson");
                login.mensagemErro();
                return false;
            }
        } catch (final Exception e) {
            Log.d("Json parsing error: ", e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        ActivityLogin.mAuthTask = null;
        login.showProgress(false);

        if (success) {
            login.finish();
        } else {
            login.inputPassword.requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        ActivityLogin.mAuthTask = null;
        login.showProgress(false);
    }
}
