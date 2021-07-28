package br.com.alura.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class TasksDistribution implements Runnable{

	private Socket socket;
	private TasksServer server;
	private ExecutorService threadPool;

	public TasksDistribution(ExecutorService threadPool, Socket socket, TasksServer server) {
		this.threadPool = threadPool;
		this.socket = socket;
		this.server = server;
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
					CommandC1 c1 = new CommandC1(output);
					threadPool.execute(c1);
					break;
				}
				case "c2": {
					output.println("Confirmação comando C2");
					CommandC2 c2 = new CommandC2(output);
					threadPool.execute(c2);
					break;
				}
				case "shutdown": {
					output.println("Desligando o servidor!");
					server.shutdown();
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
