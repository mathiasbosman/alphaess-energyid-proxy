package be.mathiasbosman.inverterdataexport.exporter;

import java.net.URI;
import org.springframework.http.HttpEntity;

/**
 * Interface for webhook adapters.
 *
 * @param <T> Type of the data to be posted.
 */
public interface WebhookAdapter<T> {

  void postData(URI targetUrl, HttpEntity<T> httpEntity);
}
