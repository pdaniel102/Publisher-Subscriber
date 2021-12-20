/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.subscriber.outputinventory.infra;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ahurtado,wpantoja
 */
public class RabbitListener implements Runnable{

    private final static String EXCHANGE_NAME = "ProductstoDelivery";
    ISubscriber myOffice;

    public RabbitListener(ISubscriber office) {
        this.myOffice = office; 
    }
    
    @Override
    public void run() {
    try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //Declaramos el exchange
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            //Enlazamos la queue
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            //Capturamos el mensaje
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                myOffice.onMessage(message);
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(RabbitListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
