Use 5120_DB;

-- Getting the safety score for suburb 
SELECT SuburbScore
FROM safety_score
WHERE SuburbPostcode = "Beaconsfield, 3807";


-- Getting crime locations for the local government areas that the suburb user input refers to
SELECT lga_crime_locations.LGA,Location, Percentage
FROM lga_crime_locations
INNER JOIN lga_suburb ON 
	lga_suburb.LGA = lga_crime_locations.LGA
INNER JOIN safety_score ON
    lga_suburb.SuburbPostcode = safety_score.SuburbPostcode
    and safety_score.SuburbPostcode ="Beaconsfield, 3807" ;   