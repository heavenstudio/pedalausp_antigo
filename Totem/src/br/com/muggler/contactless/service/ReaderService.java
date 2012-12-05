/*
 * Copyright (c) 1999-2009 Touch Tecnologia e Informatica Ltda.
 * Gomes de Carvalho, 1666, 3o. Andar, Vila Olimpia, Sao Paulo, SP, Brasil.
 * Todos os direitos reservados.
 * 
 * Este software e confidencial e de propriedade da Touch Tecnologia e
 * Informatica Ltda. (Informacao Confidencial). As informacoes contidas neste
 * arquivo nao podem ser publicadas, e seu uso esta limitado de acordo com os
 * termos do contrato de licenca.
 */

package br.com.muggler.contactless.service;


/**
 *
 *
 * @author muggler
 * @since 
 *
 */
public interface ReaderService {

    /**
     * Inicia o serviço de leitura de smartcards.
     * 
     * @since
     */
    public abstract void start();

    /**
     * Interrompe o serviço de leitura de smartcard.
     * 
     * @since
     */
    public abstract void stop();

}
