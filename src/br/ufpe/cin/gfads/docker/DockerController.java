package br.ufpe.cin.gfads.docker;

import java.util.LinkedList;
import java.util.List;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.swarm.Node;
import com.spotify.docker.client.messages.swarm.Placement;
import com.spotify.docker.client.messages.swarm.Service;
import com.spotify.docker.client.messages.swarm.ServiceSpec;


import com.spotify.docker.client.messages.swarm.TaskSpec;

public class DockerController {

	private DockerClient docker;
	private List<String> constraints = new LinkedList<String>();

	public DockerController(DockerClient docker) {
		this.docker = docker;
	}

	/**
	 * this method return the service if there is a service with the name passed in the parameter
	 * @param data.docker is the client connection with the docker 
	 * @param serviceName is the name of the service
	 * @return the service. Or null, if the service do not exist
	 * @throws DockerException
	 * @throws InterruptedException
	 */
	public Service getService(String serviceName) throws DockerException, InterruptedException {
		List<Service> list = docker.listServices();
		for (Service service : list) {
			if(service.spec().name().equals(serviceName))
				return service;
		}
		return null;
	}

	public Node getNode(String hostname) throws DockerException, InterruptedException {
		List<Node> nodes = docker.listNodes();
		for (Node node : nodes) 
			if(node.description().hostname().equals(hostname))
				return node;
		return null;

	}

	public ServiceSpec buildService(Placement place , String serviceName) throws DockerException, InterruptedException {
		Service service = this.getService(serviceName);
		if(service == null)
			return null;
		return ServiceSpec.builder()
				.labels(service.spec().labels())
				.name(service.spec().name())
				//.taskTemplate(service.spec().taskTemplate())
				.taskTemplate(getTaskSpec(place, service))
				.mode(service.spec().mode())
				.build();

	}
	
	private TaskSpec getTaskSpec(Placement place, Service service) {
		return TaskSpec.builder()
				.placement(place)
				.networks(service.spec().networks())
				.containerSpec(service.spec().taskTemplate().containerSpec())
				.build();
	}

	/**
	 * this method is to test the constraint of ==
	 * @param destinationNodes
	 * @param serviceName
	 * @return
	 * @throws DockerException
	 * @throws InterruptedException
	 */
	public Placement chooseHosts2(List<String> destinationNodes, String serviceName) throws DockerException, InterruptedException {
		Service service = this.getService(serviceName);
		if(service == null)
			return null;
		for (String dest : destinationNodes) {
			this.constraints.add("node.hostname == " + dest);
		} 		
		if(!this.constraints.isEmpty())
			//return service.spec().taskTemplate().placement().create(constraints);
			return Placement.create(constraints);
		else
			return null;
	}

	
	public Placement chooseHosts(List<String> destinationNodes, String serviceName) throws DockerException, InterruptedException {
		Service service = this.getService(serviceName);
		if(service == null)
			return null;
		List<String> denyNodes = this.denyNodes(destinationNodes);
		for (String deniedNode: denyNodes) 
			this.constraints.add("node.hostname != " + deniedNode);

		if(!this.constraints.isEmpty())
			//return service.spec().taskTemplate().placement().create(constraints);
			return Placement.create(constraints);
		else
			return null;
	}

	

	/**
	 * this method is used to set constraints using "!=". It gets the nodes the user wants and 
	 * deny all the user do not want
	 * @param destinationNodes
	 * @return
	 * @throws DockerException
	 * @throws InterruptedException
	 */
	private List<String> denyNodes(List<String> destinationNodes) throws DockerException, InterruptedException {
		List<String> nodesNotWanted = new LinkedList<String>();		
		for (Node node : this.docker.listNodes()) 
			if(!destinationNodes.contains(node.description().hostname())) {
				nodesNotWanted.add(node.description().hostname());
				//System.out.println("não adicione: " + node.description().hostname());
			}		
		return nodesNotWanted;
	}

	public List<String> chooseNewDestination(Service service){
		List<String> destinations = new LinkedList<>();
		if(service.spec().taskTemplate().placement() == null) {
			destinations.add("swarm-3");
			return destinations;
		}
		//the constrains is set to deny the nodes that contains this node names
		if(service.spec().taskTemplate().placement().toString().contains("swarm6")) 
			destinations.add("swarm6");
		if(service.spec().taskTemplate().placement().toString().contains("swarm-2"))
			destinations.add("swarm-2");
		//if it got two nodes to deploy....
		if(destinations.size() == 2)
			return destinations;
		if (service.spec().taskTemplate().placement().toString().contains("swarm-3"))
			destinations.add("swarm-3");


		return destinations;
	}
	
	/**
	 * method to test the == constraint
	 * @param service
	 * @return
	 */
	public List<String> chooseNewDestination2(Service service){
		List<String> destinations = new LinkedList<>();
		if(service.spec().taskTemplate().placement() == null) {
			destinations.add("swarm-3");
			return destinations;
		}
		//the constrains is set to deny the nodes that contains this node names
		if(service.spec().taskTemplate().placement().toString().contains("swarm6")) 
			destinations.add("swarm-3");
		if(service.spec().taskTemplate().placement().toString().contains("swarm-2"))
			destinations.add("swarm6");
		//if it got two nodes to deploy....
		if(destinations.size() == 2)
			return destinations;
		if (service.spec().taskTemplate().placement().toString().contains("swarm-3"))
			destinations.add("swarm6");


		return destinations;
	}


}
