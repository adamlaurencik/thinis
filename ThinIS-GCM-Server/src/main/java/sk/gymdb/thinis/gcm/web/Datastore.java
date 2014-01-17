/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sk.gymdb.thinis.gcm.web;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * REIMPLEMENT LATER TO CSV files
 *
 * Simple implementation of a data store using standard Java collections.
 *
 * <p/>
 * This class is thread-safe but not persistent (it will lost the data when the
 * app is restarted) - it is meant just as an example.
 */
public final class Datastore {

    private static final Set<Registration> regIds = new HashSet<Registration>();
    private static final Logger logger =
            Logger.getLogger(Datastore.class.getName());

    private Datastore() {
        throw new UnsupportedOperationException();
    }

    /**
     * Registers a device.
     */
//    @Deprecated
//    public static void register(String regId, String regClazz) {
//        logger.info("Registering [" + regId + "," + regClazz + " ]");
//        synchronized (regIds) {
//            regIds.add(new Registration(regId, regClazz));
//        }
//    }

    public static void register(Registration registration) {
        logger.info("Registering " + registration);
        synchronized (regIds) {
            regIds.add(registration);
        }
    }

    /**
     * Unregisters a device.
     */
//    @Deprecated
//    public static void unregister(String regId) {
//        logger.info("Unregistering " + regId);
//        synchronized (regIds) {
//            regIds.remove(new Registration(regId));
//        }
//    }

    public static void unregister(Registration registration) {
        logger.info("Unregistering " + registration);
        synchronized (regIds) {
            regIds.remove(registration);
        }
    }

    /**
     * Updates the registration id of a device.
     */
//    @Deprecated
//    public static void updateRegistration(String oldId, String newId, String newClazz) {
//        logger.info("Updating " + oldId + " to " + newId);
//        synchronized (regIds) {
//            regIds.remove(new Registration(oldId));
//            regIds.add(new Registration(newId, newClazz));
//        }
//    }

    public static void updateRegistration(Registration oldRegistration, Registration newRegistration) {
        logger.info("Updating " + oldRegistration + " to " + newRegistration);
        synchronized (regIds) {
            regIds.remove(oldRegistration);
            regIds.add(newRegistration);
        }
    }

    /**
     * Gets all registered devices.
     */
//    @Deprecated
//    public static List<String> getDevices() {
//        synchronized (regIds) {
//            return new ArrayList<String>(CollectionUtils.collect(regIds,new Transformer() {
//                @Override
//                public Object transform(Object o) {
//                    return ((Registration)o).getId();
//                }
//            }));
//        }
//    }

    public static List<Registration> getDevices() {
        synchronized (regIds) {
            return new ArrayList<Registration>(regIds);
        }
    }

}
