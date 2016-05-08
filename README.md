# hazardws
hazard web service

1) GET /hazard - select all the hazard
2) GET /hazard/{hazardId} - select the hazard that matches the id
3) POST /hazard - create a new hazard based on values receive in body
4) PUT /hzard/{hazardId} - update the hazard that matches the id
5) DELETE /hazard/{hazardId} - delete the hazard that matches the id
6) DELETE /hazard - delete all hazard
7) /hazard/search-by-time - search the hazard by time
                          - eg. /hazard/search-by-time?time=daytime
8) /hazard/search-by-type - search the hazard by type
9) /hazard/search-by-type-time - search the hazard by type and time
10) /hazard/search - search by isMotorAffected, isCarAffected, isTruckAffected
                  - eg. localhost:8080/hazard/search?isMotorAffected=false&isCarAffected=false&isTruckAffected=true
