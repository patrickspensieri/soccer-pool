
package packagedata;

import static packagedata.PoolData.binarySearch;
import static packagedata.PoolData.draftedPlayerArray;
import static packagedata.PoolData.printStringArray;
import static packagedata.PoolData.teamDataArray;
import static packagedata.PoolData.totalPerTeam;

/**
 *
 * @author patrickspensieri
 */
public class Team //extends PoolData
{
    private String name;            //participant's name
    private double points;             //total number of points
    
    String[][] team;
    
    //constructor
    public Team(int teamIndex)
    {
        name = teamDataArray[teamIndex][0];
        team = new String[totalPerTeam][3];
        points = 0;
        
        setTeam(teamIndex);
        setPoints();
    }
    
    private void setTeam(int teamIndex)
    {
        int pos;
        for(int i = 1; i < teamDataArray[teamIndex].length; i++)
        {
            pos = binarySearch(draftedPlayerArray, teamDataArray[teamIndex][i]);
            if (pos > -1)
            {
                team[i-1][0] = draftedPlayerArray[pos][0];
                team[i-1][1] = draftedPlayerArray[pos][1];
                team[i-1][2] = draftedPlayerArray[pos][2];
            }
        }
    }
    
    protected String[][] getTeam()
    {
        return team;
    }
    
    /**
     * Sets the total number of points a team has.
     * NOTE : This assumes all goals/assists/CS are worth one point.
     */
    private void setPoints()
    {   
        for(int i = 0; i < team.length; i++)
           points += ((Double.parseDouble(team[i][1]) + Double.parseDouble(team[i][2]))); 
    }
    
    protected double getPoints()
    {
        return points;
    }
    
    @Override
    public String toString()
    {
        return (name + " : " + Double.toString(points) + " points");
    }
    
    public void printTeam()
    {
        printStringArray(team);
    }
    
    
}
