// Declare constants used in the code
const mysql = require('mysql');
const express = require("express");
const app = express();

// Database connection details (Omitted here)
const con = mysql.createConnection({
    host: "HOSTNAME",
    user: "USERNAME",
    password: "PASSWORD",
    database: "DATABASE"
});


// Select score for a given suburb

app.get('/getSuburbScore/:SuburbPostcode',(req,res)=>{
    let sql = `SELECT SuburbScore FROM 5120_DB.safety_score WHERE SuburbPostcode =  ${req.params.SuburbPostcode}`
    let query = con.query(sql,(err,result) =>{
        if(err) throw err;
        console.log(result);
        res.send('Score fetched')
    })
})

// Select offence locations based on local government area
app.get('/getLocationPercentage/:SuburbPostcode',(req,res)=>{
        let sql = `SELECT lga_crime_locations.LGA, Location, Percentage FROM lga_crime_locations
        INNER JOIN lga_suburb ON lga_suburb.LGA = lga_crime_locations.LGA
        INNER JOIN safety_score ON lga_suburb.SuburbPostcode = safety_score.SuburbPostcode
            and safety_score.SuburbPostcode =  ${req.params.SuburbPostcode}`
        let query = con.query(sql,(err,result) =>{
            if(err) throw err;
            console.log(result);
            res.send('Score fetched')
        })
    })
    

// Print message on console with port info
app.listen('8080',()=>{
    console.log('Server started on Port 8080')
});
