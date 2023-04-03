package custom_jfx_plugin.dependency.maven;

import custom_jfx_plugin.dependency.base.Resolver;
import custom_jfx_plugin.dependency.maven.response.MavenArtifact;
import custom_jfx_plugin.dependency.maven.response.MavenResponse;
import custom_jfx_plugin.utils.Msg;
import org.jetbrains.annotations.NotNull;
import ushiosan.jvm_utilities.lang.Obj;
import ushiosan.jvm_utilities.lang.collection.Collections;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MavenResolver extends Resolver<String, String> {
	
	/**
	 * This class cannot be instantiated
	 */
	private MavenResolver() {}
	
	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/
	
	
	/**
	 * Resolver instance
	 */
	private static MavenResolver INSTANCE;
	
	
	/**
	 * Maven API checker
	 */
	private static final String API_ENTRY =
		"https://search.maven.org/solrsearch/select?q=g:%s+AND+a:%s&core=gav&rows=%d&wt=%s";
	
	/**
	 * Regular expression used to check if artifact special notation is valid
	 */
	private static final Pattern CONFIGURATION_CHECKER =
		Pattern.compile("#(.+)#");
	
	/**
	 * Regular expression to detect valid latest version
	 */
	private static final Pattern VALID_LATEST_VERSION =
		Pattern.compile("^(\\d+\\.)*(\\*|\\d+)$");
	
	/**
	 * Regular expression to detect valid early version
	 */
	private static final Pattern VALID_EARLY_VERSION =
		Pattern.compile("^(\\d+\\.)*(\\d+-ea\\+\\d+)$");
	
	/**
	 * Dependency cache container
	 */
	private final List<DependencyCache> dependencyCache = Collections.mutableListOf();
	
	/* -----------------------------------------------------------------------
	 * Methods
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Resolve dependency configuration
	 *
	 * @param configuration configuration content
	 * @param silent        don't show user information
	 * @return dependency result
	 */
	@Override
	public @NotNull String resolveDependency(@NotNull String configuration, boolean silent) {
		// Split configuration in multiples elements
		String[] elements = configuration.split(":");
		if (elements.length < 3 || elements.length > 4) return configuration;
		
		// Validate if configuration contains special configuration
		Matcher matcher = CONFIGURATION_CHECKER.matcher(configuration);
		if (!matcher.find()) return configuration;
		
		// Store special configuration
		String specialConfig = matcher.group(1);
		Optional<DependencyCache> cacheFound = dependencyCache.stream()
			.filter(it -> it.configuration.equals(configuration) && it.special.equals(specialConfig))
			.findAny();
		
		// Returns the cached result if exists
		if (cacheFound.isPresent()) return cacheFound.get().resolved;
		
		// Http artifact request
		String resolvedArtifact;
		switch (specialConfig) {
			case "latest":
				resolvedArtifact = resolveHttpArtifact(elements[0], elements[1], VALID_LATEST_VERSION);
				break;
			case "early":
				resolvedArtifact = resolveHttpArtifact(elements[0], elements[1], VALID_EARLY_VERSION);
				break;
			default:
				throw new IllegalStateException("Invalid version configuration");
		}
		
		// Generate real valid artifact configuration
		String realArtifact = configuration.substring(0, matcher.start()) + resolvedArtifact;
		
		// Attach resolved artifact to cached elements
		if (!silent) Msg.info("Resolved dependency (%s) %s", specialConfig, realArtifact);
		dependencyCache.add(new DependencyCache(configuration, realArtifact, specialConfig));
		
		return realArtifact;
	}
	
	/**
	 * Get object instance
	 *
	 * @return maven resolver instance
	 */
	public static Resolver<String, String> getInstance() {
		if (Obj.isNull(INSTANCE)) {
			INSTANCE = new MavenResolver();
		}
		
		return INSTANCE;
	}
	
	/* -----------------------------------------------------------------------
	 * Internal methods
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Resolve artifact from http request
	 *
	 * @param group          artifact group
	 * @param artifact       artifact id
	 * @param versionPattern version pattern object
	 * @return the latest artifact result
	 */
	private String resolveHttpArtifact(@NotNull String group, @NotNull String artifact, @NotNull Pattern versionPattern) {
		// Resolve API url
		String url = String.format(API_ENTRY, group, artifact, MAX_RESULTS, "json");
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.setHeader("User-Agent", HTTP_USER_AGENT)
			.GET()
			.build();
		
		// Send HTTP request
		try {
			HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
			int responseCode = response.statusCode();
			
			// Check response errors
			if (HTTP_INVALID_CODES.containsKey(responseCode)) {
				throw new IOException("Error to make http request: " + HTTP_INVALID_CODES.get(responseCode));
			}
			
			// Parse json content
			InputStreamReader reader = new InputStreamReader(response.body());
			MavenResponse mavenResponse = serializer.fromJson(reader, MavenResponse.class);
			
			// Check if response is valid
			if (mavenResponse.response() == null || mavenResponse.response().found() == 0) {
				throw new IOException("Invalid request response or empty result");
			}
			
			// Return the last artifact element
			return Arrays.stream(mavenResponse.response().content())
				.filter(it -> {
					Matcher matcher = versionPattern.matcher(it.version());
					return matcher.find();
				})
				.max(Comparator.comparing(MavenArtifact::version))
				.orElseThrow(() -> new IOException(String.format("Artifact \"%s\" not found", group + ":" + artifact)))
				.version();
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
