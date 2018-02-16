package io.egia.mqi.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "t_server")
public class Server implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="server_id") private Long serverId;
	@Column(name="server_name") private String serverName;	
	@Column(name="server_port") private String serverPort;
	@Column(name="server_type") private String serverType;
	@Column(name="server_version") private String serverVersion;
	@Column(name="chunk_size") private int chunkSize;
	@Column(name="last_updated", insertable=false) private Date lastUpdated;
	
	public Long getServerId() {
		return serverId;
	}
	
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}
	
	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}
}
