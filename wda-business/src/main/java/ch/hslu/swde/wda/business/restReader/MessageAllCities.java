package ch.hslu.swde.wda.business.restReader;

/**
 * Struktur der Nachrichten, die vom Server beim Aufruf der A01 Anfrage zurückgeschickt werden.
 * @author lunas
 *
 */

public class MessageAllCities {

		private int zip;
		private String name;

		public MessageAllCities() {

		}

		public MessageAllCities(String name) {
			this.name = name;
		}

		public int getZip() {
			return zip;
		}

		public void setZip(int zip) {
			this.zip = zip;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "City [zip=" + zip + ", name=" + name + "]";
		}

	}
