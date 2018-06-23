package demo

import java.nio.ByteBuffer
import java.util.Timer
import java.util.TimerTask

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.greengrass.javasdk.IotDataClient
import com.amazonaws.greengrass.javasdk.model.*

class HelloWorld {

    fun handleRequest(input: Any, context: Context): String {
        return "Hello from Kotlin"
    }

    companion object {
        init {
            val timer = Timer()
            // Repeat publishing a message every 5 seconds
            timer.scheduleAtFixedRate(PublishHelloWorld(), 0, 5000)
        }
    }
}

private class PublishHelloWorld : TimerTask() {
    private val iotDataClient = IotDataClient()
    private val publishMessage = String.format("Hello world! Sent from Greengrass Core running on platform: %s-%s using Java", System.getProperty("os.name"), System.getProperty("os.version"))
    private val publishRequest = PublishRequest().withTopic("hello/world").withPayload(ByteBuffer.wrap(String.format("{\"message\":\"%s\"}", publishMessage).toByteArray()))

    override fun run() {
        try {
            iotDataClient.publish(publishRequest)
        } catch (ex: Exception) {
            System.err.println(ex)
        }
    }
}
