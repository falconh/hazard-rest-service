package app.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.snapshot.BooleanNode;
import com.sun.media.jfxmedia.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.firebase.*;
import com.firebase.geofire.*;
import java.util.Properties;

import app.model.Hazard;
import app.repository.HazardRepository;

@RestController
public class HazardController {

    private Properties prop ;
    private FirebaseApp firebaseApp;
	private String firebaseRootDir ;
    private static final List<String> booleanValue = Arrays.asList("true", "false");

	@Autowired
	private HazardRepository hazardRepo;

    HazardController(){
        firebaseApp = authToFirebase();
        prop = new Properties();

        try {
            firebaseRootDir = getPropValue(HazardConstant.FIREBASE_DATABASE_ROOT);
        }catch(IOException e){

        }

    }
	
	@RequestMapping(
	        method = RequestMethod.GET,
            value = "/hazard",
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Hazard>> listAllHazard() {
		List<Hazard> hazardList = (List<Hazard>) hazardRepo.findAll();
		
		return new ResponseEntity<List<Hazard>>(hazardList, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/hazard/{hazardId}")
	public ResponseEntity<Hazard> getHazardById(@PathVariable("hazardId") String hazardId) {
		Hazard currentHazard = hazardRepo.findOne(hazardId);
		
		return new ResponseEntity<Hazard>(currentHazard, HttpStatus.OK);
	}
	
	@RequestMapping(
	        method = RequestMethod.POST,
            value = "/hazard",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hazard> createHazard(@RequestBody Hazard hazard) {
		Hazard newHazard = new Hazard(hazard.getLatitude(),
				hazard.getLongitude(),
				hazard.getTime(),
				hazard.getType(),
				hazard.getIsMotorAffected(),
				hazard.getIsCarAffected(),
				hazard.getIsTruckAffected());
		
		Hazard savedHazard = hazardRepo.save(newHazard);

        Boolean isHazardSavedToGeoFire = setGeoFireLocation(savedHazard);

        if(!isHazardSavedToGeoFire){
            return new ResponseEntity<Hazard>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

		return new ResponseEntity<Hazard>(savedHazard, HttpStatus.CREATED);
	}
	
	@RequestMapping(
	        method = RequestMethod.PUT,
            value = "/hazard/{hazardId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hazard> updateHazard(
	        @PathVariable("hazardId") String hazardId,
            @RequestBody Hazard hazard) {
		Hazard currentHazard = hazardRepo.findOne(hazardId);
		
		currentHazard.setLatitude(hazard.getLatitude());
		currentHazard.setLongitude(hazard.getLongitude());
		currentHazard.setTime(hazard.getTime());
		currentHazard.setType(hazard.getType());
		
		Hazard savedHazard = hazardRepo.save(currentHazard);

        Boolean isHazardSavedToGeoFire = setGeoFireLocation(savedHazard);

        if(!isHazardSavedToGeoFire){
            return new ResponseEntity<Hazard>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

		return new ResponseEntity<Hazard>(currentHazard, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/hazard/{hazardId}")
	public ResponseEntity<Hazard> deleteHazard(@PathVariable("hazardId") String hazardId){
	    hazardRepo.delete(hazardId);

        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference(firebaseRootDir + HazardConstant.FIREBASE_DATABASE_HAZARD_COORD);

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation("hazard-" + hazardId);
		return new ResponseEntity<Hazard>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/hazard")
	public ResponseEntity<Hazard> deleteHazard() {
		hazardRepo.deleteAll();

		return new ResponseEntity<Hazard>(HttpStatus.NO_CONTENT);
	}
    
    @RequestMapping("/hazard/search")
    public ResponseEntity<List<Hazard>> getHazardListByMotor(
            @RequestParam(value = "isMotorAffected",defaultValue = "false") String isMotorAffected,
    		@RequestParam(value = "isCarAffected", defaultValue = "false") String isCarAffected,
            @RequestParam(value = "isTruckAffected", defaultValue = "false") String isTruckAffected) {

        if(!(booleanValue.contains(isCarAffected)
                && booleanValue.contains(isMotorAffected)
                && booleanValue.contains(isTruckAffected))){
            return new ResponseEntity<List<Hazard>>(HttpStatus.BAD_REQUEST);
        }


    	List<Hazard> hazardList = (List<Hazard>) hazardRepo.findByAffectedUser(Boolean.valueOf(isMotorAffected) ? 1 : 0,
                Boolean.valueOf(isCarAffected) ? 1 : 0,
                Boolean.valueOf(isTruckAffected) ? 1 : 0);
    	
        return new ResponseEntity<List<Hazard>>(hazardList, HttpStatus.OK);
    }

    private FirebaseApp authToFirebase(){

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setDatabaseUrl(getPropValue(HazardConstant.FIREBASE_DATABASE_URL))
                    .setServiceAccount(new FileInputStream(getPropValue(HazardConstant.FIREBASE_SERVICE_ACCOUNT_FILEPATH)))
                    .build();

            return FirebaseApp.initializeApp(options);
        }catch(IOException e){
            return null;
        }

	}

	private String getPropValue(String propName) throws IOException{

	    InputStream inputStream;

        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(HazardConstant.APPLICATION_PROPERTIES);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                return null;
            }

            return prop.getProperty(propName);
        }catch(Exception e) {
            return null;
        }
    }

    private Boolean setGeoFireLocation(Hazard hazard){


        if(hazard != null){

            if(hazard.getId() == null || hazard.getId() == ""){
                return false;
            }

            DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference(firebaseRootDir + HazardConstant.FIREBASE_DATABASE_HAZARD_COORD);

            GeoFire geoFire = new GeoFire(ref);

            geoFire.setLocation(HazardConstant.FIREBASE_HAZARD_ID_PREFIX + hazard.getId()
                    , new GeoLocation(hazard.getLatitude(), hazard.getLongitude()));

            return true;
        }else{
            return false;
        }

    }

    

    
}
