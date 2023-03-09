package custom_jfx_plugin.dependency.maven.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MavenArtifact implements Serializable {

	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/

	/**
	 * The artifact id
	 */
	@SerializedName("id")
	private String id;

	/**
	 * The artifact group
	 */
	@SerializedName("g")
	private String group;

	/**
	 * The artifact name
	 */
	@SerializedName("a")
	private String artifact;

	/**
	 * The artifact version
	 */
	@SerializedName("v")
	private String version;

	/**
	 * The artifact file type
	 */
	@SerializedName("p")
	private String type;

	/**
	 * The artifact creation timestamp
	 */
	@SerializedName("timestamp")
	private long timestamp;

	/**
	 * The artifact files
	 */
	@SerializedName("ec")
	private String[] artifactFiles;

	/**
	 * The artifact tags
	 */
	@SerializedName("tags")
	private String[] tags;

	/* -----------------------------------------------------------------------
	 * Methods
	 * -----------------------------------------------------------------------*/

	public String id() {
		return id;
	}

	public String group() {
		return group;
	}

	public String artifact() {
		return artifact;
	}

	public String version() {
		return version;
	}

	public String type() {
		return type;
	}

	public long timestamp() {
		return timestamp;
	}

	public String[] artifactFiles() {
		return artifactFiles;
	}

	public String[] tags() {
		return tags;
	}

}
