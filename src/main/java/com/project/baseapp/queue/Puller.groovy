package com.project.baseapp.queue

import com.rabbitmq.client.*;
import org.springframework.stereotype.Component

@Component
class Puller {

  private final static String QUEUE_NAME = "q-session"

  String pull() {

    ConnectionFactory factory = new ConnectionFactory()
    factory.setHost("localhost")
    Connection connection = factory.newConnection()
    Channel channel = connection.createChannel()

    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C")

//    List messages = []

//    Consumer consumer = new DefaultConsumer(channel) {
//      @Override
//      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
//          throws IOException {
//        String message = new String(body, "UTF-8")
////        messages << message
//        System.out.println(" [x] Received '" + message + "'")
//      }
//    }
//
//    channel.basicConsume(QUEUE_NAME, true, consumer)

    String message
    boolean timeout
    long startTime = System.currentTimeMillis()
    while (!(message || timeout)) {
      try {
        message = new String(channel.basicGet(QUEUE_NAME, true).body)
      } catch (Exception e) {
        long endTime = System.currentTimeMillis()
        long elapsedTime = endTime - startTime
        if(elapsedTime > 10000) {
          timeout = true
        }
      }
    }




//    String message = new String(channel.basicGet(QUEUE_NAME, true).body)
    return message
  }

}
