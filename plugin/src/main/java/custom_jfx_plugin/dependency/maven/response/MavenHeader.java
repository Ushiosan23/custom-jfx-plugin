package custom_jfx_plugin.dependency.maven.response;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MavenHeader {
	
	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Maven response header status
	 */
	@SerializedName("status")
	private int status;
	
	/**
	 * Maven response header elapsed time
	 */
	@SerializedName("QTime")
	private long time;
	
	/**
	 * Maven request header params
	 */
	@SerializedName("params")
	private Map<String, String> params;
	
	/* -----------------------------------------------------------------------
	 * Methods
	 * -----------------------------------------------------------------------*/
	
	public int status() {
		return status;
	}
	
	public long time() {
		return time;
	}
	
	public Map<String, String> params() {
		return params;
	}
	
}
