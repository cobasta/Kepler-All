package com.kepler.id.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.kepler.KeplerException;
import com.kepler.config.Profile;
import com.kepler.config.PropertiesUtils;
import com.kepler.extension.Extension;
import com.kepler.id.IDGenerator;
import com.kepler.id.IDGenerators;
import com.kepler.service.Service;

/**
 * @author kim
 *
 * 2016年2月3日
 */
public class DefaultIDGenerators implements IDGenerators, Extension {

	public static final String GENERATOR_KEY = DefaultIDGenerators.class.getName().toLowerCase() + ".generator";

	private static final String GENERATOR_DEF = PropertiesUtils.get(DefaultIDGenerators.GENERATOR_KEY, IncrGenerator.NAME);

	private final Map<String, IDGenerator> ids = new HashMap<String, IDGenerator>();

	private final Profile profile;

	public DefaultIDGenerators(Profile profile) {
		this.profile = profile;
	}

	@Override
	public IDGenerator get(Service service, Method method) {
		return get(service, method.getName());
	}

	@Override
	public IDGenerator get(Service service, String method) {
		String name = PropertiesUtils.profile(this.profile.profile(service), DefaultIDGenerators.GENERATOR_KEY, DefaultIDGenerators.GENERATOR_DEF);
		if (!this.ids.containsKey(name)) {
			throw new KeplerException("Can not found mathched generator: " + name + " ... ");
		}
		return this.ids.get(name);
	}

	@Override
	public Extension install(Object instance) {
		IDGenerator generator = IDGenerator.class.cast(instance);
		this.ids.put(generator.name(), generator);
		return this;
	}

	@Override
	public Class<?> interested() {
		return IDGenerator.class;
	}
}
