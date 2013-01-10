package dll;

public class Position {

	public int lon;
	public int lat;

	public Position() {
	}

	public Position(int lon, int lat) {
		this.lon = lon;
		this.lat = lat;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getLon() {
		return this.lon;
	}

	public int getLat() {
		return this.lat;
	}

}