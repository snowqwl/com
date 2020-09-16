package com.sunshine.monitor.system.ws.util;


public interface XmlBuild {
	
	String build();
	
	public static class InAccessXmlBuild implements XmlBuild{
		private String kssj;
		private String jssj;
		private String hpzl;
		private String hphm;
		
		public InAccessXmlBuild(String kssj,String jssj,
				String hpzl,String hphm){
			this.kssj = kssj;
			this.jssj = jssj;
			this.hpzl = hpzl;
			this.hphm = hphm;
		}

		public String build() {
			String xml = "";
			xml = xml + "<?xml version=\"1.0\" encoding=\"GB2312\"?>";
			xml = xml + "<root>";
			xml = xml + "<head>";
			xml = xml + "<systemtype>01</systemtype>";
			xml = xml + "<businesstype>01</businesstype>";
			xml = xml + "</head>";
			xml = xml + "<body>";
			xml = xml + "<data>";
			xml = xml + "<kssj>" + kssj + "</kssj>";
			xml = xml + "<jssj>" + jssj + "</jssj>";
			xml = xml + "<gcxh></gcxh>";
			xml = xml + "<kdbh></kdbh>";
			xml = xml + "<fxbh></fxbh>";
			xml = xml + "<hpzl>" + hpzl + "</hpzl>";
			xml = xml + "<hphm>" + hphm + "</hphm>";
			xml = xml + "<hpys></hpys>";
			xml = xml + "<wpc>0</wpc>";
			xml = xml + "</data>";
			xml = xml + "</body>";
			xml = xml + "</root>";
			return xml;
		}

	}
}
