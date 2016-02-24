# soccer-pool

(FIRST DRAFT) A small application created to calculate weekly soccer pool standings.

Use import.io to collect and export all team data across the Italian, English,
French, Spanish and German leagues. The URLs for outfield players are in "OutfieldURL.rtf".

//As of Jan 2016, data source blocked web scraping with import.io, will try again in future.

"TeamData.csv" holds participants' names as well as their selected players. Exported outfield and 
keeper data is held in "OutfieldData.csv" and "KeeperData.csv". 

Application combs through the text files and tabulates each participant's total weekly score.
As a first draft, the search algorithm and general process has not yet been optimized.
