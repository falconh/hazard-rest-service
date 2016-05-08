package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import app.model.Hazard;

public interface HazardRepository extends CrudRepository<Hazard, String>{
	
	List<Hazard> findByTime(String time);
	
	List<Hazard> findByType(String type);
	
	List<Hazard> findByTypeAndTime(String type, String time);
	
	@Query(value = "select * from hazard h where h.is_motor_affected = :isMotorAffected and "
			+ "h.is_car_affected = :isCarAffected and h.is_truck_affected = :isTruckAffected",
			nativeQuery = true)
	List<Hazard> findByAffectedUser(@Param("isMotorAffected") boolean isMotorAffected, 
			@Param("isCarAffected") boolean isCarAffected, @Param("isTruckAffected") boolean isTruckAffected);
}
