package sk.gymdb.thinis.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 17.1.2014
 * Time: 21:33
 */
@WebListener
public class Notificator implements ServletContextListener {

    private ScheduledExecutorService substitutionsScheduler;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        substitutionsScheduler = Executors.newSingleThreadScheduledExecutor();
        substitutionsScheduler.scheduleAtFixedRate(new SubstitutionNotificator(), 0, 1, TimeUnit.MINUTES);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        substitutionsScheduler.shutdown();

    }
}
