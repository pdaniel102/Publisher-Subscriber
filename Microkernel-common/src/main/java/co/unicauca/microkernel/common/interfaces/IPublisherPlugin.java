/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.microkernel.common.interfaces;

/**
 *
 * @author ahurtado, wpantoja
 */
public interface IPublisherPlugin {
    
    /**
     *
     * @param msg
     * Mensaje a ser publicado a trav�s de un servicio de mensajer�a
     */
    public void publish(String msg);
}
