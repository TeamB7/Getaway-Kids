-- On Your Toes Iteration 2 - Data preparation for indoor_activities dataset
-- Date : 16-May-2020

-- Data insert to the database via MySQL Workbench Table data Import Wizard

-- Select all items of the dataset
SELECT * FROM 5120_DB.indoor_activities;


-- Update dataset

-- Change exercise type weight to strength
UPDATE 5120_DB.indoor_activities SET ExerciseType = 'Strength' WHERE ExerciseType = 'Weight';

-- Change exercise type Plyo to Plyometrics
UPDATE 5120_DB.indoor_activities SET ExerciseType = 'Plyometrics' WHERE ExerciseType = 'Plyo';

-- Change exercise type Plyo,Cardio to Plyometrics, Cardio
UPDATE 5120_DB.indoor_activities SET ExerciseType = 'Plyometrics, Cardio' WHERE ExerciseType = 'Plyo,Cardio';

-- Update number of reps of Side Plank exercise
UPDATE 5120_DB.indoor_activities SET Reps = '5' WHERE Exercise = 'Side Plank';

-- Rename Twisted Mountain Climbers as Mountain Climbers
UPDATE 5120_DB.indoor_activities SET Exercise = 'Mountain Climbers' WHERE Exercise = 'Twisted Mountain Climbers';

-- Rename Literally Just Jumping as Jumping
UPDATE 5120_DB.indoor_activities SET Exercise = 'Jumping' WHERE Exercise = 'Literally Just Jumping';

-- Update Modifications column of Jump Lunges
UPDATE 5120_DB.indoor_activities SET Modifications = 'Easier: Split Jump, so do not go all the way down into a lunge' WHERE Exercise = 'Jump Lunges';


-- Add primary key to the table
ALTER TABLE 5120_DB.indoor_activities add column `e_id` int(2) unsigned primary KEY AUTO_INCREMENT;


-- Add column for likes
ALTER TABLE 5120_DB.indoor_activities ADD COLUMN Likes int(3);

-- Add column for images
ALTER TABLE 5120_DB.indoor_activities ADD COLUMN Image BLOB NOT NULL;


-- Add column for cardiovascular health
alter table 5120_DB.indoor_activities  add column CardiovascularHealth bool;

-- Add column for metabolism
ALTER TABLE 5120_DB.indoor_activities ADD COLUMN Metabolism bool;

-- Add column for strength
ALTER TABLE 5120_DB.indoor_activities ADD COLUMN Strength bool;


-- Update values in the table to include new examples, cardiovascular health indicator, metabolism indicator and strength indicator - Reference(Workout Videos and Indicators.csv)

UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=cTk5xAEd54Q' WHERE e_id = 1;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=QJ0TelnXMSA' WHERE e_id = 2;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 0,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=sHLu6-liUL0' WHERE e_id = 3;

UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=vASHw8uqMVI' WHERE e_id = 4;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=NZh-ZOZLK2U' WHERE e_id = 5;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=6X45RGJu3eU' WHERE e_id = 6;

UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=LSQHFJXdqnQ' WHERE e_id = 7;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=pEVfKQ9LbDc' WHERE e_id = 8;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=NeN8c-94EOo' WHERE e_id = 9;

UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 0,  Metabolism = 1, Strength = 1,Example = 'https://dl.airtable.com/8iDmKFYoQeShWougRve4_full_skipsgifsmall.gif' WHERE e_id = 10;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=ippmP6cgJdQ' WHERE e_id = 11;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 0,  Metabolism = 1, Strength = 0,Example = 'https://dl.airtable.com/RUVFSXWHRkyIOY9dudZS_full_tenor.gif' WHERE e_id = 12;

UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=ejsnwZl95bc' WHERE e_id = 13;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=EvNPYh3OMKw' WHERE e_id = 14;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=VMhdRIulA1g' WHERE e_id = 15;

UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=i-J8VKCTrT8' WHERE e_id = 16;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=2LLSbJkB69U' WHERE e_id = 17;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=Zas5ZmknIPM' WHERE e_id = 18;

UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=Bpxd3g15ZGE' WHERE e_id = 19;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=pEVfKQ9LbDc' WHERE e_id = 20;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=Eaw48Q0iQk8' WHERE e_id = 21;

UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=Bpxd3g15ZGE' WHERE e_id = 19;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=pEVfKQ9LbDc' WHERE e_id = 20;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=Eaw48Q0iQk8' WHERE e_id = 21;

UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 0,  Metabolism = 1, Strength = 1,Example = 'https://dl.airtable.com/UNjQnUnvTzKN4WmV44aU_full_WtFJBce.gif' WHERE e_id = 22;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 0,Example = 'https://www.youtube.com/watch?v=oPbJNdEcp9M' WHERE e_id = 23;
UPDATE 5120_DB.indoor_activities SET CardiovascularHealth = 1,  Metabolism = 1, Strength = 1,Example = 'https://www.youtube.com/watch?v=jbWmbhElf3Q' WHERE e_id = 24;

-- Order table by Exercise column
ALTER TABLE 5120_DB.indoor_activities ORDER BY Exercise ASC;

SELECT * FROM 5120_DB.indoor_activities;
COMMIT;



