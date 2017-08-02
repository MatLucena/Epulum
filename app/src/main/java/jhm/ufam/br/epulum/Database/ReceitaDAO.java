package jhm.ufam.br.epulum.Database;

/**
 * Created by hendrio on 14/07/17.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jhm.ufam.br.epulum.Classes.Categoria;
import jhm.ufam.br.epulum.Classes.Receita;
import jhm.ufam.br.epulum.R;


public class ReceitaDAO {
    private SQLiteDatabase bancoDeDados;
    private Context context;

    public  ReceitaDAO(Context context){ // se conecta com o banco // Ex: connect();
        this.bancoDeDados = (new MyDBHandler(context)).getWritableDatabase();
        this.context = context;
        initializeDatabase();
    }
/*
*
* Função getReceita
*
* Usando como parâmetro o nome da receita, busca nomes semelhantes no banco de dados e retorna o primeiro resultado encontrado
* Retorna a classe Receita
*
* */
    public Receita getReceita(String nome){
        if(!isOpen()){
            open();
        };
        Receita receita = null;
        String sqlQuery = "SELECT * FROM " + MyDBHandler.TABLE_RECEITAS + " WHERE " +
                MyDBHandler.COLUNA_nome + " LIKE '" + nome + "%'";
        Cursor cursor = this.bancoDeDados.rawQuery(sqlQuery,null);
        if(cursor.moveToNext()){
            receita = new Receita();
            receita.set_idcategoria(cursor.getInt(1));
            receita.set_idusuario(cursor.getInt(2));
            receita.setNome(cursor.getString(3));
            receita.setTempopreparo(cursor.getString(4));
            receita.setDescricao(cursor.getString(5));
            receita.setFoto(cursor.getString(6));
            receita.setFotoLocal(cursor.getString(7));
            receita.setAllIngredientes(cursor.getString(8));
            receita.setAllPassos(cursor.getString(9));
            receita.setPhotoId(cursor.getInt(10));
        }
        cursor.close();
        return receita;
    }
/*
*
*
* Função getAllReceitas
*
* Utiliza uma query no estilo SELECT * FROM TABELA para, do resultado da query, retornar um
* ArrayList com todas as receitas encontradas.
*
* */
    public ArrayList<Receita> getAllReceitas(){
        ArrayList<Receita> receitas = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + MyDBHandler.TABLE_RECEITAS;
        Cursor cursor = bancoDeDados.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                Receita receita = new Receita();
                receita.set_idcategoria(cursor.getInt(1));
                receita.set_idusuario(cursor.getInt(2));
                receita.setNome(cursor.getString(3));
                receita.setTempopreparo(cursor.getString(4));
                receita.setDescricao(cursor.getString(5));
                receita.setFoto(cursor.getString(6));
                receita.setFotoLocal(cursor.getString(7));
                receita.setAllIngredientes(cursor.getString(8));
                receita.setAllPassos(cursor.getString(9));
                receita.setPhotoId(cursor.getInt(10));
                receitas.add(receita);
            } while (cursor.moveToNext());
        }
        return receitas;
    }
/*
*
* Função getReceitasByUsuario
*
* Parecida com getAllReceitas e getReceita, porém pega todos as receitas de um único usuário, utilizando seu id
*
* */
    public ArrayList<Receita> getReceitasByUsuario(long idUsuario){
        ArrayList<Receita> receitas = new ArrayList<>();
        String sqlQuery = "SELECT * FROM " + MyDBHandler.TABLE_RECEITAS + " WHERE " +
                MyDBHandler.COLUNA_idusuario + " LIKE '" + idUsuario + "%'";
        Cursor cursor = bancoDeDados.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()){
            do{
                Receita receita = new Receita();
                receita.set_idcategoria(cursor.getInt(1));
                receita.set_idusuario(cursor.getInt(2));
                receita.setNome(cursor.getString(3));
                receita.setTempopreparo(cursor.getString(4));
                receita.setDescricao(cursor.getString(5));
                receita.setFoto(cursor.getString(6));
                receita.setFotoLocal(cursor.getString(7));
                receita.setAllIngredientes(cursor.getString(8));
                receita.setAllPassos(cursor.getString(9));
                receita.setPhotoId(cursor.getInt(10));
                receitas.add(receita);
            } while (cursor.moveToNext());
        }
        return receitas;
    }
    /*
    *
    * Função getReceitasByIdCategoria
    *
    * Parecida com getAllReceitas e getReceita, porém pega todos as receitas de uma categoria, utilizando seu id
    *
    * */
    public ArrayList<Receita> getReceitasByIdCategoria(long idCategoria){
        ArrayList<Receita> receitas = new ArrayList<Receita>();
        String sqlQuery = "SELECT * FROM " + MyDBHandler.TABLE_RECEITAS + " WHERE " +
                MyDBHandler.COLUNA_idcategoria + " LIKE '" + idCategoria + "%'";
        Cursor cursor = bancoDeDados.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()){
            do{
                Receita receita = new Receita();
                receita.set_idcategoria(cursor.getInt(1));
                receita.set_idusuario(cursor.getInt(2));
                receita.setNome(cursor.getString(3));
                receita.setTempopreparo(cursor.getString(4));
                receita.setDescricao(cursor.getString(5));
                receita.setFoto(cursor.getString(6));
                receita.setFotoLocal(cursor.getString(7));
                receita.setAllIngredientes(cursor.getString(8));
                receita.setAllPassos(cursor.getString(9));
                receita.setPhotoId(cursor.getInt(10));
                receitas.add(receita);
            } while (cursor.moveToNext());
        }
        return receitas;
    }
/*
*
* Função getReceitasByNomeCategoria
*
* Parecida com getAllReceitas e getReceita, porém pega todos as receitas de uma categoria, utilizando seu nome
*
* */
    public ArrayList<Receita> getReceitasByNomeCategoria(String nomeCategoria){
        ArrayList<Receita> receitas = new ArrayList<Receita>();
        CategoriaDAO categoriaDAO = new CategoriaDAO(context);
        Categoria c = categoriaDAO.getCategoria(nomeCategoria);
        String sqlQuery = "SELECT * FROM " + MyDBHandler.TABLE_RECEITAS + " WHERE " +
                MyDBHandler.COLUNA_idcategoria + " LIKE '" + c.getTipo() + "%'";
        Cursor cursor = bancoDeDados.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()){
            do{
                Receita receita = new Receita();
                receita.set_idcategoria(cursor.getInt(1));
                receita.set_idusuario(cursor.getInt(2));
                receita.setNome(cursor.getString(3));
                receita.setTempopreparo(cursor.getString(4));
                receita.setDescricao(cursor.getString(5));
                receita.setFoto(cursor.getString(6));
                receita.setFotoLocal(cursor.getString(7));
                receita.setAllIngredientes(cursor.getString(8));
                receita.setAllPassos(cursor.getString(9));
                receita.setPhotoId(cursor.getInt(10));
                receitas.add(receita);
            } while (cursor.moveToNext());
        }
        return receitas;
    }
/*
*
*
* Função addReceita
*
* Utilizando um parâmetro Receita, adiciona essa instância no banco de dados e retorna um valor verdadeiro
* caso tenha adicionado com sucesso, e falso caso contrário.
*
* */
    public boolean addReceita(Receita r){
        if(existsReceita(r.getNome()))
            return false;
        try{
            Log.v("Valor Ingredientes", r.getIngredientesString());
            String sqlCmd = "INSERT INTO " + MyDBHandler.TABLE_RECEITAS + " ( " +
                    MyDBHandler.COLUNA_idcategoria + ", " +
                    MyDBHandler.COLUNA_idusuario + ", " +
                    MyDBHandler.COLUNA_nome + ", " +
                    MyDBHandler.COLUNA_tempopreparo + ", " +
                    MyDBHandler.COLUNA_descricao + ", " +
                    MyDBHandler.COLUNA_foto + ", " +
                    MyDBHandler.COLUNA_fotolocal + ", " +
                    MyDBHandler.COLUNA_ingredientes + ", " +
                    MyDBHandler.COLUNA_passos + ", " +
                    MyDBHandler.COLUNA_photoid + " ) " +
                    " VALUES ('" +
                    r.get_idcategoria() + "', '"+
                    r.get_idusuario() + "', '"+
                    r.getNome() + "', '"+
                    r.getTempopreparo() + "', '"+
                    r.getDescricao() + "', '" +
                    r.getFoto() + "', '" +
                    r.getFotoLocal() + "', '" +
                    r.getIngredientesString() + "', '" +
                    r.getPassosString() + "', '" +
                    r.getPhotoId() + "' );";
            this.bancoDeDados.execSQL(sqlCmd);
            return true;
        }catch (Exception e){
            Log.e("Erro ao tentar add", e.getMessage());
            return false;
        }
    }
    /*
    *
    *
    * Função removeReceita
    *
    * Utilizando Receita como parâmetro, remove a receita em questão e retorna verdadeiro em caso positivo,
    * falso caso contrário.
    *
    * */
    public boolean removeReceita(String nome){
        if(!existsReceita(nome))
            return false;
        try{
            String sqlCmd = "DELETE FROM " + MyDBHandler.TABLE_RECEITAS + " WHERE " + MyDBHandler.COLUNA_nome + "='" + nome + "';";
            this.bancoDeDados.execSQL(sqlCmd);
            return true;
        }catch (Exception e){
            Log.e("Erro ao tentar remover", e.getMessage());
            return false;
        }
    }
/*
*
*
* Função updateReceita
*
* Utilizando o nome de uma receita e os valores a alterar em Receita, atualiza-se uma instância de Receita
* no banco de dados, retornando verdadeiro caso haja sucesso e falso caso contrário.
*
* */
    public boolean updateReceita(Receita r, String nome){
        try{
            String sqlCmd = "UPDATE " + MyDBHandler.TABLE_RECEITAS + " SET " +
                    MyDBHandler.COLUNA_nome + " ='" + r.getNome() + "', " +
                    MyDBHandler.COLUNA_descricao+ " ='" + r.getDescricao() + "', " +
                    MyDBHandler.COLUNA_foto+ " ='" + r.getFoto() + "', " +
                    MyDBHandler.COLUNA_fotolocal+" ='" +r.getFotoLocal() + "', " +
                    MyDBHandler.COLUNA_ingredientes + " ='" + r.getIngredientesString() + "', " +
                    MyDBHandler.COLUNA_passos + " ='" + r.getPassosString() + "' " +
                    "WHERE " + MyDBHandler.COLUNA_nome + " = '" + nome + "' ;";
            this.bancoDeDados.execSQL(sqlCmd);
            return true;
        }catch (Exception e){
            Log.e("Erro ao tentar editar", e.getMessage());
            return false;
        }
    }
/*
*
*
* Função initializeDatabase
*
* Esta função insere vários valores pré-definidos no nosso recém-criado banco de dados, incluindo uma receita de torta,
* joelho de porco, entre outras
*
* */
    public void initializeDatabase() {
        Receita torta = new Receita("Torta de Maçã", "Uma Torta de Maçã muito gostosa e simples.", R.drawable.torta_de_maca);
        torta.addIngrediente("100 gramas de manteiga");
        torta.addIngrediente("2 gemas");
        torta.addIngrediente("4 colheres de açúcar refinado");
        torta.addIngrediente("200 gramas de farinha de trigo");
        torta.addIngrediente("500 ml de leite");
        torta.addIngrediente("1 lata de leite condensado");
        torta.addIngrediente("2 colheres de sopa de amido de milho");
        torta.addIngrediente("3 maçãs");
        torta.addPasso("misture a manteiga as gemas e o açúcar");
        torta.addPasso("Junte a farinha aos poucos até formar uma massa que não grude nas mãos.");
        torta.addPasso("Forre com a massa uma forma de torta redonda untada levemente com manteiga e fure toda a superfície com um garfo e leve ao forno pré-aquecido em temperatura média ou baixa para a massa dourar aproximadamente 15 minutos");
        torta.addPasso(" coloque a água e o açúcar numa panela e leve ao fogo");
        torta.addPasso("Ao ferver junte as fatias de maçãs para cozinhar levemente sem deixar desmanchar apenas uns 2 minutos");
        torta.addPasso("Retire as maçãs com uma escumadeira e acrescente a gelatina à água que sobrou na panela mexendo bem");
        torta.addPasso("Deixe esfriar e leve a geladeira por 10 minutos");
        torta.set_idusuario(1);
        this.addReceita(torta);

        Receita joelho = new Receita("Joelho de Porco", "Joelho de porco com a casca tostada e crocante.", R.drawable.joelho_de_porco);
        joelho.addIngrediente("500 gramas de joelho de porco");
        joelho.addIngrediente("1 cebola picada");
        joelho.addIngrediente("3 dentes de alho picado");
        joelho.addIngrediente("2 colheres de sopa de mistura de ervas aromáticas secas (pode ser: tomilho; manjerona; alecrim)");
        joelho.addIngrediente("meia xícara de suco de limão");
        joelho.addIngrediente("2 folhas de louro");
        joelho.addIngrediente("1 pitada de sal");
        joelho.addIngrediente("1 pitada de pimenta do reino");
        joelho.addPasso("Deixe o joelho de porco marinando num refratário com todos os ingredientes por 12 horas");
        joelho.addPasso("Dê início colocando o joelho de porco e todos os ingredientes da marinada a cozinhar durante 30 a 40 minutos numa panela de pressão até soltar a carne do osso");
        joelho.addPasso("Você pode conferir um aspeto dourado e mais apetitoso se o levar a assar no forno a 200 graus durante cerca de 30 minutos.");
        joelho.addPasso("Sirva o joelho de porco com chucrute e batatas cozidas");
        joelho.addPasso("Bom apetite!");
        this.addReceita(joelho);

        Receita ovo = new Receita("Ovo cozido","Aprenda a cozinhar um ovo",R.drawable.ovo_cozido);
        ovo.addIngrediente("Um ovo");
        ovo.addIngrediente("300 ml de água");
        ovo.addPasso("Ferva a água");
        ovo.addPasso("Cozinhe o ovo por 7 minutos");
        ovo.addPasso("Retire o ovo e descasque-o");
        this.addReceita(ovo);

        Receita hamburguer = new Receita("Hambúrguer Vegano", "Hambúrguer sem carne para quem quer uma refeição saudável.", R.drawable.hamburguer_vegano);
        hamburguer.addIngrediente("300 gramas de lentilha");
        hamburguer.addIngrediente("meia cebola picada");
        hamburguer.addIngrediente("Uma colher de sobremesa de mostarda");
        hamburguer.addIngrediente("200 gramas de farinha de rosca");
        hamburguer.addIngrediente("meio maço de salsinha");
        hamburguer.addIngrediente("uma colher de sopa de cominho");
        hamburguer.addIngrediente("uma colher de sopra de gengibre");
        hamburguer.addIngrediente("50 ml de molho shoyo");
        hamburguer.addPasso("Cozinhe as lentilhas na panela de pressão por 15 a 20 minutos");
        hamburguer.addPasso("Deixe esfriar a lentilha e; depois; coloque em uma vasilha. Misture a cebola e a salsinha picadas ");
        hamburguer.addPasso("Tempere a mistura de lentilha com sal; molho shoyo; mostarda e cominho");
        hamburguer.addPasso("Rale o gengibre e esprema o sumo na mistura de lentilha");
        hamburguer.addPasso("Vá acrescentando; aos poucos; a farinha de rosca");
        hamburguer.addPasso("Misture até dar liga");
        hamburguer.addPasso("vá fazendo discos em formato de hambúrguer molhando sempre as mãos com água para não grudar");
        hamburguer.addPasso("doure cada lado dos discos em uma frigideira untada com óleo");
        this.addReceita(hamburguer);

        Receita bolinho = new Receita("Bolinho De Carne Moída", "Uma simples receita, fácil de fazer e deliciosa.", R.drawable.bolinho_de_carne_moida);
        bolinho.addIngrediente("meio quilo de carne moída");
        bolinho.addIngrediente("3 colheres de sopa de farinha de trigo");
        bolinho.addIngrediente("uma cebola");
        bolinho.addIngrediente("um alho");
        bolinho.addIngrediente("um ovo");
        bolinho.addIngrediente("temperos a gosto");
        bolinho.addPasso("Tempere a carne moída com alho; cebola e temperos a gosto");
        bolinho.addPasso("Adicione o ovo e a farinha de trigo");
        bolinho.addPasso("Amasse bem até que toda farinha não apareça mais");
        bolinho.addPasso("Faça bolinhas e deixe fritar bem, para que não fique cru por dentro");
        this.addReceita(bolinho);

        Receita file = new Receita("Filé à Parmegiana", "Uma receita de alta qualidade, ideal para momentos especiais.", R.drawable.file_parmegiana);
        file.addIngrediente("500 gramas de alcatra ou contra filé em bifes");
        file.addIngrediente("2 dentes de alho amassados");
        file.addIngrediente("orégano, sal e vinagre a gosto");
        file.addIngrediente("2 ovos batidos com uma pitada de sal");
        file.addIngrediente("farinha de rosca, o suficiente");
        file.addIngrediente("meio litro de molho de tomate pronto");
        file.addIngrediente("250 gramas de mussarela em fatias");
        file.addIngrediente("queijo parmesão ralado para polvilhar");
        file.addPasso("Tempere os filés com o alho; orégano; sal e vinagre");
        file.addPasso("Passe pela farinha de rosca; nos ovos batidos e novamente pela farinha de rosca");
        file.addPasso("Frite em óleo quente");
        file.addPasso("Escorra sobre papel absorvente");
        file.addPasso("Acomode os filés em um refratário regado com um pouco de molho");
        file.addPasso("Coloque fatias de mussarela sobre os filés");
        file.addPasso("Regue com o molho");
        file.addPasso("Polvilhe o queijo parmesão ralado");
        file.addPasso("Leve ao forno pré-aquecido para derreter a mussarela");
        file.addPasso("Sirva com arroz ou purê e uma salada");
        this.addReceita(file);

        Receita costela = new Receita("Costela na pressão com Linguiça", "Uma bela receita de costela para comer com a família", R.drawable.costela_na_pressao);
        costela.addIngrediente("um quilo e meio de costela");
        costela.addIngrediente("800 gramas de linguiça toscana");
        costela.addIngrediente("uma cebola grande cortada em rodelas");
        costela.addPasso("Coloque toda a cebola cebola no fundo da panela de pressão");
        costela.addPasso("Corte a costela em pedaços e coloque metade sobre a cebola (a parte que tem mais osso para baixo)");
        costela.addPasso("Coloque metade da lingüiça por cima e faça alguns furos nos gomos");
        costela.addPasso("Repita a operação e leve no fogo médio alto por mais ou menos 40 a 45 minutos contados a partir do momento que a panela começar a chiar");
        costela.addPasso("Não adicione água; nem óleo");
        costela.addPasso("Tire a pressão e saboreie uma deliciosa costela temperada apenas com a lingüiça");
        costela.addPasso("Deliciosa! Bom apetite!");
        this.addReceita(costela);

        Receita camarao = new Receita("Camarão com creme de leite", "Uma deliciosa receita de camarão com creme de leite.", R.drawable.camarao_com_creme_de_leite);
        camarao.addIngrediente("1 quilo de camarão");
        camarao.addIngrediente("1 cebola média picada");
        camarao.addIngrediente("2 colheres de sopa de azeite");
        camarao.addIngrediente("Sal a gosto");
        camarao.addIngrediente("Pimenta a gosto");
        camarao.addIngrediente("1 colher de sobremesa de orégano");
        camarao.addIngrediente("uma lata de creme de leite sem soro");
        camarao.addIngrediente("meia xícara de salsinha picada");
        camarao.addPasso("Em uma panela coloque a cebola e o azeite e leve ao fogo médio para dourar");
        camarao.addPasso("Coloque os camarões com o sal e refogue um pouco");
        camarao.addPasso("Deixe cozinhar por 10 minutos");
        camarao.addPasso("Coloque a pimenta e o orégano");
        camarao.addPasso("Deixe cozinhar mais uns 5 minutos");
        camarao.addPasso("Quando estiver pronto; acrescente a salsinha e o creme de leite");
        this.addReceita(camarao);

        Receita sopa = new Receita("Sopa de abóbora", "", R.drawable.sopa_de_abobora);
        sopa.addIngrediente("meia abóbora moranga pequena (descascada e cortada em cubos grandes)");
        sopa.addIngrediente("uma batata média (descascada e cortada)");
        sopa.addIngrediente("meia cebola picada");
        sopa.addIngrediente("1 dente alho pequeno amassado");
        sopa.addIngrediente("1 litro de água ou mais");
        sopa.addIngrediente("1 cubo de caldo de legumes");
        sopa.addIngrediente("Cebolinha verde picada");
        sopa.addIngrediente("Sal somente se necessário");
        sopa.addPasso("Em uma panela grande; doure o alho em um fio de óleo e logo acrescente a cebola picada");
        sopa.addPasso("Mexa rápido e acrescente a abóbora picada e a batata");
        sopa.addPasso("Coloque a água e deixe ferver; após fervura colocar o caldo de legumes e mexer");
        sopa.addPasso("Deixe cozinhar com tampa até a abóbora e a batata estarem cozidas");
        sopa.addPasso("Desligue o fogo");
        sopa.addPasso("Espere amornar um pouco");
        sopa.addPasso("Depois despeje tudo no liquidificador e bata até virar creme");
        sopa.addPasso("Despeje na panela novamente e cozinhe uns 3 minutos com a cebolinha; acrescente sal se necessário");
        sopa.addPasso("Sirva quente; com cubinhos de queijo branco e torradas");
        this.addReceita(sopa);

        Receita cappuccino = new Receita("Cappuccino Caseiro","Uma bebida deliciosa para o café da manhã",R.drawable.cappuccino);
        cappuccino.addIngrediente("50 gramas de café solúvel");
        cappuccino.addIngrediente("250 gramas de leite em pó (integral ou desnatado)");
        cappuccino.addIngrediente("3 colheres (sopa) de chocolate em pó (não pode ser achocolatado)");
        cappuccino.addIngrediente("1 colher (chá) de bicarbonato de sódio");
        cappuccino.addIngrediente("1 colher (chá) de canela em pó");
        cappuccino.addIngrediente("250 gramas de açúcar (ou equivalente em adoçante)");
        cappuccino.addPasso("Bater no liquidificador o café até ficar fino");
        cappuccino.addPasso("Acrescentar aos outros ingredientes em uma tigela e peneirar tudo junto em outra");
        cappuccino.addPasso("Use 2 colheres de sobremesa para 1 xícara de água fervente");
        cappuccino.addPasso("A validade é de 6 meses");

        Receita lasanha = new Receita("Lasanha à Bolonhesa","Uma refeição ideal para a família inteira", R.drawable.lasanha);
        lasanha.addIngrediente("3 colheres de margarina");
        lasanha.addIngrediente("4 colheres de farinha de trigo");
        lasanha.addIngrediente("duas xícaras de leite");
        lasanha.addIngrediente("duas xícaras de creme de leite");
        lasanha.addIngrediente("sal e noz-moscada a gosto");
        lasanha.addIngrediente("1 colher de óleo");
        lasanha.addIngrediente("2 dentes de alho amassados");
        lasanha.addIngrediente("uma cebola ralada");
        lasanha.addIngrediente("300 gramas de carne moída");
        lasanha.addIngrediente("3 xícaras de polpa de tomate batida no liquidificador");
        lasanha.addIngrediente("três quartos de uma xícara de água quente");
        lasanha.addIngrediente("sal a gosto");
        lasanha.addIngrediente("200 gramas de presunto fatiada");
        lasanha.addIngrediente("200 gramas de mussarela fatiada");
        lasanha.addIngrediente("250 gramas de massa para lasanha");
        lasanha.addPasso("Primeiro para fazer o molho branco:");
        lasanha.addPasso("Derreta a margarina e doure a farinha em fogo baixo mexendo sempre; junte o leite aos poucos");
        lasanha.addPasso("Cozinhe até obter um molho encorpado; acrescente o creme de leite e tempere com sal e noz-moscada");
        lasanha.addPasso("Para fazer o molho à bolonhesa:");
        lasanha.addPasso("Aqueça o óleo junte o alho e a cebola até dourar");
        lasanha.addPasso("Acrescente a carne moída até fritar; quando a carne estiver frita acrescente a polpa de tomate e a água misture o sal e cozinhe até ferver");
        lasanha.addPasso("Montagem da lasanha:");
        lasanha.addPasso("Em um refratário grande; coloque uma camada de molho à bolonhesa; massa para lasanha; presunto; mussarela; molho branco");
        lasanha.addPasso("Adicione mais massa para lasanha presunto e mussarela e termine com molho à bolonhesa");
        lasanha.addPasso("Se quiser; polvilhe um pouco de queijo parmesão ralado e leve ao forno para gratinar por 20 minutos");
        this.addReceita(lasanha);

        Receita docedeleite = new Receita("Doce de Leite Caseiro","Fácil e simples de fazer, um doce caseiro que qualquer um pode fazer!",R.drawable.doce_de_leite);
        docedeleite.addIngrediente("2 litros de leite");
        docedeleite.addIngrediente("4 xícaras de açúcar");
        docedeleite.addPasso("Coloque o leite e o açúcar em uma panela grande de fundo largo");
        docedeleite.addPasso("Leve ao fogo médio; mexendo sempre com uma colher de pau; até obter fervura (cerca de 15 minutos)");
        docedeleite.addPasso("Diminua o fogo e continue mexendo até obter um doce marrom claro de consistência cremosa (cerca de 45 minutos)");
        docedeleite.addPasso("Passe o doce para um refratário; deixe esfriar bem e sirva colheradas em pratos de sobremesa com fatias de queijo branco");
        this.addReceita(docedeleite);

        Receita pudim = new Receita("Pudim de leite","Pudim de leite simples e fácil de fazer",R.drawable.pudim);
        pudim.addIngrediente("uma lata ou caixa de leite condensado");
        pudim.addIngrediente("uma lata ou caixa de creme de leite");
        pudim.addIngrediente("uma lata de leite (medida da lata de leite condensado ou creme de leite)");
        pudim.addIngrediente("2 ovos inteiros");
        pudim.addIngrediente("uma xícara e meia de açúcar");
        pudim.addPasso("Para a calda: coloque o açúcar numa panela pequena no fogo");
        pudim.addPasso("mexa até derreter e ficar bem douradinho");
        pudim.addPasso("coloque na forma de pudim e deixe esfriar e reserve");
        pudim.addPasso("Bate o leite condensado; o creme de leite; o leite os ovos no liquidificador por 5 minutos");
        pudim.addPasso("depois despeje na forma onde está a calda e cubra com papel alumínio");
        pudim.addPasso("Leve ao forno preaquecido a 180 graus em banho-maria; deixe assar por 45 minutos");
        pudim.addPasso("Deixe esfriar; leve à geladeira por 2 horas e desenforme");
        this.addReceita(pudim);

        Receita amendoim = new Receita("Amendoim doce", "Amendoim doce caseiro",R.drawable.amendoim_doce);
        amendoim.addIngrediente("duas xícaras de amendoim cru com casca");
        amendoim.addIngrediente("duas xícaras de açúcar refinada");
        amendoim.addIngrediente("duas xícaras de água");
        amendoim.addIngrediente("duas colheres (sobremesa) de chocolate em pó");
        amendoim.addPasso("Coloque todos os ingredientes em uma panela e leve ao fogo alto");
        amendoim.addPasso("Misture bem e deixe levantar fervura");
        amendoim.addPasso("Quando ferver abaixe o fogo e mexa sempre");
        amendoim.addPasso("Após 30 minutos vai virar uma calda; continue mexendo até que seque a calda e os amendoim fiquem açucarados");
        amendoim.addPasso("Coloque em uma forma de alumínio e espalhe bem; depois que esfriar coloque em saquinhos de picolé; ou guarde em um recipiente de vidro");
        this.addReceita(amendoim);

        Receita brigadeiro = new Receita("Brigadeiro","Clássica receita de brigadeiro.",R.drawable.brigadeiro);
        brigadeiro.addIngrediente("uma caixa de leite condensado");
        brigadeiro.addIngrediente("uma colher (sopa) de margarina sem sal");
        brigadeiro.addIngrediente("7 colheres (sopa) de achocolatado ou 4 colheres (sopa) de chocolate em pó");
        brigadeiro.addIngrediente("chocolate granulado");
        brigadeiro.addPasso("Em uma panela funda; acrescente o leite condensado; a margarina e o chocolate em pó");
        brigadeiro.addPasso("Cozinhe em fogo médio e mexa até que o brigadeiro comece a desgrudar da panela");
        brigadeiro.addPasso("Deixe esfriar e faça pequenas bolas com a mão passando a massa no chocolate granulado");
        this.addReceita(brigadeiro);

        Receita paodequeijo = new Receita("Pão de queijo light","Pão de queijo ideal para quem está fazendo dieta.",R.drawable.pao_de_queijo);
        paodequeijo.addIngrediente("250 gramas de batata cozida e amassada");
        paodequeijo.addIngrediente("200 gramas de queijo meia cura ralado");
        paodequeijo.addIngrediente("3 ovos");
        paodequeijo.addIngrediente("duas xícaras de chá de polvilho doce");
        paodequeijo.addIngrediente("Sal a gosto");
        paodequeijo.addPasso("Misture os ingredientes e faça bolinhas");
        paodequeijo.addPasso("Asse em forno médio baixo (150° graus) pré-aquecido");
        this.addReceita(paodequeijo);

    }

/* Testa se uma receita já existe no banco de dados com o mesmo nome*/
    public boolean existsReceita(String nome){
        return (getReceita(nome)!=null);
    }

    public void close(){
        MyDBHandler.closeDatabase(bancoDeDados);
    }

    public void open(){
        bancoDeDados = (new MyDBHandler(context)).getWritableDatabase();
    }

    public boolean isOpen(){
        return MyDBHandler.isOpen(bancoDeDados);
    }
}