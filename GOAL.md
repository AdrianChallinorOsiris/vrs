# Goals for VRS Project 

The VRS project comprises of two components: 

1. The Server (this project) 
2. A web front end (to be defined) 

The idea is that a ship's captain can enter daily reports that are sent to the server. The server then distributes these reports to consumers. The data fields for each report comprise a standard
core set of fields plus optional fields defined by fleet. A fleet is a collection of vessels. 

## User and roles

There are various roles for users who can connect to the system. 

- ** admin ** People with access to the reference data but who do not have access to the transactional data
- ** ship manager ** A person who can manage fleets, add vessels, and define the fields on forms. They can also manage the consumers of data. A manager belongs to a company. 
- ** fleet manager ** A person who controls a fleet for their company.
- ** user ** A user who belongs to a company who can access the data 
- ** vessel ** A master of a vessel who provides the data. 

Note that multiple of the above roles can be assigned to a user. 

The ** master ** has a special login. They get a URL with an unique parameter, a long, coded, unique ID. This ID is mapped to a vessel. The system then provides a JWT which the front end will access for
all other REST access. 

Other users have a username and password to access the system. Later on, we will add 2FA for these users. A user logs in, is assigned a role, and receives a JWT that is used in all other access. 

JWTs have a life time of no more than 24 hours. 

## Data Forms 

There are various forms (or reports) that the vessel can send. These include: 

- ** NoonDay **  report sent each local noon. This will not be GMT noon.
- ** Bunkers ** A report on the bunker fuel. Each vessel has a defined set of fuel grades that it has on board. This report will collect the amount of fuel for each grade that Remains on Board (ROB). In addition it will report the usage of this grade since the last report. Usage can be for Propulsion, heating, cleaning, inerting, auxilliary. This report can also include the on-take of bunkers. 
- ** OffHire ** A report that indicates a vessel has a problem and is not able to proceed. 
- ** Arrival ** A report issued on arrival in port
- ** InPort ** A cutdown version of the NoonDay report just for use when in port, that is: between an Arrival and Departure report
- ** Departure ** A report that indicates a vessel has left port

Other reports can be added. 

Reports consist of fields. Each field is of a specific data type. The most common are: 

- String. A short string 
- Text. A longer version of a string
- Integer. 
- Floating Point. 
- A choice of one from a list of options (radio button) 
- A list of options that allow multiple choices (check boxes)
- Choice from a list
- Date / time. Time is hours and minutes (no seconds)
- Duration in hours and minutes
- Location in Lat / lng
- Vessels time zone 

Each field as a name. The names are common across all users. Contrast this with the consumed data. 

There is a standard list of fields, called the IMO Compendium. These are mandatory fields. There is also the ability for a manager to add additional fields that are specific to a fleet. 

## Vessels and Fleets 

There is a standard set of vessel. Each vessel has an IMO number which is unique. The vessel also has a name, which can change over time. We need to record the current vessels name and its previous names,
including dates for these names. Vessel has exactly one  type: 

- Crude Oil Tanker
- Product Tabker
- Chemical Tanker
- Dry Cargo

Other types may be added. 

A vessel is managed by one company at a time, but this can change over time. A vessel belongs to that company for a period of time. 

A company has fleets. The fleet manager controls this. Normally a fleet will be of a specific type, but this is not always the case. 

Each vessel has exactly one person associated with them as the **Master**. That is the only person who can add date via the forms/reports. 

## Reporting to Companies

Most companies will not have ship managers. They will have a fleet of vessels that that they are interested in. A vessel will be owned by one company but can be in multiple fleets. 

Consumer Reports are generated for vessels. These reports can not add any fields, but can choose which reports they want to see and the fields that they are interested in. A consumer report can be constructed from the fields from one or more data entry reports. The order of fields, the format of the data can be specified by the fleet manager. They can also change the name of the field. 

Reports can be provided in a number of ways: 

- text on an email 
- PDF 
- Excel spreadsheet 
- or via an API. The API will return JSON data. It can be a PULL or PUSH API.
  - PULL API. The end users application connects to VRS, identifies itself, the vessel required and the report desired. The access for this method will use webhooks with a key in the header.
  - PUSH API. This is where VRS sends that data to the end users application. It is probable that we will need bespoke code for each client requesting this. 

In all cases we need to maintain a log of what was reported and when, for audit purposes. 

