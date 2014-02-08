package de.oglimmer.scg.email;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import lombok.SneakyThrows;
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

	private boolean isMaster;
	private boolean running;
	private Thread thread;

	private ImapProcessorObserver() {
		if (ScgProperties.INSTANCE.getImapEnabled()) {
			running = true;
			thread = new Thread(new MasterWatchdog(), "ImapProcessorObserver-" + Math.random());
			thread.start();
		}
	}

	@SneakyThrows(value = { InterruptedException.class, InstanceNotFoundException.class,
			MBeanRegistrationException.class, MalformedObjectNameException.class })
	public void stop() {
		if (isMaster) {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName name = new ObjectName(MBEAN_NAME);
			mbs.unregisterMBean(name);
			ImapProcessor.INSTANCE.stop();
		}
		running = false;
		synchronized (thread) {
			if (thread != null) {
				thread.interrupt();
			}
		}
		TimeUnit.SECONDS.sleep(1);
	}

	@SneakyThrows
	private boolean isMaster() {
		if (isMaster) {
			return true;
		}
		ObjectName oname = new javax.management.ObjectName(MBEAN_NAME);
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		synchronized (Object.class) {
			if (!mbs.isRegistered(oname)) {
				IDummy mbean = new DummyCls();
				StandardMBean standardmbean = new StandardMBean(mbean, IDummy.class);
				mbs.registerMBean(standardmbean, oname);
				isMaster = true;
			}
		}
		return isMaster;
	}

	interface IDummy {
		// no code
	}

	class DummyCls implements IDummy {
		// no code
	}

	class MasterWatchdog implements Runnable {

		@Override
		public void run() {
			running = true;
			while (running) {
				if (isMaster()) {
					ImapProcessor.INSTANCE.init();
					running = false;
				}
				sleep();
			}
			synchronized (thread) {
				thread = null;
			}
		}

		private void sleep() {
			try {
				if (running) {
					TimeUnit.SECONDS.sleep(15);
				}
			} catch (InterruptedException e) {
				// don't care about this
			}
		}
	}
}
