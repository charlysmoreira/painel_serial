package br.com.model.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileProperteis {

    File arquivo = null;
    
    File arquivoPorta = null;

    public void salvarParametros(List<String> lista) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(arquivo, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (String valor : lista) {
                byte reslt[];
                try {
                    reslt = Base64Utils.encode(valor.getBytes());
                    printWriter.println(new String(reslt));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public void salvarPortaSelecionada(String porta) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(arquivoPorta, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            byte reslt[];
            try {
                reslt = Base64Utils.encode(porta.getBytes());
                printWriter.println(new String(reslt));
            } catch (Exception e) {
                e.printStackTrace();
            }
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public Properties carregarPortas() {
        // Cria o manipulador de propriedades
        Properties props = new Properties();

        File fileDir = new File("C:\\serial_painel");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }

        arquivoPorta = new File("C:\\serial_painel\\portaSerial.properties");
        if (!arquivoPorta.exists()) {
            try {
                arquivoPorta.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileReader fileReader;
        List<String> result = null;
        try {
            fileReader = new FileReader(arquivoPorta);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String linha = "";
            result = new ArrayList<String>();
            while ((linha = bufferedReader.readLine()) != null) {
                System.out.println(linha);
                if (linha != null && !linha.isEmpty()) {
                    result.add(linha);
                }
            }
            fileReader.close();
            bufferedReader.close();
        } catch (IOException ex2) {
            ex2.printStackTrace();
        }
        byte reslt[];
        try {
            if(!result.isEmpty()){
                reslt = Base64Utils.decode(result.get(0).getBytes());
                props.setProperty("porta",new String(reslt));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }

    public Properties carregarParametros() {
        // Cria o manipulador de propriedades
        Properties props = new Properties();

        File fileDir = new File("C:\\serial_painel");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }

        arquivo = new File("C:\\serial_painel\\paineis.properties");
        if (!arquivo.exists()) {
            try {
                arquivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileReader fileReader;
        List<String> result = null;
        try {
            fileReader = new FileReader(arquivo);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String linha = "";
            result = new ArrayList<String>();
            while ((linha = bufferedReader.readLine()) != null) {
                System.out.println(linha);
                if (linha != null && !linha.isEmpty()) {
                    result.add(linha);
                }
            }
            fileReader.close();
            bufferedReader.close();
        } catch (IOException ex2) {
            ex2.printStackTrace();
        }
        for (int i = 0; i < result.size(); i++) {
            byte reslt[];
            try {
                reslt = Base64Utils.decode(result.get(i).getBytes());
                props.setProperty("painel " + i, new String(reslt));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return props;
    }
}
