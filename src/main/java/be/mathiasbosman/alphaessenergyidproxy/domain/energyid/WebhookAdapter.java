package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import java.net.URI;
import org.springframework.http.HttpEntity;

/**
 * Interface for webhook adapter.
 *
 * @param <T> Type of the data to be posted.
 */
public interface WebhookAdapter<T> {

  void postData(URI targetUrl, HttpEntity<T> httpEntity);
}
