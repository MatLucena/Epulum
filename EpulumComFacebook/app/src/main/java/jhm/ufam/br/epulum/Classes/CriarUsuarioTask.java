package jhm.ufam.br.epulum.Classes;

/**
 * Created by jonathascavalcante on 01/08/17.
 */

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jhm.ufam.br.epulum.Activities.ActivityCreateUser;
import jhm.ufam.br.epulum.Activities.ActivityLogin;

public class CriarUsuarioTask extends AsyncTask<Void, Void, Boolean>{
    /**
     * Created by jonathascavalcante on 31/07/17.
     */
    ActivityCreateUser msg = new ActivityCreateUser();

    ActivityLogin login = new ActivityLogin();

    private EditText inputPassword;
    private Usuario user = new Usuario();
    private String mEmail;
    private String mnome;
    private String mPassword;
    static boolean retorno = false;

    private View mProgressView;
    private View mLoginFormView;

    public CriarUsuarioTask(String email, String nome, String password) {
        this.mEmail = email;
        this.mnome = nome;
        this.mPassword = password;
    }

    @SuppressLint("LongLogTag")
    private boolean objectJson(String jsonResponse){
        boolean sucesso;

        try {
            JSONObject response = new JSONObject(jsonResponse);
            // Getting JSON Array node
            sucesso = response.getString("Sucess").equals("1");

            final String MENSAGEM  = response.getString("Mensagem");
            Log.d("Debug","sucesso::" + sucesso);
            if(sucesso){
                Log.d("Debug:: ", "deveria ter entrado no if de sucesso");
                JSONObject usuario  = response.getJSONObject("Usuario");
                String id = usuario.getString("Id");
                String email = usuario.getString("Email");
                String nome = usuario.getString("Nome");
                String senha = usuario.getString("Senha");
                String foto = usuario.getString("Foto");
                user = new Usuario(id,email,nome,senha,foto);
                login.mensagemLogin(user);
                Log.d("Debug", "retornei true na classe criar Usuario Task" + MENSAGEM);
                retorno = true;
                return true;
            }else{
                Log.d("Debug", "retornei false 1" + MENSAGEM);
                login.mensagemErroLogin();

                return false;
            }
        } catch (final Exception e) {
            Log.d("Debug::Json parsing error: ", e.getMessage());
            login.mensagemErro();
            msg.mensagemErroCreateUser();
            return false;
        }
    }
    public static final String MD5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        HttpHandler sh = new HttpHandler();
        String url = "https://epulum.000webhostapp.com/epulumDev/getController.php?acao=createUsuario&email="+mEmail+"&nome="+mnome+"&senha="+MD5(mPassword);
        Log.d("Debug", url);
        String jsonResponse = sh.makeServiceCall(url);
        Log.d("debug "," go: " + jsonResponse);
        if(objectJson(jsonResponse)){
            Log.d("debug", "Deveria abrir o activity login a patir do criarUsuarioTask");
            //login.logIn(LoginActivity.context); // nao pode ter login pq a activity ja é chamada na classe principal
            return true;

        }else{
            //login.logOut(LoginActivity.context);
            Log.d("debug", "Deveria permanecer na actiity de criar usuario");
            return false;
        }
        // TODO: register the new account here.
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
