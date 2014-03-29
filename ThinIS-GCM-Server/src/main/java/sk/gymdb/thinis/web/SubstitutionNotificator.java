package sk.gymdb.thinis.web;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import sk.gymdb.thinis.gcm.server.*;
import sk.gymdb.thinis.gcm.web.Datastore;
import sk.gymdb.thinis.gcm.web.Registration;
import sk.gymdb.thinis.service.SubstitutionService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.entity.StringEntity;

/**
 * Created by matejkobza on 21.12.2013.
 */
public class SubstitutionNotificator implements Runnable {

    private static final int MULTICAST_SIZE = 1000;
    static final String ATTRIBUTE_ACCESS_KEY = "api.key";
    private static final String PATH = "application.properties";

    private SubstitutionService service;
    private static final Executor threadPool = Executors.newFixedThreadPool(5);
    private Sender sender;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public SubstitutionNotificator() {
        service = new SubstitutionService();

        // load api key
        String key;
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(PATH));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load api key.");
        }
        key = properties.getProperty(ATTRIBUTE_ACCESS_KEY);
        sender = new Sender(key);
    }

    @Override
    public void run() {
        System.out.println("Hello from a thread!");
        // hashmap key is class, value is message
        HashMap<String, String> substitutions = null;
        try {
            substitutions = service.findSubstitutions();
        } catch (IOException e) {
            e.printStackTrace();        }
        if (substitutions != null) {
            if (!substitutions.isEmpty()) {
                for (String clazz : substitutions.keySet()) {
                    // send substitution
                    List<Registration> devices = Datastore.getDevices(clazz);
                    String message = substitutions.get(clazz);
                    sendMessages(devices, message);
                }
            }
        }
    }

    private void sendMessages(List<Registration> ids, String message) {
        String status;
        if (ids.isEmpty()) {
            status = "Message ignored as there is no device registered!";
        } else {
            int total = ids.size();
            List<Registration> partialDevices = new ArrayList<Registration>(total);
            int counter = 0;
            int tasks = 0;
            // divide into messages sent by 1000 per request
            for (Registration id : ids) {
                counter++;
                partialDevices.add(id);
                int partialSize = partialDevices.size();
                if (partialSize == MULTICAST_SIZE || counter == total) {
                    asyncSend(partialDevices, message);
                    partialDevices.clear();
                    tasks++;
                }
            }
            status = "Asynchronously sending " + tasks + " multicast messages to " +
                    total + " devices";
        }
        System.out.println(status);
    }

    private void asyncSend(List<Registration> partialDevices, final String message) {
        // make a copy
        final List<Registration> devices = new ArrayList<Registration>(partialDevices);
        // run sending in separate thread (thread in thread WTF?)
        threadPool.execute(new Runnable() {

            public void run() {
                Message.Builder messageBuilder = new Message.Builder();
                System.out.println("SPRAVA: "+ message);
                try {
                    messageBuilder.addData("SPRAVA", URLEncoder.encode(message,"UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(SubstitutionNotificator.class.getName()).log(Level.SEVERE, null, ex);
                }
                Message message = messageBuilder.build();
                MulticastResult multicastResult;
                try {
                    multicastResult = sender.send(message, (List<String>) CollectionUtils.collect(devices, new Transformer() {
                        @Override
                        public Object transform(Object o) {
                            return ((Registration) o).getId();
                        }
                    }), 5);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error posting messages", e);
                    return;
                }
                List<Result> results = multicastResult.getResults();
                // analyze the results
                for (int i = 0; i < devices.size(); i++) {
//          String regId = devices.get(i);
                    Registration registration = devices.get(i);
                    Result result = results.get(i);
                    String messageId = result.getMessageId();
                    if (messageId != null) {
                        logger.fine("Succesfully sent message to device: " + registration.getId() +
                                "; messageId = " + messageId);
                        if (result.getCanonicalRegistrationId() != null) {
                            // same device has more than on registration id: update it
                            Registration canonicalRegId = new Registration(result.getCanonicalRegistrationId(), registration.getClazz());
                            logger.info("canonicalRegId " + canonicalRegId);
                            Datastore.updateRegistration(registration, canonicalRegId);
                        }
                    } else {
                        String error = result.getErrorCodeName();
                        if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                            // application has been removed from device - unregister it
                            logger.info("Unregistered device: " + registration.getId());
                            Datastore.unregister(registration);
                        } else {
                            logger.severe("Error sending message to " + registration.getId() + ": " + error);
                        }
                    }
                }
            }
        });
    }
}
