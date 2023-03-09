package custom_jfx_plugin.dependency.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import ushiosan.jvm_utilities.lang.collection.Collections;
import ushiosan.jvm_utilities.lang.collection.elements.Pair;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;

public abstract class Resolver<T, V> {
	
	/* -----------------------------------------------------------------------
	 * Properties
	 * -----------------------------------------------------------------------*/
	
	/**
	 * Max response results
	 */
	protected static final int MAX_RESULTS = 50;
	
	/**
	 * Http user agent
	 */
	protected static final String HTTP_USER_AGENT = "Java/HttpClient";
	
	/**
	 * Invalid http response codes
	 */
	protected static final Map<Integer, String> HTTP_INVALID_CODES = Collections.mapOf(
		Pair.of(400, "Bad request"),
		Pair.of(401, "Unauthorized"),
		Pair.of(402, "Payment required"),
		Pair.of(403, "Forbidden"),
		Pair.of(404, "Not found"),
		Pair.of(405, "Method not allowed"),
		Pair.of(406, "Not acceptable"),
		Pair.of(407, "Proxy authentication required"),
		Pair.of(408, "Request timeout"),
		Pair.of(409, "Conflict"),
		Pair.of(410, "Gone"),
		Pair.of(429, "Too many requests"),
		Pair.of(500, "Internal server error"),
		Pair.of(501, "Not implemented"),
		Pair.of(502, "Bad gateway"),
		Pair.of(503, "Service Unavailable")
	);
	
	/**
	 * Resolver http client
	 */
	protected final HttpClient httpClient = HttpClient.newBuilder()
		.version(HttpClient.Version.HTTP_2)
		.connectTimeout(Duration.ofSeconds(5))
		.build();
	
	/**
	 * Json serialization instance
	 */
	protected final Gson serializer = (new GsonBuilder())
		.serializeNulls()
		.setPrettyPrinting()
		.create();
	
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
	public abstract @NotNull T resolveDependency(@NotNull V configuration, boolean silent);
	
}
