package io.openems.edge.simulator.meter.nrc.acting;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "Simulator NRCMeter Acting", //
		description = "This simulates an 'acting' non-regulated-consumption meter using data provided by a data source.")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "meter2";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;

	@AttributeDefinition(name = "Datasource-ID", description = "ID of Simulator Datasource.")
	String datasource_id();

	@AttributeDefinition(name = "Minimum Ever Active Power", description = "This is automatically updated.")
	int minActivePower();

	@AttributeDefinition(name = "Maximum Ever Active Power", description = "This is automatically updated.")
	int maxActivePower();

	@AttributeDefinition(name = "Datasource target filter", description = "This is auto-generated by 'Datasource-ID'.")
	String datasource_target() default "";

	String webconsole_configurationFactory_nameHint() default "Simulator NRCMeter Acting [{id}]";
}