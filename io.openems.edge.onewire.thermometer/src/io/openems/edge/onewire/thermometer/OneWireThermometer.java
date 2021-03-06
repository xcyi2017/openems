package io.openems.edge.onewire.thermometer;

import java.util.function.Consumer;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.event.EventConstants;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.adapter.DSPortAdapter;
import com.dalsemi.onewire.container.OneWireContainer;
import com.dalsemi.onewire.container.TemperatureContainer;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.bridge.onewire.BridgeOnewire;
import io.openems.edge.common.channel.StateChannel;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.thermometer.api.Thermometer;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "OneWire.Thermometer", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = { //
				EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_BEFORE_PROCESS_IMAGE, //
		})
public class OneWireThermometer extends AbstractOpenemsComponent implements Thermometer, OpenemsComponent {

	private final Logger log = LoggerFactory.getLogger(OneWireThermometer.class);

	@Reference
	private ConfigurationAdmin cm;

	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
	private BridgeOnewire bridge;

	private Config config;
	private TemperatureContainer _container = null;
	private byte[] state = null;

	public OneWireThermometer() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Thermometer.ChannelId.values(), //
				ThisChannelId.values() //
		);
	}

	private Consumer<DSPortAdapter> task = (adapter) -> {
		try {
			TemperatureContainer container = this.getDeviceContainer(adapter);
			if (this.state != null) {
				container.doTemperatureConvert(this.state);
			}
			this.state = container.readDevice();
			double temp = container.getTemperature(this.state);

			this.getTemperature().setNextValue(temp * 10 /* convert to decidegree */);
			this.setCommunicationFailed(false);

		} catch (OneWireException | OpenemsException e) {
			this.logError(this.log, e.getMessage());

			this.getTemperature().setNextValue(null);
			this.setCommunicationFailed(true);
		}
	};

	@Activate
	void activate(ComponentContext context, Config config) throws OpenemsException {
		super.activate(context, config.id(), config.alias(), config.enabled());
		this.config = config;

		// update filter for 'bridge'
		if (OpenemsComponent.updateReferenceFilter(this.cm, this.servicePid(), "bridge", config.bridge_id())) {
			return;
		}

		if (this.isEnabled()) {
			this.bridge.addTask(this.task);
		}
	}

	private TemperatureContainer getDeviceContainer(DSPortAdapter adapter) throws OpenemsException {
		if (this._container != null) {
			return this._container;
		}
		OneWireContainer owc = adapter.getDeviceContainer(config.address());
		if (!(owc instanceof OneWireContainer)) {
			throw new OpenemsException("This is not a OneWire Temperature Container");
		}
		TemperatureContainer container = (TemperatureContainer) owc;
		this._container = container;
		return this._container;
	}

	@Deactivate
	protected void deactivate() {
		this.bridge.removeTask(this.task);
		super.deactivate();
	}

	@Override
	public String debugLog() {
		return this.getTemperature().value().asString();
	}

	private StateChannel getCommunicationFailedChannel() {
		return this.channel(ThisChannelId.COMMUNICATION_FAILED);
	}

	private void setCommunicationFailed(boolean value) {
		this.getCommunicationFailedChannel().setNextValue(value);
	}
}
