package io.openems.backend.metadata.odoo;

import io.openems.backend.metadata.api.Edge;
import io.openems.common.types.EdgeConfig;

public class MyEdge extends Edge {

	private final int odooId;
	private final String apikey;

	public MyEdge(int odooId, String edgeId, String apikey, String comment, State state, String version,
			String producttype, EdgeConfig config, Integer soc, String ipv4) {
		super(edgeId, comment, state, version, producttype, config, soc, ipv4);
		this.apikey = apikey;
		this.odooId = odooId;
	}

	public int getOdooId() {
		return this.odooId;
	}

	public String getApikey() {
		return apikey;
	}

}
