package com.project.baseapp.queue

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import org.springframework.stereotype.Component

@Component
class Pusher {

  private final static String QUEUE_NAME = "q-session"
  
  void push(String message) {
    ConnectionFactory factory = new ConnectionFactory()
    factory.setHost("localhost")
    Connection connection = factory.newConnection()
    Channel channel = connection.createChannel()

    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
    channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"))
    System.out.println(" [x] Sent '" + message + "'")

    channel.close()
    connection.close()
  }

}
