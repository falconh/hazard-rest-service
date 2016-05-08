package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hazard")
public class Hazard {
	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private String id;
	  private float latitude;
	  private float longitude;
	  private String time;
	  private String type;
	  private boolean isMotorAffected;
	  private boolean isCarAffected;
	  private boolean isTruckAffected;
	  
	  public Hazard() {
		  
	  }
	  
	  public Hazard(float longitude, float latitude, String time, String type, boolean isMotorAffected,
			  boolean isCarAffected, boolean isTruckAffected) {
		  this.longitude = longitude;
		  this.latitude = latitude;
		  this.time = time;
		  this.type = type;
		  this.isMotorAffected = isMotorAffected;
		  this.isCarAffected = isCarAffected;
		  this.isTruckAffected = isTruckAffected;
	  }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean getIsMotorAffected() {
		return isMotorAffected;
	}
	
	public void setIsMotorAffected(boolean isMotorAffected) {
		this.isMotorAffected = isMotorAffected;
	}
	
	public boolean getIsCarAffected() {
		return isCarAffected;
	}
	
	public void setIsCarAffected(boolean isCarAffected) {
		this.isCarAffected = isCarAffected;
	}
	
	public boolean getIsTruckAffected() {
		return isTruckAffected;
	}
	
	public void setIsTruckAffected(boolean isTruckAffected) {
		this.isTruckAffected = isTruckAffected;
	}
	
}
