
package packagedata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Reads and writes data from text files exported from import.io.
 * 
 * @author patrickspensieri
 */
public final class StatReader 
{
    private Scanner scanner;
    private String line = "line";
    final protected int totalOutfield;                     //total number of outfield players from data file
    final protected int totalKeeper;                     //total number of keepers from data file
    final protected int totalTeams;                       //total number of teams
    final private File teamFile;
    final private File outfieldFile;
    final private File keeperFile;
    
    //Constructor opens file to read statistics from	
    public StatReader(String team, String outfield, String keeper) throws IOException
    {
        teamFile = new File(team);
        outfieldFile = new File(outfield);
        keeperFile = new File(keeper);
        
        totalOutfield = countLines(outfield);
        totalKeeper = countLines(keeper);
        totalTeams = 7;
        System.out.println("totalTeams : " + totalTeams);
        System.out.println("playerData : " + totalOutfield);
        System.out.println("goalieData : " + totalKeeper);
    }
    
    /**
     * Counts number of lines in a file efficiently. (From StackOverflow)
     * @param filename File that is being read from.
     * @return Number of lines in the file.
     * @throws IOException 
     */
    private static int countLines(String filename) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        int lines = 0;
        while (reader.readLine() !=null)
            lines++;
        reader.close();
        return lines;
    }
	
    /**
     * Strips accents from a String.
     * @param s String from which accents are being stripped.
     * @return String without accents.
     */
    private static String stripAccents(String s) 
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
	
    /**
     * Closes the Scanner object.
     * @throws IOException 
     */  
    public void close() throws IOException
    {
        scanner.close();
    }
        
    /**
     * Creates Team objects read from the team file. Assigns list of outfield players and
     * keepers to the respective Team owner.
     * @return 2D arr
     * @throws IOException 
     */
    protected String[][] createTeamDataArray() throws IOException
    {
        scanner = new Scanner(new FileInputStream(teamFile), "UTF-8");

        int row = 0;
        int col = 0;
        String[][] teamArray = new String[totalTeams][PoolData.totalPerTeam + 1];
        while(row < totalTeams)
        {
            line = scanner.nextLine();
            StringTokenizer strTokenizer = new StringTokenizer(line, ",");
            while(strTokenizer.hasMoreTokens())
            {
                teamArray[row][col] = strTokenizer.nextToken();
                col++;
            }
            row++;
            col = 0;
        }
        return teamArray;
    }
	
    protected String[][] createOutfieldDataArray() throws IOException
    {
        scanner = new Scanner(new FileInputStream(outfieldFile), "UTF-8");

        int index = 0;
        String[][] outfieldArray = new String[totalOutfield][3];
        while(index < totalOutfield)
        {
            line = scanner.nextLine();
            StringTokenizer strTokenizer = new StringTokenizer(line, ",");
            outfieldArray[index][0] = strTokenizer.nextToken();
            outfieldArray[index][1] = strTokenizer.nextToken();
            outfieldArray[index][2] = strTokenizer.nextToken();

            index++;
        }

        return outfieldArray;
    }
	
    public String[][] createKeeperDataArray() throws IOException
    {
        scanner = new Scanner(new FileInputStream(keeperFile), "UTF-8");

        int index = 0;
        String[][] keeperArray = new String[totalKeeper][3];
//        while(scanner.hasNext())
        while(index < totalKeeper)
        {
            line = scanner.nextLine();
            StringTokenizer strTokenizer = new StringTokenizer(line, ",");
            keeperArray[index][0] = strTokenizer.nextToken();
            keeperArray[index][1] = strTokenizer.nextToken();
            keeperArray[index][2] = "0.0";

            index++;
        }

        return keeperArray;
    }
}