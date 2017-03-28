package br.com.model.utils;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JOptionPane;

import br.com.model.model.Menssagem;

/**
 * @author Charlys
 *
 */
public class ConfigSerial implements Runnable, SerialPortEventListener, Serializable {

    /**
     * Responsavel pelo o envio de dados para a serial.
     */
    private static final long serialVersionUID = 5834352699140604205L;

    static String keyPath = "SYSTEM\\CurrentControlSet\\Enum\\FTDIBUS\\VID_0404+PID_6000+A6XF73S9A\\";

    private CommPortIdentifier portId = null;

    private SerialPort port = null;

    protected String[] portas;

    protected Enumeration<?> listaDePortas;

    private OutputStream saida;

    private InputStream entrada;

    private Thread threadLeitura;

    private byte[] buffer = new byte[1024];

    private String MSN;

    private String portaSelecionada;

    private int boud = 9600;

    private boolean enviado;

    public boolean confEnvio;

    private String respostaValidada;

    static Properties props;

    FileProperteis fileProperteis;

    public static List<String> valoresSerial;

    private Map<Character, Integer> map = new LinkedHashMap<Character, Integer>();

    public String[] getPortas() {
        return portas;
    }

    public void setPortas(String[] portas) {
        this.portas = portas;
    }

    public Enumeration<?> getListaDePortas() {
        return listaDePortas;
    }

    public void setListaDePortas(Enumeration<?> listaDePortas) {
        this.listaDePortas = listaDePortas;
    }

    public ConfigSerial() {
        fileProperteis = new FileProperteis();
        valoresSerial = new ArrayList<String>();
        props = fileProperteis.carregarParametros();
        for (int i = 0; i < props.size(); i++) {
            valoresSerial.add(props.getProperty("painel " + i));
        }
    }

    public String getMSN() {
        return MSN;
    }

    public String getPortaSelecionada() {
        return portaSelecionada;
    }

    public void setPortaSelecionada(String portaSelecionada) {
        this.portaSelecionada = portaSelecionada;
    }

    public int getBoud() {
        return boud;
    }

    public void setBoud(int boud) {
        this.boud = boud;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public String getRespostaValidada() {
        return respostaValidada;
    }

    // Parte que abri a portas.
    public void abrirPorta(String portaSelecionada, int boud) throws PortInUseException,
            UnsupportedCommOperationException {
        if (portaSelecionada != null) {
            try {
                setPortaSelecionada(portaSelecionada);
                setBoud(boud);
                portId = CommPortIdentifier.getPortIdentifier(portaSelecionada);
                port = (SerialPort) portId.open(this.getClass().getName(), 2000);
                port.setSerialPortParams(boud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            } catch (NoSuchPortException e) {
                throw new PortInUseException();
            }
        }
    }

    public synchronized void close() {
        if (port != null) {
            port.removeEventListener();
            port.close();
        }
    }

    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                abrirPorta(portaSelecionada, boud);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    public static String getFriendlyName(String registryKey) {
        if (registryKey == null || registryKey.isEmpty()) {
            throw new IllegalArgumentException("'registryKey' null or empty");
        }
        try {
            int hkey = WinRegistry.HKEY_LOCAL_MACHINE;
            return WinRegistry.readString(hkey, registryKey, "FriendlyName");
        } catch (Exception ex) { // catch-all: 
            // readString() throws IllegalArg, IllegalAccess, InvocationTarget
            System.err.println(ex.getMessage());
            return null;
        }

    }

    /**
     * Busca a COM para se conectar.
     */
    int i = 0;
    public void listarPortas() {
        portas =  new String[10];
        try {
            for (String subKey : WinRegistry.readStringSubKeys(WinRegistry.HKEY_LOCAL_MACHINE, keyPath)) {
                String friendlyName = getFriendlyName(keyPath + subKey);
                if (friendlyName != null && friendlyName.contains("PLED") && friendlyName.contains("COM")) {
                    int beginIndex = friendlyName.indexOf("COM") + 3 /*length of 'COM'*/;
                    int endIndex = friendlyName.indexOf(")");
                    portas[i] = "COM" + Integer.parseInt(friendlyName.substring(beginIndex, endIndex));
                   i++;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    boolean inicaiConfig = true;

    /**
     * Aqui prepara envio
     * 
     * @param list
     */
    public void prepararEnvio(List<Menssagem> list) {
        try {
            abrirPorta(PropertiesLoaderImpl.getValor("painel.conf.porta.serial"), 2400);
        } catch (PortInUseException e) {
        } catch (UnsupportedCommOperationException e) {
        }
        enviarUmaString(0xef);
        enviarUmaString(list.size());
        inicaiConfig = false;
        for (Menssagem menssagem : list) {
            enviarUmaString(0xfc);
            if (menssagem.isTemp()) {
                enviarUmaString(0xeb);
            }
            if (menssagem.isData()) {
                enviarUmaString(0xf5);
            }
            if (menssagem.isHora()) {
                enviarUmaString(0xf4);
            }
            if (menssagem.getEfeito() != null && menssagem.getEfeito() != 0) {
                enviarUmaString(menssagem.getEfeito());
            }
            if (menssagem.getVelocidade() != null && menssagem.getVelocidade() != 0) {
                enviarUmaString(menssagem.getVelocidade());
            }
            if (menssagem.getTexto() != null && !menssagem.getTexto().equals("")) {
                convertStringToHex(menssagem.getTexto());
            }
        }
        enviarUmaString(0xf7);
    }

    public void convertStringToHex(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            enviarUmaString((int) chars[i]);
        }
    }
    
    int[] valores = new int[20];

    public void enviarUmaString(int dado) {
        try {
            if (port != null) {
                saida = port.getOutputStream();
            }
        } catch (IOException e) {
            System.out.println("Erro.STATUS: " + e);
        }
        try {
            if (saida != null) {
                saida.write(dado);
                Thread.sleep(100);
                saida.flush();
                if(0xef == dado){
                    setEnviado(true);
                }
                valores = lerDados();
                if (isEnviado()) {
                    if(171 == dado){
                        if (valores[0] == dado) {
                            setEnviado(true);
                        } else {
                            setEnviado(false);
                        }
                    }else{
                        if (valores[1] == dado) {
                            setEnviado(true);
                        } else {
                            setEnviado(false);
                        }
                    }
                    
                }
            }
        } catch (Exception e) {
            if(isEnviado()){
                JOptionPane.showMessageDialog(null, "Falha na comunicação", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
            setEnviado(false);
        }
    }

    public int[] lerDados() {
        int[] respostaDoEnvio = new int[60];
        try {
            if (port != null) {
                entrada = port.getInputStream();
                port.addEventListener(this);
                port.notifyOnDataAvailable(true);
            }
        } catch (Exception e) {
            System.out.println("Erro de stream: " + e);
        }
        try {
            threadLeitura = new Thread(this);
            threadLeitura.start();
            run();
            int data;
            try {
                int len = 0;
                int index = 0;
                while ((data = entrada.read()) > -1) {
                    if (data == '\n') {
                        break;
                    }
                    respostaDoEnvio[index] = data;
                    index++;
                    buffer[len++] = (byte) data;
                }
                String resposta = new String(buffer, 0, len);
                System.out.print(new String(buffer, 0, len));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Erro de Thred: " + e);
        }
        return respostaDoEnvio;
    }

    private boolean validaRetornoPainel(int[] valores, List<String> valoresSerial) {
        List<String[]> listsSerialArray = new ArrayList<String[]>();
        for (int i = 0; i < valoresSerial.size(); i++) {
            String serialToCheckPreparado[] = new String[11];
            for (int j = 0; j < serialToCheckPreparado.length - 1; j++) {
                serialToCheckPreparado[j + 1] = valoresSerial.get(i).substring(j, j + 1);
            }
            listsSerialArray.add(serialToCheckPreparado);
        }

        List<String[]> listaFinal = new ArrayList<String[]>();
        for (int i = 0; i < listsSerialArray.size(); i++) {
            String serialFinal[] = new String[11];
            serialFinal[0] = "AB";
            for (int j = 0; j < listsSerialArray.get(i).length - 1; j++) {
                serialFinal[j + 1] = Utils.protoculoToValidarId()[j] + listsSerialArray.get(i)[j + 1];
            }
            listaFinal.add(serialFinal);
        }

        if (validaSerial(listaFinal, valores)) {
            return true;
        } else {
            return false;
        }

    }

    private boolean validaSerial(List<String[]> listaFinal, int[] valores) {
        for (int i = 0; i < listaFinal.size(); i++) {
            String[] serial = listaFinal.get(i);
            if (Integer.parseInt(serial[0], 16) == valores[0] && Integer.parseInt(serial[10], 16) == valores[1]
                    && Integer.parseInt(serial[9], 16) == valores[2] && Integer.parseInt(serial[8], 16) == valores[3]
                    && Integer.parseInt(serial[7], 16) == valores[4] && Integer.parseInt(serial[6], 16) == valores[5]
                    && Integer.parseInt(serial[5], 16) == valores[6] && Integer.parseInt(serial[4], 16) == valores[7]
                    && Integer.parseInt(serial[3], 16) == valores[8] && Integer.parseInt(serial[2], 16) == valores[9]
                    && Integer.parseInt(serial[1], 16) == valores[10]) {

                String idToProperteis = serial[1].substring(1, 2);
                for (int j = 2; j < serial.length; j++) {
                    idToProperteis += serial[j].substring(1, 2);
                }
                PropertiesLoaderImpl.incluirPropriedade("painel.conf.id", idToProperteis);
                return true;
            }
        }
        return false;
    }

    public boolean validaPainel(int valorToValidar, String porta) throws PortInUseException, UnsupportedCommOperationException {
          abrirPorta(porta, 2400);
          portaSelecionada = porta;
          boud = 2400;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        enviarUmaString(valorToValidar);
//        valores = lerDados();

        System.out.println(valores);
        if (validaRetornoPainel(valores, valoresSerial)) {
            PropertiesLoaderImpl.incluirPropriedade("painel.conf.valido", "true");
            this.close();
            return true;
        }
        return false;
    }

    public void run() {

    }

    /** 
     * Aqui envia a data e a hora para configurar.
     */
    public void verificaLed() {
        try {
            abrirPorta(PropertiesLoaderImpl.getValor("painel.conf.porta.serial"), 2400);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (port != null) {
            enviarUmaString(0xac);
            confEnvio = true;
            this.close();
        } else {
            JOptionPane.showMessageDialog(null, "Porta serial não conectada!!", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** 
     * Aqui envia a data e a hora para configurar.
     */
    public void configuraDataHora() {
        try {
            abrirPorta(PropertiesLoaderImpl.getValor("painel.conf.porta.serial"), 2400);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (port != null) {
            popularMap();
            enviarUmaString(0xda);
            getHora();
            getData();
            enviarUmaString(0xdf);
            this.close();
            confEnvio = true;
        } else {
            JOptionPane.showMessageDialog(null, "Porta serial não conectada!!", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void getData() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String data = sdf.format(new Date());
        char[] chars = data.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char valor = chars[i];
            if (valor != '/') {
                for (Entry<Character, Integer> entry : map.entrySet()) {
                    if (entry.getKey().equals(valor)) {
                        enviarUmaString(entry.getValue());
                    }
                }
            }
        }

    }

    public void getHora() {
        StringBuilder sb = new StringBuilder();
        GregorianCalendar d = new GregorianCalendar();
        sb.append(d.get(GregorianCalendar.HOUR_OF_DAY));
        sb.append(":");
        sb.append(d.get(GregorianCalendar.MINUTE));
        sb.append(":");
        sb.append(d.get(GregorianCalendar.SECOND));
        char[] chars = sb.toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char valor = chars[i];
            if (valor != ':') {
                for (Entry<Character, Integer> entry : map.entrySet()) {
                    if (entry.getKey().equals(valor)) {
                        enviarUmaString(entry.getValue());
                    }
                }
            }
        }
    }

    public Map<Character, Integer> popularMap() {
        map.put('0', new Integer(0xd0));
        map.put('1', new Integer(0xd1));
        map.put('2', new Integer(0xd2));
        map.put('3', new Integer(0xd3));
        map.put('4', new Integer(0xd4));
        map.put('5', new Integer(0xd5));
        map.put('6', new Integer(0xd6));
        map.put('7', new Integer(0xd7));
        map.put('8', new Integer(0xd8));
        map.put('9', new Integer(0xd9));
        return map;

    }
}
