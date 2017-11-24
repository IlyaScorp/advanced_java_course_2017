package edu.technopolis.advanced.boatswain;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.technopolis.advanced.boatswain.incoming.request.Message;
import edu.technopolis.advanced.boatswain.incoming.request.MessageNotification;
import edu.technopolis.advanced.boatswain.request.GetSubscriptionsRequest;
import edu.technopolis.advanced.boatswain.request.SendMessagePayload;
import edu.technopolis.advanced.boatswain.request.SendMessageRequest;
import edu.technopolis.advanced.boatswain.request.SendRecipient;
import edu.technopolis.advanced.boatswain.request.SubscribePayload;
import edu.technopolis.advanced.boatswain.request.SubscribeRequest;
import edu.technopolis.advanced.boatswain.response.CurrencyResponse;
import edu.technopolis.advanced.boatswain.response.GetSubscriptionsResponse;
import edu.technopolis.advanced.boatswain.response.SendMessageResponse;
import edu.technopolis.advanced.boatswain.response.SubscribeResponse;
import edu.technopolis.advanced.boatswain.response.Subscription;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class BoatswainBot {
    private static final Logger log = LoggerFactory.getLogger(BoatswainBot.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        log.info("Reading application properties...");
        try {
            props.load(BoatswainBot.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            log.error("Failed to read application properties. Terminating application...");
            System.exit(1);
        }
        ApiClient okClient = null;
        try {
            okClient = createClient(props);
            GetSubscriptionsResponse response = okClient.get(
                    new GetSubscriptionsRequest(props.getProperty("ok.api.endpoint.subscriptions")), GetSubscriptionsResponse.class);
            String botEndpoint = props.getProperty("bot.message.endpoint");
            log.info("Checking that bot is subscribed to messages...");
            if (checkSubscribed(botEndpoint, response)) {
                log.info("Subscription exists");
            } else {
                log.info("Subscription does not exist. Making a subscription...");
                subscribe(okClient, props, botEndpoint);
                log.info("Subscription is ok");
            }
            log.info("Creating endpoint...");
            createServer(okClient, props);
            log.info("Server created. Waiting for incoming connections...");
        } catch (Exception e) {
            log.error("Unexpected failure", e);
            closeClient(okClient);
            System.exit(1);
        }
    }

    private static void closeClient(ApiClient client) {
        if (client != null) {
            try {
                client.close();
            } catch (IOException ce) {
                log.error("Failed to close client", ce);
            }
        }
    }

    private static void subscribe(ApiClient client, Properties props, String botEndpoint) throws IOException {
        SubscribeRequest req = new SubscribeRequest(props.getProperty("ok.api.endpoint.subscribe"),
                new SubscribePayload(botEndpoint, props.getProperty("bot.phrase")));
        SubscribeResponse post = client.post(req, SubscribeResponse.class);
        if (!post.isSuccess()) {
            throw new IllegalStateException("Failed to subscribe bot to messages");
        }
    }

    private static boolean checkSubscribed(String botEndpoint, GetSubscriptionsResponse subscriptionsResponse) {
        if (subscriptionsResponse.getSubscriptions() == null || subscriptionsResponse.getSubscriptions().isEmpty()) {
            return false;
        }

        for (Subscription subscription : subscriptionsResponse.getSubscriptions()) {
            if (Objects.equals(subscription.getUrl(), botEndpoint)) {
                return true;
            }
        }
        return false;
    }

    private static ApiClient createClient(Properties props) throws IOException {
        String schema = props.getProperty("ok.api.schema", "https");
        String host = props.getProperty("ok.api.host");
        String tokenParamName = props.getProperty("ok.api.param.token");
        String token = props.getProperty("ok.api.access_token");
        return new ApiClient(schema, host, tokenParamName + '=' + token);
    }

    private static void createServer(ApiClient okClient, Properties props) {
        try {
            BotServer botServer = new BotServer(props.getProperty("bot.message.local.endpoint"),
                    new MessageSender(okClient, props)::send);
            Runtime
                    .getRuntime()
                    .addShutdownHook(new Thread(() -> {
                        closeClient(okClient);
                        botServer.stop();
                    }));
            botServer.start();
        } catch (IOException e) {
            log.error("Failed to initialize http server on port 80", e);
        }
    }

    private static class MessageSender {

        private final ApiClient client;
        private final String phrase;
        private final String joke;
        private final String sendEndpoint;

        MessageSender(ApiClient okClient, Properties props) {
            this.client = okClient;
            this.phrase = props.getProperty("bot.phrase");
            this.joke = props.getProperty("bot.joke");
            this.sendEndpoint = props.getProperty("ok.api.endpoint.send");
        }

        boolean send(MessageNotification notif) {
            if (notif == null || notif.getMessage() == null || notif.getMessage().getText() == null) {
                log.info("Message notification contains no text <{}>", notif);
                return true;
            }
            if (!notif.getMessage().getText().startsWith(phrase)) {
                log.info("Message notification does not contain phrase <{}>", notif);
                return true;
            }
            if (notif.getRecipient() == null || notif.getRecipient().getChatId() == null) {
                log.warn("Message notification does not contain chat id <{}>", notif);
                return false;
            }
            SendMessageRequest req = new SendMessageRequest(sendEndpoint, notif.getRecipient().getChatId())
                    .setPayload(
                            new SendMessagePayload(
                                    new SendRecipient(notif.getSender().getUserId()),
                                    new Message(joke)
                            )
                    );
            try {
                return client.post(req, SendMessageResponse.class).getMessageId() != null;
            } catch (IOException e) {
                log.error("Failed to send message ", e);
                return false;
            }
        }
    }

}
