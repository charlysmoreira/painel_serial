package br.com.model.model;

import java.io.Serializable;


public class ConfigTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1049224225115626264L;

    private String portaSelecionada;

    private int boud = 9600;

    public ConfigTO() {
        // TODO Auto-generated constructor stub
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

}
