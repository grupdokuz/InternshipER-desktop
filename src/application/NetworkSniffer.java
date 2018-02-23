
package application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

public class NetworkSniffer {
        

	public static void listenNetwork() {
		List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
		StringBuilder errbuf = new StringBuilder(); // For any error msgs

		/***************************************************************************
		 * First get a list of devices on this system
		 **************************************************************************/
		int r = Pcap.findAllDevs(alldevs, errbuf);
		if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
			System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
			return;
		}

		System.out.println("Network devices found:");
                ExecutorService executorService = Executors.newFixedThreadPool(alldevs.size());
		int i = 0;
		for (PcapIf device : alldevs) {
			String description = (device.getDescription() != null) ? device.getDescription()
					: "No description available";
			System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
                        
                        //executorService.submit(FaceDetection::detectFaces);
                        executorService.submit(new MyRunnable(device));
		}
	}
}