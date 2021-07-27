package br.com.alura.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class TasksDistribution implements Runnable{

	private Socket socket;

	public TasksDistribution(Socket socket) {
		this.socket = socket;
		
	}

	@Override
	public void run() {
		
		System.out.println("Distributing tasks");
		
		try {
			Scanner clientInput = new Scanner(socket.getInputStream());
			
			PrintStream output = new PrintStream(socket.getOutputStream());
			
			while(clientInput.hasNextLine()) {
				String command = clientInput.nextLine();
				
				System.out.println("Executando comando " + command);
				switch (command) {
				case "c1": {
					output.println("Confirmação comando C1");
					break;
				}
				case "c2": {
					output.println("Confirmação comando C2");
					break;
				}
				default:
					output.println("Comando não encontrado");
				}
				
				System.out.println(command);
			}
			
			output.close();
			clientInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
