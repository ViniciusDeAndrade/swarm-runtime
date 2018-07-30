package br.ufpe.cin.gfads.moviment;

/**
 * this class was developed by Adalberto and it represents the moviment of k8 objects
 * among nodes
 * @author Adalberto Jr.
 *
 */
public class Moviment {
	/**
	 * application = cluster virtual name
	 */
	public final String namespace;
	/**
	 * service name
	 */
	public final String service;
	/**
	 * the actual host/node
	 */
	public final String hostSource;
	/**
	 * the node/host you want to move on
	 */
	public final String hostDestination;
	
	/**
	 * 
	 * @param namespace is the cluster virtual name
	 * @param service is the name of the service
	 * @param hostSource is the actual host/node
	 * @param hostDestination is the node/host you want to move on
	 */
	private Moviment(String namespace, String service, String hostSource, String hostDestination) {
		super();
		this.namespace = namespace;
		this.service = service;
		this.hostSource = hostSource;
		this.hostDestination = hostDestination;
	}
	/**
	 * this method just create a new object of moviment
	 * @param namespace is the virtual cluster name
	 * @param service is the application/pod name
	 * @param hostSource is the actual host/node of the application/pod
	 * @param hostDestination is the node/host you want to move to
	 * @return a new moviment object
	 */
	public static Moviment create(String namespace, String service, String hostSource, String hostDestination) {
		return new Moviment(namespace, service, hostSource, hostDestination);
	}

	public String getNamespace() {
		return namespace;
	}

	public String getService() {
		return service;
	}

	public String getHostSource() {
		return hostSource;
	}

	public String getHostDestination() {
		return hostDestination;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		result = prime * result + ((hostDestination == null) ? 0 : hostDestination.hashCode());
		result = prime * result + ((hostSource == null) ? 0 : hostSource.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Moviment other = (Moviment) obj;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		if (hostDestination == null) {
			if (other.hostDestination != null)
				return false;
		} else if (!hostDestination.equals(other.hostDestination))
			return false;
		if (hostSource == null) {
			if (other.hostSource != null)
				return false;
		} else if (!hostSource.equals(other.hostSource))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return namespace + "." + service + " :: " + hostSource + " --> " + hostDestination;
	}
	
}