
use 5120_DB;

-- Add primary key to safety_score table
ALTER TABLE safety_score MODIFY SuburbPostcode VARCHAR(40) NOT NULL;
ALTER TABLE safety_score ADD PRIMARY KEY(SuburbPostcode);

-- Add primary key to lga_crime_locations table
ALTER TABLE lga_crime_locations MODIFY LGA VARCHAR(40) NOT NULL;
ALTER TABLE lga_crime_locations MODIFY Location VARCHAR(60) NOT NULL;
ALTER TABLE lga_crime_locations ADD PRIMARY KEY(LGA,Location);

-- Add primary key to lga_suburb table
ALTER TABLE lga_suburb MODIFY LGA VARCHAR(40) NOT NULL;
ALTER TABLE lga_suburb MODIFY SuburbPostcode VARCHAR(40) NOT NULL;
ALTER TABLE lga_suburb ADD PRIMARY KEY(SuburbPostcode,LGA);

-- ALTER TABLE safety_score DROP LGA;

-- Getting the safety score for suburb 
SELECT Indicator FROM safety_score WHERE SuburbPostcode = "Longwarry, 3816";

CREATE TABLE `safety_score` ( `SuburbPostcode` varchar(40) NOT NULL,  `SuburbScore` double DEFAULT NULL,  `Indicator` text,  PRIMARY KEY (`SuburbPostcode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



SELECT *
FROM safety_score
INNER JOIN lga_suburb ON 
    lga_suburb.SuburbPostcode = safety_score.SuburbPostcode
    and lga_suburb.LGA ="Alpine";
    
SELECT Location, Percentage FROM lga_crime_locations
INNER JOIN lga_suburb ON lga_suburb.LGA = lga_crime_locations.LGA
INNER JOIN safety_score ON lga_suburb.SuburbPostcode = safety_score.SuburbPostcode
    and safety_score.SuburbPostcode ="Beaconsfield, 3807" ;  
    
    
SELECT *
FROM lga_crime_locations
INNER JOIN lga_suburb ON 
	lga_suburb.LGA = lga_crime_locations.LGA;
    
CREATE TABLE `lga_crime_locations` (
  `LGA` varchar(40) NOT NULL,
  `Location` varchar(60) NOT NULL,
  `Percentage` double DEFAULT NULL,
  PRIMARY KEY (`LGA`,`Location`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Rename 'Exercise Type' column of  indoor_exercises table to 'ExerciseType'
ALTER TABLE 5120_DB.indoor_exercises RENAME column "Exercise Type" TO "ExerciseType";

-- Update the facilities table 
ALTER TABLE 5120_DB.facilities MODIFY FacilityName VARCHAR(100) NOT NULL;
ALTER TABLE 5120_DB.facilities MODIFY UpdatedSportsPlayed VARCHAR(50) NOT NULL;
ALTER TABLE 5120_DB.facilities MODIFY SuburbTown VARCHAR(45) NOT NULL;
ALTER TABLE 5120_DB.facilities MODIFY Postcode VARCHAR(4) NOT NULL;
ALTER TABLE 5120_DB.facilities MODIFY Latitude DOUBLE NOT NULL;
ALTER TABLE 5120_DB.facilities MODIFY Longitude DOUBLE NOT NULL;




-- Create primary key for facilities tableindoor_activitiesindoor_activities
ALTER TABLE 5120_DB.facilities add column `FacilityID` int(4) unsigned primary KEY AUTO_INCREMENT;

select * from 5120_DB.facilities;

