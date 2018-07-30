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

public class TesteMove {

	public static void main(String[] args) throws DockerException, InterruptedException {
		DockerClient docker = ClusterConnection.connGFADS();
		DockerController control = new DockerController(docker);
		Service service = control.getService("nginx-test");
		System.out.println(service.spec().toString());
		
		List<String> destinationNodes = new LinkedList<String> ();
		destinationNodes.add("swarm6");
		Placement place = control.chooseHosts(destinationNodes, service.spec().name());
		ServiceSpec spec = control.buildService(place, service.spec().name());
		docker.updateService(service.id(), service.version().index(), spec);
		System.out.println("done");
		docker.close();
	}
}
