package br.ufpe.cin.gfads.cluster;

import java.net.URI;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;

public class ClusterConnection {

	public static DockerClient connGFADS() {
		DockerClient docker = null;		
		docker = DefaultDockerClient.builder().uri(URI.create("http://10.66.66.24:2376")).build();
		return docker;
		
	}
	
	public static DockerClient connLocal() {
		DockerClient docker = null;		
		try {
			docker = DefaultDockerClient.fromEnv().build();
		} catch (DockerCertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docker;
		
	}
	
}
