package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {

	public static void main(String[] args) {

		try (DatagramSocket server = new DatagramSocket(1500)) {
			
			System.out.println("Listening port 1500");

			while (true) {
				DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
				server.receive(packet);
				Action action = new Action(packet, server);
				action.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

class Action extends Thread {

	private DatagramPacket packet;
	private DatagramSocket server;

	public Action(DatagramPacket packet, DatagramSocket server) {
		this.packet = packet;
		this.server = server;
	}

	@Override
	public void run() {
		
		System.out.println("Client connected");

		String fileName = new String(packet.getData(), 0, packet.getLength());
		File file = new File("serverFiles/" + fileName);
		
		System.out.println("\tClient wants to read: " + fileName);

		String message = "", correct = "";

		if (file.exists()) {

			// read file
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {

				String linea = "";
				while ((linea = br.readLine()) != null) {
					message += linea + "\n";
				}
				
				correct = "\tFile sent correctly";

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			message = "Error: File doesn't exists";
			correct = "\t" + message;
		}

		DatagramPacket packSend = new DatagramPacket(message.getBytes(), message.length(), packet.getAddress(),
				packet.getPort());
		try {
			server.send(packSend);
			System.out.println(correct);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Client disconnected");
	}

}
