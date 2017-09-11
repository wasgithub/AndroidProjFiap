package br.com.washington.androidprojfiap.domain;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.washington.androidprojfiap.R;
import livroandroid.lib.utils.FileUtils;
import livroandroid.lib.utils.HttpHelper;
import livroandroid.lib.utils.IOUtils;
import livroandroid.lib.utils.SDCardUtils;
import livroandroid.lib.utils.XMLUtils;

/**
 * Created by washington on 09/09/2017.
 */

public class DogService {
    private static final boolean LOG_ON = false;
    private static final String TAG = "DogService";

    private static final String URL = "http://dog_{tipo}.json";

    public static List<Dog> getDogs(Context context, int tipo, boolean refresh) throws IOException {
        // Busca os dogs no banco de dados (somente se refresh=false)
        List<Dog> dogs = !refresh ? getDogsFromBanco(context, tipo) : null;
        if (dogs != null && dogs.size() > 0) {
            // Encontrou o arquivo
            return dogs;
        }
        // Se não encontrar, busca no web service
        //dogs = getDogsFromWebService(context, tipo);
        dogs = getDogsFromArquivo(context, tipo);
        return dogs;
    }

    public static List<Dog> getDogsFromBanco(Context context, int tipo) throws IOException {
        DogDB db = new DogDB(context);
        try {
            String tipoString = getTipo(tipo);
            List<Dog> dogs = db.findAllByTipo(tipoString);
            Log.d(TAG, "Retornando " + dogs.size() + " dogs [" + tipoString + "] do banco");
            return dogs;
        } finally {
            db.close();
        }
    }

    // Abre o arquivo salvo, se existir, e cria a lista de dogs
    public static List<Dog> getDogsFromArquivo(Context context, int tipo) throws IOException {
        String tipoString = getTipo(tipo);
        String fileName = String.format("dogs_%s.json", tipoString);
        Log.d(TAG, "Abrindo arquivo: " + fileName);
        // Lê o arquivo da memória interna
        String json = readFile(context, tipo);//FileUtils.readFile(context, fileName, "UTF-8");
        if (json == null) {
            Log.d(TAG, "Arquivo " + fileName + " não encontrado.");
            return null;
        }
        List<Dog> dogs = parserJSON(context, json);
        Log.d(TAG, "Retornando dogs do arquivo " + fileName + ".");
        // Depois de buscar salva os dogs
        salvarDogs(context, tipo, dogs);
        return dogs;
    }

    // Faz a requisição HTTP, cria a lista de dogs e salva o JSON em arquivo
    public static List<Dog> getDogsFromWebService(Context context, int tipo) throws IOException {
        String tipoString = getTipo(tipo);
        String url = URL.replace("{tipo}", tipoString);
        Log.d(TAG, "URL: " + url);
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url);
        List<Dog> dogs = parserJSON(context, json);
//        salvaArquivoNaMemoriaInterna(context, url, json);
        // Depois de buscar salva os dogs
        salvarDogs(context, tipo, dogs);
        return dogs;
    }

    // Salva os dogs no banco de dados
    private static void salvarDogs(Context context, int tipo, List<Dog> dogs) {
        String tipoString = getTipo(tipo);
        DogDB db = new DogDB(context);
        try {
            // Deleta os dogs antigos pelo tipo para limpar o banco
            db.deleteDogsByTipo(tipoString);
            // Salva todos os dogs
            for (Dog c : dogs) {
                c.tipo = tipoString;
                Log.d(TAG, "Salvando o dog " + c.nome);
                db.save(c);
            }
        } finally {
            db.close();
        }
    }

    private static void salvaArquivoNaMemoriaExterna(Context context, String url, String json) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        // Cria um arquivo privado
        File f = SDCardUtils.getPrivateFile(context, fileName, Environment.DIRECTORY_DOWNLOADS);
        IOUtils.writeString(f, json);
        Log.d(TAG, "1) Arquivo privado salvo na pasta downloads: " + f);

        // Cria um arquivo público
        f = SDCardUtils.getPublicFile(fileName, Environment.DIRECTORY_DOWNLOADS);
        IOUtils.writeString(f, json);
        Log.d(TAG, "2) Arquivo público salvo na pasta downloads: " + f);
    }


    private static void salvaArquivoNaMemoriaInterna(Context context, String url, String json) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        File f = FileUtils.getFile(context, fileName);
        IOUtils.writeString(f, json);
        Log.d(TAG, "Arquivo salvo: " + f);

    }

    private static String getTipo(int tipo) {
        if (tipo == R.string.small) {
            return "small";
        } else if (tipo == R.string.medium) {
            return "medium";
        }
        return "large";
    }

    public static List<Dog> getDogsFromRaw(Context context, int tipo) throws IOException {
        String xml = readFile(context, tipo);
        List<Dog> dogs = parserXML(context, xml);
        //String json = readFile(context, tipo);
        //List<Dog> dogs = parserJSON(context, json);

        return dogs;
    }

    private static List<Dog> parserJSON(Context context, String json) throws IOException {
        List<Dog> dogs = new ArrayList<Dog>();
        try {
            JSONObject root = new JSONObject(json);
            JSONObject obj = root.getJSONObject("dogs");
            JSONArray jsonDogs = obj.getJSONArray("dog");
            // Insere cada dog na lista
            for (int i = 0; i < jsonDogs.length(); i++) {
                JSONObject jsonDog = jsonDogs.getJSONObject(i);
                Dog c = new Dog();
                // Lê as informações de cada dog
                c.nome = jsonDog.optString("nome");
                c.desc = jsonDog.optString("desc");
                c.urlFoto = jsonDog.optString("url_foto");
                c.urlInfo = jsonDog.optString("url_info");
                c.urlVideo = jsonDog.optString("url_video");
                c.latitude = jsonDog.optString("latitude");
                c.longitude = jsonDog.optString("longitude");
                if (LOG_ON) {
                    Log.d(TAG, "Dog " + c.nome + " > " + c.urlFoto);
                }
                dogs.add(c);
            }
            if (LOG_ON) {
                Log.d(TAG, dogs.size() + " encontrados.");
            }
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return dogs;
    }


    // Faz a leitura do arquivo que está na pasta /res/raw
    private static String readFile(Context context, int tipo) throws IOException {
        if (tipo == R.string.small) {
            return FileUtils.readRawFileString(context, R.raw.dogs_small, "UTF-8");
        } else if (tipo == R.string.medium) {
            return FileUtils.readRawFileString(context, R.raw.dogs_medium, "UTF-8");
        }
        return FileUtils.readRawFileString(context, R.raw.dogs_large, "UTF-8");
    }

    // Faz o parser do XML e cria a lista de dogs
    private static List<Dog> parserXML(Context context, String xml) {
        List<Dog> dogs = new ArrayList<Dog>();
        Element root = XMLUtils.getRoot(xml, "UTF-8");
        // Le todas as tags <dog>
        List<Node> nodeDogs = XMLUtils.getChildren(root, "carro");
        // Insere cada dog na lista
        for (Node node : nodeDogs) {
            Dog c = new Dog();
            // Lê as informações de cada dog
            c.nome = XMLUtils.getText(node, "nome");
            c.desc = XMLUtils.getText(node, "desc");
            c.urlFoto = XMLUtils.getText(node, "url_foto");
            c.urlInfo = XMLUtils.getText(node, "url_info");
            c.urlVideo = XMLUtils.getText(node, "url_video");
            c.latitude = XMLUtils.getText(node, "latitude");
            c.longitude = XMLUtils.getText(node, "longitude");
            if (LOG_ON) {
                Log.d(TAG, "Dog " + c.nome + " > " + c.urlFoto);
            }
            dogs.add(c);
        }
        if (LOG_ON) {
            Log.d(TAG, dogs.size() + " encontrados.");
        }
        return dogs;
    }
}