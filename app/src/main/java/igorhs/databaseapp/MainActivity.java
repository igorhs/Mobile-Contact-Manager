package igorhs.databaseapp;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.app.AlertDialog.Builder;

public class MainActivity extends ActionBarActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Sobre) {

            AlertDialog DialogTest;
            DialogTest = new AlertDialog.Builder(this).create();
            DialogTest.setTitle("Sobre");
            DialogTest.setMessage("APLICAÇÃO INTEGRANDO SQLITE PARA FINS DE REGISTRO CRIADA EM 23/03/2016");
            DialogTest.show();

            return true;
        }

        if (id == R.id.Sair) {
            System.exit(0);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Primeira parte: componentes da tabela.

    EditText editNome, editContato, editTipo;
    SQLiteDatabase db;
    private String nome = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //Segunda parte: instanciando os componentes da tabela com os dados dos "EditText".
        editNome = (EditText) findViewById(R.id.editTextNome);
        editContato = (EditText) findViewById(R.id.editTextContato);
        editTipo = (EditText) findViewById(R.id.editTextTipo);

        //Terceira parte: criação da tabela "ContatosDB".
        db = openOrCreateDatabase("ContatosDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Contatos(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Nome VARCHAR, Contato VARCHAR, Tipo VARCHAR);");
    }

    //Botão Adicionar
    public void adicionar (View view){

        if(editNome.getText().toString().trim().length() == 0 || editContato.getText().toString().trim().length() == 0 ||
                editTipo.getText().toString().length() == 0)/*Verifica se são valores invalidos.*/{

            showMessage("Erro", "Por favor, preencha corretamente os campos.");
            return;
        }
        db.execSQL("INSERT INTO Contatos(Nome, Contato, Tipo) VALUES('"+editNome.getText()+"', '"+editContato.getText()+"', '"+editTipo.getText()+"');");
        showMessage("Ok", "Dados salvos com sucesso.");
        clearText();
    }

    //Botão Excluir
    public void excluir (View view){

        if (editNome.getText().toString().trim().length() == 0) /*Verifica se o nome é invalido.*/ {
            showMessage("Erro", "Por favor, preencha corretamente o campo 'Nome'");
            return;
        }

        Cursor c = db.rawQuery("SELECT * FROM Contatos WHERE Nome='"+editNome.getText()+"'", null);
        if (c.moveToFirst()){
            db.execSQL("DELETE FROM Contatos WHERE Nome='"+editNome.getText()+"'");
            showMessage("Feito!", "Dados excluidos com sucesso");
        }
        else
        {
            showMessage("Erro", "Inválido");
        }
        clearText();
    }

    //Botão Alterar
    public void alterar (View view) {

        if (editNome.getText().toString().trim().length() == 0) /*Verifica se o nome é invalido.*/ {
            showMessage("Erro", "Por favor, preencha corretamente o campo 'Nome'");
            return;
        }
        nome = editNome.getText().toString();
        db.execSQL("UPDATE Contatos SET Nome='" + nome + "', Contato='" + editContato.getText() + "', Tipo='" + editTipo.getText() + "' WHERE Nome='" + nome + "'");
        showMessage("Feito!", "Dados alterados com sucesso");
    }

    //Botão Pesquisar
    public void pesquisar (View view) {

        if (editNome.getText().toString().trim().length() == 0) /*Verifica se o nome é invalido.*/ {
            showMessage("Erro", "Por favor, preencha corretamente o campo 'Nome'");
            return;
        }

        Cursor c = db.rawQuery("SELECT * FROM Contatos WHERE Nome='"+editNome.getText()+"'", null);
        if (c.moveToFirst()){
            editNome.setText(c.getString(1));
            editContato.setText(c.getString(2));
            editTipo.setText(c.getString(3));
        }
        else{
            showMessage("Erro", "Nome inválido");
            clearText();
        }
    }

    //Botão Listar Contatos
    public void listar (View view){

        Cursor c = db.rawQuery("SELECT * FROM Contatos", null);
        c.moveToFirst();
        if(c.getCount() == 0){
            showMessage("Erro", "Nada encontrado");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            buffer.append("Nome: " + c.getString(1) + "\n" + "Tipo: " + c.getString(3) + "\n");

        }

        showMessage("Detalhes dos contatos", buffer.toString());
    }

    //Botão Limpar Campos
    public void limpar (View view){

        editNome.setText("");
        editContato.setText("");
        editTipo.setText("");
    }

    //Criando método showMessage()
    public void showMessage (String titulo, String mensagem){

        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);
        builder.show();
    }

    //Criando o método clearText()
    public void clearText (){

        editNome.setText("");
        editContato.setText("");
        editTipo.setText("");
    }

}