package br.ufpe.cin.gfads.main;

import java.util.LinkedList;
import java.util.List;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.swarm.Placement;
import com.spotify.docker.client.messages.swarm.Service;
import com.spotify.docker.client.messages.swarm.ServiceSpec;

import br.ufpe.cin.gfads.cluster.ClusterConnection;
import br.ufpe.cin.gfads.docker.DockerController;
import br.ufpe.cin.gfads.experiment.SettleTuple;
import br.ufpe.cin.gfads.experiment.Tuple;

/**
 * 
 * @author Marcos Vinicius de Araújo Andrade
 * This is the main class with the code to make CoMMAR works and make the experiment
 */

public class DockerApp {
	public static void main(String[] args) throws DockerException, InterruptedException {
		DockerClient docker = ClusterConnection.connGFADS();
		DockerController control = new DockerController(docker);
		String servicesName [] = new String[5];
		Service service;

		servicesName[0] = "users1";
		servicesName[1] = "users2";
		servicesName[2] = "users3";
		servicesName[3] = "users4";
		servicesName[4] = "users5";

		SettleTuple settleTuple ;
		long start, finish;
		int line = 0;

		//setar outro for depois com o n sendo o tamanho da amostra
		int n = 50;
		//gere k amostras
		settleTuple = new SettleTuple("docker-user-replicas3.csv");
		//faça n moves
		for(int index = 0 ; index < n ; index++) {			
			//
			for(int j = 0; j < servicesName.length; j++) {
				String serviceName = servicesName[j];

				start = System.nanoTime();
				service = control.getService(serviceName);
				Placement place = control.chooseHosts(control.chooseNewDestination(service), serviceName);
				ServiceSpec spec = control.buildService(place, serviceName);
				docker.updateService(service.id(), service.version().index(), spec);
				finish = System.nanoTime() - start;
				settleTuple.addTuple(new Tuple("Docker Swarm", 1, index, finish/1000000));

				System.out.println("service name = " + serviceName + "\n"+ "TTM = " 
						+ finish/1000000 + "\n" +"line = " + line +"\n"+ "--------------------");

				Thread.sleep(5000);
				control = new DockerController(docker);
				line++;
			}	
		}
		settleTuple.flushCSV();

		System.out.println("terminou");
		docker.close();


	}


}
