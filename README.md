# 14erSuggester🏔

## Project Description
This Java command-line application lets users search through Colorado’s 14’er routes and can recommend routes based
on current weather and filters.

## Design Principles
14erSuggester scrapes various [14ers.com](https://www.14ers.com/) DOMs for route and trailhead data
and supports three subcommands (as referenced in greater detail in
**Using 14erSuggester**), each of which accesses route data stored
locally with MySQL
.

This application also scrapes weather forecast data from [NOAA](https://www.noaa.gov/) 
for 14erSuggester’s route-recommendation model. 
The model assesses the interaction between weather and user-defined filters, significantly
prioritizing probability of precipitation, wind-chill, and consequence severity of the
14’er routes.

## Using 14erSuggester
14erSuggester supports three subcommands: ```search```, ```compare```, and ```suggest```.
- Jar File: hikesuggesterCli/target/14erSuggesterCli.jar
- Command: ```java -jar <pathToJar> <subcommand>```


### ```search```
The ```search``` subcommand supports 20 filter types (see **``search`` and ``suggest`` optional 
queries** for names and descriptions of each filter) and neatly outputs the respective rows
from the local database.

[![Search-Hike-Suggester.png](https://i.postimg.cc/tCbFN3s2/Search-Hike-Suggester.png)](https://postimg.cc/jC8WsJQ7)

### ```compare```
The ```compare``` subcommand displays differences between two user-given 14’er routes.
The user can select the 14'er routes by the supplying the 14’ers.com urls or the route
& mountain names of the two 14'er routes. 

[![Compare-Hike-Suggester.png](https://i.postimg.cc/WzGgP3HG/Compare-Hike-Suggester.png)](https://postimg.cc/kR5BQn9G)

### ```suggest```
The ```suggest``` subcommand is similar mechanically to the ``search`` subcommand; 
however, it suggests up to  five 14’er routes according to 
mountain forecast data and filters.

<p align="center">
  <img src="https://media.giphy.com/media/K2WYUTgkttjIUk05Dc/giphy.gif" width="600"/>
</p>


Like all bash scripts, picocli supports multiple single-character flags. 
As an example, ```-sm``` fetches table rows of the given mountain names and 
that are standard routes only.

#### ```search``` and ```suggest``` optional queries

| Filter                | Command Line Names      | Description                                                                                                                                                                                                                                                |   
|-----------------------|-------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Mountain Names        | `-m`, `--mountainnames` | Filter by name of mountain. If searching by more than one mountain name, split list with spaces only.                                                                                                                                                      |
| Route Names           | `-r`, `--routenames`    | Filter by name of route. If searching by more than one route name, split lists with spaces only.                                                                                                                                                           |
| Is Standard Route     | `-s`, `--standardroute` | Filter if route is a standard route only.                                                                                                                                                                                                                  |
| Is Snow Route         | `--snowroute`           | Filter if route is a snow route only.                                                                                                                                                                                                                      |
| Route Grades          | `--grades`              | Filter by grade of route. If searching by more than one grade, split list with spaces only.                                                                                                                                                                |
| Route Grade Qualities | `--gradeQualities`      | Filter by grade qualities of route. Valid options include 'easy', 'difficult', and an empty string. If searching by more than one grade, split list with spaces only.                                                                                      |
| Trailhead Names       | `--trailheads`          | Filter by trailhead name. If searching by more than one trailhead, split list with spaces only.                                                                                                                                                            |
| Start Elevation       | `--startelevation`      | Filter by start elevation. Output will yield all results greater than or equal to input.                                                                                                                                                                   |
| Summit Elevation      | `--summitelevation`     | Filter by summit elevation. Output will yield all results greater than or equal to input.                                                                                                                                                                  |
| Total Gain            | `--totalgain`           | Filter by total gain. Output will yield all results greater than or equal to input.                                                                                                                                                                        |
| Route Length          | `--routelength`         | Filter by length of route. Output will yield all results greater than or equal to input.                                                                                                                                                                   |
| Exposure              | `--exposure`            | Filter by exposure. Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.                                                                                                                                                       |
| Rockfall Potential    | `--rockfallpotential`   | Filter by rockfall potential. Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.                                                                                                                                             |
| Route Finding         | `--routefinding`        | Filter by route finding. Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.                                                                                                                                                  |
| Commitment            | `--commitment`          | Filter by commitment. Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.                                                                                                                                                     |
| Has Multiple Routes   | `--mulitpleroutes`      | Filter if there are multiple routes of varied elevation gain and route length. Meaning, the approach road may have winter closures or road difficultly varies across total length. This will be noted as a '0' in the route length and total gain columns. |
| 14'er Route Urls      | `--routeurls`           | Filter by route url. If searching by more than one url, split list with spaces only.                                                                                                                                                                       |
| Road Difficulties     | `--roaddifficulty`      | Filter by road difficulty. Output will yield results less than or equal to input. The highest road difficulty is 6.                                                                                                                                        |
| 14'er Trailhead Urls  | `--trailheadurls`       | Filter by trailhead url. If searching by more than one url, split list with spaces only.                                                                                                                                                                   |


#### ```compare``` optional queries

| Filter           | Command Line Names | Description                                                                                           |   
|------------------|--------------------|-------------------------------------------------------------------------------------------------------|
| Mountain Name 1  | -m1, --mountain1   | First argument, mountain name.                                                                        |
| Route Name 1     | -r1, --route1      | First argument, route name.                                                                           |
| Mountain Name 2  | -m1, --mountain1   | Second argument, mountain name.                                                                       |
| Route Name 2     | -r1, --route1      | Second argument, route name.                                                                          |
| 14'er Route Urls | -u, --url          | Rather than entering in two separate Route and Mountain Names, enter two 14'er route urls to compare. |

## Looking Ahead
Currently, 14’er route and trailhead data live exclusively in a
local database; future iterations of 14erSuggester would have data available
centrally. It would also interface with a web application rather than by 
the command line for better transportability.

## Credits
[Picocli - A Mighty Tiny Command Interface](https://picocli.info/)

[14ers.com - Home of the Colorado 14ers](https://www.14ers.com/)

[National Oceanic and Atmospheric Administration](https://www.noaa.gov/)
