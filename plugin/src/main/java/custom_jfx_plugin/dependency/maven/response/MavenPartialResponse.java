package custom_jfx_plugin.dependency.maven.response;

import com.google.gson.annotations.SerializedName;

public class MavenPartialResponse {
	
	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Total of artifacts
	 */
	@SerializedName("numFound")
	private int found;
	
	/**
	 * The index with the first artifact
	 */
	@SerializedName("start")
	private int start;
	
	/**
	 * All artifacts content
	 */
	@SerializedName("docs")
	private MavenArtifact[] content;
	
	/* -----------------------------------------------------------------------
	 * Methods
	 * -----------------------------------------------------------------------*/
	
	public int found() {
		return found;
	}
	
	public int start() {
		return start;
	}
	
	public MavenArtifact[] content() {
		return content;
	}
	
}
