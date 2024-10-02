package com.mytradingsetup.model;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class OIData {
	
	List<Double> putoi, calloi, price, putchoi,callchoi;
	List<String> date;
	
	public OIData(List<MapData> mdata)
	{
		mdata=mdata.stream().sorted(Comparator.comparing(MapData::getDate)).collect(Collectors.toList());
		calloi = mdata.stream().filter(o->o.getType().equals("CE")).map(o->o.getStrike()).collect(Collectors.toList());
		callchoi = mdata.stream().filter(o->o.getType().equals("CE")).map(o->o.getChoi()).collect(Collectors.toList());
		putoi = mdata.stream().filter(o->o.getType().equals("PE")).map(o->o.getStrike()).collect(Collectors.toList());
		putchoi = mdata.stream().filter(o->o.getType().equals("PE")).map(o->o.getChoi()).collect(Collectors.toList());
		date = mdata.stream().map(o->o.getDate()).distinct().collect(Collectors.toList());
		price=mdata.stream().map(o->o.getDate()+"#"+o.getPrice()).distinct().map(o->Double.parseDouble(o.split("#")[1])).collect(Collectors.toList());
	}

}
