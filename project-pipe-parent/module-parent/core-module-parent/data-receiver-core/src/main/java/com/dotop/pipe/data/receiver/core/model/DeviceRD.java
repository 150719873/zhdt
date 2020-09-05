package com.dotop.pipe.data.receiver.core.model;

import java.util.List;

import lombok.Data;

@Data
public class DeviceRD {

	private InfoRD info;

	private List<PropertyRD> properties;
}
