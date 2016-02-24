
package packagedata;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author patrickspensieri
 */
public class PoolData 
{
    protected static int outfieldPerTeam;           //number of drafted players per team
    protected static int keeperPerTeam;           //number of drafted goalkeepers per team
    protected static int totalPerTeam;      //number of total goalies and players per team
    
    private boolean alphaOrder;             //states whether draftedArray is in alphabetical order
    protected static String[][] teamDataArray;      //holds all team info (from TeamData.rtf)
    private static String[][] outfieldDataArray;     //holds all player stats (from PlayerData.csv)
    private static String[][] keeperDataArray;     //holds all goalie stats (from GoalieData.csv)
    protected static String[][] draftedPlayerArray;  //holds all drafted players' (outfield/keeper) stats (in alphabetical order)
    
    protected static Team[] teamArray;

    public static void main(String[]args)
    {
        PoolData pool = new PoolData();
    }
    
    
    public PoolData()
    {
        //build StatReader object and necessary data arrays here
        alphaOrder = false;
        outfieldPerTeam = 13;
        keeperPerTeam = 2;
        totalPerTeam = outfieldPerTeam + keeperPerTeam;
             
        try 
        {
            StatReader stats = new StatReader("TeamData.csv", "OutfieldData.csv", "KeeperData.csv");
            
            teamDataArray = stats.createTeamDataArray();
            
            createDraftedDataArray();

            outfieldDataArray = stats.createOutfieldDataArray();
            
            keeperDataArray = stats.createKeeperDataArray();
        } 
        catch (IOException ex) 
        {
            System.out.println("Failed to read files. ");
            System.exit(0);
        }

        alphaQuickSort(draftedPlayerArray);
        updateStats();
        createTeams();
        
        //print teams in order of ranking
        for(int i = 0; i < teamArray.length; i++)
        {
            System.out.println(teamArray[i].toString());
            teamArray[i].printTeam();
            System.out.println();
        }
            
    }
    
    private static void createTeams()
    {
        teamArray = new Team[teamDataArray.length];
        for(int i = 0; i < teamDataArray.length; i++)
        {
            teamArray[i] = new Team(i);
        }
    }
    
    protected void createDraftedDataArray()
    {
        int col = 0;
        draftedPlayerArray = new String[teamDataArray.length * totalPerTeam][3];
        for (int row = 0; row < teamDataArray.length; row++)
        {
            for (col = 0; col < totalPerTeam; col++)
            {
                draftedPlayerArray[(row*totalPerTeam) + col][0] = teamDataArray[row][col + 1];
                draftedPlayerArray[(row*totalPerTeam) + col][1] = "0.0";
                draftedPlayerArray[(row*totalPerTeam) + col][2] = "0.0";
            }
        }        
    }
    
    /**
     * Updates statistics for relevant outfield players and keepers.
     * Does so by iterating through outFieldDataArray and keeperDataArray,
     * and using binary search to seek relevant player in the draftedPlayArray.
     * If player is not found, goals/assists/clean sheets remain as "-".
     * Gathers statistics when found.
     */
    private static void updateStats()
    {
        int pos;
        for(int i = 0; i < outfieldDataArray.length; i++)
        {
            pos = binarySearch(draftedPlayerArray, outfieldDataArray[i][0]);
            if (pos > -1)
            {
                draftedPlayerArray[pos][1] = outfieldDataArray[i][1];
                draftedPlayerArray[pos][2] = outfieldDataArray[i][2];
            }
        }
        
        for(int j = 0; j < keeperDataArray.length; j++)
        {
            pos = binarySearch(draftedPlayerArray, keeperDataArray[j][0]);
            if (pos > -1)
                draftedPlayerArray[pos][1] = keeperDataArray[j][1];
        }         
    }
    
    // alpha sort /////////////////////////////////////////////////////////////
    /**
     * Calls alphaQuickSort, passes relevant parameters.
     * @param array 2D String array to be sorted (assumes 2nd and 3rd col are empty)
     */
    private static void alphaQuickSort(String[][] array)
    {
        doQuickSort(array, 0, array.length - 1);
    }
    
    /**
     * Sorts the 2D String array alphabetically with QuickSort algorithm.
     * @param array Array to be sorted.
     * @param start Starting index.
     * @param end Ending index.
     */
    private static void doQuickSort(String[][] array, int start, int end)
    {
        int pivotPoint;
        
        if (start < end)
        {
            //get the pivot point
            pivotPoint = partition(array, start, end);
            //sort the first sublist
            doQuickSort(array, start, pivotPoint - 1);
            //sort the second sublist
            doQuickSort(array, pivotPoint + 1, end);
        }
    }
    
    private static int partition(String[][] array, int start, int end)
    {
        String pivotValue;             //to hold the pivot value
        int endOfLeftList;          //last element in the left sublist
        int mid;                    //to hold the mid-point subscript
        
        //find the subscript of the middle element
        //this will be the pivot value
        mid = (start + end) / 2;  
        //swap the middle element with the first element
        //this moves the pivot value to the start of the list
        swap(array, start, mid);
        //save the pivot value for comparisons
        pivotValue = Arrays.toString(array[start]);
        //for now, the end of the left sublist is the first element
        endOfLeftList = start;

        //scan the entire list and move any values that are less
        //than the pivot point value to the left sublist
        for (int scan = start + 1; scan <= end; scan++)
        {
            if (Arrays.toString(array[scan]).compareToIgnoreCase(pivotValue) < 0)
            {
                endOfLeftList++;
                swap(array, endOfLeftList, scan);
            }
        }
        
        //move the pivot value to the end of the left sublist
        swap(array, start, endOfLeftList);
        //return the subscript of the pivot value
        return endOfLeftList;
    }
    
    /**
     * Swaps two particular elements in the 2D String array.
     * @param array Array from which elements are being swapped.
     * @param a First element.
     * @param b Second element.
     */
    private static void swap(String[][] array, int a, int b)
    {
        String temp;
        temp = array[a][0];
        array[a][0] = array[b][0];
        array[b][0] = temp;
    }
    // end alpha sort //////////////////////////////////////////////////////////
    
    // binary search ///////////////////////////////////////////////////////////
    public static int binarySearch (String[][] array, String name)
    {
        int first;      //first array element
        int last;       //last array element
        int middle;     //mid point of search
        int position;   //position of search value
        boolean found;  //flag
        
        //set the initial values
        first = 0;
        last = array.length - 1;
        position = -1;
        found = false;
        
        //search for the value
        while (!found && first <= last)
        {
            //calculate the midpoint
            middle = (first + last) / 2;

            //if value is found at midpoint...
            if(name.equals(array[middle][0]))
            {
                found = true;
                position = middle;
            }
            //else if value is in lower half
            else if (name.compareToIgnoreCase(array[middle][0]) < 0)
                last = middle - 1;
            //else if the value is in upper half
            else
                first = middle + 1;
        }
            //return the position of item, or -1
            //if it is not found
            return position;
    }
    // end binary search ///////////////////////////////////////////////////////
    
    protected static void printStringArray(String[] array)
    {
        for (int row = 0; row < array.length; row++)
            System.out.println(array[row]);
    }
    
    protected static void printStringArray(String[][] array)
    {
        for (int row = 0; row < array.length; row++)
        {
            for (int col = 0; col < array[row].length; col++)
                System.out.print(array[row][col] + ", ");
            System.out.println();
        }
    }
}
