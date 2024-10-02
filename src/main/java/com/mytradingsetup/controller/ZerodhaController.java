package com.mytradingsetup.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mytradingsetup.model.OIData;
import com.mytradingsetup.model.ZData;
import com.mytradingsetup.model.ZOIData;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "ZERODHA")
public class ZerodhaController {
	
	@RequestMapping(value = "demo")
	public String demo() {
		return "demo";

	}

	static RestTemplate template = new RestTemplate();
	static HttpHeaders headers = new HttpHeaders();
	static HttpEntity<String> entity = null;
	static List<String> instruments = null;

	{
		headers.set("Authorization",
				"enctoken qdsi3EgPGgi0mpl4tP7U6AW5GCWmwHuWba47qtlQCSeXuJrqxzH0Wb3/R0+E3OAvM/Z3ZKgTR6lE57P+tiFT9kz7TBhSJhVdrtc/I3Rao4Mt2dpWH0MDdg==");
		headers.set("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		entity = new HttpEntity<String>(headers);
		instruments = getInstruments();
	}

//	private List<String> getInstruments() {
//		
//		System.out.println("in getInstruments");
//
//		String output = template.exchange("https://kite.zerodha.com/oms/instruments/historical/14988290/minute?user_id=IO7052&oi=1&from=2024-08-03&to=2024-10-02", HttpMethod.GET, entity, String.class)
//				.getBody();
//
//		List<String> instruments = Arrays.asList(output.split("\n"));
//		
//		return instruments;
//	}
	


	private List<String> getInstruments() {

		String output = template.exchange("https://api.kite.trade/instruments", HttpMethod.GET, entity, String.class)
				.getBody();

		List<String> instruments = Arrays.asList(output.split("\n"));
		return instruments;
	}

	
	@RequestMapping(value = "getData")
	public List<ZOIData> getData(@RequestParam("instrument") String instrument) {
		String call[]= {"CE","PE"};
		Double lastPrice = getLastPrice(instrument);

		System.out.println(lastPrice);
		List<String> mapids = getCEPE(lastPrice);
		System.out.println(mapids);
		List<ZOIData> OIDataList = new ArrayList<>();

		for (int j = 0; j < mapids.size(); j++) {

			String spiltData[] = mapids.get(j).split(",");

			for (int k = 0; k < 2; k++) {

				ZOIData oiData = new ZOIData();
				oiData.setPrice(Long.valueOf(spiltData[0]));
				oiData.setInstrument(Long.valueOf(spiltData[1 + k]));
				oiData.setCall(call[k]);

				String output = template.exchange(
						"https://kite.zerodha.com/oms/instruments/historical/" + oiData.getInstrument()
								+ "/minute?user_id=IO7052&oi=1&from=2024-09-30&to=2024-10-02",
						HttpMethod.GET, entity, String.class).getBody();
				
				//System.out.println(output);

				int index = output.indexOf("candles");
				int index1 = output.indexOf("[[", index);
				String dataStr = output.toString().substring(index1 + 2).replace("],[", "\n").replace("]]}}", "");
				String candles[] = dataStr.split("\n");
				
				int startFrom=candles.length-100;
				if(startFrom<0)
					startFrom=0;

				List<ZData> data = new ArrayList<>();

				for (int i = startFrom; i < candles.length; i++) {

					String date1;
					Double open1, high1, close1, low1, volume1, oi1;
					date1 = candles[i].split(",")[0];
					open1 = Double.parseDouble(candles[i].split(",")[1]);
					high1 = Double.parseDouble(candles[i].split(",")[2]);
					low1 = Double.parseDouble(candles[i].split(",")[3]);
					close1 = Double.parseDouble(candles[i].split(",")[4]);
					volume1 = Double.parseDouble(candles[i].split(",")[5]);
					oi1 = Double.parseDouble(candles[i].split(",")[6]);
					data.add(new ZData(date1, open1, high1, low1, close1, volume1, oi1));
				}
				
				
				
				
				oiData.setClose(data.stream().map(o->o.getClose()).collect(Collectors.toList()));
				oiData.setOpen(data.stream().map(o->o.getOpen()).collect(Collectors.toList()));
				oiData.setHigh(data.stream().map(o->o.getHigh()).collect(Collectors.toList()));
				oiData.setLow(data.stream().map(o->o.getLow()).collect(Collectors.toList()));
				oiData.setOi(data.stream().map(o->(o.getOi()/25)).collect(Collectors.toList()));
				oiData.setVol(data.stream().map(o->o.getVolume()).collect(Collectors.toList()));
				oiData.setDate(data.stream().map(o->o.getDate().replaceAll("\"","").substring(11,19)).collect(Collectors.toList()));
				

				
				OIDataList.add(oiData);
			}

		}
		return OIDataList;
	}
	
	@RequestMapping(value = "getLastPrice")
	public Double getLastPrice(@RequestParam("instrument") String instrument) {

		String output = template.exchange(
				"https://kite.zerodha.com/oms/instruments/historical/" + instrument
						+ "/minute?user_id=IO7052&oi=1&from=2024-09-30&to=2024-10-02",
				HttpMethod.GET, entity, String.class).getBody();
		
		System.out.println(output);

		int index = output.indexOf("candles");
		int index1 = output.indexOf("[[", index);
		String dataStr = output.toString().substring(index1 + 2).replace("],[", "\n").replace("]]}}", "");
		String candles[] = dataStr.split("\n");
		return Double.parseDouble(candles[candles.length - 1].split(",")[4]);
	}

	@RequestMapping(value = "getCEPE")
	public List<String> getCEPE(@RequestParam("lastPrice") Double lastPrice) {

		Long price = Math.round(lastPrice / 100) * 100;

		List<Long> prices = new ArrayList<>();
		List<String> data = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			Long p = price - i * 50;
			prices.add(p);
		}

		for (int i = 0; i < 3; i++) {
			Long p = price + i * 50;
			prices.add(p);
		}

		prices = prices.stream().distinct().sorted().collect(Collectors.toList());

		for (int i = 0; i < prices.size(); i++) {
			String pricestr = prices.get(i) + "";
			String CE = (instruments.stream()
					.filter(o -> o.indexOf("\"NIFTY\"") != -1 && o.indexOf("2024-10-03") != -1
							&& o.indexOf(pricestr) != -1 && o.indexOf("CE") != -1)
					.collect(Collectors.toList()).get(0)).split(",")[0];
			String PE = (instruments.stream()
					.filter(o -> o.indexOf("\"NIFTY\"") != -1 && o.indexOf("2024-10-03") != -1
							&& o.indexOf(pricestr) != -1 && o.indexOf("PE") != -1)
					.collect(Collectors.toList()).get(0)).split(",")[0];

			data.add(pricestr + "," + CE + "," + PE);
		}
		return data;
	}


}
