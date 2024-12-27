package br.com.roecker.cm.modelo;

@FunctionalInterface
public interface CampoObeservador {

    public void eventoOcoreu(Campo campo, CampoEvento evento);

}
