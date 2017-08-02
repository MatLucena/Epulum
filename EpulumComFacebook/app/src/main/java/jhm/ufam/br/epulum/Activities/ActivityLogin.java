package jhm.ufam.br.epulum.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jhm.ufam.br.epulum.Classes.HttpHandler;
import jhm.ufam.br.epulum.Classes.LoginUsuarioTask;
import jhm.ufam.br.epulum.Classes.Usuario;
import jhm.ufam.br.epulum.R;


public class ActivityLogin extends AppCompatActivity {

    public static final String APP_PREFS = "Epulum_prefs";
    private final String key_EMAIL = "email";
    private final String key_NOME = "nome";
    private final String key_UID = "Id";

    //private UserLoginTask mAuthTask = null; // Acompanhe a tarefa de login para garantir que possamos cancelá-la, se solicitado

    // interface de usuário (UI) references.
    private AutoCompleteTextView inputEmail; // Email de entrada
    public static  EditText inputPassword;          // Senha de entrada

    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView idTextView;

    public static View mProgressView;
    public static View mLoginFormView;
    public static LoginUsuarioTask mAuthTask = null;
    public static Context context;
    public static android.content.res.Resources res;

    String id;
    String email;
    String nome;
    String photoUrl;

    private LoginButton loginButton; // Cria a variável do botão facebook
    CallbackManager callbackManager; // Cria a variável de chamada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo("com.example.jonathascavalcante.mylogin", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        AccessToken.setCurrentAccessToken(null);
        Profile.getCurrentProfile().setCurrentProfile(null);
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        res = getResources();

        loginButton = (LoginButton)findViewById(R.id.loginButton); // pega o id do botão
        loginButton.setReadPermissions("public_profile", "email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Debug", " abrindo activity fake com o botão do facebook... " );
                Intent it = new Intent(ActivityLogin.this,ActivityMain.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                //it.putExtra("Usuario", user);
                startActivity(it);
            }

            @Override
            public void onCancel() {
                Log.d("Debug", " não conseguiu logar com botão do facebook... " );
                Toast.makeText(getApplicationContext(),R.string.com_facebook_loginview_cancel_action,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Debug", " erro acorreu com botão do facebook... " );
                Toast.makeText(getApplicationContext(),R.string.com_facebook_internet_permission_error_message,Toast.LENGTH_SHORT).show();
            }
        });


        // Set up the login form.
        inputEmail = (AutoCompleteTextView) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

//        SharedPreferences settings = getSharedPreferences(APP_PREFS, 0);
//        inputEmail.setText(settings.getString(key_EMAIL,""));
//        if(!inputEmail.getText().equals(null)){
//            final Intent it = new Intent(ActivityLogin.this,ActivityMain.class);
//            startActivity(it);
//        }

        Button btn_login = (Button) findViewById(R.id.bnt_entrar);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button btn_Tela_Cadastro = (Button) findViewById(R.id.bnt_cadastrar);
        btn_Tela_Cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debug", "Deverá abrir a tela de criar usuário");
                final Intent it = new Intent(ActivityLogin.this,ActivityCreateUser.class);
                startActivity(it);
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        inputEmail.setError(null);
        inputPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        boolean inputValid = true;
        View focusView = null;

        // Check for a valid email address.
        if (!isEmailValid(email)) { // verifica se contem @ no conteudo do email
            Log.d("Debug"," Email::não possui '@' no conteudo do email ");
            inputEmail.setError(getString(R.string.error_invalid_email));
            focusView = inputEmail;
            inputValid = false;
        }
        // Check for a valid password, if the user entered one.
        if(!isPasswordValid(password)){
            inputPassword.setError(getString(R.string.error_invalid_password));
            focusView = inputPassword;
            inputValid = false;
        }

        if (inputValid) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Log.d("Debug"," cancel::true");
            showProgress(true);
            mAuthTask = new LoginUsuarioTask(email, password);
            mAuthTask.execute((Void) null);
        } else {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            Log.d("Debug"," cancel::false");
            focusView.requestFocus();
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = res.getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
    public void mensagemErroLogin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, "Tentando logar... ", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
    public void mensagemErro() {
    }
    public void mensagemLogin(final Usuario user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, "Seja bem vindo(a) " + user.getEmail(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        Usuario user = new Usuario();
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            /*
            Can't create handler inside thread that has not called Looper.prepare()
            TODO: attempt authentication against a network service.
            */
            HttpHandler sh = new HttpHandler();
            String url = "https://epulum.000webhostapp.com/epulumDev/getController.php?acao=login&email="+mEmail+"&senha="+mPassword;
            String jsonResponse = sh.makeServiceCall(url);
            Log.d("debug", jsonResponse);
            if(objectJson(jsonResponse)){
                Log.d("debug", "Deveria abrir o actiity fake");
                final Intent it = new Intent(ActivityLogin.this,ActivityMain.class);
                it.putExtra("Usuario", user);
                SharedPreferences settings = getSharedPreferences(APP_PREFS, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(key_EMAIL,user.getEmail());
                editor.putString(key_UID,user.getId());
                editor.putString(key_NOME,user.getNome());
                editor.commit();
                startActivity(it);
            }else{
                Intent novaView = new Intent(ActivityLogin.this,ActivityLogin.class);
                startActivity(novaView);
            }

            // TODO: register the new account here.
            return true;
        }
        boolean objectJson(String jsonResponse){
            boolean sucesso;
            try {
                JSONObject response = new JSONObject(jsonResponse);
                // Getting JSON Array node
                sucesso = Boolean.valueOf(response.getString("Sucess"));
                final String MENSAGEM  = response.getString("Mensagem");
                if(sucesso){
                    Log.d("Debug:: ", "deveria ter entrado no if de sucesso");
                    JSONObject usuario  = response.getJSONObject("Usuario");
                    String id = usuario.getString("Id");
                    String email = usuario.getString("Email");
                    String nome = usuario.getString("Nome");
                    String senha = usuario.getString("Senha");
                    String foto = usuario.getString("Foto");
                    user = new Usuario(id,email,nome,senha,foto);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), MENSAGEM, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    Log.d("Debug", "retornei true");
                    return true;
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), MENSAGEM, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    Log.d("Debug", "retornei false 1");
                    return false;
                }
            } catch (final Exception e) {
                Log.d("Json parsing error: ", e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
                Log.d("Debug", "retornei false 2");
                return false;
            }
        }

        public void mensagemLogin(final Usuario user ){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(context, "Seja bem vindo(a) " + user.getEmail(), Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                inputPassword.setError(getString(R.string.error_incorrect_password));
                inputPassword.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

