package br.com.model.model;


public class Menssagem {
    
    private Integer tipoTexto;

    private String texto;

    private Integer efeito;

    private Integer velocidade;

    private boolean temp;

    private boolean data;

    private boolean hora;

    public Menssagem(String texto, Integer efeito, Integer velocidade, Integer tipoTexto) {
        this.texto = texto;
        this.efeito = efeito;
        this.velocidade = velocidade;
        this.tipoTexto = tipoTexto;
    }

    public Menssagem() {}

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getEfeito() {
        return efeito;
    }

    public void setEfeito(Integer efeito) {
        this.efeito = efeito;
    }

    public Integer getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(Integer velocidade) {
        this.velocidade = velocidade;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    public boolean isHora() {
        return hora;
    }

    public void setHora(boolean hora) {
        this.hora = hora;
    }

    
    public Integer getTipoTexto() {
        return tipoTexto;
    }

    
    public void setTipoTexto(Integer tipoTexto) {
        this.tipoTexto = tipoTexto;
    }

}
