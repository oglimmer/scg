package de.oglimmer.scg.email;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import lombok.SneakyThrows;
import lombok.Synchronized;
import de.oglimmer.scg.web.ScgProperties;

/**
 * This class works beyond classloader boundaries. So only one instance of ImapProcessorManager in the JVM can be the
 * master, all others (in other classloaders) are master=false
 * 
 * @author oli
 */
public enum ImapProcessorObserver {
	INSTANCE;

	private static final String MBEAN_NAME = "de.oglimmer.scg:type=ImapProcessor";

	private boolean isThisClassLoaderMaster;
	private Thread watchThread;
	private MBeanCheck mbeanCheck = new MBeanCheck();

	private ImapProcessorObserver() {
		if (ScgProperties.INSTANCE.getImapEnabled()) {
			startWatchThread();
		}
	}

	@Synchronized
	private void startWatchThread() {
		watchThread = new Thread(new WatcherThread(), "ImapProcessorObserver-" + Math.random());
		watchThread.start();
	}

	@Synchronized
	private void stopWatchThread() {
		if (watchThread != null) {
			watchThread.interrupt();
			watchThread = null;
		}
	}

	@SneakyThrows(value = { InterruptedException.class })
	public void stop() {
		if (isThisClassLoaderMaster) {
			mbeanCheck.unregisterMBean();
			ImapProcessor.INSTANCE.stopThread();
		}
		stopWatchThread();
		TimeUnit.SECONDS.sleep(1);
	}

	private boolean isThisClassLoaderMaster() {
		if (!isThisClassLoaderMaster && mbeanCheck.isThisClassloaderRegisteredAsMaster()) {
			isThisClassLoaderMaster = true;
		}
		return isThisClassLoaderMaster;
	}

	/**
	 * Check an MBean if in this JVM a master is already running
	 */
	static class MBeanCheck {

		private ObjectName objectName;
		private MBeanServer mbeanServer;

		@SneakyThrows(value = MalformedObjectNameException.class)
		public MBeanCheck() {
			objectName = new javax.management.ObjectName(MBEAN_NAME);
			mbeanServer = ManagementFactory.getPlatformMBeanServer();
		}

		@SneakyThrows(value = { NotCompliantMBeanException.class, InstanceAlreadyExistsException.class,
				MBeanRegistrationException.class })
		boolean isThisClassloaderRegisteredAsMaster() {
			boolean registeredAsMaster = false;
			synchronized (Object.class) {
				if (!isMBeanRegistered()) {
					registerMBean();
					registeredAsMaster = true;
				}
			}
			return registeredAsMaster;
		}

		private void registerMBean() throws NotCompliantMBeanException, InstanceAlreadyExistsException,
				MBeanRegistrationException {
			StandardMBean standardmbean = new StandardMBean(new DummyCls(), IDummy.class);
			mbeanServer.registerMBean(standardmbean, objectName);
		}

		private boolean isMBeanRegistered() {
			return mbeanServer.isRegistered(objectName);
		}

		@SneakyThrows(value = { InstanceNotFoundException.class, MBeanRegistrationException.class })
		void unregisterMBean() {
			mbeanServer.unregisterMBean(objectName);
		}

		interface IDummy {
			// no code
		}

		class DummyCls implements IDummy {
			// no code
		}
	}

	/**
	 * If this classloader isn't a master, run this thread to check if it should become one
	 */
	class WatcherThread implements Runnable {

		@Override
		public void run() {
			while (watchThread != null) {
				if (isThisClassLoaderMaster()) {
					actAsMasterOnThisClassloader();
				}
				sleep();
			}
		}

		private void actAsMasterOnThisClassloader() {
			ImapProcessor.INSTANCE.startThreadInitImap();
			stopWatchThread();
		}

		private void sleep() {
			try {
				if (watchThread != null) {
					TimeUnit.SECONDS.sleep(15);
				}
			} catch (InterruptedException e) {
				// don't care about this
			}
		}
	}
}
