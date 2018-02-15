
package application;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.PcapHandler;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

public class NetworkSniffer {

	/**
	 * Main startup method
	 * 
	 * @param args
	 *            ignored
	 */
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

		int i = 0;
		for (PcapIf device : alldevs) {
			String description = (device.getDescription() != null) ? device.getDescription()
					: "No description available";
			System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
		}

		System.out.println(new Date());
		PcapIf device = alldevs.get(2); // We know we have atleast 1 device
		System.out.printf("\nChoosing '%s' on your behalf:\n",
				(device.getDescription() != null) ? device.getDescription() : device.getName());

		/***************************************************************************
		 * Second we open up the selected device
		 **************************************************************************/
		int snaplen = 64 * 1024; // Capture all packets, no trucation
		int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
		int timeout = 10 * 1000; // 10 seconds in millis
		
		Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

		if (pcap == null) {
			System.err.printf("Error while opening device for capture: " + errbuf.toString());
			return;
		}
		/**
		 * Filterin change this step
		 */
		PcapBpfProgram filter = new PcapBpfProgram();
		String expression = "tcp and src ! 185.22.184.240 and dst ! 185.22.184.240";
		int optimize = 0; // 0 = false
		int netmask = 0xFFFFFF00; // 255.255.255.0
		if (pcap.compile(filter, expression, optimize, netmask) != Pcap.OK) {
			System.err.println(pcap.getErr());
			return;
		}
		if (pcap.setFilter(filter) != Pcap.OK) {
			System.err.println(pcap.getErr());
			return;
		}
		
		String dirname="network";
		File dir = new File(dirname);
		dir.mkdir();
		Date now=new Date();
		String ofile = "network\\"+now.getDate()+now.getHours()+now.getMinutes()+now.getSeconds()+".cap";
		PcapDumper dumper = pcap.dumpOpen(ofile); // output file


		/***************************************************************************
		 * Third we create a packet handler which will receive packets from the libpcap
		 * loop.
		 **************************************************************************/
		PcapHandler<PcapDumper> dumpHandler = new PcapHandler<PcapDumper>() {

			public void nextPacket(PcapDumper dumper, long seconds, int useconds, int caplen, int len,
					ByteBuffer buffer) {
				dumper.dump(seconds, useconds, caplen, len, buffer);
			}
		};

		/***************************************************************************
		 * Fourth we enter the loop and tell it to capture 10 packets. The loop method
		 * does a mapping of pcap.datalink() DLT value to JProtocol ID, which is needed
		 * by JScanner. The scanner scans the packet buffer and decodes the headers. The
		 * mapping is done automatically, although a variation on the loop method exists
		 * that allows the programmer to sepecify exactly which protocol ID to use as
		 * the data link type for this pcap interface.
		 **************************************************************************/
		pcap.loop(Integer.MAX_VALUE, dumpHandler, dumper);

		/***************************************************************************
		 * Last thing to do is close the pcap handle
		 **************************************************************************/
		dumper.close();
		pcap.close();
	}
}