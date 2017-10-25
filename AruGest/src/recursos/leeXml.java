package recursos;

public class leeXml {
	/*
	InputStream ips = ClassLoader.getSystemClassLoader().getResourceAsStream("database.xml");
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	Document document = builder.parse(ips);
	Element version = (Element) document.getElementsByTagName("version").item(0);

	int maxItems = version.getElementsByTagName("vnumber").getLength();

	String sql;
	String currentVersion = "1";
	
	for (int i = 0; i<maxItems; i++){ 
		Element versionElement = (Element) version.getElementsByTagName("vnumber").item(i);
		String versionNumber = versionElement.getAttribute("valor");

		if (Integer.parseInt(currentVersion) >= Integer.parseInt(versionNumber)) {
			continue;
		}

		// LOGGER.info("Version: " + versionNumber);ç

		int maxSql = versionElement.getElementsByTagName("sql").getLength();
		long sqlSize = 0;

		for (int j = 0; j < maxSql; j++) {
			sql = versionElement.getElementsByTagName("sql").item(j).getTextContent();
			sqlSize += sql.length();
		}

		if (sqlSize >= MAX_LENGTH_SENTENCE) {
			throw new SAXException("Superado el límite...");
		} else {
			executeTransaction(versionElement, versionNumber, maxSql);
		}
	}*/

}
