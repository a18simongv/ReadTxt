package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {

		Scanner keyboard = new Scanner(System.in);

		try (DatagramSocket server = new DatagramSocket(1501)) {

			String file="";
			
			System.out.print("Write the file name: ");
			file= keyboard.nextLine();
			System.out.println();
			
			DatagramPacket packet = new DatagramPacket(file.getBytes("UTF-8"), file.length());
			packet.setPort(1500);
			packet.setAddress(InetAddress.getByName("127.0.0.1"));
			server.send(packet);

			DatagramPacket packetRec = new DatagramPacket(new byte[1024], 1024);
			server.receive(packetRec);
			System.out.println(new String(packetRec.getData(), 0, packetRec.getLength()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		keyboard.close();

	}

}
