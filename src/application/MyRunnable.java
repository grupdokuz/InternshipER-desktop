/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.concurrent.Callable;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.PcapHandler;
import org.jnetpcap.PcapIf;

/**
 *
 * @author User
 */
public class MyRunnable implements Callable {
            PcapIf device;
           public MyRunnable(PcapIf device) {
               this.device=device;
           }

            @Override
           public Integer call() throws Exception {
               StringBuilder errbuf = new StringBuilder(); // For any error msgs

		int snaplen = 64 * 1024; // Capture all packets, no trucation
		int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
		int timeout = 10 * 1000; // 10 seconds in millis
		
		Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
		if (pcap == null) {
			System.err.printf("Error while opening device for capture: " + errbuf.toString());
			return -1;
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
			return -1;
		}
		if (pcap.setFilter(filter) != Pcap.OK) {
			System.err.println(pcap.getErr());
			return -1;
		}
		
		String dirname="network";
		File dir = new File(dirname);
		dir.mkdir();
		Date now=new Date();
		String ofile = "network\\"+now.getDate()+now.getHours()+now.getMinutes()+now.getSeconds()+device.getDescription()+".cap";
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
                return 0;
           }
        }
