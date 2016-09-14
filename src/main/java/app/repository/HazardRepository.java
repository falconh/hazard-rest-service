package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import app.model.Hazard;

public interface HazardRepository extends CrudRepository<Hazard, String>{
	
	@Query(value = "select h from Hazard h where h.is_motor_affected = :isMotorAffected and "
			+ "h.is_car_affected = :isCarAffected and h.is_truck_affected = :isTruckAffected",
			nativeQuery = true)
	List<Hazard> findByAffectedUser(@Param("isMotorAffected") int isMotorAffected,
			@Param("isCarAffected") int isCarAffected, @Param("isTruckAffected") int isTruckAffected);
}
