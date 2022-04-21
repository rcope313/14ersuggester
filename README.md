# 14erSuggester🏔

## Project Description
This command-line application lets users search and filter through Colorado’s 14’er routes
and can recommend filtered routes based on current mountain weather.

## Design Principles
Currently, this application supports three subcommands (as referenced in greater detail in **Using 14erSuggester**).
14erSuggester scrapes 14’er route and trailhead data from 
[14ers.com](https://www.14ers.com/); each subcommand communicates with a dao to interface with the local database.

This application also scrapes weather forecast data from [NOAA](https://www.noaa.gov/); while not stored, 
it’s used to build on 14erSuggester’s route-recommendation model. 
The route-recommendation model assesses the interaction between mountain weather 
and filtered 14’er routes, significantly prioritizing probability of precipitation,
wind-chill, consequence severity of the 14’er routes, and other qualifiers.

## Using 14erSuggester
14erSuggest supports three subcommands: ```search```, ```compare```, and ```suggest```.
- Jar File: hikesuggesterCli/target/14erSuggesterCli.jar
- Command: ```java -jar <pathToJar> <subcommand>```


### ```search```
The ```search``` subcommand supports 20 filter types (see search and  suggest optional 
queries for names and descriptions of each) and coherently outputs the respective rows
from the local database.

[![Search-Hike-Suggester.png](https://i.postimg.cc/tCbFN3s2/Search-Hike-Suggester.png)](https://postimg.cc/jC8WsJQ7)

### ```compare```
The ```compare``` subcommand legigably denotes the difference between two 14’er routes.
The two 14’er routes are distinguished by their route names and mountain names 
respectively, or can be distinguished by their 14’ers.com url. Compare subcommand 
will prioritize fetching data by the urls if all information is given.

[![Compare-Hike-Suggester.png](https://i.postimg.cc/WzGgP3HG/Compare-Hike-Suggester.png)](https://postimg.cc/kR5BQn9G)

### ```suggest```
The ```suggest``` subcommand is similar mechanically to the search subcommand; 
however, it suggests to the user up to five 14’er routes according to 
relevant mountain forecast data and user-given filters.

<p align="center">
  <img src="https://media.giphy.com/media/K2WYUTgkttjIUk05Dc/giphy.gif" width="600"/>
</p>


Like all bash scripts, picocli supports multiple single-character flags. 
As an example, ```-sm``` fetches rows of the specified mountain names and 
that are standard routes only.

#### ```search``` and ```suggest``` optional queries

| Filter                | Command Line Names      | Description                                                                                                                                                                                                                                                          |   
|-----------------------|-------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Mountain Names        | `-m`, `--mountainnames` | Filter by name of mountain. If searching by more than one mountain name, split list with spaces only.                                                                                                                                                                |
| Route Names           | `-r`, `--routenames`    | Filter by name of route. If searching by more than one route name, split lists with spaces only.                                                                                                                                                                     |
| Is Standard Route     | `-s`, `--standardroute` | Filter if route is a standard route only.                                                                                                                                                                                                                            |
| Is Snow Route         | `--snowroute`           | Filter if route is a snow route only.                                                                                                                                                                                                                                |
| Route Grades          | `--grades`              | Filter by grade of route. If searching by more than one grade, split list with spaces only.                                                                                                                                                                          |
| Route Grade Qualities | `--gradeQualities`      | Filter by grade qualities of route. Valid options include 'easy', 'difficult', and an empty string. If searching by more than one grade, split list with spaces only.                                                                                                |
| Trailhead Names       | `--trailheads`          | Filter by trailhead name. If searching by more than one trailhead, split list with spaces only.                                                                                                                                                                      |
| Start Elevation       | `--startelevation`      | Filter by start elevation. Output will yield all results greater than or equal to input.                                                                                                                                                                             |
| Summit Elevation      | `--summitelevation`     | Filter by summit elevation. Output will yield all results greater than or equal to input.                                                                                                                                                                            |
| Total Gain            | `--totalgain`           | Filter by total gain. Output will yield all results greater than or equal to input.                                                                                                                                                                                  |
| Route Length          | `--routelength`         | Filter by length of route. Output will yield all results greater than or equal to input.                                                                                                                                                                             |
| Exposure              | `--exposure`            | Filter by exposure. Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.                                                                                                                                                                 |
| Rockfall Potential    | `--rockfallpotential`   | Filter by rockfall potential. Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.                                                                                                                                                       |
| Route Finding         | `--routefinding`        | Filter by route finding. Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.                                                                                                                                                            |
| Commitment            | `--commitment`          | Filter by commitment. Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.                                                                                                                                                               |
| Has Multiple Routes   | `--mulitpleroutes`      | Filter if there are multiple routes of varied elevation gain and route length. Meaning, the approach road may have winter closures or such that the road difficulty significantly increases. This will be noted as a '0' in the route length and total gain columns. |
| 14'er Route Urls      | `--routeurls`           | Filter by route url. If searching by more than one url, split list with spaces only.                                                                                                                                                                                 |
| Road Difficulties     | `--roaddifficulty`      | Filter by road difficulty. Output will yield results less than or equal to input. The highest road difficulty is 6.                                                                                                                                                  |
| 14'er Trailhead Urls  | `--trailheadurls`       | Filter by trailhead url. If searching by more than one url, split list with spaces only.                                                                                                                                                                             |


#### ```compare``` optional queries

| Filter           | Command Line Names | Description                                                                                           |   
|------------------|--------------------|-------------------------------------------------------------------------------------------------------|
| Mountain Name 1  | -m1, --mountain1   | First argument, mountain name.                                                                        |
| Route Name 1     | -r1, --route1      | First argument, route name.                                                                           |
| Mountain Name 2  | -m1, --mountain1   | Second argument, mountain name.                                                                       |
| Route Name 2     | -r1, --route1      | Second argument, route name.                                                                          |
| 14'er Route Urls | -u, --url          | Rather than entering in two separate Route and Mountain Names, enter two 14'er route urls to compare. |

## Looking Ahead
Currently, 14’er route and trailhead data exclusively live in a
localized database; future iterations of 14erSuggester would host data 
centrally, and it would interface with a web application rather than by 
the command line for better transportability.

## Credits
[Picocli - A Mighty Tiny Command Interface](https://picocli.info/)

[14ers.com - Home of the Colorado 14ers](https://www.14ers.com/)

[National Oceanic and Atmospheric Administration](https://www.noaa.gov/)
