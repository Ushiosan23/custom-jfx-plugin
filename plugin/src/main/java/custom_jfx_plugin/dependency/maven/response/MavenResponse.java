package custom_jfx_plugin.dependency.maven.response;

import com.google.gson.annotations.SerializedName;

public class MavenResponse {
	
	/**
	 * Maven response header
	 */
	@SerializedName("responseHeader")
	private MavenHeader header;
	
	/**
	 * Maven response content
	 */
	@SerializedName("response")
	private MavenPartialResponse response;
	
	/* -----------------------------------------------------------------------
	 * Methods
	 * -----------------------------------------------------------------------*/
	
	public MavenHeader header() {
		return header;
	}
	
	public MavenPartialResponse response() {
		return response;
	}
	
}
