package br.com.model.utils;

import java.io.File;
import java.io.IOException;


public class CinfigPainel {
    
    private String recuperarDiretorio() {
       return getClass().getResource("rxtxSerial.dll").getPath();
    }

    public static void main(String[] args) throws IOException {
        // arquivo a ser movido
        File arquivo = new File(new CinfigPainel().recuperarDiretorio());
        
        // diretorio de destino
        String pasta = System.getProperty("java.home"); 
        System.loadLibrary("rxtxSerial.dll");
        File dir = new File(pasta + "\\bin");
        
        // move o arquivo para o novo diretorio
        boolean ok = arquivo.renameTo(new File(dir, arquivo.getName()));
        
        if (ok) {
            System.out.println("Arquivo foi movido com sucesso");
        } else {
            System.out.println("Nao foi possivel mover o arquivo");
        }
    }
}
