package jayray.net;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.annotation.Metered;
import com.yammer.metrics.annotation.Timed;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Gauge;

@Path("hello")
public class HelloWorldResource {
	private final Counter numberOfHellos = Metrics.newCounter(HelloWorldResource.class, "number-of-hello");
	private final Counter numberOfEchos = Metrics.newCounter(HelloWorldResource.class, "number-of-echos");
	private final Counter numberOfEchoCharacters = Metrics.newCounter(HelloWorldResource.class, "number-of-echo-characters");

	{
		// by uncommenting the line below, the metrics are outputed to STDOUT
		// every second
		// ConsoleReporter.enable(1, TimeUnit.SECONDS);

		Metrics.newGauge(HelloWorldResource.class, "sample-gauge", new Gauge<Integer>() {
			@Override
			public Integer value() {
				return 5;
			}
		});
	}

	@GET
	@Timed(name = "sayHello-timer")
	@Metered(name = "sayHello-meter")
	public String sayhello() {
		numberOfHellos.inc();
		return "hello";
	}

	@GET
	@Timed(name = "echo-timer")
	@Metered(name = "echo-meter")
	@Path("echo")
	public String echo(@QueryParam("message") String message) {
		numberOfEchos.inc();

		if (message != null)
			numberOfEchoCharacters.inc(message.length());

		return "echo: " + message;
	}
}