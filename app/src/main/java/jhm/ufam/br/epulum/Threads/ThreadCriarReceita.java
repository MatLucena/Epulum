package jhm.ufam.br.epulum.Threads;


import android.content.Context;
import android.util.Log;

import jhm.ufam.br.epulum.Activities.ActivityCriarReceita;
import jhm.ufam.br.epulum.Classes.Receita;
import jhm.ufam.br.epulum.Classes.SpeechWrapper;
import jhm.ufam.br.epulum.RVAdapter.RVIngredienteAdapter;
import jhm.ufam.br.epulum.RVAdapter.RVPassosAdapter;

/**
 * Created by Mateus on 11/07/2017.
 */

public class ThreadCriarReceita implements Runnable {

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private final String key_SALVA = "salva";
    private final String key_ADICIONAR = "adicionar";
    private final String key_SIM = "sim";
    private final String key_NAO = "não";
    private final String key_PARA = "para";
    private final String key_VOLTA = "volta";
    private final String key_ESPERA = "espera";
    private final String key_RECEITA = "receita";
    private final String key_INGREDIENTE = "ingrediente";
    private final String key_INGREDIENTES = "ingredients";
    private final String key_PASSO = "passo";
    private final String key_PASSOS = "passos";
    private final String key_PREPARAR = "preparar";
    private final String key_PROXIMO = "próximo";
    private final String key_PROXIMA = "próxima";
    private final String key_PRO = "pro";
    private final String key_PROS = "prós";
    private final String key_POSSO = "posso";
    private final String key_POSSE = "posse";
    private final String key_IRPARA = "ir para";
    private final String key_RECIFE = "para recife";
    private final String key_VOLTAR = "voltar";
    private final String key_RETORNAR = "retornar";
    private final String key_DENOVO = "de novo";
    private final String key_REPETE = "repete";
    private final String key_REPETIR = "repetir";
    private final String key_APAGA = "apaga";
    private final String key_APAGAR = "apagar";
    private final String key_CORRIGE = "corrige";
    private Receita receita;
    private Context context;
    private String result;
    private boolean newResult;
    private boolean para;
    private estados eAgora;
    private SpeechWrapper sw;
    private ActivityCriarReceita arr;
    private RVIngredienteAdapter rv_ingr;
    private RVPassosAdapter rv_pass;
    private int c_ingr;
    private int c_pass;
    private boolean askedResult;
    public ThreadCriarReceita(Receita receita,Context context, SpeechWrapper swr, ActivityCriarReceita arrr, RVIngredienteAdapter ingre, RVPassosAdapter pss) {
        this.receita = receita;
        this.context = context;
        this.sw = swr;
        this.arr = arrr;
        eAgora = estados.INICIO;
        this.c_ingr = -1;
        this.c_pass = -1;
        this.rv_ingr=ingre;
        this.rv_pass=pss;
        para=false;
        askedResult = false;
    }

    @Override
    public void run() {
        while (!para) {
            if (newResult) {
                novoResultado();
            } else if (!askedResult) {
                semResultado();
            }
            sleep(30);
        }
    }

    public void novoResultado() {
        newResult = false;

        if (temPara()) {
            para = true;
            askedResult = false;

        } else if(hasWord(key_SALVA)){
            arr.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arr.salva.performClick();
                }
            });
            askedResult = false;
        } else {
            switch (eAgora) {
                case INICIO:
                    if (!para) {
                        if (irParaIngredientes()) {
                            eAgora = estados.INGREDIENTES;
                            askedResult = false;
                        }
                        else if (irParaPassos()) {
                            eAgora = estados.PASSOS;
                            askedResult = false;
                        }
                    }
                    break;
                case INGREDIENTES:
                    if (!para) {
                        if (adicionarPasso()) {
                            eAgora = estados.PASSOS;
                            askedResult = false;
                        } /*else if(apaga()){
                            if(this.c_ingr<0){
                                receita.removeIngrediente(c_ingr);
                                Log.v("apaguei","apaguei ingrediente "+c_ingr);
                                arr.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rv_ingr.
                                        rv_ingr.notifyDataSetChanged();
                                    }
                                });
                                c_ingr--;

                            }
                        }*/ else {
                            c_ingr++;
                            receita.addIngrediente(result);
                            arr.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rv_ingr.notifyItemInserted(receita.getIngredientes().size() - 1);
                                }
                            });
                            askedResult = false;
                            Speak("próximo");
                        }
                    }
                    break;
                case PASSOS:
                    if (!para) {
                        if (adicionarIngrediente()) {
                            eAgora = estados.INGREDIENTES;
                            askedResult = false;
                        }/*else if(apaga()){
                            if(this.c_pass<0){
                                receita.removeIngrediente(c_pass);
                                Log.v("apaguei","apaguei passo "+c_pass);
                                arr.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rv_pass.notifyDataSetChanged();
                                    }
                                });
                                c_pass--;

                            }
                        } */ else {
                            receita.addPasso(result);
                            c_pass++;
                            arr.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rv_pass.notifyItemInserted(receita.getPassos().size() - 1);
                                }
                            });
                            askedResult = false;
                            Speak("Próximo");
                        }
                    }
                    break;
            }
        }
    }

    public void semResultado() {
        switch (eAgora) {
            case INICIO:
                Speak("adicionar ingrediente ou passo?");
                getSpeech();

                break;
            case INGREDIENTES:
                getSpeech();

                break;
            case PASSOS:
                getSpeech();
                break;
        }
    }

    private boolean temPara() {
        return hasWord(key_PARA) && result.length()==4;
    }

    private boolean adicionarPasso(){
        return hasWord(key_ADICIONAR) && hasWord(key_PASSO) ||
                hasWord(key_ADICIONAR) && hasWord(key_PASSOS);
    }

    private boolean adicionarIngrediente(){
        return hasWord(key_ADICIONAR) && hasWord(key_INGREDIENTE);
    }

    private boolean irParaIngredientes() {
        return hasWord(key_INGREDIENTE) || hasWord(key_INGREDIENTES);
    }

    private boolean irParaPassos() {
        return hasWord(key_PASSO) || hasWord(key_PASSOS);
    }

    private boolean proximo() {
        return hasWord(key_SIM) || hasWord(key_PROXIMO) || hasWord(key_PROXIMA) || hasWord(key_PRO) || hasWord(key_PROS) || hasWord(key_POSSO) || hasWord(key_POSSE);
    }

    private boolean voltar(){
        return hasWord(key_VOLTA) || hasWord(key_VOLTAR) || hasWord(key_RETORNAR);
    }

    private boolean repetir(){
        return hasWord(key_DENOVO) || hasWord(key_REPETE) || hasWord(key_REPETIR);
    }

    private boolean apaga(){
        return hasWord(key_APAGA) && result.length()==5 ||
                hasWord(key_APAGAR) && result.length()==6 ||
                hasWord(key_CORRIGE) && result.length()==7;
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean hasWord(String word) {
        if (result != null)
        return result.contains(word);
        else return false;
    }

    public void Speak(String pal) {
        if(!para) {
            sw.Speak(pal);
            while (sw.isSpeaking() && !para) ;
        }
    }

    public void getSpeech() {
        result=null;
        if(!para) {
            arr.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arr.promptSpeechInput();
                }
            });

            askedResult = true;
        }
    }

    public void sleep(int amount) {
        try {
            Thread.sleep(amount);
        } catch (Exception e) {
        }
    }

    public boolean isNewResult() {
        return newResult;
    }

    public void setNewResult(boolean newResult) {
        this.newResult = newResult;
    }

    public boolean isPara() {
        return para;
    }

    public void setPara(boolean para) {
        this.para = para;
    }

    public boolean isAskedResult() {
        return askedResult;
    }

    public void setAskedResult(boolean askedResult) {
        this.askedResult = askedResult;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private enum estados {
        INICIO,
        I_P,
        P_,
        INGREDIENTES,
        PASSOS,
        PAROU
    }
}
