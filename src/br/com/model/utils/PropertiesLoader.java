package br.com.model.utils;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertiesLoader {

    public Properties prop;

    public URL filename;

    private String nomeDoProperties = "/br/com/model/utils/propriedade.properties";

    protected PropertiesLoader() {
        prop = new Properties();
        try {
            filename = this.getClass().getResource(nomeDoProperties);
            InputStream in = filename.openStream();
            prop.load(in);
        } catch (Exception e) {
            System.out.println("Could not load Properties file :\n" + e);
        }
    }

    protected String getValor(String chave) {
        return (String) prop.getProperty(chave);
    }

    protected void incluirPropriedade(String chave, String valor) {
        prop.setProperty(chave, valor);
    }
}
