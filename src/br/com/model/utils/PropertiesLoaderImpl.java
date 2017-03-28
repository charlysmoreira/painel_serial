package br.com.model.utils;



public class PropertiesLoaderImpl {

    private static PropertiesLoader loader = new PropertiesLoader();

    public static String getValor(String chave) {
        return (String) loader.getValor(chave);
    }

    public static void incluirPropriedade(String chave, String valor) {
        loader.incluirPropriedade(chave, valor);
    }

}
