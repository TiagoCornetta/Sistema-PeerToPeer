
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java. util. Scanner;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Peer {
	public static void main(String[] args) throws Exception {
		Peer externa = new Peer();
		
		//Inicializando o scanner para pegar as informações do cliente
		Scanner scan = new Scanner(System.in);
		//Lista para guardar os nomes dos arquivos iniciais
		List<String> lista = new ArrayList<>();
		
		//Estabelecendo conexão com o servidor via RMI
		Registry reg = LocateRegistry.getRegistry();
		IntMetServidor shc = (IntMetServidor) reg.lookup("rmi://127.0.0.1/servicoServ");
		
		//IP;Porta;Path;
		int porta = 0;
		String path = null;
		String IP = null ;
		String arqBuscado = null;
		trataPeer trataTcp = null;
					
		
		int aux = 0;
		while(aux != 4) {
			System.out.println("Qual opcao deseja executar?");
			System.out.println("1 - Request_Join");
			System.out.println("2 - Request_Search");
			System.out.println("3 - Request_Download");
			System.out.println("4 - Finalizar Programa");
			System.out.println("Digite sua resposta:");
			aux = scan.nextInt(); scan.nextLine();
			
			//Ira fazer a Request_Join
			if(aux == 1) {
				
				//Sera definido cada uma das variáveis
				System.out.println("Digite o IP do Client:");
				IP = scan.nextLine();
				System.out.println("Digite a Porta do Client:");
				porta = scan.nextInt();scan.nextLine();	
				System.out.println("Digite a Pasta do Client:");
				path = scan.nextLine();
				
				//Função para pegar os nomes dos arquivos nos diretórios
				File file = new File(path);
				File [] arquivos = file.listFiles();
				
				for (File arquivo : arquivos) {
					String nome = new String(arquivo.getName());
					lista.add(nome);
				}
				
				
				//PEER IRA REQUISITAR ENTRADA NO BANCO DE DADOS DO SERVIDOR ATRAVES DO PROTOCOLO RMI
				String res = shc.requisicaoJoin(IP,porta,lista);
				//System.out.println(res);
				
				//Apos receber o Join_OK o Peer ira se apresentar
				System.out.print("Sou peer ["+ IP + "]:" + porta + " adicionados com arquivos[ ");		
				for(int i = 0; i < lista.size(); i++) {
					System.out.print("'" +lista.get(i)+ "'" + " ");
				}
				System.out.print("]");
				System.out.println("");
				
				//Parte para inicializar 
				 trataTcp = externa.new trataPeer(porta,IP,path);
				 trataTcp.start();
				
			}
			
			//Ira fazer a Request_Search
			else if(aux == 2){
				//PEER IRA REQUISITAR UMA BUSCA NO BANCO DE DADOS NO SERVIDOR
				System.out.println("Qual servico deseja encontrar?");
				String servicoBuscado = scan.nextLine();
				arqBuscado = servicoBuscado;
				List<Servico> servicosEncontrados = shc.requisicaoSearch(IP, porta, servicoBuscado);
				
				//Sera apresentado a lista de PEERS com o arquivo buscado
				System.out.println("Peers com arquivo solicitado:");
				for(int i = 0; i < servicosEncontrados.size(); i++) {
					System.out.println("IP:" + servicosEncontrados.get(i).IP +" Porta:"+ servicosEncontrados.get(i).porta);
				}
			}
			
			//Ira fazer o Resquest_Donwload
			else if(aux == 3) {
				//Ira realizar a captura do IP e porta que deve ser buscado
				int portaAux;
				String ipAux;
				System.out.println("Digite o Ip do peer que deseja conectar:");
				ipAux = scan.nextLine();
				System.out.println("Digite a porta desse peer:");
				portaAux = scan.nextInt(); scan.nextLine();
				
				//Ira criar um socket com o IP da maquina entre a porta 1024 a 65535
				Socket s = new Socket("127.0.0.1", portaAux);
				
				
				// Envia o arquivo que devera acontecer o download
	            OutputStream out = s.getOutputStream();
	            DataOutputStream writer = new DataOutputStream(out);
	            writer.writeUTF(arqBuscado);
	            
	            // Prepara para receber o arquivo do peer de destino
	            InputStream in = s.getInputStream();
	            
	            FileOutputStream fr =new FileOutputStream(path +"/" + arqBuscado);
	            
	         // Recebe o arquivo e escreve no disco
	            byte[] b = new byte[4096];
	            int leitor;
	            while ((leitor = in.read(b)) != -1) {
	                fr.write(b, 0, leitor);
	            }
	            
	            writer.close();
	            fr.close();
	            in.close();
	            out.close();
	            s.close();
	            
			
				String res = shc.requisicaoUpdate(IP, porta, arqBuscado);
				System.out.println("Arquivo " + arqBuscado + " baixo com sucesso na pasta:" + path);
				
				
			}
			//Ira finalizar o programa
			else if(aux == 4) {
				System.out.println("PROGRAMA FINALIZADO! OBRIGADO E VOLTE SEMPRE! <3");
				System.exit(0);
			}
			else {
				System.out.println("PorFavor digite um numero valido");
				
			}
			System.out.println();
			
		}
		
		
	}
	
	public class trataPeer extends Thread{
		private int porta;
		ServerSocket serverSocket=  null;
		String path;
		
		public trataPeer(int porta,String IP,String path) {
			this.porta = porta;
			this.path = path;
			try {
				this.serverSocket = new ServerSocket(porta);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void run() {
			while(true) {
			
					Socket no = null;
					try {
						no = serverSocket.accept();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					//Leitura de informações
					
					try {
						//Pegando o nome do arquivo
						InputStream is = no.getInputStream();
						DataInputStream reader = new DataInputStream(is);
						String arq = reader.readUTF();
						
						//Abrindo o Arquivo
						File file = new File(path +"/" + arq);
						FileInputStream fr = new FileInputStream(path +"/" + arq);
						
						//Preparando para enviar ao Peer
						OutputStream writer = no.getOutputStream();
						byte[] buffer = new byte[4096];
						int leitorBytes;
						while((leitorBytes = fr.read(buffer)) != -1) {
							writer.write(buffer,0,leitorBytes);
						}
						
						reader.close();
						writer.close();
						is.close();
						fr.close();
						
						
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};
					
			

			}
			
			
			
		}
		
	}
	
	
	
}
