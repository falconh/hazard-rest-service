package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.model.Hazard;
import app.repository.HazardRepository;

@RestController
public class HazardController {
	
	@Autowired
	private HazardRepository hazardRepo;
	
	@RequestMapping(method = RequestMethod.GET, value = "/hazard")
	public ResponseEntity<List<Hazard>> listAllHazard() {
		List<Hazard> hazardList = (List<Hazard>) hazardRepo.findAll();
		
		return new ResponseEntity<List<Hazard>>(hazardList, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/hazard/{hazardId}")
	public ResponseEntity<Hazard> getHazardById(@PathVariable("hazardId") String hazardId) {
		Hazard currentHazard = hazardRepo.findOne(hazardId);
		
		return new ResponseEntity<Hazard>(currentHazard, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> createHazard(@RequestBody Hazard hazard) {
		Hazard newHazard = new Hazard(hazard.getLatitude(),
				hazard.getLongitude(),
				hazard.getTime(),
				hazard.getType(),
				hazard.getIsMotorAffected(),
				hazard.getIsCarAffected(),
				hazard.getIsTruckAffected());
		
		hazardRepo.save(newHazard);
		
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/hazard/{hazardId}")
	public ResponseEntity<Hazard> updateHazard(@PathVariable("hazardId") String hazardId, @RequestBody Hazard hazard) {
		Hazard currentHazard = hazardRepo.findOne(hazardId);
		
		currentHazard.setLatitude(hazard.getLatitude());
		currentHazard.setLongitude(hazard.getLongitude());
		currentHazard.setTime(hazard.getTime());
		currentHazard.setType(hazard.getType());
		
		hazardRepo.save(currentHazard);
		
		return new ResponseEntity<Hazard>(currentHazard, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/hazard/{hazardId}")
	public ResponseEntity<Hazard> deleteHazard(@PathVariable("hazardId") String hazardId) {
		hazardRepo.delete(hazardId);
		
		return new ResponseEntity<Hazard>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/hazard")
	public ResponseEntity<Hazard> deleteHazard() {
		hazardRepo.deleteAll();
		
		return new ResponseEntity<Hazard>(HttpStatus.NO_CONTENT);
	}
	
    @RequestMapping("/hazard/search-by-time")
    public ResponseEntity<List<Hazard>> getHazardListByTime(@RequestParam(value = "time") String time) {
    	List<Hazard> hazardList = (List<Hazard>) hazardRepo.findByTime(time);
    	
        return new ResponseEntity<List<Hazard>>(hazardList, HttpStatus.OK);
    }
    
    @RequestMapping("/hazard/search-by-type")
    public ResponseEntity<List<Hazard>> getHazardListByType(@RequestParam(value = "type") String type) {
    	List<Hazard> hazardList = (List<Hazard>) hazardRepo.findByType(type);
    	
        return new ResponseEntity<List<Hazard>>(hazardList, HttpStatus.OK);
    }
    
    @RequestMapping("/hazard/search-by-type-time")
    public ResponseEntity<List<Hazard>> getHazardListByTypeAndTime(@RequestParam(value = "type") String type, @RequestParam(value = "time") String time) {
    	List<Hazard> hazardList = (List<Hazard>) hazardRepo.findByTypeAndTime(type, time);
    	
        return new ResponseEntity<List<Hazard>>(hazardList, HttpStatus.OK);
    }
    
    @RequestMapping("/hazard/search-by-affected")
    public ResponseEntity<List<Hazard>> getHazardListByAffectedUser(@RequestParam(value = "isMotorAffected") boolean isMotorAffected,
    		@RequestParam(value = "isCarAffected") boolean isCarAffected, @RequestParam(value = "isTruckAffected") boolean isTruckAffected) {
    	List<Hazard> hazardList = (List<Hazard>) hazardRepo.findByIsMotorAffectedAndIsCarAffectedAndIsTruckAffected(isMotorAffected, isCarAffected, isTruckAffected);
    	
        return new ResponseEntity<List<Hazard>>(hazardList, HttpStatus.OK);
    }
    
    @RequestMapping("/hazard/search")
    public ResponseEntity<List<Hazard>> getHazardListByMotor(@RequestParam(value = "isMotorAffected") boolean isMotorAffected,
    		@RequestParam(value = "isCarAffected") boolean isCarAffected, @RequestParam(value = "isTruckAffected") boolean isTruckAffected) {
    	List<Hazard> hazardList = (List<Hazard>) hazardRepo.findByAffectedUser(isMotorAffected, isCarAffected, isTruckAffected);
    	
        return new ResponseEntity<List<Hazard>>(hazardList, HttpStatus.OK);
    }

    

    
}
