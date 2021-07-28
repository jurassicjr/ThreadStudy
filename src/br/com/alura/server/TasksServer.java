package br.com.alura.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class TasksServer {

	private static Socket socket;
	private ServerSocket server;
	private ExecutorService threadPool;
	private AtomicBoolean isRunning;

	public TasksServer() throws IOException {
		System.out.println("---- Starting the server ----");
		this.server = new ServerSocket(11011);
		this.threadPool = Executors.newCachedThreadPool(new ThreadsFactory());
		this.isRunning = new AtomicBoolean(true);
	}

	public void run() throws IOException {
		while (this.isRunning.get()) {
			try {
				socket = server.accept();
				System.out.println("--- Accepting new client at port - " + socket.getPort());

				TasksDistribution tasksDistribution = new TasksDistribution(threadPool, socket, this);
				threadPool.execute(tasksDistribution);
			} catch (SocketException e) {
				System.out.println("socket exception, is running? " + this.isRunning);
			}
		}
	}
	
	public void shutdown() throws IOException {
		this.isRunning.set(false);
		server.close();
		threadPool.shutdown();
	}

	public static void main(String[] args) throws IOException {
		TasksServer server = new TasksServer();
		server.run();
		server.shutdown();
	}
}
